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

package me.raatiniemi.oclint.rules.writer

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

/**
 * Write content to file at the supplied path.
 */
internal fun writeToFile(path: String, contents: () -> String) = File(path).printWriter()
    .use { it.print(contents()) }

/**
 * Preconfigured Jackson JSON mapper.
 */
private val jsonMapper = jacksonObjectMapper()
    .enable(SerializationFeature.INDENT_OUTPUT)

/**
 * Write any object to an JSON document.
 */
internal val writeAsJson: (Any) -> String = { value ->
    jsonMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(value)
        .let { "$it\n" }
}
