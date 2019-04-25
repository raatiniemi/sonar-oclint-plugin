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

package me.raatiniemi.sonar.oclint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ViolationComparableTest {
    @Nonnull
    private final String message;
    @Nonnull
    private final Violation source;
    @Nonnull
    private final Violation other;
    private final int expected;

    public ViolationComparableTest(
            @Nonnull String message,
            @Nonnull Violation source,
            @Nonnull Violation other,
            int expected
    ) {
        this.message = message;
        this.source = source;
        this.other = other;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "With equal",
                                Violation.builder().build(),
                                Violation.builder().build(),
                                0
                        },
                        {
                                "With different paths",
                                Violation.builder()
                                        .setPath("a")
                                        .build(),
                                Violation.builder()
                                        .setPath("b")
                                        .build(),
                                -1
                        },
                        {
                                "With different paths, reversed order",
                                Violation.builder()
                                        .setPath("b")
                                        .build(),
                                Violation.builder()
                                        .setPath("a")
                                        .build(),
                                1
                        },
                        {
                                "With same paths and different lines",
                                Violation.builder()
                                        .setPath("a")
                                        .setStartLine(1)
                                        .build(),
                                Violation.builder()
                                        .setPath("a")
                                        .setStartLine(2)
                                        .build(),
                                -1
                        },
                        {
                                "With same paths and different lines, reversed order",
                                Violation.builder()
                                        .setPath("a")
                                        .setStartLine(2)
                                        .build(),
                                Violation.builder()
                                        .setPath("a")
                                        .setStartLine(1)
                                        .build(),
                                1
                        },
                        {
                                "With same paths and line",
                                Violation.builder()
                                        .setPath("a")
                                        .setStartLine(1)
                                        .build(),
                                Violation.builder()
                                        .setPath("a")
                                        .setStartLine(1)
                                        .build(),
                                0
                        }
                }
        );
    }

    @Test
    public void test() {
        int actual = source.compareTo(other);

        assertEquals(message, expected, actual);
    }
}
