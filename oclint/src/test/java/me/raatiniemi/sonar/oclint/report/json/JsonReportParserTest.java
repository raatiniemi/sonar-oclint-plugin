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

package me.raatiniemi.sonar.oclint.report.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.raatiniemi.sonar.oclint.Violation;
import me.raatiniemi.sonar.oclint.report.SampleReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class JsonReportParserTest {
    private final Path resourcePath = Paths.get("src", "test", "resources", "oclint", "json");
    private JsonReportParser parser;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parser = new JsonReportParser(objectMapper);
    }

    @Test
    public void parse_matchSampleReport() {
        Path documentPath = Paths.get(resourcePath.toString(), "sample.json");
        List<Violation> expected = SampleReport.build();

        Optional<List<Violation>> actual = parser.parse(documentPath.toFile());

        assertTrue("No violations are available", actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
