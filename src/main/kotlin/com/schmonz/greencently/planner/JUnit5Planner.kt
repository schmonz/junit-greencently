package com.schmonz.greencently.planner

import org.junit.platform.engine.discovery.ClassNameFilter
import org.junit.platform.engine.discovery.DiscoverySelectors
import org.junit.platform.launcher.TestPlan
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import java.nio.file.Paths

class JUnit5Planner(private val testClassesMatching: String = ".*") {
    private val testClasspath = discoverTestClasspath()

    fun prepareTestPlan(): TestPlan =
        LauncherFactory
            .create()
            .discover(
                LauncherDiscoveryRequestBuilder
                    .request()
                    .selectors(DiscoverySelectors.selectClasspathRoots(setOf(testClasspath)))
                    .filters(ClassNameFilter.includeClassNamePatterns(testClassesMatching))
                    .build()
            )

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
}
