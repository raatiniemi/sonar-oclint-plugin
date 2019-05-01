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

package me.raatiniemi.sonar.oclint.report

import me.raatiniemi.sonar.oclint.Violation

object SampleReport {
    private const val PRODUCT_DETAIL_API_CLIENT_PATH = "sample-project/API/ProductDetailAPIClient.m"
    private const val FUND_FINDER_API_CLIENT_PATH = "sample-project/API/FundFinderAPIClient.m"
    private const val MOBILE_API_CLIENT_PATH = "sample-project/API/MobileAPIClient.m"
    private const val PRODUCT_LISTING_API_CLIENT_PATH = "sample-project/API/ProductListingAPIClient.m"
    private const val INSIGHTS_API_CLIENT_PATH = "sample-project/InsightsAPIClient.m"
    private const val CHANNEL_CONTENT_API_CLIENT_PATH = "sample-project/ChannelContentAPIClient.m"
    private const val VIEW_ALL_HOLDINGS_API_CLIENT_PATH = "sample-project/ViewAllHoldingsAPIClient.m"

    private const val LONG_LINE_RULE_NAME = "long line"
    private const val UNUSED_METHOD_PARAMETER_RULE_NAME = "unused method parameter"
    private const val PARAMETER_REASSIGNMENT_RULE_NAME = "parameter reassignment"

