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

import org.apache.tools.ant.DirectoryScanner
import org.sonar.api.utils.log.Loggers
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

internal class ReportFinder private constructor(private val reportDirectory: File) {
    private val reportDirectoryPath: String by lazy {
        reportDirectory.absolutePath
    }

    fun findReportMatching(pattern: String): File? {
        LOGGER.debug("Trying to find report matching {} in {}", pattern, reportDirectoryPath)

        return getBasenameForMatchingFiles(pattern)
            .map(prependBaseDirectoryPath())
            .map { it.toFile() }
            .firstOrNull()
    }

    private fun getBasenameForMatchingFiles(pattern: String): List<String> {
        if (!reportDirectory.exists()) {
            LOGGER.warn("Report directory do not exists {}", reportDirectoryPath)
            return emptyList()
        }

        val scanner = DirectoryScanner()
        scanner.setIncludes(arrayOf(pattern))
        scanner.basedir = reportDirectory
        scanner.scan()

        val basenameForFiles = scanner.includedFiles
        val numberOfFiles = basenameForFiles.size

        if (numberOfFiles == 0) {
            LOGGER.debug("No report(s) matching {} was found in {}", pattern, reportDirectoryPath)
            return emptyList()
        }

        LOGGER.debug("Found {} report(s) matching {} in {}", numberOfFiles, pattern, reportDirectoryPath)
        return basenameForFiles.toList()
    }

    private fun prependBaseDirectoryPath(): (String) -> Path {
        return { filename ->
            Paths.get(reportDirectoryPath, filename)
        }
    }

    companion object {
        private val LOGGER = Loggers.get(ReportFinder::class.java)

        fun create(reportDirectory: File): ReportFinder {
            return ReportFinder(reportDirectory)
        }
    }
}
