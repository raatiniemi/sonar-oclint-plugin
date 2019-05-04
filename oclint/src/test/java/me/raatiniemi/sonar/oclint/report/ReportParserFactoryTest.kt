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

import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

@RunWith(JUnit4::class)
class ReportParserFactoryTest {
    private lateinit var factory: ReportParserFactory

    @Before
    fun setUp() {
        factory = ReportParserFactory.create()
    }

    @Test(expected = UnsupportedViolationReportTypeException::class)
    fun from_withTextViolationReportType() {
        val reportFile = File("oclint.txt")

        factory.from(reportFile)
    }

    @Test(expected = UnsupportedViolationReportTypeException::class)
    fun from_withHtmlViolationReportType() {
        val reportFile = File("oclint.html")

        factory.from(reportFile)
    }

    @Test(expected = UnsupportedViolationReportTypeException::class)
    fun from_withPmdViolationReportType() {
        val reportFile = File("oclint.pmd")

        factory.from(reportFile)
    }

    @Test
    fun from_withXmlViolationReportType() {
        val reportFile = File("oclint.xml")

        val parser = factory.from(reportFile)

        assertTrue(parser is XmlReportParser)
    }

    @Test
    fun from_withJsonViolationReportType() {
        val reportFile = File("oclint.json")

        val parser = factory.from(reportFile)

        assertTrue(parser is JsonReportParser)
    }
}
