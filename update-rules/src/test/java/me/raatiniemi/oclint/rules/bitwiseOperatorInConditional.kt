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

internal val bitwiseOperatorInConditional = Rule(
    "Bitwise operator in conditional",
    """
    <p>Checks for bitwise operations in conditionals. Although being written on purpose in some rare cases, bitwise operations are considered to be too “smart”. Smart code is not easy to understand.</p>
    <p>This rule is defined by the following class: <a class="reference external" href="https://github.com/oclint/oclint/blob/master/oclint-rules/rules/basic/BitwiseOperatorInConditionalRule.cpp">oclint-rules/rules/basic/BitwiseOperatorInConditionalRule.cpp</a></p>
    <p><strong>Example:</strong></p>
    <pre>void example(int a, int b)
    {
        if (a | b)
        {
        }
        if (a & b)
        {
        }
    }</pre>
    """.trimIndent(),
    basic.name,
    basic.severity
)
