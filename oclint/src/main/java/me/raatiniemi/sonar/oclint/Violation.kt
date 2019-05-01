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

data class Violation(
    internal val path: String,
    internal val startLine: Int,
    internal val rule: String,
    internal val message: String
) : Comparable<Violation> {
    override fun compareTo(other: Violation): Int {
        val comparisonForPath = path.compareTo(other.path)

        return if (comparisonForPath == 0) {
            Integer.compare(startLine, other.startLine)
        } else {
            comparisonForPath
        }
    }

    class Builder internal constructor() {
        private var path = ""
        private var startLine: Int = 0
        private var rule = ""
        private var message = ""

        fun setPath(path: String): Builder {
            this.path = path
            return this
        }

        fun setStartLine(startLine: Int): Builder {
            this.startLine = startLine
            return this
        }

        fun setRule(rule: String): Builder {
            this.rule = rule
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun build() = Violation(
            path = path,
            startLine = startLine,
            rule = rule,
            message = message
        )
    }

    companion object {
        fun builder() = Builder()
    }
}