    fun build(): List<Violation> {
        val violations = listOf(
            buildViolation(
                PRODUCT_DETAIL_API_CLIENT_PATH,
                37,
                LONG_LINE_RULE_NAME,
                "Line with 115 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_DETAIL_API_CLIENT_PATH,
                38,
                LONG_LINE_RULE_NAME,
                "Line with 113 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_DETAIL_API_CLIENT_PATH,
                47,
                LONG_LINE_RULE_NAME,
                "Line with 115 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_DETAIL_API_CLIENT_PATH,
                48,
                LONG_LINE_RULE_NAME,
                "Line with 121 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_DETAIL_API_CLIENT_PATH,
                37,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_DETAIL_API_CLIENT_PATH,
                37,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_DETAIL_API_CLIENT_PATH,
                38,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_DETAIL_API_CLIENT_PATH,
                38,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                FUND_FINDER_API_CLIENT_PATH,
                25,
                LONG_LINE_RULE_NAME,
                "Line with 186 characters exceeds limit of 100"
            ),
            buildViolation(
                FUND_FINDER_API_CLIENT_PATH,
                27,
                LONG_LINE_RULE_NAME,
                "Line with 104 characters exceeds limit of 100"
            ),
            buildViolation(
                FUND_FINDER_API_CLIENT_PATH,
                28,
                LONG_LINE_RULE_NAME,
                "Line with 121 characters exceeds limit of 100"
            ),
            buildViolation(
                FUND_FINDER_API_CLIENT_PATH,
                25,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                FUND_FINDER_API_CLIENT_PATH,
                25,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                FUND_FINDER_API_CLIENT_PATH,
                25,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                FUND_FINDER_API_CLIENT_PATH,
                25,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                MOBILE_API_CLIENT_PATH,
                28,
                LONG_LINE_RULE_NAME,
                "Line with 166 characters exceeds limit of 100"
            ),
            buildViolation(
                MOBILE_API_CLIENT_PATH,
                46,
                LONG_LINE_RULE_NAME,
                "Line with 117 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                34,
                LONG_LINE_RULE_NAME,
                "Line with 151 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                37,
                LONG_LINE_RULE_NAME,
                "Line with 141 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                38,
                LONG_LINE_RULE_NAME,
                "Line with 154 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                41,
                LONG_LINE_RULE_NAME,
                "Line with 218 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                42,
                LONG_LINE_RULE_NAME,
                "Line with 154 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                45,
                LONG_LINE_RULE_NAME,
                "Line with 287 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                47,
                LONG_LINE_RULE_NAME,
                "Line with 138 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                50,
                LONG_LINE_RULE_NAME,
                "Line with 321 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                59,
                LONG_LINE_RULE_NAME,
                "Line with 113 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                60,
                LONG_LINE_RULE_NAME,
                "Line with 121 characters exceeds limit of 100"
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                37,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                37,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                41,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                41,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                41,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                41,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                41,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                45,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                45,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                45,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                45,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                45,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                50,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                50,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                50,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                PRODUCT_LISTING_API_CLIENT_PATH,
                50,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                11,
                LONG_LINE_RULE_NAME,
                "Line with 132 characters exceeds limit of 100"
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                12,
                LONG_LINE_RULE_NAME,
                "Line with 120 characters exceeds limit of 100"
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                26,
                LONG_LINE_RULE_NAME,
                "Line with 192 characters exceeds limit of 100"
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                28,
                LONG_LINE_RULE_NAME,
                "Line with 117 characters exceeds limit of 100"
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                29,
                LONG_LINE_RULE_NAME,
                "Line with 127 characters exceeds limit of 100"
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                33,
                LONG_LINE_RULE_NAME,
                "Line with 258 characters exceeds limit of 100"
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                41,
                LONG_LINE_RULE_NAME,
                "Line with 127 characters exceeds limit of 100"
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                45,
                LONG_LINE_RULE_NAME,
                "Line with 124 characters exceeds limit of 100"
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                50,
                LONG_LINE_RULE_NAME,
                "Line with 128 characters exceeds limit of 100"
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                36,
                PARAMETER_REASSIGNMENT_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                37,
                PARAMETER_REASSIGNMENT_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                26,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                26,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                26,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                26,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                33,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                33,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                33,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                33,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                45,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                45,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                46,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                INSIGHTS_API_CLIENT_PATH,
                46,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                11,
                LONG_LINE_RULE_NAME,
                "Line with 116 characters exceeds limit of 100"
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                28,
                LONG_LINE_RULE_NAME,
                "Line with 216 characters exceeds limit of 100"
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                32,
                LONG_LINE_RULE_NAME,
                "Line with 127 characters exceeds limit of 100"
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                36,
                LONG_LINE_RULE_NAME,
                "Line with 213 characters exceeds limit of 100"
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                40,
                LONG_LINE_RULE_NAME,
                "Line with 128 characters exceeds limit of 100"
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                28,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                28,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                28,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                28,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                36,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                36,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                36,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                CHANNEL_CONTENT_API_CLIENT_PATH,
                36,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                VIEW_ALL_HOLDINGS_API_CLIENT_PATH,
                23,
                LONG_LINE_RULE_NAME,
                "Line with 225 characters exceeds limit of 100"
            ),
            buildViolation(
                VIEW_ALL_HOLDINGS_API_CLIENT_PATH,
                25,
                LONG_LINE_RULE_NAME,
                "Line with 131 characters exceeds limit of 100"
            ),
            buildViolation(
                VIEW_ALL_HOLDINGS_API_CLIENT_PATH,
                26,
                LONG_LINE_RULE_NAME,
                "Line with 101 characters exceeds limit of 100"
            ),
            buildViolation(
                VIEW_ALL_HOLDINGS_API_CLIENT_PATH,
                27,
                LONG_LINE_RULE_NAME,
                "Line with 101 characters exceeds limit of 100"
            ),
            buildViolation(
                VIEW_ALL_HOLDINGS_API_CLIENT_PATH,
                28,
                LONG_LINE_RULE_NAME,
                "Line with 121 characters exceeds limit of 100"
            ),
            buildViolation(
                VIEW_ALL_HOLDINGS_API_CLIENT_PATH,
                23,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                VIEW_ALL_HOLDINGS_API_CLIENT_PATH,
                23,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                VIEW_ALL_HOLDINGS_API_CLIENT_PATH,
                23,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            ),
            buildViolation(
                VIEW_ALL_HOLDINGS_API_CLIENT_PATH,
                23,
                UNUSED_METHOD_PARAMETER_RULE_NAME,
                ""
            )
        )

        return violations.sorted()
    }

    private fun buildViolation(
        path: String,
        startLine: Int,
        rule: String,
        message: String
    ) = Violation.builder()
        .setPath(path)
        .setStartLine(startLine)
        .setRule(rule)
        .setMessage(message)
        .build()
}
