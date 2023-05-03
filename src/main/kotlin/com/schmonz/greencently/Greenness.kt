package com.schmonz.greencently

import com.schmonz.greencently.summary.JUnit5Summary

class Greenness(private val actuallyRan: JUnit5Summary, private val couldHaveRun: JUnit5Summary) {
    private fun enoughTestsRan() =
        actuallyRan.testCount == couldHaveRun.testCount

    private fun allTestsGreen() =
        actuallyRan.anyTestsGreen && !actuallyRan.anyTestsRed

    fun isTotal() =
        enoughTestsRan() && allTestsGreen()
}
