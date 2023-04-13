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

## Alternatives

Abstractly, the algorithm is a sequence of guard clauses:

1. Do nothing new if the tests that just ran were not all green
2. Do nothing new if they were not all the tests that could possibly have been run
3. Do nothing (this is new) if we're about to _unnecessarily_ run all possible tests again

Concretely, this implementation accomplishes (1) and (2) via JUnit callbacks and (3) by making it possible for an
external program to decide.

### Other implementations

Gradle has a build cache. If you run `./gradlew test` all green and then run it again without having changed anything,
it knows not to run the tests again. Benefits: no additional pre-commit logic, no time window for changing what's being
committed.

IntelliJ can use Gradle to run tests, in which case the Gradle build cache is your friend. But this is often much slower
than IntelliJ's internal test runner, and avoidable slowness is precisely what we're trying to avoid.

A clever IntelliJ Run Configuration can do the trick, via [@rradczewski](https://github.com/rradczewski):

1. Create a Shell Script configuration "Clever Test Task"
2. Execute script text `touch when-all-tests-were-green`
3. Add a "Before launch" task -> Run Another Configuration -> your existing all-tests task (with "Activate tool window"
   selected, so you see the output)
4. Whenever you'd run all tests, run Clever Test Task instead

If and only if the "Before launch" task succeeds, the shell script will run.

### Additional possibilities

IntelliJ has Run -> Test History, but the on-disk representation is not updated anywhere near real time. If it did, the
following command would approximately get you to the next problem, which is deducing all-green or not from the results:

    xq '.project.component[] | select(.["@name"] == "TestHistory") | .["history-entry"][]["@file"]' \
      < workspace/something-or-other.xml | grep ^\"All

If JUnit is configured to generate reports, you could try to determine from the content of a report whether that all
possible tests ran and all of them were green, and if so, check the file's timestamp. Likewise with the Surefire reports
if running under Maven with Surefire.

Perhaps a pseudo-test could be arranged to run only when the whole suite runs, to always run last, and to introspect the
results of the other tests.
