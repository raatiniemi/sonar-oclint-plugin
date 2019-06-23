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

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WriteRulesTest {
    @Test
    fun `write rules with one rule`() {
        val rules = listOf(bitwiseOperatorInConditional)
        val writeRules = writeRules(rules)
        val expected = """
            Available issues:

            OCLint
            ======

            bitwise operator in conditional
            ----------

            Summary: Name: bitwise operator in conditional
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

            Severity: 3
            Type: CODE_SMELL
            Category: OCLint


        """.trimIndent()

        val actual = writeRules()

        assertEquals(expected, actual)
    }

    @Test
    fun `write rules with multiple rules`() {
        val rules = listOf(
            bitwiseOperatorInConditional,
            highNpathComplexity
        )
        val writeRules = writeRules(rules)
        val expected = """
            Available issues:

            OCLint
            ======

            bitwise operator in conditional
            ----------

            Summary: Name: bitwise operator in conditional
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

            Severity: 3
            Type: CODE_SMELL
            Category: OCLint

            high npath complexity
            ----------

            Summary: Name: high npath complexity
            <p>NPath complexity is determined by the number of execution paths through that method. Compared to cyclomatic complexity, NPath complexity has two outstanding characteristics: first, it distinguishes between different kinds of control flow structures; second, it takes the various type of acyclic paths in a flow graph into consideration.</p>
            <p>Based on studies done by the original author in AT&amp;T Bell Lab, an NPath threshold value of 200 has been established for a method.</p>
            <p>This rule is defined by the following class: <a class="reference external" href="https://github.com/oclint/oclint/blob/master/oclint-rules/rules/size/NPathComplexityRule.cpp">oclint-rules/rules/size/NPathComplexityRule.cpp</a></p>
            <p><strong>Example:</strong></p>
            <pre>void example()
            {
                // complicated code that is hard to understand
            }</pre>
            <p><strong>Thresholds:</strong></p>
            <dl><dt> NPATH_COMPLEXITY</dt> <dd> The NPath complexity reporting threshold, default value is 200.</dd></dl>
            <p><strong>Suppress:</strong></p>
            <pre>__attribute__((annotate("oclint:suppress[high npath complexity]")))</pre>
            <p><strong>References:</strong></p>
            <p>Brian A. Nejmeh (1988). <a class="reference external" href="http://dl.acm.org/citation.cfm?id=42379">“NPATH: a measure of execution path complexity and its applications”</a>. <em>Communications of the ACM 31 (2) p. 188-200</em></p>

            Severity: 3
            Type: CODE_SMELL
            Category: OCLint


        """.trimIndent()

        val actual = writeRules()

        assertEquals(expected, actual)
    }
}
