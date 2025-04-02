package com.schmonz.greencently.summary

import com.schmonz.greencently.planner.JUnit5Planner
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Summary(completedTestPlan: TestPlan, internal val greenCount: Long, internal val redCount: Long) {
    private val actualCount = countTestsInPlan(completedTestPlan)

    private fun countTestsInPlan(testPlan: TestPlan): Long = testPlan.countTestIdentifiers(TestIdentifier::isTest)

    fun isCompleteAndGreen(): Boolean {
        val expectedCount = countTestsInPlan(JUnit5Planner(null).prepareTestPlan())

        val greencently = redCount == 0L && 0 < greenCount && actualCount == expectedCount

        if (System.getenv("GREENCENTLY_SUMMARY") !== null) {
            System.err.println(
                "SUMMARY (expected, actual, green, red): $expectedCount, $actualCount, $greenCount, $redCount"
            )
            System.err.println("TEST RUN COMPLETE AND GREEN: $greencently")
        }

        return greencently
    }
}
