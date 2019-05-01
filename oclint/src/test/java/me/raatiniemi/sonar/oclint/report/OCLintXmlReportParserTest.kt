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
package me.raatiniemi.sonar.oclint.report

import me.raatiniemi.sonar.oclint.Violation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.sonar.api.utils.log.LogTester
import org.sonar.api.utils.log.LoggerLevel
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory

@RunWith(JUnit4::class)
class OCLintXmlReportParserTest {
    private val resourcePath = Paths.get("src", "test", "resources", "oclint", "report")

    @Rule
    @JvmField
    var logTester = LogTester()

    private lateinit var parser: OCLintXmlReportParser

    @Before
    fun setUp() {
        val factory = DocumentBuilderFactory.newInstance()
        parser = OCLintXmlReportParser(factory.newDocumentBuilder())
    }

    @Test
    fun parse_withEmptyDocument() {
        val documentPath = Paths.get(resourcePath.toString(), "empty.xml")

        val actual = parser.parse(documentPath.toFile())

        assertTrue("No violations are available", actual.isPresent)
        assertTrue(actual.get().isEmpty())
    }

    @Test
    fun parse_withDocumentWithEmptyStartLine() {
        val documentPath = Paths.get(resourcePath.toString(), "with-empty-start-line.xml")
        val expected = listOf(
            Violation.builder()
                .setPath("sample-project/API/ProductDetailAPIClient.m")
                .setStartLine(1)
                .setRule("long line")
                .setMessage("Line with 115 characters exceeds limit of 100")
                .build()
        )

        val actual = parser.parse(documentPath.toFile())

        assertTrue("No violations are available", actual.isPresent)
        assertEquals(expected, actual.get())
        assertTrue(logTester.logs(LoggerLevel.WARN).contains("Found empty start line in report for path: sample-project/API/ProductDetailAPIClient.m"))
    }

    @Test
    fun parse_withSampleDocument() {
        val documentPath = Paths.get(resourcePath.toString(), "sample.xml")
        val expected = SampleReport.build()

        val actual = parser.parse(documentPath.toFile())

        assertTrue("No violations are available", actual.isPresent)
        assertEquals(expected, actual.get().sorted())
    }
}
