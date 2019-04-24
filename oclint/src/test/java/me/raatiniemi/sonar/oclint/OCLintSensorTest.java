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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.DefaultActiveRules;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.rule.RuleKey;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class OCLintSensorTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final Path resourcePath = Paths.get("src", "test", "resources", "oclint");
    private final MapSettings settings = new MapSettings();

    private SensorContextTester context;
    private OCLintSensor sensor;

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

    private void createReportFile(@Nonnull Path sourcePath, @Nonnull String destinationPath) {
        try {
            List<String> reportLines = Files.readAllLines(Paths.get(resourcePath.toString(), sourcePath.toString()));

            Path destination = Paths.get(temporaryFolder.getRoot().getAbsolutePath(), destinationPath);
            Files.createDirectories(destination.getParent());
            Files.createFile(destination);
            Files.write(destination, reportLines);
        } catch (IOException e) {
            fail(String.format("Unable to create report file: %s", e.getMessage()));
        }
    }

    private boolean isIssuePresent(@Nonnull String ruleKey) {
        String ruleKeyWithRepository = "OCLint:" + ruleKey;

        return context.allIssues()
                .stream()
                .map(Issue::ruleKey)
                .map(RuleKey::toString)
                .anyMatch(rk -> rk.equalsIgnoreCase(ruleKeyWithRepository));
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
        createReportFile(Paths.get("xml", "sample.xml"), "sonar-reports/oclint.xml");

        sensor.execute(context);

        assertTrue(isIssuePresent("long line"));
        assertTrue(isIssuePresent("unused method parameter"));
        assertTrue(isIssuePresent("parameter reassignment"));
    }

    @Test
    public void execute_withReportPath() {
        settings.setProperty("sonar.objectivec.oclint.reportPath", "oclint.xml");
        createReportFile(Paths.get("xml", "sample.xml"), "oclint.xml");

        sensor.execute(context);

        assertTrue(isIssuePresent("long line"));
        assertTrue(isIssuePresent("unused method parameter"));
        assertTrue(isIssuePresent("parameter reassignment"));
    }

    @Test
    public void execute_withJsonReportPath() {
        settings.setProperty("sonar.objectivec.oclint.reportPath", "oclint.json");
        createReportFile(Paths.get("json", "sample.json"), "oclint.json");

        sensor.execute(context);

        assertTrue(isIssuePresent("long line"));
        assertTrue(isIssuePresent("unused method parameter"));
        assertTrue(isIssuePresent("parameter reassignment"));
    }
}
