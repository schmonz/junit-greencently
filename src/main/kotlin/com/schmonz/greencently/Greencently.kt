package com.schmonz.greencently

import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import java.time.Instant
import kotlin.io.path.deleteIfExists
import kotlin.io.path.setLastModifiedTime

class Greencently(suffix: String) {
    private val path = Paths.get(System.getProperty("user.dir"))
        .resolve(".greencently-$suffix")

    private fun newlyCompleteAndGreen() = path.toFile().createNewFile()

    private fun againCompleteAndGreen() = path.setLastModifiedTime(FileTime.from(Instant.now()))

    private fun notCompleteOrNotGreen() = path.deleteIfExists()

    fun setStatus(completeAndGreen: Boolean) {
        if (completeAndGreen) {
            if (!newlyCompleteAndGreen()) againCompleteAndGreen()
        } else {
            notCompleteOrNotGreen()
        }
    }
}
