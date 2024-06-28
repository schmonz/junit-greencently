package com.schmonz.greencently

import com.schmonz.greencently.summary.JUnit5Summary

class Greenness(private val actuallyRan: JUnit5Summary, private val couldHaveRun: JUnit5Summary) {
    fun isTotal() =
        actuallyRan == couldHaveRun
}
