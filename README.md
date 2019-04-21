# sonar-oclint-plugin

![OCLint compatible version](https://img.shields.io/badge/oclint-0.13-blue.svg)
[![pipeline status](https://gitlab.com/raatiniemi/sonar-oclint-plugin/badges/master/pipeline.svg)](https://gitlab.com/raatiniemi/sonar-oclint-plugin/commits/master)
[![quality gate](https://sonarcloud.io/api/project_badges/measure?project=me.raatiniemi.sonar%3Aoclint&metric=alert_status)](https://sonarcloud.io/dashboard?id=me.raatiniemi.sonar%3Aoclint)
[![code test coverage](https://sonarcloud.io/api/project_badges/measure?project=me.raatiniemi.sonar%3Aoclint&metric=coverage)](https://sonarcloud.io/dashboard?id=me.raatiniemi.sonar%3Aoclint)
[![technical dept](https://sonarcloud.io/api/project_badges/measure?project=me.raatiniemi.sonar%3Aoclint&metric=sqale_index)](https://sonarcloud.io/dashboard?id=me.raatiniemi.sonar%3Aoclint)

The repository is a SonarQube plugin for parsing reports from [OCLint](http://oclint.org/). *This is not a standalone
plugin yet, i.e. it needs to be [embedded within another plugin](https://gitlab.com/raatiniemi/sonar-objective-c).*

## Usage

In order to include the code from this repository, you'll first need to add the repository.

```gradle
repositories {
    maven {
        url  "https://dl.bintray.com/raatiniemi/maven"
    }
}
```

And, then you need to declare it as a dependency using `compile 'me.raatiniemi.sonar:oclint:$latestVersion'`.

*Dependency examples are using Gradle, for additional dependency options you can checkout
[sonar-oclint-plugin at bintray](https://bintray.com/raatiniemi/maven/sonar-oclint-plugin).*
