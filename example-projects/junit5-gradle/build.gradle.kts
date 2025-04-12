import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.20"
}

kotlin {
    jvmToolchain(8)
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testRuntimeOnly("com.schmonz:junit-greencently:local")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        kotlin.compilerOptions.jvmTarget = JvmTarget.JVM_1_8
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    environment("SET_NON_EMPTY_TO_FAIL_TEST_ONE", project.findProperty("setNonEmptyToFailTestOne") ?: "")
}
