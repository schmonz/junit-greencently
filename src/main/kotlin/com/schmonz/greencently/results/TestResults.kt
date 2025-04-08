package com.schmonz.greencently.results

class TestResults(
    private val expectedCount: Long,
    private val actualCount: Long,
    private val greenCount: Long,
    private val redCount: Long,
) {
    fun areCompleteAndGreen(): Boolean {
        val looksGood = redCount == 0L && 0L < greenCount && actualCount == expectedCount
        if (wantLogging()) logToStderr(looksGood)
        return looksGood
    }

    private fun logToStderr(looksGood: Boolean) {
        System.err.println("GREENCENTLY (expected actual green red): $expectedCount $actualCount $greenCount $redCount")
        System.err.println("GREENCENTLY (complete and green): $looksGood")
    }

    private fun wantLogging(): Boolean =
        System.getenv("GREENCENTLY_SUMMARY") !== null
}
