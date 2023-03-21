# When All Tests Were Green

Do you like small commits? Do you _usually_ remember to run the tests first? Make safe, tested commits faster with When All Tests Were Green.

Whenever all tests are green, this JUnit extension updates a timestamp in the build directory. Then, if tests were green within the last (say) 30 seconds, your pre-commit hook can avoid running them again. And if they weren't green that recently, well, _that's_ why you have a pre-commit hook: not to annoy you, but to help you do what you meant to do.

Optimize your commit flow. Run your tests once and only once with When All Tests Were Green.

## How to use

1. `./gradlew build`
2. Add the resulting `.jar` to your project's classpath
3. Add something like this to your `build.gradle`:
```groovy
test {
    systemProperty("junit.jupiter.extensions.autodetection.enabled", true)
}
```
Or if you have a `build.gradle.kts` instead:
```kotlin
tasks.withType<Test> {
    jvmArgs("-Djunit.jupiter.extensions.autodetection.enabled=true")
}
```
4. Run all your tests greenly
5. Note that a file appears in your build directory
6. Check the file's modification time in your pre-commit hook

## Alternatives

Gradle has a build cache. If you run `./gradlew test` all green and then run it again without having changed anything, it knows not to run the tests again. This has benefits: no additional logic needed in your pre-commit hook, and no time window for changing what's being committed.

IntelliJ can use Gradle to run tests, in which case the Gradle build cache is your friend. But this can be much slower than the IntelliJ test runner, and avoidable slowness is what we're trying to avoid.
