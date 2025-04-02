package com.schmonz.greencently

import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import java.time.Instant
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.setLastModifiedTime
import kotlin.io.path.writeText

class Greencently(filename: String) {
    private val directory = Paths.get(System.getProperty("user.dir")).resolve(".greencently")
    private val gitignore = directory.resolve(".gitignore")
    private val status = directory.resolve(filename)

    fun writeStatus(completeAndGreen: Boolean) {
        if (completeAndGreen) {
            yes()
        } else {
            no()
        }
    }

    private fun yes() {
        directory.createDirectories()
        gitignore.writeText("*")
        status.writeText("")
        status.setLastModifiedTime(FileTime.from(Instant.now()))
    }

    private fun no() =
        status.deleteIfExists()
}
