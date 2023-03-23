package com.schmonz.whenalltestsweregreen.sample.junit5gradle

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class SomeTests {
    @Test
    fun `where timestamp file would go`() {
        // XXX depends on whether we're running these ourselves or via the main project's tests
        assertTrue(System.getProperty("user.dir").endsWith("/junit5-gradle"))
    }

    @Test
    fun `false is false`() {
        assertFalse(false)
    }

    @Disabled
    @Test
    fun `disabled tests should get counted some type of way`() {
        assertTrue(true)
    }
}
