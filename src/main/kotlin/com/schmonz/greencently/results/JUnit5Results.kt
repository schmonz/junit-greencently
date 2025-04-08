package com.schmonz.greencently.results

import com.schmonz.greencently.planner.JUnit5Planner
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Results(
    private val actualCount: Long,
    private val greenCount: Long,
    private val redCount: Long,
) {
    fun areCompleteAndGreen(): Boolean {
        val expectedCount = countTestsInPlanDos(JUnit5Planner().prepareTestPlan())
        val looksGood = redCount == 0L && 0L < greenCount && actualCount == expectedCount
        if (wantLogging()) logToStderr(expectedCount, looksGood)
        return looksGood
    }

    private fun countTestsInPlanDos(testPlan: TestPlan): Long =
        testPlan.countTestIdentifiers(TestIdentifier::isTest)

    private fun logToStderr(expectedCount: Long, looksGood: Boolean) {
        System.err.println("GREENCENTLY (expected actual green red): $expectedCount $actualCount $greenCount $redCount")
        System.err.println("GREENCENTLY (complete and green): $looksGood")
    }

    private fun wantLogging(): Boolean =
        System.getenv("GREENCENTLY_SUMMARY") !== null
}
