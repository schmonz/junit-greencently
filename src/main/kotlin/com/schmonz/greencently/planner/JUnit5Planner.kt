package com.schmonz.greencently.planner

import org.junit.platform.engine.discovery.ClassNameFilter
import org.junit.platform.engine.discovery.DiscoverySelectors
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import java.nio.file.Path
import java.nio.file.Paths

class JUnit5Planner(testClasspath: Path?, private val testClassesMatching: String = ".*") {
    private val testClasspath = testClasspath ?: discoverTestClasspath()

    private fun discoverTestClasspath() =
        Paths.get(
            System.getProperty("java.class.path").split(":").first {
                it.startsWith(System.getProperty("user.dir")) && isRecognizedOutputClasspath(it)
            }
        )

    private fun isRecognizedOutputClasspath(path: String) =
        isIntelliJOutputClasspath(path) || isGradleOutputClasspath(path)

    private fun isIntelliJOutputClasspath(path: String) =
        path.endsWith("/test/classes")

    private fun isGradleOutputClasspath(path: String) =
        path.endsWith("/test")

    fun getTestCount(): Long =
        LauncherFactory
            .create()
            .discover(
                LauncherDiscoveryRequestBuilder
                    .request()
                    .selectors(DiscoverySelectors.selectClasspathRoots(setOf(testClasspath)))
                    .filters(ClassNameFilter.includeClassNamePatterns(testClassesMatching))
                    .build()
            )
            .countTestIdentifiers(TestIdentifier::isTest)
}
