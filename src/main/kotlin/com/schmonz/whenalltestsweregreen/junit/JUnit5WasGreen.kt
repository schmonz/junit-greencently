package com.schmonz.whenalltestsweregreen.junit

import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL
import org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns
import org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import java.time.Instant
import kotlin.io.path.setLastModifiedTime

class JUnit5WasGreen : TestExecutionListener {
    private var anyTestsRed = false
    private var anyTestsGreen = false

    override fun executionFinished(testIdentifier: TestIdentifier, testExecutionResult: TestExecutionResult) =
        when (testExecutionResult.status) {
            SUCCESSFUL -> {
                anyTestsGreen = true
            }

            else -> {
                anyTestsRed = true
            }
        }

    override fun testPlanExecutionFinished(testPlan: TestPlan) {
        if (anyTestsRed || !anyTestsGreen) return
        else if (countTests(testPlan) != countTests(planWhichTests(whereToLookForTests))) return
        else updateTimestamp(whereToWriteTimestamp)
    }

    companion object {
        val whereToWriteTimestamp: Path
            get() =
                Paths.get(System.getProperty("user.dir")).resolve("junit5-when-all-tests-were-green")

        private val whereToLookForTests
            get() =
                Paths.get(System.getProperty("java.class.path").split(":").first {
                    it.startsWith(System.getProperty("user.dir")) && isRecognizedOutputClasspath(it)
                })

        fun countTests(plan: TestPlan) =
            plan.countTestIdentifiers(TestIdentifier::isTest)

        fun planWhichTests(outputClasspath: Path, filenameRegex: String = ".*"): TestPlan =
            LauncherFactory
                .create()
                .discover(
                    LauncherDiscoveryRequestBuilder
                        .request()
                        .selectors(selectClasspathRoots(setOf(outputClasspath)))
                        .filters(includeClassNamePatterns(filenameRegex))
                        .build()
                )

        private fun isRecognizedOutputClasspath(path: String) =
            isIntelliJOutputClasspath(path) || isGradleOutputClasspath(path)

        private fun isIntelliJOutputClasspath(path: String) =
            path.endsWith("/test/classes")

        private fun isGradleOutputClasspath(path: String) =
            path.endsWith("/test")

        private fun updateTimestamp(filepath: Path) =
            createFile(filepath) || modifyFile(filepath).toString().isNotEmpty()

        private fun modifyFile(filepath: Path) =
            filepath.setLastModifiedTime(FileTime.from(Instant.now()))

        private fun createFile(filepath: Path) =
            filepath.toFile().createNewFile()
    }
}