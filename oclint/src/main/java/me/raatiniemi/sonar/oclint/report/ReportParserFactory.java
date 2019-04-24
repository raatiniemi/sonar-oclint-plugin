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
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

public final class ReportParserFactory {
    private static final Logger LOGGER = Loggers.get(ReportParserFactory.class);

    private ReportParserFactory() {
    }

    public static ReportParserFactory create() {
        return new ReportParserFactory();
    }

    @Nonnull
    public ViolationReportParser from(@Nonnull File file) {
        String path = file.getPath();
        if (isXml(path)) {
            return buildXmlReportParser();
        }

        if (isJson(path)) {
            return buildJsonReportParser();
        }

        throw new UnsupportedViolationReportTypeException();
    }

    private boolean isXml(@Nonnull String path) {
        return path.endsWith("xml");
    }

    @Nonnull
    private OCLintXmlReportParser buildXmlReportParser() {
        return new OCLintXmlReportParser(createDocumentBuilder());
    }

    @Nonnull
    private DocumentBuilder createDocumentBuilder() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("Unable to create new document builder", e);
            throw new UnableToConfigureXmlReportParserException("Unable to create DocumentBuilderFactory", e);
        }
    }

    private boolean isJson(@Nonnull String path) {
        return path.endsWith("json");
    }

    @Nonnull
    private ViolationReportParser buildJsonReportParser() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new JsonReportParser(objectMapper);
    }
}
