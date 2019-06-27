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

internal val unusedMethodParameter = Rule(
    name = "Unused method parameter",
    description = """
        <p>This rule detects parameters that are not used in the method.</p>
        <p>This rule is defined by the following class: <a class="reference external" href="https://github.com/oclint/oclint/blob/master/oclint-rules/rules/unused/UnusedMethodParameterRule.cpp">oclint-rules/rules/unused/UnusedMethodParameterRule.cpp</a></p>
        <p><strong>Example:</strong></p>
        <pre>int example(int a)  // parameter a is not used
        {
            return 0;
        }</pre>
        <p><strong>Suppress:</strong></p>
        <pre>__attribute__((annotate("oclint:suppress[unused method parameter]")))</pre>
    """.trimIndent(),
    category = "Unused",
    severity = RuleSeverity.INFO
)
