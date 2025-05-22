package com.schmonz.greencently

import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import java.time.Instant
import kotlin.io.path.deleteIfExists
import kotlin.io.path.setLastModifiedTime
import kotlin.io.path.writeText

class Greencently(filename: String) {
    private val directory = Paths.get(System.getProperty("user.dir"))
    private val status = directory.resolve(filename)

    fun writeStatus(completeAndGreen: Boolean) {
        if (completeAndGreen) {
            yes()
        } else {
            no()
        }
    }

    private fun yes() {
        status.writeText(
            "# Deprecation notice\n\n" +
                "In your Gradle config, please replace\n\n" +
                "    com.schmonz.junit-greencently\n\n" +
                "with\n\n" +
                "    com.schmonz.greencently"
        )
        status.setLastModifiedTime(FileTime.from(Instant.now()))
    }

    private fun no() =
        status.deleteIfExists()
}
