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
package me.raatiniemi.sonar.oclint;

import me.raatiniemi.sonar.core.internal.FileSystemHelpers;
import me.raatiniemi.sonar.oclint.report.SampleReport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.DefaultActiveRules;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.batch.sensor.issue.IssueLocation;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.rule.RuleKey;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class OCLintSensorTest {
    private final Path resourcePath = Paths.get("src", "test", "resources", "oclint", "report");
    private final MapSettings settings = new MapSettings();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private SensorContextTester context;
    private OCLintSensor sensor;

    @Nonnull
    private static List<Violation> transformIssuesToViolations(@Nonnull Collection<Issue> issues) {
        List<Violation> violations = issues.stream()
                .map(transformIssueToViolation())
                .collect(Collectors.toList());

        return sort(violations);
    }

    @Nonnull
    private static List<Violation> sort(@Nonnull Collection<Violation> violations) {
        return violations.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Nonnull
    private static Function<Issue, Violation> transformIssueToViolation() {
        return issue -> {
            IssueLocation issueLocation = issue.primaryLocation();

            Violation.Builder builder = Violation.builder()
                    .setPath(removePrefixFromKey(issueLocation.inputComponent()))
                    .setStartLine(readStartLine(issueLocation))
                    .setRule(issue.ruleKey().rule());

            String message = issueLocation.message();
            if (message != null) {
                builder.setMessage(message);
            }

            return builder.build();
        };
    }

    @Nonnull
    private static String removePrefixFromKey(@Nonnull InputComponent inputComponent) {
        String key = inputComponent.key();
        return key.replace("projectKey:", "");
    }

    private static int readStartLine(@Nonnull IssueLocation issueLocation) {
        TextRange textRange = issueLocation.textRange();
        if (textRange == null) {
            return 1;
        }
        return textRange.start().line();
    }

    @Before
    public void setUp() {
        context = SensorContextTester.create(temporaryFolder.getRoot());
        sensor = new OCLintSensor(settings.asConfig());

        List<NewActiveRule> rules = new ArrayList<>();
        ActiveRulesBuilder builder = new ActiveRulesBuilder();
        rules.add(builder.create(RuleKey.of(OCLintRulesDefinition.REPOSITORY_KEY, "long line")));
        rules.add(builder.create(RuleKey.of(OCLintRulesDefinition.REPOSITORY_KEY, "unused method parameter")));
        rules.add(builder.create(RuleKey.of(OCLintRulesDefinition.REPOSITORY_KEY, "parameter reassignment")));
        context.setActiveRules(new DefaultActiveRules(rules));

        FileSystemHelpers helpers = FileSystemHelpers.create(context);
        helpers.addToFileSystem(createFile("sample-project/API/ProductDetailAPIClient.m"));
        helpers.addToFileSystem(createFile("sample-project/API/FundFinderAPIClient.m"));
        helpers.addToFileSystem(createFile("sample-project/API/MobileAPIClient.m"));
        helpers.addToFileSystem(createFile("sample-project/API/ProductListingAPIClient.m"));
        helpers.addToFileSystem(createFile("sample-project/InsightsAPIClient.m"));
        helpers.addToFileSystem(createFile("sample-project/ChannelContentAPIClient.m"));
        helpers.addToFileSystem(createFile("sample-project/ViewAllHoldingsAPIClient.m"));
    }

    @Nonnull
    private TestInputFileBuilder buildInputFile(@Nonnull String relativePath) {
        List<String> intStream = IntStream.range(1, 100)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());

        return new TestInputFileBuilder(context.module().key(), relativePath)
                .setLanguage("objc")
                .initMetadata(String.join("\n", intStream));
    }

    @Nonnull
    private DefaultInputFile createFile(@Nonnull String relativePath) {
        return buildInputFile(relativePath)
                .setType(InputFile.Type.MAIN)
                .build();
    }

    private void createReportFile(@Nonnull String sourcePath, @Nonnull String destinationPath) {
        try {
            List<String> reportLines = Files.readAllLines(Paths.get(resourcePath.toString(), sourcePath));

            Path destination = Paths.get(temporaryFolder.getRoot().getAbsolutePath(), destinationPath);
            Files.createDirectories(destination.getParent());
            Files.createFile(destination);
            Files.write(destination, reportLines);
        } catch (IOException e) {
            fail(String.format("Unable to create report file: %s", e.getMessage()));
        }
    }

    @Test
    public void describe() {
        DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();

        sensor.describe(descriptor);

        assertEquals("OCLint violation sensor", descriptor.name());
        assertTrue(descriptor.languages().contains("objc"));
    }

    @Test
    public void execute_withDefaultReportPath() {
        createReportFile("sample.xml", "sonar-reports/oclint.xml");
        List<Violation> expected = sort(SampleReport.build());

        sensor.execute(context);

        List<Violation> actual = transformIssuesToViolations(context.allIssues());
        assertEquals(expected, actual);
    }

    @Test
    public void execute_withReportPath() {
        settings.setProperty("sonar.objectivec.oclint.reportPath", "oclint.xml");
        createReportFile("sample.xml", "oclint.xml");
        List<Violation> expected = sort(SampleReport.build());

        sensor.execute(context);

        List<Violation> actual = transformIssuesToViolations(context.allIssues());
        assertEquals(expected, actual);
    }

    @Test
    public void execute_withJsonReportPath() {
        settings.setProperty("sonar.objectivec.oclint.reportPath", "oclint.json");
        createReportFile("sample.json", "oclint.json");
        List<Violation> expected = sort(SampleReport.build());

        sensor.execute(context);

        List<Violation> actual = transformIssuesToViolations(context.allIssues());
        assertEquals(expected, actual);
    }
}
