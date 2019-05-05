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

package me.raatiniemi.sonar.oclint

import org.sonar.api.batch.fs.InputFile
import org.sonar.api.batch.fs.InputModule
import org.sonar.api.batch.fs.internal.DefaultInputFile
import org.sonar.api.batch.fs.internal.TestInputFileBuilder
import org.sonar.api.batch.sensor.SensorContext
import org.sonar.api.batch.sensor.internal.SensorContextTester

internal class InputFileConfiguration(val module: InputModule) {
    var relativePath: String = ""
    var language: String = ""
    var type: InputFile.Type = InputFile.Type.MAIN
    var content: () -> String = {
        (1..100).joinToString("\n") { it.toString() }
    }
}

internal fun mainFile(module: InputModule, configure: InputFileConfiguration.() -> Unit) =
    InputFileConfiguration(module)
        .also {
            it.configure()
        }
        .let(inputFileBuilder())
        .build()

private fun inputFileBuilder(): InputFileConfiguration.() -> TestInputFileBuilder {
    return {
        TestInputFileBuilder(module.key(), relativePath)
            .also {
                if (language.isNotBlank()) {
                    it.setLanguage(language)
                }
            }
            .setType(type)
            .initMetadata(content())
    }
}

internal fun addToFileSystem(context: SensorContextTester): (DefaultInputFile) -> Unit {
    return { inputFile ->
        addToFileSystem(context, inputFile)
    }
}

internal fun addToFileSystem(context: SensorContextTester, buildInputFile: SensorContext.() -> DefaultInputFile) {
    addToFileSystem(context, buildInputFile(context))
}

private fun addToFileSystem(context: SensorContextTester, file: DefaultInputFile) {
    context.fileSystem().add(file)
}
