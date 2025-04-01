# Testing

- [ ] `./gradlew -PskipSigning -Pversion=99.99.99-SNAPSHOT assemble publishToMavenLocal`
- [ ] In the IDE, open "Kotlin" portion of GildedRose-Refactoring-Kata
- [ ] Make sure that project's `build.gradle.kts` contains:
```
repositories {
	mavenLocal();
}

dependencies {
	testRuntimeOnly("com.schmonz:junit-greencently:99.99.99-SNAPSHOT")
}
```
- [ ] Test other project
  - [ ] Run with a red, expect file not created or updated
  - [ ] Run only one test green, expect file not created or update
  - [ ] Run all tests green, expect file yes created or updated
