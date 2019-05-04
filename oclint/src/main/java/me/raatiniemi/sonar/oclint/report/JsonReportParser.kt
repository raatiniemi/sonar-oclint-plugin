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
import org.sonar.api.utils.log.Loggers
import java.io.File
import java.io.IOException

internal class JsonReportParser(private val objectMapper: ObjectMapper) : ViolationReportParser {
    override fun parse(reportFile: File): List<Violation> {
        return try {
            val report = objectMapper.readValue(reportFile, JsonReport::class.java)

            report.violations.map { transformToViolation(it) }
        } catch (e: IOException) {
            LOGGER.warn("Unable to parse report file: ${reportFile.path}")
            emptyList()
        }
    }

    private fun transformToViolation(violation: JsonReport.Violation) = Violation.builder()
        .setPath(violation.path)
        .setStartLine(readStartLine(violation))
        .setRule(violation.rule)
        .setMessage(violation.message)
        .build()

    private fun readStartLine(violation: JsonReport.Violation): Int = with(violation) {
        if (startLine == 0) {
            LOGGER.warn("Found empty start line in report for path: {}", path)
            return 1
        }

        return startLine
    }

    companion object {
        private val LOGGER = Loggers.get(JsonReportParser::class.java)
    }
}
