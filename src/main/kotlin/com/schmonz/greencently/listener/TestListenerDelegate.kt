package com.schmonz.greencently.listener

import com.schmonz.greencently.Greencently
import com.schmonz.greencently.results.TestResults

class TestListenerDelegate {
    private var greenCount = 0L
    private var redCount = 0L

    fun onExecutionFinished(isTest: Boolean, isSuccessful: Boolean) =
        incrementGreenOrRedCount(isTest, isSuccessful)

    fun onTestPlanExecutionFinished(expectedCount: Long, totalCount: Long) =
        setGreencentlyStatus(expectedCount, totalCount)

    private fun incrementGreenOrRedCount(isTest: Boolean, succeeded: Boolean) {
        if (isTest) {
            if (succeeded) {
                greenCount++
            } else {
                redCount++
            }
        }
    }

    private fun setGreencentlyStatus(totalCount: Long, expectedCount: Long) {
        val results = TestResults(
            expectedCount,
            totalCount,
            greenCount,
            redCount,
        )
        Greencently("junit5").writeStatus(results.areCompleteAndGreen())
    }
}
