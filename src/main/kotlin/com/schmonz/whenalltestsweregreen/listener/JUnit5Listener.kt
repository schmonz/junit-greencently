package com.schmonz.whenalltestsweregreen.listener

import com.schmonz.whenalltestsweregreen.Greenness
import com.schmonz.whenalltestsweregreen.Timestamp
import com.schmonz.whenalltestsweregreen.planner.JUnit5Planner
import com.schmonz.whenalltestsweregreen.summary.JUnit5Summary
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Listener : TestExecutionListener {
    private var anyTestsRed = false
    private var anyTestsGreen = false

    override fun executionFinished(testIdentifier: TestIdentifier, testExecutionResult: TestExecutionResult) =
        observeResultOfOneTest(testExecutionResult)

    override fun testPlanExecutionFinished(testPlan: TestPlan) =
        judgeResultsOfAllTests(testPlan)

    private fun observeResultOfOneTest(testExecutionResult: TestExecutionResult) {
        if (testExecutionResult.status == SUCCESSFUL) {
            anyTestsGreen = true
        } else {
            anyTestsRed = true
        }
    }

    private fun judgeResultsOfAllTests(testPlan: TestPlan) {
        if (Greenness(
                JUnit5Summary(
                        testPlan,
                        anyTestsRed,
                        anyTestsGreen
                    ),
                JUnit5Summary(
                        JUnit5Planner(null).prepareTestPlan(),
                        anyTestsRed = false,
                        anyTestsGreen = false
                    )
            ).isTotal()
        ) {
            Timestamp("junit5-").setToNow()
        }
    }
}
