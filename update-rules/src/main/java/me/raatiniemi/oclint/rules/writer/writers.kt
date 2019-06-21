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
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File

/**
 * Write content to file at the supplied path.
 */
internal fun writeToFile(path: String, contents: () -> String) = File(path).printWriter()
    .use { it.print(contents()) }

/**
 * Preconfigured Jackson XML mapper.
 */
private val xmlMapper by lazy {
    val module = JacksonXmlModule()
    module.setDefaultUseWrapper(false)

    val mapper = XmlMapper(module)
    mapper.enable(SerializationFeature.INDENT_OUTPUT)
}

/**
 * Write any object to an xml document.
 */
internal val writeAsXml: (Any) -> String = {
    StringBuilder()
        .apply {
            appendln("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>")
            append(xmlMapper.writeValueAsString(it))
        }
        .toString()
}
