package com.schmonz.greencently.listener

import com.schmonz.greencently.Timestamp
import com.schmonz.greencently.planner.JUnit5Planner
import com.schmonz.greencently.summary.JUnit5Summary
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Listener : TestExecutionListener {
    private var greenTestCount = 0L
    private var redTestCount = 0L

    override fun executionFinished(testIdentifier: TestIdentifier, testExecutionResult: TestExecutionResult) =
        accumulateResultsOfEachTest(testExecutionResult)

    override fun testPlanExecutionFinished(testPlan: TestPlan) =
        updateTimestampIfAndOnlyIfAllTestsPass()

    private fun accumulateResultsOfEachTest(testExecutionResult: TestExecutionResult) {
        if (testExecutionResult.status == SUCCESSFUL) {
            greenTestCount++
        } else {
            redTestCount++
        }
    }

    private fun updateTimestampIfAndOnlyIfAllTestsPass() {
        if (System.getenv("GREENCENTLY_SUMMARY") !== null) {
            System.err.println(
                "greencently (total, green, red): " +
                    "(${greenTestCount + redTestCount}, $greenTestCount, $redTestCount)"
            )
        }

        val expectedTestCount = JUnit5Planner(null).getTestCount()
        val expected = JUnit5Summary(expectedTestCount, expectedTestCount, 0)
        val actual = JUnit5Summary(greenTestCount + redTestCount, greenTestCount, redTestCount)
        if (actual == expected) Timestamp("junit5").setToNow()
    }
}
