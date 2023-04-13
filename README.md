# When All Tests Were Green

Do you like small commits? Do you _usually_ remember to run the tests first? Make safe, tested commits faster with When
All Tests Were Green.

Whenever all tests are green, this JUnit extension updates a timestamp in the build directory. Then, if tests were green
within the last (say) 30 seconds, your pre-commit hook can avoid running them again. And if they weren't green that
recently, well, _that's_ why you have a pre-commit hook: not to annoy you, but to help you do what you meant to do.

Optimize your commit flow. Run your tests once and only once with When All Tests Were Green.

## How to use

1. Add to your `build.gradle.kts`:

```kotlin
dependencies {
    testRuntimeOnly("com.schmonz.whenalltestsweregreen:junit-whenalltestsweregreen:LATEST_VERSION_HERE")
}
tasks.withType<Test> {
    jvmArgs("-Djunit.jupiter.extensions.autodetection.enabled=true")
    maxParallelForks = 1  // until we know how to combine results from Gradle's Test Executors
}
```

2. Add to your `.gitignore`: `*when-all-tests-were-green`
3. Run all your tests greenly
4. Watch `junit5-when-all-tests-were-green` appear at the top level of your repo (while _not_ appearing in `git status`)
5. Check its modification time in your pre-commit hook
