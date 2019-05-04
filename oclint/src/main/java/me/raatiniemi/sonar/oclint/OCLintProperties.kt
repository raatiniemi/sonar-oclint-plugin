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

const val CONFIG_REPORT_PATH_KEY = "sonar.oclint.reportPath"
const val CONFIG_REPORT_PATH_DEFAULT_VALUE = "sonar-reports/oclint.xml"
const val CONFIG_REPORT_PATH_NAME = "Path to OCLint violation report"
const val CONFIG_REPORT_PATH_DESCRIPTION = "Relative to projects' root."

const val DEPRECATED_CONFIG_REPORT_PATH_KEY = "sonar.objectivec.oclint.reportPath"
const val DEPRECATED_CONFIG_REPORT_PATH_DESCRIPTION =
    "Relative to projects' root, deprecated use `$CONFIG_REPORT_PATH_KEY` instead."
