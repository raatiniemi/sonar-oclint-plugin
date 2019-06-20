/*
 * Copyright © 2012 OCTO Technology, Backelite (${email})
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
import org.sonar.api.rules.RuleType
import org.sonar.api.server.rule.RulesDefinition
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import kotlin.streams.toList

class OCLintRulesDefinition : RulesDefinition {
    override fun define(context: RulesDefinition.Context) {
        val repository = createRepository(context)

        try {
            loadRules(repository)
        } catch (e: IOException) {
            LOGGER.error("Failed to load OCLint rules", e)
        }

        repository.done()
    }

    internal fun createRepository(context: RulesDefinition.Context): RulesDefinition.NewRepository {
        return context.createRepository(REPOSITORY_KEY, "objc")
            .setName(REPOSITORY_NAME)
    }

    internal fun loadRules(repository: RulesDefinition.NewRepository) {
        val reader = BufferedReader(
            InputStreamReader(
                javaClass.getResourceAsStream(RULES_FILE),
                StandardCharsets.UTF_8
            )
        )

        val listLines = reader.lines().toList()
        populateRepositoryWithRulesFromLines(repository, listLines)
    }

    internal fun populateRepositoryWithRulesFromLines(
        repository: RulesDefinition.NewRepository,
        listLines: List<String>
    ) {
        parser.parseRuleDefinitionsFromLines(listLines)
            .forEach { (key, name, description, severity, type) ->
                repository.createRule(key)
                    .apply {
                        setName(name)
                        setSeverity(severity)
                        setHtmlDescription(description)
                        setType(RuleType.valueOf(type))
                    }
            }
    }

    companion object {
        private const val RULES_FILE = "/me/raatiniemi/sonar/oclint/rules.txt"
        internal const val REPOSITORY_KEY = "OCLint"
        internal const val REPOSITORY_NAME = REPOSITORY_KEY

        private val LOGGER = LoggerFactory.getLogger(OCLintRulesDefinition::class.java)

        private val parser = RuleDefinitionParser.create()
    }
}
