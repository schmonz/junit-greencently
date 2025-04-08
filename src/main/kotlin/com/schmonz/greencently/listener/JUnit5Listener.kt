package com.schmonz.greencently.listener

import com.schmonz.greencently.planner.JUnit5Planner
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Listener : TestExecutionListener {
    private val delegate = TestListenerDelegate()

    override fun executionFinished(id: TestIdentifier, result: TestExecutionResult) =
        delegate.incrementGreenOrRedCount(
            id.isTest,
            result.status == TestExecutionResult.Status.SUCCESSFUL,
        )

    override fun testPlanExecutionFinished(completedTestPlan: TestPlan) =
        delegate.setGreencentlyStatus(
            countTests(JUnit5Planner().prepareTestPlan()),
            countTests(completedTestPlan),
        )

    private fun countTests(testPlan: TestPlan) =
        testPlan.countTestIdentifiers(TestIdentifier::isTest)
}
