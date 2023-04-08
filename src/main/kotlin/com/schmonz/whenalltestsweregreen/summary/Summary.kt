package com.schmonz.whenalltestsweregreen.summary

abstract class Summary {
    abstract val anyTestsRed: Boolean
    abstract val anyTestsGreen: Boolean
    abstract val testCount: Long

    init {
        if (System.getProperty("WHENALLTESTSWEREGREEN_SUMMARY") != null) {
            println(
                "total tests: ${this.testCount}\nany green: $anyTestsGreen\nany red: $anyTestsRed\n",
            )
        }
    }
}
