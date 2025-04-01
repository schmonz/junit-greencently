package com.schmonz.greencently.summary

import com.schmonz.greencently.planner.JUnit5Planner
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Summary(testPlanThatJustFinished: TestPlan, internal val greenTestCount: Long, internal val redTestCount: Long) {
    private val actualTestCount = countTestsInPlan(testPlanThatJustFinished)

    private fun countTestsInPlan(testPlan: TestPlan): Long = testPlan.countTestIdentifiers(TestIdentifier::isTest)

    fun isRunCompleteAndGreen(): Boolean {
        val expectedTestCount = countTestsInPlan(JUnit5Planner(null).prepareTestPlan())

        val greencently = redTestCount == 0L && 0 < greenTestCount && actualTestCount == expectedTestCount

        if (System.getenv("GREENCENTLY_SUMMARY") !== null) {
            System.err.println(
                "SUMMARY (expected, actual, green, red): $expectedTestCount, $actualTestCount, $greenTestCount, $redTestCount"
            )
            System.err.println("TEST RUN COMPLETE AND GREEN: $greencently")
        }

        return greencently
    }
}
