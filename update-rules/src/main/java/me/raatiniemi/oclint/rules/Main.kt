/*
 * Copyright (c) 2018 Tobias Raatiniemi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.raatiniemi.oclint.rules

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import me.raatiniemi.oclint.rules.writer.writeAsXml
import me.raatiniemi.oclint.rules.writer.writeToFile
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

private const val PATH_TO_READ_ME = "README.md"
private const val PATH_TO_RULES = "oclint/src/main/resources/me/raatiniemi/sonar/oclint/rules.txt"
private const val PATH_TO_PROFILE = "oclint/src/main/resources/me/raatiniemi/sonar/oclint/profile-oclint.xml"

private const val BASE_URL = "http://docs.oclint.org/en/stable/rules"
private val availableRuleCategoriesWithSeverity = mapOf(
    "Basic" to RuleSeverity.CRITICAL,
    "Cocoa" to RuleSeverity.MINOR,
    "Convention" to RuleSeverity.MAJOR,
    "Design" to RuleSeverity.MAJOR,
    "Empty" to RuleSeverity.CRITICAL,
    "Migration" to RuleSeverity.MINOR,
    "Naming" to RuleSeverity.MAJOR,
    "Redundant" to RuleSeverity.MINOR,
    "Size" to RuleSeverity.CRITICAL,
    "Unused" to RuleSeverity.INFO
)

fun main(args: Array<String>) {
    FuelManager.instance.basePath = BASE_URL

    writeVersionToReadMe(readVersion())
    listRuleCategoriesWithMissingSeverity(nameForAvailableRuleCategories())

    val rules = availableRuleCategoriesWithSeverity
        .map { RuleCategory(name = it.key, severity = it.value) }
        .flatMap { fetchRulesFor(it) }
        .sortedBy { it.name }

    println("Found ${rules.count()} rules.")

    println("Writing available rules to rules.txt")
    writeToFile(PATH_TO_RULES, writeRules(rules))

    println("Writing available rules to profile-oclint.xml")
    writeToFile(PATH_TO_PROFILE) {
        writeAsXml(buildProfile(rules))
    }
}

private fun writeRules(rules: List<Rule>): () -> String = {
    StringBuilder()
        .apply {
            appendln(headerTemplate())

            rules.forEach {
                appendln(ruleTemplate(it))
            }
        }
        .toString()
}

private fun buildProfile(rules: List<Rule>): Profile {
    val profileRules = rules.map { ProfileRule(key = it.key) }
        .toList()

    return Profile(rule = profileRules)
}

private fun readVersion(): String {
    return fetch("index.html")
        .select("#rule-index > p")
        .map { it.text() }
        .mapNotNull {
            val regex = """OCLint ([\d\.]+) includes""".toRegex()
            val (version) = regex.find(it)?.destructured ?: return@mapNotNull null
            return@mapNotNull version
        }
        .firstOrNull() ?: ""
}

private fun writeVersionToReadMe(version: String) {
    val lines = File(PATH_TO_READ_ME).readLines()
        .map {
            val regex = """oclint-([\d\.]+)-blue""".toRegex()
            val result = regex.find(it) ?: return@map it
            val (previousVersion) = result.destructured

            it.replace(previousVersion, version)
        }
        .joinToString("\n")

    writeToFile(PATH_TO_READ_ME) { "$lines\n" }
}

private fun listRuleCategoriesWithMissingSeverity(availableRuleCategories: List<String>) {
    (availableRuleCategories - availableRuleCategoriesWithSeverity.keys)
        .forEach { println("Rule \"$it\" is missing severity") }
}

private fun nameForAvailableRuleCategories(): List<String> {
    return fetch("index.html")
        .select(".toctree-l1 > a")
        .map { it.text() }
}

private fun fetchRulesFor(category: RuleCategory): List<Rule> {
    return fetch(basenamePath(category))
        .select(".section > .section")
        .map { Rule.from(category, html = it) }
}

private fun fetch(path: String): Document {
    val (_, _, result) = "/$path".httpGet().responseString()
    when (result) {
        is Result.Failure -> {
            throw result.error
        }
        is Result.Success -> {
            return Jsoup.parse(result.value)
        }
    }
}
