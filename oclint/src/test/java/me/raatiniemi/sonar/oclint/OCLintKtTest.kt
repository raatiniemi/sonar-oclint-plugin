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

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.sonar.api.config.Configuration
import org.sonar.api.config.internal.MapSettings

@RunWith(JUnit4::class)
class OCLintKtTest {
    private val settings = MapSettings()
    private val configuration: Configuration by lazy {
        settings.asConfig()
    }

    @Test
    fun `read report path without value for keys`() {
        val expected = CONFIG_REPORT_PATH_DEFAULT_VALUE

        val actual = readReportPath(configuration)

        assertEquals(expected, actual)
    }

    @Test
    fun `read report path with value for key`() {
        val expected = "oclint.xml"
        settings.setProperty(CONFIG_REPORT_PATH_KEY, expected)

        val actual = readReportPath(configuration)

        assertEquals(expected, actual)
    }

    @Test
    fun `read report path with value for deprecated key`() {
        val expected = "oclint.xml"
        settings.setProperty(DEPRECATED_CONFIG_REPORT_PATH_KEY, expected)

        val actual = readReportPath(configuration)

        assertEquals(expected, actual)
    }

    @Test
    fun `read report path with value for both keys`() {
        val expected = "oclint.xml"
        settings.setProperty(CONFIG_REPORT_PATH_KEY, expected)
        settings.setProperty(DEPRECATED_CONFIG_REPORT_PATH_KEY, "deprecated.xml")

        val actual = readReportPath(configuration)

        assertEquals(expected, actual)
    }
}
