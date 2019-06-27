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

package me.raatiniemi.oclint.rules.parser

import me.raatiniemi.oclint.rules.readResource
import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ParserKtTest {
    @Test
    fun `parse version with document`() {
        val document = Jsoup.parse(readResource("index.html"))
        val expected = "0.13"

        val actual = parseVersion(document)

        assertEquals(expected, actual)
    }

    @Test
    fun `parse rule categories with document`() {
        val document = Jsoup.parse(readResource("index.html"))
        val expected = listOf(
            "Basic",
            "Cocoa",
            "Convention",
            "Design",
            "Empty",
            "Migration",
            "Naming",
            "Redundant",
            "Size",
            "Unused"
        )

        val actual = parseRuleCategories(document)

        assertEquals(expected, actual)
    }
}
