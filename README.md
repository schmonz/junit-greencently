[![Build status](https://github.com/schmonz/junit-greencently/actions/workflows/main-build.yml/badge.svg)](https://github.com/schmonz/junit-greencently/actions/workflows/main-build.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.schmonz/junit-greencently/badge.svg?gav=true)](https://central.sonatype.com/artifact/com.schmonz/junit-greencently)

# [Greencently](https://schmonz.com/software/greencently)

## Setup

For JUnit 5:

1. Update `build.gradle.kts`:
```gradle
dependencies {
    testRuntimeOnly("com.schmonz:junit-greencently:VERSION_NUMBER_HERE")
}
tasks.withType<Test> {
    jvmArgs("-Djunit.jupiter.extensions.autodetection.enabled=true")
    maxParallelForks = 1  // see #4
}
```
2. Run all tests for project
3. If green, observe top-level `.when-all-tests-were-green-junit5`
4. Append to top-level `.gitignore`: `*when-all-tests-were-green*`
5. In pre-commit hook, inspect file modification time. Example:
```sh
#!/bin/sh
all_tests_were_recently_green() {
    thenstamp=$(date -r .when-all-tests-were-green-junit5 '+%s' 2>/dev/null || echo 0)
    nowstamp=$(date '+%s')
    secondsago=$(expr ${nowstamp} - ${thenstamp})
    [ ${secondsago} -lt 30 ]
}
if all_tests_were_recently_green; then
    ./gradlew clean build -x test
else
    ./gradlew clean build
fi
```
