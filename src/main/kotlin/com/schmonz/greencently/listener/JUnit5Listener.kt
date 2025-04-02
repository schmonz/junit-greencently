package com.schmonz.greencently.listener

import com.schmonz.greencently.Greencently
import com.schmonz.greencently.results.JUnit5Results
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Listener : TestExecutionListener {
    private var greenCount = 0L
    private var redCount = 0L

    override fun executionFinished(id: TestIdentifier, result: TestExecutionResult) {
        if (id.isTest) {
            incrementGreenOrRedCount(result.status == SUCCESSFUL)
        }
    }

    override fun testPlanExecutionFinished(completedTestPlan: TestPlan) =
        setGreencentlyStatus(completedTestPlan)

    private fun incrementGreenOrRedCount(succeeded: Boolean) {
        if (succeeded) {
            greenCount++
        } else {
            redCount++
        }
    }

    private fun setGreencentlyStatus(completedTestPlan: TestPlan) =
        Greencently("junit5").writeStatus(JUnit5Results(completedTestPlan, greenCount, redCount).areCompleteAndGreen())
}
