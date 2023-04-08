package com.schmonz.whenalltestsweregreen.summary

import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan

class JUnit5Summary(
    testPlan: TestPlan,
    override val anyTestsRed: Boolean,
    override val anyTestsGreen: Boolean,
) : Summary() {
    override val testCount = testPlan.countTestIdentifiers(TestIdentifier::isTest)
}

// XXX describe to stdout
