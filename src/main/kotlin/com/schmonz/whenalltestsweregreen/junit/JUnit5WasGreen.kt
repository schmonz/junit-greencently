package com.schmonz.whenalltestsweregreen.junit

import org.junit.platform.engine.TestExecutionResult
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

    private fun DEBUGGING(message: String) {
        System.err.println("SCHMONZ: $message")
    }

    override fun executionFinished(testIdentifier: TestIdentifier?, testExecutionResult: TestExecutionResult?) {
        super.executionFinished(testIdentifier, testExecutionResult)
        if (testExecutionResult!!.status == TestExecutionResult.Status.SUCCESSFUL) {
            anyTestsGreen = true
        } else {
            anyTestsRed = true
        }
    }

    override fun testPlanExecutionFinished(testPlan: TestPlan?) {
        super.testPlanExecutionFinished(testPlan)
        if (anyTestsRed || !anyTestsGreen) return

        val expected = countTests(discoverAllTests(whereToLookForTests()))
        val actualCount = countTests(testPlan)

        if (actualCount == expected) {
            updateTimestamp(whereToWriteTimestamp())
        } else {
            DEBUGGING("expected $expected, was $actualCount")
        }
    }

    private fun countTests(plan: TestPlan?) =
        plan?.roots?.sumOf {
            plan.getDescendants(it).size
        } ?: 0

    private fun discoverAllTests(outputClasspath: Path) =
        LauncherFactory
            .create()
            .discover(
                LauncherDiscoveryRequestBuilder
                    .request()
                    .selectors(selectClasspathRoots(setOf(outputClasspath)))
                    .build()
            )

    private fun whereToLookForTests() =
        Paths.get(System.getProperty("java.class.path").split(":").first {
            it.startsWith(System.getProperty("user.dir")) && isRecognizedOutputClasspath(it)
        })

    private fun isRecognizedOutputClasspath(path: String) =
        isIntelliJOutputClasspath(path) || isGradleOutputClasspath(path)

    private fun isIntelliJOutputClasspath(path: String) =
        path.endsWith("/test/classes")

    private fun isGradleOutputClasspath(path: String) =
        path.endsWith("/test")

    private fun whereToWriteTimestamp() =
        Paths.get(System.getProperty("user.dir")).resolve("junit5-when-all-tests-were-green")

    private fun updateTimestamp(filepath: Path) {
        if (!filepath.toFile().createNewFile()) {
            filepath.setLastModifiedTime(FileTime.from(Instant.now()))
        }
        DEBUGGING("updated $filepath")
    }
}
