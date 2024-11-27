package com.schmonz.greencently.summary

import java.util.Objects

class JUnit5Summary(internal val testCount: Long, internal val anyTestsGreen: Long, internal val anyTestsRed: Long) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JUnit5Summary) return false
        return (testCount == other.testCount && anyTestsGreen > 0 && anyTestsRed == 0L)
    }

    override fun hashCode(): Int =
        Objects.hash(anyTestsGreen, anyTestsRed, testCount)
}
