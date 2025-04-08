package com.schmonz.greencently

import com.schmonz.greencently.results.JUnit5Results

class TestListenerDelegate {
    private var greenCount = 0L
    private var redCount = 0L

    fun onExecutionFinished(isTest: Boolean, isSuccessful: Boolean) =
        incrementGreenOrRedCount(isTest, isSuccessful)

    fun onTestPlanExecutionFinished(totalCount: Long) =
        setGreencentlyStatus(totalCount)

    private fun incrementGreenOrRedCount(isTest: Boolean, succeeded: Boolean) {
        if (isTest) {
            if (succeeded) {
                greenCount++
            } else {
                redCount++
            }
        }
    }

    private fun setGreencentlyStatus(totalCount: Long) {
        val results = JUnit5Results(totalCount, greenCount, redCount)
        Greencently("junit5").writeStatus(results.areCompleteAndGreen())
    }
}
