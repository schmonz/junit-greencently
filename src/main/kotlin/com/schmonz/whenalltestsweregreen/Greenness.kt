package com.schmonz.whenalltestsweregreen

import com.schmonz.whenalltestsweregreen.summary.Summary

class Greenness(private val actuallyRan: Summary, private val couldHaveRun: Summary) {
    private fun enoughTestsRan() =
        actuallyRan.testCount == couldHaveRun.testCount

    private fun allTestsGreen() =
        actuallyRan.anyTestsGreen && !actuallyRan.anyTestsRed

    fun isTotal() =
        enoughTestsRan() && allTestsGreen()
}
