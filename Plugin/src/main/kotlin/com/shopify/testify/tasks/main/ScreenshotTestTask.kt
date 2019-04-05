/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Shopify Inc.
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
package com.shopify.testify.tasks.main

import com.shopify.testify.TestifySettings
import com.shopify.testify.internal.Adb
import com.shopify.testify.internal.AdbParam
import com.shopify.testify.internal.AnsiFormat
import com.shopify.testify.internal.TestOptionsBuilder
import com.shopify.testify.internal.fromEnv
import com.shopify.testify.internal.println
import com.shopify.testify.tasks.internal.TaskDependencyProvider
import com.shopify.testify.tasks.internal.TaskNameProvider
import com.shopify.testify.tasks.internal.TestifyDefaultTask
import com.shopify.testify.tasks.utility.DisableSoftKeyboardTask
import com.shopify.testify.tasks.utility.HidePasswordsTasks
import com.shopify.testify.tasks.utility.LocaleTask
import com.shopify.testify.tasks.utility.TimeZoneTask
import com.shopify.testify.testifySettings
import org.gradle.api.Project

open class ScreenshotTestTask : TestifyDefaultTask() {

    open val isRecordMode: Boolean = false

    override fun getDescription() = "Run the Testify screenshot tests"

    private val settings: TestifySettings
        get() = project.testifySettings

    private val testTarget: AdbParam?
        get() {
            val testClass = project.properties["testClass"] as String?
            val testName = project.properties["testName"] as String?

            return if (testClass != null && testClass.isNotEmpty()) {
                AdbParam("class", "$testClass${if (testName != null && testName.isNotEmpty()) "#$testName" else ""}")
            } else {
                null
            }
        }

    private val shardParams: Set<AdbParam>
        get() {
            val shardIndex = project.properties["shardIndex"] as Int?
            val shardCount = project.properties["shardCount"] as Int?

            val params = HashSet<AdbParam>()
            if (shardCount != null && shardIndex != null) {
                println("  Running test shard $shardIndex of $shardCount...")
                params.add(AdbParam("numShards", "$shardCount"))
                params.add(AdbParam("shardIndex", "$shardIndex"))
            }
            return params
        }

    private val runtimeParams: List<AdbParam>
        get() {
            val params = ArrayList<AdbParam>()

            val useSdCard = "TESTIFY_USE_SDCARD".fromEnv(settings.useSdCard)
            val outputFormat = "TESTIFY_OUTPUT_FORMAT".fromEnv(settings.outputFileNameFormat)

            if (useSdCard)
                params.add(AdbParam("useSdCard", "true"))

            if (outputFormat?.isNotEmpty() == true)
                params.add(AdbParam("outputFileNameFormat", outputFormat))

            if (settings.moduleName.isNotEmpty())
                params.add(AdbParam("moduleName", settings.moduleName))

            if (isRecordMode)
                params.add(AdbParam("isRecordMode", "true"))

            return params
        }

    private val annotation = AdbParam("annotation", "com.shopify.testify.annotation.ScreenshotInstrumentation")

    override fun taskAction() {

        if (isRecordMode) {
            val clearTask = project.tasks.getByName(ScreenshotClearTask.taskName()) as ScreenshotClearTask
            clearTask.taskAction()
        }

        val testOptions = TestOptionsBuilder()
        testOptions
            .addAll(shardParams)
            .add(testTarget)
            .addAll(runtimeParams)
            .add(annotation)

        val log = Adb()
            .shell()
            .argument("am")
            .argument("instrument")
            .testOptions(testOptions)
            .argument("-w")
            .argument("${settings.testPackageId}/${settings.testRunner}")
            .stream(true)
            .execute()

        if (!isRecordMode && (log.contains("FAILURES!!!") || log.contains("INSTRUMENTATION_CODE: 0") || log.contains("Process crashed while executing"))) {
            println(AnsiFormat.Red, "SCREENSHOT TESTS HAVE FAILED!!!")
            throw RuntimeException("Screenshot tests have failed")
        }

        if (isRecordMode) {
            val pullTask = project.tasks.getByName(ScreenshotPullTask.taskName()) as ScreenshotPullTask
            pullTask.taskAction()
        }
    }

    companion object : TaskNameProvider, TaskDependencyProvider {
        override fun taskName() = "screenshotTest"

        override fun setDependencies(taskNameProvider: TaskNameProvider, project: Project) {
            val task = project.tasks.getByName(taskNameProvider.taskName())

            task.dependsOn(
                HidePasswordsTasks.taskName(),
                DisableSoftKeyboardTask.taskName(),
                LocaleTask.taskName(),
                TimeZoneTask.taskName()
            )

            val installDebugAndroidTestTask = project.tasks.findByPath(":${project.testifySettings.moduleName}:installDebugAndroidTest")
            if (installDebugAndroidTestTask != null) {
                task.dependsOn(installDebugAndroidTestTask)
            }

            val installDebugTask = project.tasks.findByPath(":${project.testifySettings.moduleName}:installDebug")
            if (installDebugTask != null) {
                task.dependsOn(installDebugTask)
            }
        }
    }
}