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

import me.raatiniemi.sonar.core.ReportFinder
import me.raatiniemi.sonar.core.ReportSensor
import me.raatiniemi.sonar.oclint.report.ReportParserFactory
import org.sonar.api.Properties
import org.sonar.api.Property
import org.sonar.api.batch.sensor.SensorContext
import org.sonar.api.batch.sensor.SensorDescriptor
import org.sonar.api.config.Configuration
import java.io.File
import java.util.*

@Properties(
    Property(
        key = OCLintSensor.REPORT_PATH_KEY,
        defaultValue = OCLintSensor.DEFAULT_REPORT_PATH,
        name = "Path to OCLint violation report",
        description = "Relative to projects' root.",
        global = false,
        project = true
    )
)
class OCLintSensor(configuration: Configuration) : ReportSensor(configuration) {
    private val reportParserFactory = ReportParserFactory.create()

    override fun describe(descriptor: SensorDescriptor) {
        descriptor.name(NAME)
        descriptor.onlyOnLanguage("objc")
    }

    override fun execute(context: SensorContext) {
        val violations = collectAndParseAvailableReports(context.fileSystem().baseDir())

        val persistence = OCLintSensorPersistence.create(context)
        persistence.saveMeasures(violations)
    }

    private fun collectAndParseAvailableReports(projectDirectory: File) = findReport(projectDirectory)
        .map { parseReport(it) }
        .orElse(emptyList())

    private fun findReport(projectDirectory: File): Optional<File> {
        return ReportFinder.create(projectDirectory)
            .findReportMatching(readReportPath())
    }

    private fun parseReport(reportFile: File): List<Violation> {
        val parser = reportParserFactory.from(reportFile)
        val violations = parser.parse(reportFile)

        return violations.orElse(emptyList())
    }

    override fun getReportPathKey() = REPORT_PATH_KEY

    override fun getDefaultReportPath() = DEFAULT_REPORT_PATH

    companion object {
        const val REPORT_PATH_KEY = "sonar.objectivec.oclint.reportPath"
        const val DEFAULT_REPORT_PATH = "sonar-reports/oclint.xml"

        private const val NAME = "OCLint violation sensor"
    }
}
