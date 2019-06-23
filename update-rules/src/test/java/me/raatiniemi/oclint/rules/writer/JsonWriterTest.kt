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

import me.raatiniemi.oclint.rules.bitwiseOperatorInConditional
import me.raatiniemi.oclint.rules.highNpathComplexity
import me.raatiniemi.oclint.rules.profile.profile
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class JsonWriterTest {
    @Test
    fun `write as json with one rule profile`() {
        val rules = listOf(bitwiseOperatorInConditional)
        val profile = profile(rules)
        val expected = """
            {
              "name" : "OCLint",
              "ruleKeys" : [ "bitwise operator in conditional" ]
            }

        """.trimIndent()

        val actual = writeAsJson(profile)

        assertEquals(expected, actual)
    }

    @Test
    fun `write as json with multiple rules profile`() {
        val rules = listOf(
            bitwiseOperatorInConditional,
            highNpathComplexity
        )
        val profile = profile(rules)
        val expected = """
            {
              "name" : "OCLint",
              "ruleKeys" : [ "bitwise operator in conditional", "high npath complexity" ]
            }

        """.trimIndent()

        val actual = writeAsJson(profile)

        assertEquals(expected, actual)
    }
}
