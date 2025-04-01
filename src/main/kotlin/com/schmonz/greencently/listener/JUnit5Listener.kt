package com.schmonz.greencently.listener

import com.schmonz.greencently.Timestamp
import com.schmonz.greencently.summary.JUnit5Summary
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.TestExecutionResult.Status
import org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Listener : TestExecutionListener {
    private var greenTestCount = 0L
    private var redTestCount = 0L

    override fun executionFinished(testIdentifier: TestIdentifier, testExecutionResult: TestExecutionResult) =
        accumulateResultsOfEachTest(testIdentifier.isTest, testExecutionResult.status)

    override fun testPlanExecutionFinished(testPlan: TestPlan) =
        updateTimestampIfAndOnlyIfAllTestsPass(testPlan)

    private fun accumulateResultsOfEachTest(isTest: Boolean, testExecutionResultStatus: Status) {
        if (!isTest) return

        if (testExecutionResultStatus == SUCCESSFUL) greenTestCount++
        else redTestCount++
    }

    private fun updateTimestampIfAndOnlyIfAllTestsPass(testPlan: TestPlan) {
        val actual = JUnit5Summary(testPlan, greenTestCount, redTestCount)
        if (actual.isRunCompleteAndGreen()) Timestamp("junit5").setToNow()
    }
}
