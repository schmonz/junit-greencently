import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    jacoco
    id("com.github.ben-manes.versions") version "0.48.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"

    // publishing
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

kotlin {
    jvmToolchain(8)
}

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 5 test discovery
    implementation("org.junit.platform:junit-platform-launcher:1.10.0")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
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

detekt {
    autoCorrect = true
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

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("Greencently")
                description.set("Optimize your development flow. All tests green? Commit now, quickly.")
                url.set("https://schmonz.com/software/greencently")
                licenses {
                    license {
                        name.set("The Unlicense")
                        url.set("https://unlicense.org")
                    }
                }
                developers {
                    developer {
                        id.set("schmonz")
                        name.set("Amitai Schleier")
                        email.set("schmonz-web-greencently@schmonz.com")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:schmonz/junit-greencently.git")
                    developerConnection.set("scm:git:git@github.com:schmonz/junit-greencently.git")
                    url.set("https://github.com/schmonz/junit-greencently.git")
                }
            }
        }
    }
}

nexusPublishing {
    this.repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
