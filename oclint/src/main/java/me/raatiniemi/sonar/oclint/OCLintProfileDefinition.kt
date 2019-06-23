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

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader

class OCLintProfileDefinition : BuiltInQualityProfilesDefinition {
    override fun define(context: BuiltInQualityProfilesDefinition.Context) {
        context.createBuiltInQualityProfile(PROFILE_NAME, LANGUAGE)
            .also(loadProfile)
            .done()
    }

    private val loadProfile: (BuiltInQualityProfilesDefinition.NewBuiltInQualityProfile) -> Unit = { profile ->
        BuiltInQualityProfileJsonLoader.load(
            profile,
            OCLintRulesDefinition.REPOSITORY_KEY,
            pathToJsonProfile
        )
    }

    companion object {
        private const val pathToJsonProfile = "me/raatiniemi/sonar/oclint/profile.json"
    }
}
