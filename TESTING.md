# Testing

## 1. Build just like GitHub Actions (sans signing)

```shell
:; ./gradlew -PskipSigning clean detekt classes testClasses check assemble publishToMavenLocal
```

(Keep in sync with [main-build.yml](.github/workflows/main-build.yml).)

## 2. Use from a JDK 1.8 project

Its `build.gradle.kts` needs:

```kotlin
repositories {
    mavenLocal()
}

dependencies {
    testRuntimeOnly("com.schmonz:junit-greencently:local")
}
```

## 3. Run that project's tests a few ways

```shell
alias stamp="date -r .greencently/junit5 '+%s' 2>/dev/null || echo 0"
```

- [ ] Run all tests with a red, expect `stamp` == 0
- [ ] Run all tests green; expect `stamp` > 0
- [ ] Run all tests green; expect `stamp` > 0 by a bit more
- [ ] Run one test green; expect `stamp` == 0
- [ ] Run all tests green; expect `stamp` > 0
- [ ] Run all tests with a red; expect `stamp` == 0
