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

package me.raatiniemi.sonar.oclint.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.raatiniemi.sonar.oclint.Violation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class JsonReportParserTest {
    @Rule
    public LogTester logTester = new LogTester();

    private final Path resourcePath = Paths.get("src", "test", "resources", "oclint", "json");
    private JsonReportParser parser;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parser = new JsonReportParser(objectMapper);
    }

    @Test
    public void parse_withEmptyReport() {
        Path documentPath = Paths.get(resourcePath.toString(), "empty.json");
        List<Violation> expected = new ArrayList<>();

        Optional<List<Violation>> actual = parser.parse(documentPath.toFile());

        assertTrue("No violations are available", actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void parse_withReportUsingZeroAsStartLine() {
        Path documentPath = Paths.get(resourcePath.toString(), "with-empty-start-line.json");
        List<Violation> expected = new ArrayList<>();
        expected.add(
                Violation.builder()
                        .setPath("sample-project/API/ProductDetailAPIClient.m")
                        .setStartLine(1)
                        .setRule("long line")
                        .setMessage("Line with 115 characters exceeds limit of 100")
                        .build()
        );

        Optional<List<Violation>> actual = parser.parse(documentPath.toFile());

        assertTrue("No violations are available", actual.isPresent());
        assertEquals(expected, actual.get());
        assertTrue(logTester.logs(LoggerLevel.WARN).contains("Found empty start line in report for path: sample-project/API/ProductDetailAPIClient.m"));
    }

    @Test
    public void parse_withSampleReport() {
        Path documentPath = Paths.get(resourcePath.toString(), "sample.json");
        List<Violation> expected = SampleReport.build();

        Optional<List<Violation>> actual = parser.parse(documentPath.toFile());

        assertTrue("No violations are available", actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
