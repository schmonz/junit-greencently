# When All Tests Were Green

Do you like small commits? Do you _usually_ remember to run the tests first? Make safe, tested commits faster with When
All Tests Were Green.

Whenever all tests are green, this JUnit extension updates a timestamp in the build directory. Then, if tests were green
within the last (say) 30 seconds, your pre-commit hook can avoid running them again. And if they weren't green that
recently, well, _that's_ why you have a pre-commit hook: not to annoy you, but to help you do what you meant to do.

Optimize your development flow. All tests green? Commit now, quickly, with When All Tests Were Green.

## How to try it

- [Documentation](https://schmonz.com/software/when-all-tests-were-green)
