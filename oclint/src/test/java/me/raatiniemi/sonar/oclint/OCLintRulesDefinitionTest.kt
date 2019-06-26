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
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.sonar.api.server.rule.RulesDefinition
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

@RunWith(JUnit4::class)
class OCLintRulesDefinitionTest {
    private val resourcePath = Paths.get("src", "test", "resources", "oclint")

    private lateinit var context: RulesDefinition.Context
    private lateinit var rulesDefinition: OCLintRulesDefinition

    @Before
    fun setUp() {
        context = RulesDefinition.Context()
        rulesDefinition = OCLintRulesDefinition()
    }

    @Test
    fun createRepository() {
        val actual = rulesDefinition.createRepository(context)

        assertEquals(OCLintRulesDefinition.REPOSITORY_KEY, actual.key())
    }

    @Test
    fun loadRules() {
        val repository = rulesDefinition.createRepository(context)

        rulesDefinition.loadRules(repository)

        assertEquals(71, repository.rules().size)
    }

    @Test
    fun populateRepositoryWithRulesFromLines() {
        val repository = rulesDefinition.createRepository(context)
        val rulesPath = Paths.get(resourcePath.toString(), "rules.txt")
        val lines = Files.lines(rulesPath).toList()

        rulesDefinition.populateRepositoryWithRulesFromLines(repository, lines)

        assertEquals(2, repository.rules().size)
        assertNotNull(repository.rule("avoid branching statement as last in loop"))
        assertNotNull(repository.rule("bitwise operator in conditional"))
    }
}
