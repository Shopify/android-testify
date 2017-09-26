/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.testify.task.testify

import com.shopify.testify.DeviceUtility
import com.shopify.testify.task.TestifyDefaultTask
import com.shopify.testify.task.internal.RecordModeTask
import org.gradle.api.Project

class ScreenshotTestTask extends TestifyDefaultTask {

    @Override
    String getDescription() {
        return "Run the screenshot tests"
    }

    def getTestTarget() {
        def testClass = project.getProperties().get("testClass")

        def testTarget = ""
        if (testClass != null && testClass != "") {
            testTarget = "-e class " + testClass
            def testName = project.getProperties().get("testName")
            if (testName != null && testName != "") {
                testTarget += "#" + testName
            }
        }

        return testTarget
    }

    static def runAndLog(command) {
        def log
        def process = command.execute()
        process.in.eachLine { line ->
            println line
            log += line
        }
        return log
    }

    private def getShardParams() {
        def shardParams = ""
        def shardIndex = project.getProperties().get("shardIndex")
        def shardCount = project.getProperties().get("shardCount")
        if (shardIndex != null && shardCount != null) {
            shardParams = "-e numShards ${shardCount} -e shardIndex ${shardIndex}"
            println "\nRunning test shard ${shardIndex} of ${shardCount}..."
        }
        return shardParams
    }

    private def getRuntimeParams() {
        def runtimeParams = ""
        def useSdCard = System.getenv("TESTIFY_USE_SDCARD")
        if (useSdCard != null && useSdCard.toBoolean()) {
            runtimeParams = "-e useSdCard true"
        }
        if (RecordModeTask.isRecordMode) {
            runtimeParams += "-e isRecordMode true"
        }
        String outputFileNameFormat = project.testify.outputFileNameFormat
        if (System.getenv("TESTIFY_OUTPUT_FORMAT") != null) {
            // Environment variable takes priority
            outputFileNameFormat = System.getenv("TESTIFY_OUTPUT_FORMAT")
        }
        if (outputFileNameFormat != null && !outputFileNameFormat.isEmpty()) {
            runtimeParams += "-e outputFileNameFormat ${outputFileNameFormat}"
        }
        return runtimeParams
    }

    @Override
    def taskAction() {
        def shardParams = getShardParams()
        def testPackage = project.testify.testPackageId
        def testRunner = project.testify.testRunner
        def testTarget = getTestTarget()
        def runtimeParams = getRuntimeParams()

        if (RecordModeTask.isRecordMode) {
            new DeviceUtility(project).clearScreenshots()
        }

        def command = [new DeviceUtility(project).getAdbPath(), '-e', 'shell', 'am', 'instrument', runtimeParams, shardParams, testTarget, '-e', 'annotation', 'com.shopify.testify.annotation.ScreenshotInstrumentation', '-w', "${testPackage}/${testRunner}"]
        def log = runAndLog(command)

        if (!RecordModeTask.isRecordMode && (log.contains("FAILURES!!!") || log.contains("INSTRUMENTATION_CODE: 0") || log.contains("Process crashed while executing"))) {
            throw new Exception("Screenshot tests failed");
        }

        if (RecordModeTask.isRecordMode) {
            new DeviceUtility(project).pullScreenshots();
        }
    }

    def addDependencies(Project project) {
        def tasks = project.tasks

        this.dependsOn "hidePasswords"
        this.dependsOn "disableSoftKeyboard"
        this.dependsOn "showLocale"
        this.dependsOn "showTimeZone"

        def installDebugAndroidTestTask = tasks.findByPath(":${project.testify.moduleName}:installDebugAndroidTest")
        if (installDebugAndroidTestTask != null) {
            this.dependsOn installDebugAndroidTestTask
        }

        def installDebugTask = tasks.findByPath(":${project.testify.moduleName}:installDebug")
        if (installDebugTask != null) {
            this.dependsOn installDebugTask
        }
    }
}
