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

package me.raatiniemi.oclint.rules

import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RuleTest {
    @Test
    fun from_simpleRuleDescription() {
        val rule = readResource("rules/bitwiseOperatorInConditional.html")
        val element = Jsoup.parse(rule)

        val actual = Rule.from(basic, element)

        assertEquals(bitwiseOperatorInConditional, actual)
    }

    @Test
    fun from_advancedRuleDescription() {
        val rule = readResource("rules/highNpathComplexity.html")
        val element = Jsoup.parse(rule)

        val actual = Rule.from(size, element)

        assertEquals(highNpathComplexity, actual)
    }
}
