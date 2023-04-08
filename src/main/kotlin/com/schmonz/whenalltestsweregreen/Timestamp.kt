package com.schmonz.whenalltestsweregreen

import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import java.time.Instant
import kotlin.io.path.setLastModifiedTime

class Timestamp(prefix: String) {
    private val path = Paths.get(System.getProperty("user.dir"))
        .resolve(prefix + "when-all-tests-were-green")

    private fun modifyFile() =
        path.setLastModifiedTime(FileTime.from(Instant.now()))

    private fun createFile() =
        path.toFile().createNewFile()

    fun setToNow() {
        if (!createFile()) {
            modifyFile()
        }
    }
}
