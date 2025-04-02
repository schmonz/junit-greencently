# Testing

- [ ] `./gradlew -PskipSigning clean detekt classes testClasses check assemble publishToMavenLocal`
  - (When `.github/main-build.yml` changes, sync to here)
- [ ] In the IDE, open "Kotlin" portion of GildedRose-Refactoring-Kata
- [ ] Make sure that project's `build.gradle.kts` contains:
```
repositories {
	mavenLocal();
}

dependencies {
	testRuntimeOnly("com.schmonz:junit-greencently:local")
}
```
- [ ] Test other project
  - [ ] Run all tests with a red, expect file not exists
  - [ ] Run all tests green, expect file exists
    - `date -r .greencently-junit5`
  - [ ] Run all tests green, expect file modtime updated
    - `date -r .greencently-junit5`
  - [ ] Run one test green, expect file not exists
  - [ ] Run all tests green, expect file exists
  - [ ] Run all tests with a red, expect file not exists
