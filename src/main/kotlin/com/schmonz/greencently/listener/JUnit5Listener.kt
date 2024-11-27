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
        updateTimestampIfAndOnlyIfAllTestsPass(testPlan)

    private fun accumulateResultsOfEachTest(testExecutionResult: TestExecutionResult) {
        if (testExecutionResult.status == SUCCESSFUL) {
            greenTestCount++
        } else {
            redTestCount++
        }
    }

    private fun updateTimestampIfAndOnlyIfAllTestsPass(testPlan: TestPlan) {
        if (System.getenv("GREENCENTLY_SUMMARY") !== null) {
            System.err.println(
                "greencently (green, red): ($greenTestCount, $redTestCount)"
            )
        }

        val expected = JUnit5Summary(JUnit5Planner(null).prepareTestPlan(), 0, 0)
        val actual = JUnit5Summary(testPlan, greenTestCount, redTestCount)
        if (actual == expected) Timestamp("junit5").setToNow()
    }
}
