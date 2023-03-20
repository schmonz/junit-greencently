package com.schmonz.junit.whenalltestsweregreen

import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import java.io.File
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import java.time.Instant
import kotlin.io.path.setLastModifiedTime

class WhenAllTestsWereGreen: TestExecutionListener {
    private var outputDir: File? = null
    private var anyTestsRed = false
    private var anyTestsGreen = false

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
        val expectedCount = countTests(discoverAllTests())
        if (expectedCount == 0) return
        if (countTests(testPlan) == expectedCount) {
            updateLatestGreenTestsTimestamp()
        }
    }


    private fun countTests(testPlan: TestPlan?) =
        testPlan!!.roots.sumOf { testPlan.getDescendants(it).size }

    private fun discoverAllTests(): TestPlan? {
        val classpathRoots = mutableSetOf<Path>()

        listOf(
            File("out/test/classes"),
            File("build/classes/kotlin/test"),
        ).forEach {
            if (it.isDirectory) {
                outputDir = it
                classpathRoots.add(outputDir!!.toPath())
            }
        }

        return LauncherFactory
            .create()
            .discover(
                LauncherDiscoveryRequestBuilder
                    .request()
                    .selectors(selectClasspathRoots(classpathRoots))
                    .build()
            )
    }

    private fun updateLatestGreenTestsTimestamp() {
        val timestampFile = File(outputDir, "when-all-tests-were-green")
        if (!timestampFile.createNewFile()) {
            timestampFile.toPath().setLastModifiedTime(FileTime.from(Instant.now()))
        }
        System.err.println("updated timestamp on ${timestampFile.path}")
    }
}
