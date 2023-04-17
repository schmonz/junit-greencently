# When All Tests Were Green

Do you like small commits? Do you _usually_ remember to run the tests first? Make safe, tested commits faster with When
All Tests Were Green.

Whenever all tests are green, this JUnit extension updates a timestamp in the build directory. Then, if tests were green
within the last (say) 30 seconds, your pre-commit hook can avoid running them again. And if they weren't green that
recently, well, _that's_ why you have a pre-commit hook: not to annoy you, but to help you do what you meant to do.

Optimize your commit flow. Run your tests once and only once with When All Tests Were Green.

## When not to try

### No appreciable benefit if...

- You don't have or want a pre-commit hook that runs the tests, and/or
- Your complete test suite runs in microseconds (_nice!_), and/or
- You commit 2-3 times per day and that's totally fine

### Bad tradeoff if...

- In your context it somehow adds more risk than it mitigates, and/or
- The behavior change you want to see first is something other than "smaller, more frequent commits", and/or
- You were hoping to introduce something revolutionary, or at least challenging for people to adjust to

## How to try

1. Add to your `build.gradle.kts`:

```kotlin
dependencies {
    testRuntimeOnly("com.schmonz:junit-whenalltestsweregreen:LATEST_VERSION_HERE")
}
tasks.withType<Test> {
    jvmArgs("-Djunit.jupiter.extensions.autodetection.enabled=true")
    maxParallelForks = 1  // until we know how to combine results from Gradle's Test Executors
}
```

2. Add to your `.gitignore`: `*when-all-tests-were-green`
3. Run all your tests greenly
4. Watch `junit5-when-all-tests-were-green` appear at the top level of your repo (while _not_ appearing in `git status`)
5. Check its modification time in your pre-commit hook, perhaps like so:

```sh
#!/bin/sh

all_tests_were_recently_green() {
    thenstamp=$(stat -f '%m' junit5-when-all-tests-were-green 2>/dev/null || echo 0)
    nowstamp=$(date '+%s')
    secondsago=$(expr ${nowstamp} - ${thenstamp})
    [ ${secondsago} -lt 30 ]
}

all_tests_were_recently_green || ./gradlew clean build
```

## Endorsements

> Completely antithetical to test-driven development.
>
> Please do not use and do not advertise that extension.
> It's going to cause damage to your teams and others'.  
> -- [Andrea L. on LinkedIn](https://www.linkedin.com/feed/update/urn:li:activity:7052043459565682688?commentUrn=urn%3Ali%3Acomment%3A%28activity%3A7052043459565682688%2C7052311132509757440%29&replyUrn=urn%3Ali%3Acomment%3A%28activity%3A7052043459565682688%2C7052341771158102016%29&dashCommentUrn=urn%3Ali%3Afsd_comment%3A%287052311132509757440%2Curn%3Ali%3Aactivity%3A7052043459565682688%29&dashReplyUrn=urn%3Ali%3Afsd_comment%3A%287052341771158102016%2Curn%3Ali%3Aactivity%3A7052043459565682688%29)

I'm as yet unable to imagine why or how, but if anything happens to prove him right, please do let us all know.
