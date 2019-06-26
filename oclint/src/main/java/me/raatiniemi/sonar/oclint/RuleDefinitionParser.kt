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

package me.raatiniemi.sonar.oclint

import org.slf4j.LoggerFactory
import java.util.*

internal class RuleDefinitionParser private constructor() {
    fun parseRuleDefinitionsFromLines(lines: List<String>): Set<RuleDefinition> {
        val rulesDefinitions = LinkedHashSet<RuleDefinition>()

        var previousLine: String? = null
        var inDescription = false

        var builder: RuleDefinition.Builder = RuleDefinition.builder()
        for (line in lines) {
            if (isLineIgnored(line)) {
                inDescription = false
                previousLine = line
                continue
            }

            if (isLineSeparator(line)) {
                LOGGER.debug("RuleDefinition found : {}", previousLine)

                builder = RuleDefinition.builder()
                builder.setKey(Objects.requireNonNull<String>(previousLine))
                val name = previousLine?.capitalize() ?: ""
                builder.setName(Objects.requireNonNull(name))
                inDescription = false
                previousLine = line
                continue
            }

            if (isSummary(line)) {
                val summary = line.substring(line.indexOf(':') + 1)
                builder.setDescription("<p>$summary</p>")
                inDescription = true
                previousLine = line
                continue
            }

            if (isCategory(line)) {
                rulesDefinitions.add(builder.build())
                inDescription = false
                previousLine = line
                continue
            }

            if (isSeverity(line)) {
                val severity = line.substring("Severity: ".length)
                builder.setSeverity(RuleSeverity.valueOfInt(Integer.valueOf(severity)).name)
                inDescription = false
                previousLine = line
                continue
            }

            if (isType(line)) {
                val type = line.substring("Type: ".length)
                builder.setType(type)
                inDescription = false
                previousLine = line
                continue
            }

            if (inDescription) {
                builder.appendToDescription(line)
            }
            previousLine = line
        }

        return rulesDefinitions
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RuleDefinitionParser::class.java)

        private fun isLineIgnored(line: String): Boolean {
            return line.matches("\\=.*".toRegex()) || line.matches("Priority:.*".toRegex())
        }

        private fun isLineSeparator(line: String): Boolean {
            return line.matches("[\\-]{4,}.*".toRegex())
        }

        private fun isSummary(line: String): Boolean {
            return line.matches("Summary:.*".toRegex())
        }

        private fun isCategory(line: String): Boolean {
            return line.matches("Category:.*".toRegex())
        }

        private fun isSeverity(line: String): Boolean {
            return line.matches("Severity:.*".toRegex())
        }

        private fun isType(line: String): Boolean {
            return line.matches("Type:.*".toRegex())
        }

        fun create() = RuleDefinitionParser()
    }
}
