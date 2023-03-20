# When All Tests Were Green

Do you like small commits? Do you _usually_ remember to run the tests first? Make safe, tested commits faster with When All Tests Were Green.

Whenever all tests are green, this JUnit extension updates a timestamp in the build directory. Then, if tests were green within the last (say) 30 seconds, your pre-commit hook can avoid running them again. And if they weren't green that recently, well, _that's_ why you have a pre-commit hook: not to annoy you, but to help you do what you meant to do.

Optimize your commit flow. Run your tests once and only once with When All Tests Were Green.

## How to use

1. `./gradlew build`
2. Add the resulting `.jar` to your classpath
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
