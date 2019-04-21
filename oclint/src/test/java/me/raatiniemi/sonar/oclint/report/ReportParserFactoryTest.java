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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ReportParserFactoryTest {
    private ReportParserFactory factory;

    @Before
    public void setUp() {
        factory = ReportParserFactory.create();
    }

    @Test(expected = UnsupportedViolationReportTypeException.class)
    public void from_withTextViolationReportType() {
        File reportFile = new File("oclint.txt");

        factory.from(reportFile);
    }

    @Test(expected = UnsupportedViolationReportTypeException.class)
    public void from_withHtmlViolationReportType() {
        File reportFile = new File("oclint.html");

        factory.from(reportFile);
    }

    @Test(expected = UnsupportedViolationReportTypeException.class)
    public void from_withPmdViolationReportType() {
        File reportFile = new File("oclint.pmd");

        factory.from(reportFile);
    }

    @Test
    public void from_withXmlViolationReportType() {
        File reportFile = new File("oclint.xml");

        ViolationReportParser parser = factory.from(reportFile);

        assertTrue(parser instanceof OCLintXmlReportParser);
    }

    @Test
    public void from_withJsonViolationReportType() {
        File reportFile = new File("oclint.json");

        ViolationReportParser parser = factory.from(reportFile);

        assertTrue(parser instanceof JsonReportParser);
    }
}
