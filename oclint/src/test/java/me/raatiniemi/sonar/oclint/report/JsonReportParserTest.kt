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

package me.raatiniemi.sonar.oclint.report

import com.fasterxml.jackson.databind.ObjectMapper
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

@RunWith(JUnit4::class)
class JsonReportParserTest {
    private val resourcePath = Paths.get("src", "test", "resources", "oclint", "report")

    @Rule
    @JvmField
    var logTester = LogTester()

    private lateinit var parser: JsonReportParser

    @Before
    fun setUp() {
        parser = JsonReportParser(ObjectMapper())
    }

    @Test
    fun parse_withEmptyReport() {
        val documentPath = Paths.get(resourcePath.toString(), "empty.json")
        val expected = emptyList<Violation>()

        val actual = parser.parse(documentPath.toFile())

        assertTrue("No violations are available", actual.isPresent)
        assertEquals(expected, actual.get())
    }

    @Test
    fun parse_withReportUsingZeroAsStartLine() {
        val documentPath = Paths.get(resourcePath.toString(), "with-empty-start-line.json")
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
    fun parse_withSampleReport() {
        val documentPath = Paths.get(resourcePath.toString(), "sample.json")
        val expected = SampleReport.build()

        val actual = parser.parse(documentPath.toFile())

        assertTrue("No violations are available", actual.isPresent)
        assertEquals(expected, actual.get().sorted())
    }
}
