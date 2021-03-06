/*
 * Copyright (c) 2019 Tobias Raatiniemi
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
import me.raatiniemi.oclint.rules.parser.parseRuleCategories
import me.raatiniemi.oclint.rules.parser.parseRules
import me.raatiniemi.oclint.rules.parser.parseVersion
import me.raatiniemi.oclint.rules.profile.profile
import me.raatiniemi.oclint.rules.writer.writeAsJson
import me.raatiniemi.oclint.rules.writer.writeToFile
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

private const val PATH_TO_READ_ME = "README.md"
private const val PATH_TO_RULES = "oclint/src/main/resources/me/raatiniemi/sonar/oclint/rules.txt"
private const val PATH_TO_PROFILE = "oclint/src/main/resources/me/raatiniemi/sonar/oclint/profile.json"

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

    println("Writing available rules to: $PATH_TO_PROFILE")
    writeToFile(PATH_TO_PROFILE) {
        writeAsJson(profile(rules))
    }
}

internal fun writeRules(rules: List<Rule>): () -> String = {
    StringBuilder()
        .apply {
            appendln(headerTemplate())

            rules.forEach {
                appendln(ruleTemplate(it))
            }
        }
        .toString()
}

private fun readVersion(): String {
    return parseVersion(fetch("index.html"))
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
    return parseRuleCategories(fetch("index.html"))
}

private fun fetchRulesFor(category: RuleCategory): List<Rule> {
    return parseRules(category, fetch(basenamePath(category)))
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
