[![Build status](https://github.com/schmonz/greencently/actions/workflows/main-build.yml/badge.svg)](https://github.com/schmonz/greencently/actions/workflows/main-build.yml)
[![Sonatype Central](https://maven-badges.sml.io/sonatype-central/com.schmonz/greencently/badge.svg?style=flat&gav=true)](https://central.sonatype.com/artifact/com.schmonz/greencently)

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
    testRuntimeOnly("com.schmonz:greencently:CHECK_ABOVE_FOR_VERSION")
}
tasks.withType<Test> {
    maxParallelForks = 1  // see issue #4
}
```

2. Run all tests in your project
3. If green, see that `.greencently/junit5` exists (and it's already `.gitignore`'d)
4. Run only one test, or only some tests, or all tests where at least one of them is red
5. See that `.greencently/junit5` no longer exists
6. From now on, whenever you've just run tests, all of them, and they're all green,
   the pre-commit hook can decide not to run them again. Example:

```sh
#!/bin/sh

ACCEPTABLY_LARGE_NUMBER_OF_SECONDS_AGO=30

greencently() {
    too_many_seconds_ago=$1
    thenstamp=$(date -r .greencently/junit5 '+%s' 2>/dev/null || echo 0)
    nowstamp=$(date '+%s')
    secondsago=$(expr ${nowstamp} - ${thenstamp})
    [ ${secondsago} -lt ${too_many_seconds_ago} ]
}

if greencently ${ACCEPTABLY_LARGE_NUMBER_OF_SECONDS_AGO}; then
    ./gradlew clean build --exclude-task test
else
    ./gradlew clean build
fi
```

## More information

See the
[Greencently webpage](https://schmonz.com/software/greencently).
