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
        .map { Rule.from(category, html = it) }
