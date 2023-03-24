# Before release

## Correctness

- What are dynamic tests? Am I counting them the same as JUnit?
- Running sample project tests depends on having built main project's `.jar`
- Running main project tests depends on sample project tests being compiled and green
- Treat warnings as build errors
- Be able to accumulate results from Gradle with parallel Test Executors
- Be able to run our own tests in parallel (requires unique-per-test names for the timestamp file)

## Usability

- What do we mean by "all tests", exactly?
- Is it obvious _when_ to add this to your project?
- Is it obvious _how_ to add this to your project?
- Is it easy to tell whether a typical run-tests-and-then-commit got faster?
- Is it potentially annoying to put a file in the build directory?
  Say, because it winds up getting shipped in a `.jar` by mistake?
- How can a project configure a different location for the file?

## Completeness

- JUnit 4
- TestNG
- Maven
- IntelliJ
- Eclipse
- VSCode
- Jest
- ?

## Organization

- A repo per impl, or all of them in one big repo?
