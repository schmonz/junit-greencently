package com.schmonz.greencently.results

import com.schmonz.greencently.planner.JUnit5Planner
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Results(completedTestPlan: TestPlan, internal val greenCount: Long, internal val redCount: Long) {
    private val actualCount = countTestsInPlan(completedTestPlan)

    fun areCompleteAndGreen(): Boolean {
        val expectedCount = countTestsInPlan(JUnit5Planner().prepareTestPlan())

        val looksGood = redCount == 0L && 0 < greenCount && actualCount == expectedCount

        if (System.getenv("GREENCENTLY_SUMMARY") !== null) {
            System.err.println(
                "GREENCENTLY (expected actual green red): $expectedCount $actualCount $greenCount $redCount"
            )
            System.err.println("GREENCENTLY (complete and green): $looksGood")
        }

        return looksGood
    }

    private fun countTestsInPlan(testPlan: TestPlan): Long =
        testPlan.countTestIdentifiers(TestIdentifier::isTest)
}
