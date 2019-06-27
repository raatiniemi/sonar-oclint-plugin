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

package me.raatiniemi.oclint.rules.parser

import me.raatiniemi.oclint.rules.Rule
import me.raatiniemi.oclint.rules.RuleCategory
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

internal fun parseVersion(document: Document): String {
    return document.select("#rule-index > p")
        .map { it.text() }
        .mapNotNull {
            val regex = """OCLint ([\d\.]+) includes""".toRegex()
            val (version) = regex.find(it)?.destructured ?: return@mapNotNull null
            return@mapNotNull version
        }
        .firstOrNull() ?: ""
}

internal fun parseRuleCategories(document: Document): List<String> {
    return document.select(".toctree-l1 > a")
        .map { it.text() }
}

internal fun parseRules(category: RuleCategory, document: Document) =
    document.select(".section > .section")
        .map { parseRule(category, html = it) }

private fun parseRule(category: RuleCategory, html: Element): Rule {
    val elements = skipVersion(elementsFrom(html))

    return Rule(
        name = readName(elements),
        description = readDescription(elements),
        category = category.name,
        severity = category.severity
    )
}

private fun elementsFrom(html: Element): Elements = html.select("p, pre, dl")

private fun skipVersion(elements: List<Element>) = elements.drop(1)

private fun readName(elements: List<Element>): String {
    return elements.first()
        .select("p > strong")
        .text()
        .removePrefix("Name: ")
        .capitalize()
}

private fun readDescription(elements: List<Element>): String {
    return elements.drop(1)
        .joinToString(separator = "\n") { buildElement(it) }
}

private fun buildElement(element: Element): String {
    val tagName = element.tagName()// ?: return ""

    return when (tagName) {
        "pre" -> "<pre>${element.text()}</pre>"
        null -> ""
        else -> "<$tagName>${removeNewLines(element)}</$tagName>"
    }
}

private fun removeNewLines(element: Element): String {
    return element.html().replace("\n", "")
}
