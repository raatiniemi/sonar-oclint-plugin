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
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ViolationComparableTest(
    private val message: String,
    private val source: Violation,
    private val other: Violation,
    private val expected: Int
) {
    @Test
    fun test() {
        val actual = source.compareTo(other)

        assertEquals(message, expected, actual)
    }

    companion object {
        @Suppress("LongMethod")
        @JvmStatic
        @Parameterized.Parameters
        fun parameters() = listOf<Array<Any>>(
            arrayOf(
                "With equal",
                Violation.builder().build(),
                Violation.builder().build(),
                0
            ),
            arrayOf(
                "With different paths",
                Violation.builder()
                    .setPath("a")
                    .build(),
                Violation.builder()
                    .setPath("b")
                    .build(),
                -1
            ),
            arrayOf(
                "With different paths, reversed order",
                Violation.builder()
                    .setPath("b")
                    .build(),
                Violation.builder()
                    .setPath("a")
                    .build(),
                1
            ),
            arrayOf(
                "With same paths and different lines",
                Violation.builder()
                    .setPath("a")
                    .setStartLine(1)
                    .build(),
                Violation.builder()
                    .setPath("a")
                    .setStartLine(2)
                    .build(),
                -1
            ),
            arrayOf(
                "With same paths and different lines, reversed order",
                Violation.builder()
                    .setPath("a")
                    .setStartLine(2)
                    .build(),
                Violation.builder()
                    .setPath("a")
                    .setStartLine(1)
                    .build(),
                1
            ),
            arrayOf(
                "With same paths and line",
                Violation.builder()
                    .setPath("a")
                    .setStartLine(1)
                    .build(),
                Violation.builder()
                    .setPath("a")
                    .setStartLine(1)
                    .build(),
                0
            )
        )
    }
}
