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
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

@RunWith(JUnit4::class)
class RuleDefinitionParserTest {
    private val resourcePath = Paths.get("src", "test", "resources", "oclint")

    private val parser = RuleDefinitionParser.create()

    @Test
    fun parseRuleDefinitionsFromLines() {
        val expected = setOf(
            RuleDefinition.builder()
                .setKey("avoid branching statement as last in loop")
                .setName("Avoid branching statement as last in loop")
                .setSeverity("MAJOR")
                .setDescription(
                    "<p> Name: avoid branching statement as last in loop</p>\n" +
                            "<p>Having branching statement as the last statement inside a loop is very confusing, and could largely be forgetting of something and turning into a bug.</p>\n" +
                            "<p>This rule is defined by the following class: <a class=\"reference external\" href=\"https://github.com/oclint/oclint/blob/master/oclint-rules/rules/convention/AvoidBranchingStatementAsLastInLoopRule.cpp\">oclint-rules/rules/convention/AvoidBranchingStatementAsLastInLoopRule.cpp</a></p>\n" +
                            "<p><strong>Example:</strong></p>\n" +
                            "<pre>void example()\n" +
                            "{\n" +
                            "    for (int i = 0; i < 10; i++)\n" +
                            "    {\n" +
                            "        if (foo(i))\n" +
                            "        {\n" +
                            "            continue;\n" +
                            "        }\n" +
                            "        break;      // this break is confusing\n" +
                            "    }\n" +
                            "}</pre>\n"
                )
                .setType("CODE_SMELL")
                .build(),
            RuleDefinition.builder()
                .setKey("bitwise operator in conditional")
                .setName("Bitwise operator in conditional")
                .setSeverity("CRITICAL")
                .setDescription(
                    "<p> Name: bitwise operator in conditional</p>\n" +
                            "<p>Checks for bitwise operations in conditionals. Although being written on purpose in some rare cases, bitwise operations are considered to be too “smart”. Smart code is not easy to understand.</p>\n" +
                            "<p>This rule is defined by the following class: <a class=\"reference external\" href=\"https://github.com/oclint/oclint/blob/master/oclint-rules/rules/basic/BitwiseOperatorInConditionalRule.cpp\">oclint-rules/rules/basic/BitwiseOperatorInConditionalRule.cpp</a></p>\n" +
                            "<p><strong>Example:</strong></p>\n" +
                            "<pre>void example(int a, int b)\n" +
                            "{\n" +
                            "    if (a | b)\n" +
                            "    {\n" +
                            "    }\n" +
                            "    if (a & b)\n" +
                            "    {\n" +
                            "    }\n" +
                            "}</pre>\n"
                )
                .setType("BUG")
                .build()
        )
        val rulesPath = Paths.get(resourcePath.toString(), "rules.txt")
        val lines = Files.lines(rulesPath).toList()

        val actual = parser.parseRuleDefinitionsFromLines(lines)

        assertEquals(expected, actual)
    }

    @Test
    fun parseRuleDefinitionsFromLines_withoutSummary() {
        val expected = setOf(
            RuleDefinition.builder()
                .setKey("unused method parameter")
                .setName("Unused method parameter")
                .setSeverity("INFO")
                .setDescription("<p></p>\n")
                .setType("CODE_SMELL")
                .build()
        )
        val rulesPath = Paths.get(resourcePath.toString(), "rules-without-summary.txt")
        val lines = Files.lines(rulesPath).toList()

        val actual = parser.parseRuleDefinitionsFromLines(lines)

        assertEquals(expected, actual)
    }
}
