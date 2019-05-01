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
import org.sonar.api.utils.log.Loggers
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class ReportParserFactory private constructor() {
    fun from(file: File): ViolationReportParser {
        if (isXml(file)) {
            return buildXmlReportParser()
        }

        if (isJson(file)) {
            return buildJsonReportParser()
        }

        throw UnsupportedViolationReportTypeException()
    }

    private fun buildXmlReportParser(): OCLintXmlReportParser {
        return OCLintXmlReportParser(createDocumentBuilder())
    }

    private fun createDocumentBuilder(): DocumentBuilder {
        try {
            return DocumentBuilderFactory.newInstance()
                .apply {
                    setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
                    setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
                    isXIncludeAware = false
                    isExpandEntityReferences = false
                }
                .run { newDocumentBuilder() }
        } catch (e: ParserConfigurationException) {
            LOGGER.error("Unable to create new document builder", e)
            throw UnableToConfigureXmlReportParserException("Unable to create DocumentBuilderFactory", e)
        }
    }

    private fun buildJsonReportParser(): ViolationReportParser {
        val objectMapper = ObjectMapper()
        return JsonReportParser(objectMapper)
    }

    companion object {
        private val LOGGER = Loggers.get(ReportParserFactory::class.java)

        @JvmStatic
        fun create() = ReportParserFactory()
    }
}

private fun isXml(file: File) = file.path.endsWith("xml")

private fun isJson(file: File) = file.path.endsWith("json")
