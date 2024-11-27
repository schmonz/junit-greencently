package com.schmonz.greencently.summary

import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import java.util.Objects

class JUnit5Summary(testPlan: TestPlan, internal val greenTestCount: Long, internal val redTestCount: Long) {
    internal val testCount = testPlan.countTestIdentifiers(TestIdentifier::isTest)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JUnit5Summary) return false

        val thinkItsEqual = testCount == other.testCount && greenTestCount > 0 && redTestCount == 0L

        if (System.getenv("GREENCENTLY_SUMMARY") !== null) {
            System.err.println("SUMMARY: $testCount, $greenTestCount, $redTestCount")
            System.err.println("OTHER: ${other.testCount}, ${other.greenTestCount}, ${other.redTestCount}")
            System.err.println("EQUAL? $thinkItsEqual")
        }

        return thinkItsEqual
    }

    override fun hashCode(): Int =
        Objects.hash(greenTestCount, redTestCount, testCount)
}
