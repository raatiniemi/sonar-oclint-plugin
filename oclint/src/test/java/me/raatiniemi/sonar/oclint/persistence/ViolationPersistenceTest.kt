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
package me.raatiniemi.sonar.oclint.persistence

import me.raatiniemi.sonar.oclint.OCLintRulesDefinition
import me.raatiniemi.sonar.oclint.Violation
import me.raatiniemi.sonar.oclint.addToFileSystem
import me.raatiniemi.sonar.oclint.mainFile
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder
import org.sonar.api.batch.rule.internal.DefaultActiveRules
import org.sonar.api.batch.sensor.internal.SensorContextTester
import org.sonar.api.rule.RuleKey
import org.sonar.api.utils.log.LogTester
import org.sonar.api.utils.log.LoggerLevel
import java.util.*

@RunWith(JUnit4::class)
class ViolationPersistenceTest {
    @Rule
    @JvmField
    var temporaryFolder = TemporaryFolder()

    @Rule
    @JvmField
    var logTester = LogTester()

    private lateinit var context: SensorContextTester
    private lateinit var persistence: ViolationPersistence

    @Before
    fun setUp() {
        context = SensorContextTester.create(temporaryFolder.root)

        persistence = ViolationPersistence.create(context)

        val builder = ActiveRulesBuilder()
        val rules = listOf(
            builder.create(RuleKey.of(OCLintRulesDefinition.REPOSITORY_KEY, "deep nested block")),
            builder.create(RuleKey.of(OCLintRulesDefinition.REPOSITORY_KEY, "unused method parameter"))
        )
        context.setActiveRules(DefaultActiveRules(rules))
    }

    private fun isIssuePresent(ruleKey: String): Boolean {
        val ruleKeyWithRepository = "OCLint:$ruleKey"

        return context.allIssues()
            .map { it.ruleKey() }
            .map { it.toString() }
            .any { it.equals(ruleKeyWithRepository, ignoreCase = true) }
    }

    @Test
    fun saveMeasures_withoutMeasures() {
        persistence.saveMeasures(emptyList())

        assertTrue(context.allIssues().isEmpty())
        assertTrue(logTester.logs().isEmpty())
    }

    @Test
    fun saveMeasures_withoutMatchingFile() {
        val violations = listOf(
            Violation.builder()
                .setPath("TargetName/ClassName.m")
                .setStartLine(1)
                .setMessage("Block depth of 6 exceeds limit of 5")
                .setRule("deep nested block")
                .build()
        )

        persistence.saveMeasures(violations)

        assertTrue(context.allIssues().isEmpty())
        assertTrue(logTester.logs(LoggerLevel.WARN).contains("No path available for TargetName/ClassName.m"))
    }

    @Test
    fun saveMeasures_withMeasure() {
        val violations = listOf(
            Violation.builder()
                .setPath("TargetName/ClassName.m")
                .setStartLine(1)
                .setMessage("Block depth of 6 exceeds limit of 5")
                .setRule("deep nested block")
                .build()
        )
        addToFileSystem(context) {
            mainFile(module()) {
                relativePath = "TargetName/ClassName.m"
                language = "objc"
            }
        }

        persistence.saveMeasures(violations)

        assertTrue(isIssuePresent("deep nested block"))
        assertTrue(logTester.logs().isEmpty())
    }

    @Test
    fun saveMeasures_withMeasures() {
        val violations = listOf(
            Violation.builder()
                .setPath("TargetName/ClassName.m")
                .setStartLine(1)
                .setMessage("Block depth of 6 exceeds limit of 5")
                .setRule("deep nested block")
                .build(),
            Violation.builder()
                .setPath("TargetName/ClassName.m")
                .setStartLine(1)
                .setMessage("The parameter 'commit' is unused.")
                .setRule("unused method parameter")
                .build()
        )
        addToFileSystem(context) {
            mainFile(module()) {
                relativePath = "TargetName/ClassName.m"
                language = "objc"
            }
        }

        persistence.saveMeasures(violations)

        assertTrue(isIssuePresent("deep nested block"))
        assertTrue(isIssuePresent("unused method parameter"))
        assertTrue(logTester.logs().isEmpty())
    }

    @Test
    fun saveMeasures_withUnknownRule() {
        val violations = listOf(
            Violation.builder()
                .setPath("TargetName/ClassName.m")
                .setStartLine(1)
                .setMessage("Message for unknown rule")
                .setRule("unknown rule")
                .build()
        )
        addToFileSystem(context) {
            mainFile(module()) {
                relativePath = "TargetName/ClassName.m"
                language = "objc"
            }
        }

        persistence.saveMeasures(violations)

        assertTrue(isIssuePresent("unknown rule"))
        assertTrue(logTester.logs().isEmpty())
    }

    @Test
    fun saveMeasures_withFileForAnotherLanguage() {
        val violations = LinkedHashSet<Violation>()
        violations.add(
            Violation.builder()
                .setPath("TargetName/ClassName.swift")
                .setStartLine(1)
                .setMessage("Block depth of 6 exceeds limit of 5")
                .setRule("deep nested block")
                .build()
        )
        addToFileSystem(context) {
            mainFile(module()) {
                relativePath = "TargetName/ClassName.swift"
                language = "swift"
            }
        }

        persistence.saveMeasures(violations)

        assertFalse(isIssuePresent("deep nested block"))
        assertTrue(logTester.logs(LoggerLevel.DEBUG).contains("TargetName/ClassName.swift belong to language swift"))
    }
}
