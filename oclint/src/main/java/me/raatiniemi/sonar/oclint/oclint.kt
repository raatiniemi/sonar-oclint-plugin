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

import org.sonar.api.config.Configuration

internal fun readReportPath(configuration: Configuration): Pair<String, String> {
    val keys = listOf(CONFIG_REPORT_PATH_KEY, DEPRECATED_CONFIG_REPORT_PATH_KEY)

    return readFirstKey(configuration, keys) ?: Pair("none", CONFIG_REPORT_PATH_DEFAULT_VALUE)
}

private fun readFirstKey(configuration: Configuration, keys: List<String>) =
    readKeys(configuration, keys).firstOrNull()

private fun readKeys(configuration: Configuration, keys: List<String>) =
    keys.filter { configuration.hasKey(it) }
        .mapNotNull(readKey(configuration))

private fun readKey(configuration: Configuration): (String) -> Pair<String, String>? {
    return { key ->
        configuration.get(key)
            .takeIf { it.isPresent }
            ?.let { Pair(key, it.get()) }
    }
}
