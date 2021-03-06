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

import me.raatiniemi.sonar.oclint.Violation
import org.sonar.api.utils.log.Loggers
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import javax.xml.parsers.DocumentBuilder

internal class XmlReportParser(private val documentBuilder: DocumentBuilder) : ViolationReportParser {
    override fun parse(reportFile: File): List<Violation> {
        if (!reportFile.exists()) {
            LOGGER.warn("No XML report exist at path: {}", reportFile)
            return emptyList()
        }

        return try {
            val document = documentBuilder.parse(reportFile)
            parse(document)
        } catch (e: SAXException) {
            LOGGER.error("Unable to process XML file named: {}", reportFile, e)
            emptyList()
        } catch (e: IOException) {
            LOGGER.error("Unable to process XML file named: {}", reportFile, e)
            emptyList()
        }
    }

    private fun parse(document: Document): List<Violation> {
        return getViolationElements(document)
            .map { buildViolation(it) }
    }

    companion object {
        private val LOGGER = Loggers.get(XmlReportParser::class.java)

        private const val VIOLATION = "violation"
        private const val PATH = "path"
        private const val START_LINE = "startline"
        private const val RULE = "rule"
        private const val MESSAGE = "message"

        private fun buildViolation(element: Element): Violation = Violation.builder()
            .setPath(parsePath(element))
            .setStartLine(parseStartLine(element))
            .setRule(parseRule(element))
            .setMessage(parseMessage(element))
            .build()

        private fun parsePath(element: Element) = element.getAttribute(PATH)

        private fun parseStartLine(element: Element) = try {
            Integer.parseInt(element.getAttribute(START_LINE))
        } catch (e: NumberFormatException) {
            LOGGER.warn("Found empty start line in report for path: {}", parsePath(element))
            1
        }

        private fun parseRule(element: Element) = element.getAttribute(RULE)

        private fun parseMessage(element: Element) = element.getAttribute(MESSAGE)

        private fun getViolationElements(document: Document) = getElements(document, VIOLATION)
    }
}
