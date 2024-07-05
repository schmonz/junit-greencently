[![Build status](https://github.com/schmonz/junit-greencently/actions/workflows/main-build.yml/badge.svg)](https://github.com/schmonz/junit-greencently/actions/workflows/main-build.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.schmonz/junit-greencently/badge.svg?gav=true)](https://central.sonatype.com/artifact/com.schmonz/junit-greencently)

# Greencently

Do you usually run your tests before you commit, to check your work?

Do your tests also get run by a
[pre-commit hook](https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks)?

This is helpful in the worst case --
the pre-commit hook has your back when you forget --
but in the typical case it's harmful:

- Disrupts flow
- Doubles cost
- Adds no marginal benefit
- Discourages frequent small well-tested commits
- Engenders learned helplessness

We can fix this.

## Setup for JUnit 5

1. Update `build.gradle.kts`:
```gradle.kts
dependencies {
    testRuntimeOnly("com.schmonz:junit-greencently:CHECK_ABOVE_FOR_VERSION")
}
tasks.withType<Test> {
    jvmArgs("-Djunit.jupiter.extensions.autodetection.enabled=true")
    maxParallelForks = 1  // see issue #4
}
```
2. Run all tests in project
3. If green, observe top-level timestamp file `.when-all-tests-were-green-junit5`
4. Append to top-level `.gitignore`: `*when-all-tests-were-green*`
5. Observe `git status` _not_ showing timestamp file
6. In pre-commit hook, inspect file modification time. Example:
```sh
#!/bin/sh
all_tests_were_recently_green() {
    too_many_seconds_ago=$1
    thenstamp=$(date -r .when-all-tests-were-green-junit5 '+%s' 2>/dev/null || echo 0)
    nowstamp=$(date '+%s')
    secondsago=$(expr ${nowstamp} - ${thenstamp})
    [ ${secondsago} -lt ${too_many_seconds_ago} ]
}
if all_tests_were_recently_green 30; then
    ./gradlew clean build -x test
else
    ./gradlew clean build
fi
```

## More information

See the
[Greencently webpage](https://schmonz.com/software/greencently).
