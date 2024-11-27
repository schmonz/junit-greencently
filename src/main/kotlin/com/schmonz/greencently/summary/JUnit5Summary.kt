package com.schmonz.greencently.summary

import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import java.util.Objects

class JUnit5Summary(testPlan: TestPlan, internal val anyTestsGreen: Boolean, internal val anyTestsRed: Boolean) {
    internal val testCount = testPlan.countTestIdentifiers(TestIdentifier::isTest)

    init {
        if (System.getProperty("GREENCENTLY_SUMMARY") != null) {
            println(
                "total tests: ${this.testCount}\n" +
                    "any green: $anyTestsGreen\n" +
                    "any red: $anyTestsRed\n",
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JUnit5Summary) return false
        return (testCount == other.testCount && anyTestsGreen && !anyTestsRed)
    }

    override fun hashCode(): Int {
        return Objects.hash(anyTestsGreen, anyTestsRed, testCount)
    }
}
