plugins {
    kotlin("jvm") version "1.8.10"
    id("java-library")
}

group = "com.schmonz.junit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.platform:junit-platform-launcher:1.9.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}
