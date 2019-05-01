/*
 * Copyright © 2012 OCTO Technology, Backelite (${email})
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

import com.google.common.io.Closeables
import org.slf4j.LoggerFactory
import org.sonar.api.profiles.ProfileDefinition
import org.sonar.api.profiles.RulesProfile
import org.sonar.api.utils.ValidationMessages
import java.io.InputStreamReader
import java.io.Reader

class OCLintProfile(private val profileImporter: OCLintProfileImporter) : ProfileDefinition() {
    override fun createProfile(messages: ValidationMessages): RulesProfile {
        LOGGER.info("Creating OCLint Profile")
        var config: Reader? = null

        try {
            val resourceAsStream = javaClass.getResourceAsStream(PROFILE_PATH)
            config = InputStreamReader(resourceAsStream)

            return profileImporter.importProfile(config, messages)
                .apply {
                    name = OCLintRulesDefinition.REPOSITORY_NAME
                    language = "objc"
                }
        } finally {
            Closeables.closeQuietly(config)
        }
    }

    companion object {
        const val PROFILE_PATH = "/me/raatiniemi/sonar/oclint/profile-oclint.xml"

        private val LOGGER = LoggerFactory.getLogger(OCLintProfile::class.java)
    }
}
