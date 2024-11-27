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
    private var anyTestsGreen = false
    private var anyTestsRed = false

    override fun executionFinished(testIdentifier: TestIdentifier, testExecutionResult: TestExecutionResult) =
        accumulateResultsOfEachTest(testExecutionResult)

    override fun testPlanExecutionFinished(testPlan: TestPlan) =
        updateTimestampIfAndOnlyIfAllTestsPass(testPlan)

    private fun accumulateResultsOfEachTest(testExecutionResult: TestExecutionResult) {
        if (testExecutionResult.status == SUCCESSFUL) {
            anyTestsGreen = true
        } else {
            anyTestsRed = true
        }
    }

    private fun updateTimestampIfAndOnlyIfAllTestsPass(testPlan: TestPlan) {
        val expected = JUnit5Summary(JUnit5Planner(null).prepareTestPlan(), anyTestsGreen = false, anyTestsRed = false)
        val actual = JUnit5Summary(testPlan, anyTestsGreen, anyTestsRed)
        if (actual == expected) Timestamp("junit5").setToNow()
    }
}
