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

internal data class RuleDefinition(
    val key: String,
    val name: String,
    val description: String,
    val severity: String,
    val type: String
) {
    internal class Builder internal constructor() {
        private var key = ""
        private var name = ""
        private val description = StringBuilder()
        private var severity = ""
        private var type = "CODE_SMELL"

        fun setKey(key: String): Builder {
            this.key = key
            return this
        }

        fun setName(name: String): Builder {
            this.name = name
            return this
        }

        fun setDescription(description: String): Builder {
            this.description.append(description)
            return this
        }

        fun appendToDescription(description: String) {
            this.description.append("\n")
            this.description.append(description)
        }

        fun setSeverity(severity: String): Builder {
            this.severity = severity
            return this
        }

        fun setType(type: String): Builder {
            this.type = when (type) {
                "BUG", "VULNERABILITY" -> type
                else -> "CODE_SMELL"
            }
            return this
        }

        fun build() = RuleDefinition(
            key = key,
            name = name,
            description = description.toString(),
            severity = severity,
            type = type
        )
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()
    }
}
