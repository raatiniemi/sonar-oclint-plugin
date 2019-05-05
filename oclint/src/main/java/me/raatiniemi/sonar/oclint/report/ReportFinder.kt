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
