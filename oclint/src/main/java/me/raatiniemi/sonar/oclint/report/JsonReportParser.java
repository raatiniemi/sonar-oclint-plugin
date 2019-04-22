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
import me.raatiniemi.sonar.core.xml.XmlReportParser;
import me.raatiniemi.sonar.oclint.Violation;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class JsonReportParser implements ViolationReportParser {
    private static final Logger LOGGER = Loggers.get(XmlReportParser.class);

    private final ObjectMapper objectMapper;

    JsonReportParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Nonnull
    @Override
    public Optional<List<Violation>> parse(@Nonnull File reportFile) {
        try {
            JsonReport report = objectMapper.readValue(reportFile, JsonReport.class);
            List<Violation> violations = report.violations.stream()
                    .map(this::transformToViolation)
                    .collect(Collectors.toList());

            return Optional.of(violations);
        } catch (IOException e) {
            LOGGER.warn("Unable to parse report file: " + reportFile.getPath());
            return Optional.empty();
        }
    }

    private Violation transformToViolation(JsonReport.Violation violation) {
        return Violation.builder()
                .setPath(violation.path)
                .setStartLine(readStartLine(violation))
                .setRule(violation.rule)
                .setMessage(violation.message)
                .build();
    }

    private int readStartLine(JsonReport.Violation violation) {
        int startLine = violation.startLine;
        if (startLine == 0) {
            LOGGER.warn("Found empty start line in report for path: {}", violation.path);
            return 1;
        }

        return startLine;
    }
}
