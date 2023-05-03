# Greencently

Got a pre-commit hook that runs JUnit 5 tests?
Add "Greencently" to your build.
Your workflow doesn't change.
Nobody needs to learn anything.
Commits finish faster.

Your intention is to commit only code that passes tests.
The purpose of the pre-commit hook is to help you carry out your intention.
Greencently updates a timestamp file on each green and complete test run.
If the timestamp isn't recent enough, you forgot to run the tests; the pre-commit hook will catch the mistake.
If the timestamp _is_ recent enough, the pre-commit hook might have other important jobs, but running the same tests on the same code isn't one of them.

With friction reduced, maybe you'll find yourselves committing more often.
With time and energy saved, maybe you'll invest in further test speedups.

All tests green? Commit now, quickly, with Greencently.

## How to try it

- [Documentation](https://schmonz.com/software/greencently)
