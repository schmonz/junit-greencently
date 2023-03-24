import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    id("java-library")
    id("jacoco")
    id("com.github.ben-manes.versions") version "0.46.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

group = "com.schmonz.whenalltestsweregreen"
version = "1.0-SNAPSHOT"

val jvmVersion = JavaLanguageVersion.of(8)

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.platform:junit-platform-launcher:1.9.2")
    testImplementation(kotlin("test"))
    testRuntimeOnly(files("sample-projects/junit5-gradle/build/classes/kotlin/test"))
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(false)
        html.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
    gradleReleaseChannel = "current"
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
