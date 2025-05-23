package com.schmonz.greencently.listener

import com.schmonz.greencently.Greencently
import com.schmonz.greencently.results.TestResults

class TestListenerDelegate {
    private var greenCount = 0L
    private var redCount = 0L

    fun incrementGreenOrRedCount(isTest: Boolean, isSuccessful: Boolean) {
        if (isTest) {
            if (isSuccessful) {
                greenCount++
            } else {
                redCount++
            }
        }
    }

    fun setGreencentlyStatus(accordingTo: String, expectedCount: Long, totalCount: Long) =
        Greencently(accordingTo).writeStatus(
            TestResults(
                expectedCount,
                totalCount,
                greenCount,
                redCount,
            ).areCompleteAndGreen()
        )
}
