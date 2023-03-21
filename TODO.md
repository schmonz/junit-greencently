# Before release


## Filenames

- Is it potentially annoying to put a file in the build directory? Say, because it winds up getting shipped in someone's .jar by mistake?
- How can the project configure a different file location?
- Should I prefix the filename with `junit-` so the IntelliJ plugin would have a distinct timestamp file?


## Usability

- Is it obvious _when_ to add this to your project?
- Is it obvious _how_ to add this to your project?
- Is it easy to tell whether a typical run-tests-and-then-commit got faster?


## Correctness

- Get rid of `var`s and `null`s wherever possible
- Ask the running JUnit for paths, rather than guessing
- Stop generating console output


## Organization

- One repo per impl, or one repo holding all of them?
- Specifically, wanna ship IntelliJ plugin too
- Adding an Eclipse plugin would help clarify the interface and the build
- My current TypeScript project needs a test-runner plugin too
