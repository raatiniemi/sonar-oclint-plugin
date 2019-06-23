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
import org.junit.runners.JUnit4
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition


@RunWith(JUnit4::class)
class OCLintProfileDefinitionTest {
    private val context = BuiltInQualityProfilesDefinition.Context()
    private val profileDefinition = OCLintProfileDefinition()

    @Test
    fun `define load profile from json`() {
        profileDefinition.define(context)

        assertEquals(1, context.profilesByLanguageAndName().size)
        val profile = context.profile(LANGUAGE, PROFILE_NAME)
        assertEquals(LANGUAGE, profile.language())
        assertEquals(PROFILE_NAME, profile.name())
        assertEquals(71, profile.rules().size)
    }
}
