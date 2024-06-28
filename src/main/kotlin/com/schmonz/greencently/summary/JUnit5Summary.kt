package com.schmonz.greencently.summary

import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Summary(testPlan: TestPlan, internal val anyTestsRed: Boolean, internal val anyTestsGreen: Boolean) {
    internal val testCount = testPlan.countTestIdentifiers(TestIdentifier::isTest)

    init {
        if (System.getProperty("GREENCENTLY_SUMMARY") != null) {
            println(
                "total tests: ${this.testCount}\nany green: $anyTestsGreen\nany red: $anyTestsRed\n",
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is JUnit5Summary) {
            return false
        }
        if (testCount == other.testCount && anyTestsRed == other.anyTestsRed && anyTestsGreen == other.anyTestsGreen) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = anyTestsRed.hashCode()
        result = 31 * result + anyTestsGreen.hashCode()
        result = 31 * result + testCount.hashCode()
        return result
    }
}
