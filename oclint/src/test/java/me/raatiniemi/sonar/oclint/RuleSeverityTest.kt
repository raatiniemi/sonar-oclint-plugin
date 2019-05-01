/*
 * Copyright (c) 2018 Tobias Raatiniemi
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

@RunWith(JUnit4::class)
class RuleSeverityTest {
    @Test
    fun valueOfInt_withInfo() {
        val severity = RuleSeverity.valueOfInt(0)

        assertEquals(RuleSeverity.INFO, severity)
    }

    @Test
    fun valueOfInt_withMinor() {
        val severity = RuleSeverity.valueOfInt(1)

        assertEquals(RuleSeverity.MINOR, severity)
    }

    @Test
    fun valueOfInt_withMajor() {
        val severity = RuleSeverity.valueOfInt(2)

        assertEquals(RuleSeverity.MAJOR, severity)
    }

    @Test
    fun valueOfInt_withCritical() {
        val severity = RuleSeverity.valueOfInt(3)

        assertEquals(RuleSeverity.CRITICAL, severity)
    }

    @Test
    fun valueOfInt_withBlocker() {
        val severity = RuleSeverity.valueOfInt(4)

        assertEquals(RuleSeverity.BLOCKER, severity)
    }
}
