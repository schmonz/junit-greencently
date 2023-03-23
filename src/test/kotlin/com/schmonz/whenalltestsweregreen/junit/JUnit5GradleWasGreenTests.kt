package com.schmonz.whenalltestsweregreen.junit

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.api.parallel.ResourceAccessMode.READ_WRITE
import org.junit.jupiter.api.parallel.ResourceLock
import org.junit.platform.launcher.core.LauncherFactory
import org.junit.platform.launcher.listeners.SummaryGeneratingListener
import java.io.File
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.fail

private const val SAMPLE_PROJECT_ROOT = "sample-projects/junit5-gradle"

// XXX duplicated with build.gradle.kts -- extract to properties file?
private const val SAMPLE_PROJECT_TEST_CLASSPATH = "${SAMPLE_PROJECT_ROOT}/build/classes/kotlin/test"

@Execution(ExecutionMode.SAME_THREAD)
class JUnit5GradleWasGreenTests {
    companion object {
        @JvmStatic
        @BeforeAll
        fun `build the tests`() {
            val commandLine = listOf("../../gradlew", "compileTestKotlin")
            val exitCode = ProcessBuilder(commandLine)
                .directory(File(System.getProperty("user.dir"), SAMPLE_PROJECT_ROOT))
                .start()
                .waitFor()
            if (exitCode != 0) {
                fail("'$commandLine' exited $exitCode")
            }
        }
    }

    @BeforeEach
    @AfterEach
    fun `remove timestamp file`() {
        Files.deleteIfExists(JUnit5WasGreen.whereToWriteTimestamp)
    }

    private fun discoverAndRunTests(filenameRegex: String = ".*"): SummaryGeneratingListener {
        val listener = SummaryGeneratingListener()

        val launcher = LauncherFactory.openSession().launcher
        launcher.registerTestExecutionListeners(listener)
        launcher.execute(JUnit5WasGreen.planWhichTests(Paths.get(SAMPLE_PROJECT_TEST_CLASSPATH), filenameRegex))

        return listener
    }

    @Test
    @ResourceLock(value = "same file", mode = READ_WRITE)
    fun `count total tests the same way JUnit counts total tests`() {
        val myCount: Long

        val allTests = JUnit5WasGreen.planWhichTests(Paths.get(SAMPLE_PROJECT_TEST_CLASSPATH))
        myCount = JUnit5WasGreen.countTests(allTests)
        val launcher = LauncherFactory.openSession().launcher
        val listener = SummaryGeneratingListener()
        launcher.registerTestExecutionListeners(listener)
        launcher.execute(allTests)

        assertEquals(myCount, listener.summary.testsFoundCount)
    }

    @Test
    @ResourceLock(value = "same file", mode = READ_WRITE)
    fun `when not all tests ran, do not create timestamp`() {
        val listener = discoverAndRunTests(".*Other.*")

        assertEquals(2, listener.summary.testsFoundCount)
        assertEquals(2, listener.summary.testsSucceededCount)
        assertEquals(0, listener.summary.testsSkippedCount)
        assertEquals(0, listener.summary.testsFailedCount)
        assertEquals(0, listener.summary.testsAbortedCount)

        assertFalse(Files.exists(JUnit5WasGreen.whereToWriteTimestamp))
    }

    @Test
    @ResourceLock(value = "same file", mode = READ_WRITE)
    fun `when all tests ran green, create timestamp`() {
        val listener = discoverAndRunTests()

        assertEquals(5, listener.summary.testsFoundCount)
        assertEquals(3, listener.summary.testsSucceededCount)
        assertEquals(2, listener.summary.testsSkippedCount)
        assertEquals(0, listener.summary.testsFailedCount)
        assertEquals(0, listener.summary.testsAbortedCount)

        assertTrue(Files.exists(JUnit5WasGreen.whereToWriteTimestamp))
    }

    @Test
    @ResourceLock(value = "same file", mode = READ_WRITE)
    fun `when all tests ran but not all green, do not update timestamp`() {
        val contentsOfEphemeralTestFile = """
            package com.schmonz.whenalltestsweregreen.sample.junit5gradle

            import org.junit.jupiter.api.Assertions.assertTrue
            import org.junit.jupiter.api.Test

            class YouNeverSawMe {
                @Test
                fun `this test will be red`() {
                    assertTrue(false)
                }
            }
        """.trimIndent()
        val ephemeralTestFile = File(
            System.getProperty("user.dir"),
            "${SAMPLE_PROJECT_ROOT}/src/test/kotlin/com/schmonz/whenalltestsweregreen/sample/junit5gradle/YouNeverSawMe.kt"
        )
        ephemeralTestFile.writer().write(contentsOfEphemeralTestFile)
        `build the tests`()

        val listener = discoverAndRunTests()

        Files.deleteIfExists(ephemeralTestFile.toPath())
        `build the tests`()

        listener.summary.printTo(PrintWriter(System.err))
        assertEquals(5, listener.summary.testsFoundCount)
        assertEquals(2, listener.summary.testsSucceededCount)
        assertEquals(2, listener.summary.testsSkippedCount)
        assertEquals(1, listener.summary.testsFailedCount)
        assertEquals(0, listener.summary.testsAbortedCount)

        assertFalse(Files.exists(JUnit5WasGreen.whereToWriteTimestamp))
    }
}
