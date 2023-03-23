# Before release

## Correctness

- Handle Gradle with parallel Test Executors

## Usability

- Is it obvious _when_ to add this to your project?
- Is it obvious _how_ to add this to your project?
- Is it easy to tell whether a typical run-tests-and-then-commit got faster?
- Is it potentially annoying to put a file in the build directory?
  Say, because it winds up getting shipped in a `.jar` by mistake?
- How can a project configure a different location for the file?

## Organization

- One repo per impl, or one repo holding all of them?
- Specifically, wanna ship IntelliJ plugin too
- Adding an Eclipse plugin would help clarify the interface and the build
- My current TypeScript project needs a test-runner plugin too
