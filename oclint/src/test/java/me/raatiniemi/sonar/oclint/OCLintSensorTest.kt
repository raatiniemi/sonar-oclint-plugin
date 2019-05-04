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
package me.raatiniemi.sonar.oclint

import me.raatiniemi.sonar.core.internal.FileSystemHelpers
import me.raatiniemi.sonar.oclint.report.SampleReport
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.sonar.api.batch.fs.InputComponent
import org.sonar.api.batch.fs.InputFile
import org.sonar.api.batch.fs.internal.TestInputFileBuilder
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder
import org.sonar.api.batch.rule.internal.DefaultActiveRules
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor
import org.sonar.api.batch.sensor.internal.SensorContextTester
import org.sonar.api.batch.sensor.issue.Issue
import org.sonar.api.batch.sensor.issue.IssueLocation
import org.sonar.api.config.internal.MapSettings
import org.sonar.api.rule.RuleKey
import org.sonar.api.utils.log.LogTester
import org.sonar.api.utils.log.LoggerLevel
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

@RunWith(JUnit4::class)
class OCLintSensorTest {
    private val resourcePath = Paths.get("src", "test", "resources", "oclint", "report")
    private val settings = MapSettings()

    @Rule
    @JvmField
    var temporaryFolder = TemporaryFolder()

    @Rule
    @JvmField
    var logTester = LogTester()

    private lateinit var context: SensorContextTester
    private lateinit var sensor: OCLintSensor

    private fun transformIssuesToViolations(issues: Collection<Issue>) = issues
        .map(transformIssueToViolation())
        .sorted()

    private fun transformIssueToViolation(): (Issue) -> Violation = { issue ->
        val issueLocation = issue.primaryLocation()

        val builder = Violation.builder()
            .setPath(removePrefixFromKey(issueLocation.inputComponent()))
            .setStartLine(readStartLine(issueLocation))
            .setRule(issue.ruleKey().rule())

        issueLocation.message()
            ?.let { builder.setMessage(it) }

        builder.build()
    }

    private fun removePrefixFromKey(inputComponent: InputComponent): String {
        val key = inputComponent.key()
        return key.replace("projectKey:", "")
    }

    private fun readStartLine(issueLocation: IssueLocation): Int {
        val textRange = issueLocation.textRange() ?: return 1
        return textRange.start().line()
    }

    @Before
    fun setUp() {
        context = SensorContextTester.create(temporaryFolder.root)
        sensor = OCLintSensor(settings.asConfig())

        val builder = ActiveRulesBuilder()
        val rules = listOf(
            builder.create(RuleKey.of(OCLintRulesDefinition.REPOSITORY_KEY, "long line")),
            builder.create(RuleKey.of(OCLintRulesDefinition.REPOSITORY_KEY, "unused method parameter")),
            builder.create(RuleKey.of(OCLintRulesDefinition.REPOSITORY_KEY, "parameter reassignment"))
        )
        context.setActiveRules(DefaultActiveRules(rules))

        val helpers = FileSystemHelpers.create(context)
        helpers.addToFileSystem(createFile("sample-project/API/ProductDetailAPIClient.m"))
        helpers.addToFileSystem(createFile("sample-project/API/FundFinderAPIClient.m"))
        helpers.addToFileSystem(createFile("sample-project/API/MobileAPIClient.m"))
        helpers.addToFileSystem(createFile("sample-project/API/ProductListingAPIClient.m"))
        helpers.addToFileSystem(createFile("sample-project/InsightsAPIClient.m"))
        helpers.addToFileSystem(createFile("sample-project/ChannelContentAPIClient.m"))
        helpers.addToFileSystem(createFile("sample-project/ViewAllHoldingsAPIClient.m"))
    }

    private fun buildInputFile(relativePath: String): TestInputFileBuilder {
        val content = (1..100).joinToString("\n") {
            it.toString()
        }

        return TestInputFileBuilder(context.module().key(), relativePath)
            .setLanguage("objc")
            .initMetadata(content)
    }

    private fun createFile(relativePath: String) = buildInputFile(relativePath)
        .setType(InputFile.Type.MAIN)
        .build()

    private fun createReportFile(sourcePath: String, destinationPath: String) {
        try {
            val reportLines = Files.readAllLines(Paths.get(resourcePath.toString(), sourcePath))

            val destination = Paths.get(temporaryFolder.root.absolutePath, destinationPath)
            Files.createDirectories(destination.parent)
            Files.createFile(destination)
            Files.write(destination, reportLines)
        } catch (e: IOException) {
            fail(String.format("Unable to create report file: %s", e.message))
        }
    }

    @Test
    fun describe() {
        val descriptor = DefaultSensorDescriptor()

        sensor.describe(descriptor)

        assertEquals("OCLint violation sensor", descriptor.name())
        assertTrue(descriptor.languages().contains("objc"))
    }

    @Test
    fun `execute without value for report path keys`() {
        createReportFile("sample.xml", CONFIG_REPORT_PATH_DEFAULT_VALUE)
        val expected = SampleReport.build()

        sensor.execute(context)

        val actual = transformIssuesToViolations(context.allIssues())
        assertEquals(expected, actual)
        assertTrue(logTester.logs().contains("Found no report path, using default path"))
    }

    @Test
    fun `execute with xml report path`() {
        settings.setProperty(CONFIG_REPORT_PATH_KEY, "oclint.xml")
        createReportFile("sample.xml", "oclint.xml")
        val expected = SampleReport.build()

        sensor.execute(context)

        val actual = transformIssuesToViolations(context.allIssues())
        assertEquals(expected, actual)
        assertTrue(logTester.logs(LoggerLevel.WARN).isEmpty())
    }

    @Test
    fun `execute with json report path`() {
        settings.setProperty(CONFIG_REPORT_PATH_KEY, "oclint.json")
        createReportFile("sample.json", "oclint.json")
        val expected = SampleReport.build()

        sensor.execute(context)

        val actual = transformIssuesToViolations(context.allIssues())
        assertEquals(expected, actual)
        assertTrue(logTester.logs(LoggerLevel.WARN).isEmpty())
    }

    @Test
    fun `execute with xml report path for deprecated key`() {
        settings.setProperty(DEPRECATED_CONFIG_REPORT_PATH_KEY, "oclint.xml")
        createReportFile("sample.xml", "oclint.xml")
        val expected = SampleReport.build()

        sensor.execute(context)

        val actual = transformIssuesToViolations(context.allIssues())
        assertEquals(expected, actual)
        assertTrue(
            logTester.logs(LoggerLevel.WARN)
                .contains("Using deprecated report path key, use $CONFIG_REPORT_PATH_KEY instead")
        )
    }

    @Test
    fun `execute with json report path for deprecated key`() {
        settings.setProperty(DEPRECATED_CONFIG_REPORT_PATH_KEY, "oclint.json")
        createReportFile("sample.json", "oclint.json")
        val expected = SampleReport.build()

        sensor.execute(context)

        val actual = transformIssuesToViolations(context.allIssues())
        assertEquals(expected, actual)
        assertTrue(
            logTester.logs(LoggerLevel.WARN)
                .contains("Using deprecated report path key, use $CONFIG_REPORT_PATH_KEY instead")
        )
    }
}
