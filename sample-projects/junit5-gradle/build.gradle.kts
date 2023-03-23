plugins {
    kotlin("jvm") version "1.8.10"
}

group = "com.schmonz.whenalltestsweregreen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.2")

    testRuntimeOnly(files("../../build/libs/junit-whenalltestsweregreen-1.0-SNAPSHOT.jar"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-Djunit.jupiter.extensions.autodetection.enabled=true")
    maxParallelForks = 1
}

kotlin {
    jvmToolchain(8)
}
