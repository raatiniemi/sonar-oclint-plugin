/*
 * Copyright © 2012 OCTO Technology, Backelite (${email})
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
package me.raatiniemi.sonar.oclint

import me.raatiniemi.sonar.core.SensorPersistence
import org.sonar.api.batch.fs.FileSystem
import org.sonar.api.batch.fs.InputFile
import org.sonar.api.batch.sensor.SensorContext
import org.sonar.api.rule.RuleKey
import org.sonar.api.utils.log.Loggers
import java.util.*

internal class OCLintSensorPersistence private constructor(
    context: SensorContext,
    private val fileSystem: FileSystem
) : SensorPersistence<Violation>(context) {
    override fun saveMeasures(measures: Collection<Violation>) = measures.groupBy { it.path }
        .forEach { (path, violations) ->
            saveViolationsGroupedByFile(path, violations)
        }

    private fun saveViolationsGroupedByFile(path: String, violations: List<Violation>) {
        val value = buildInputFile(path)
        value.map {
            violations.forEach { violation ->
                val rule = RuleKey.of(OCLintRulesDefinition.REPOSITORY_KEY, violation.rule)
                if (isRuleActive(rule)) {
                    LOGGER.warn("\"{}\" is not an active rule", rule.toString())
                    return@forEach
                }

                val newIssue = context.newIssue().forRule(rule)
                val location = newIssue.newLocation()
                    .on(it)
                    .at(it.selectLine(violation.startLine))
                    .message(violation.message)

                newIssue.at(location).save()
            }
        }
    }

    private fun buildInputFile(path: String): Optional<InputFile> {
        return buildInputFile(fileSystem.predicates().hasPath(path), path)
    }

    private fun isRuleActive(rule: RuleKey): Boolean {
        return null == context.activeRules().find(rule)
    }

    companion object {
        private val LOGGER = Loggers.get(OCLintSensorPersistence::class.java)

        @JvmStatic
        fun create(context: SensorContext): OCLintSensorPersistence {
            return OCLintSensorPersistence(context, context.fileSystem())
        }
    }
}
