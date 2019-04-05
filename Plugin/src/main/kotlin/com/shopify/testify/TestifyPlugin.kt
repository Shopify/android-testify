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

package com.shopify.testify

import com.shopify.testify.TestifyPlugin.Companion.EVALUATED_SETTINGS
import com.shopify.testify.internal.Adb
import com.shopify.testify.tasks.main.ScreenshotClearTask
import com.shopify.testify.tasks.main.ScreenshotPullTask
import com.shopify.testify.tasks.main.ScreenshotRecordTask
import com.shopify.testify.tasks.main.ScreenshotTestTask
import com.shopify.testify.tasks.utility.DeviceKeyTask
import com.shopify.testify.tasks.utility.DevicesTask
import com.shopify.testify.tasks.utility.DisableSoftKeyboardTask
import com.shopify.testify.tasks.utility.GenerateDiffImagesTask
import com.shopify.testify.tasks.utility.HidePasswordsTasks
import com.shopify.testify.tasks.utility.ImageMagickTask
import com.shopify.testify.tasks.utility.LocaleTask
import com.shopify.testify.tasks.utility.RemoveDiffImagesTask
import com.shopify.testify.tasks.utility.SettingsTask
import com.shopify.testify.tasks.utility.TimeZoneTask
import com.shopify.testify.tasks.utility.VersionTask
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

class TestifyPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        with(project) {
            extensions.create(TestifyExtension.NAME, TestifyExtension::class.java)
            createTasks()
            afterEvaluate(AfterEvaluate)
        }
    }

    private object AfterEvaluate : Action<Project> {
        override fun execute(project: Project) {
            val settings = TestifySettingsFactory.create(project)
            project.validateExtension()
            settings.override(project.getTestifyExtension()).validate()
            project.extensions.add(EVALUATED_SETTINGS, settings)

            project.addDependencies()

            val version = javaClass.getPackage().implementationVersion

            // Do not auto-inject dependency in local configuration
            if (version?.contains("local", ignoreCase = true) == false) {
                project.dependencies.add("androidTestImplementation", "com.shopify.testify:testify:$version")
            }

            Adb.init(project)
        }

        private fun Project.addDependencies() {
            ScreenshotTestTask.setDependencies(ScreenshotTestTask.Companion, project = this)
            ScreenshotRecordTask.setDependencies(ScreenshotRecordTask.Companion, project = this)
            GenerateDiffImagesTask.setDependencies(GenerateDiffImagesTask.Companion, project = this)
        }
    }

    private fun Project.createTasks() {
        tasks.create(DeviceKeyTask.taskName(), DeviceKeyTask::class.java)
        tasks.create(DevicesTask.taskName(), DevicesTask::class.java)
        tasks.create(DisableSoftKeyboardTask.taskName(), DisableSoftKeyboardTask::class.java)
        tasks.create(GenerateDiffImagesTask.taskName(), GenerateDiffImagesTask::class.java)
        tasks.create(HidePasswordsTasks.taskName(), HidePasswordsTasks::class.java)
        tasks.create(ImageMagickTask.taskName(), ImageMagickTask::class.java)
        tasks.create(LocaleTask.taskName(), LocaleTask::class.java)
        tasks.create(RemoveDiffImagesTask.taskName(), RemoveDiffImagesTask::class.java)
        tasks.create(ScreenshotClearTask.taskName(), ScreenshotClearTask::class.java)
        tasks.create(ScreenshotPullTask.taskName(), ScreenshotPullTask::class.java)
        tasks.create(ScreenshotRecordTask.taskName(), ScreenshotRecordTask::class.java)
        tasks.create(ScreenshotTestTask.taskName(), ScreenshotTestTask::class.java)
        tasks.create(SettingsTask.taskName(), SettingsTask::class.java)
        tasks.create(TimeZoneTask.taskName(), TimeZoneTask::class.java)
        tasks.create(VersionTask.taskName(), VersionTask::class.java)
    }

    companion object {
        const val EVALUATED_SETTINGS = "testify_evaluated_settings"
    }
}

internal val Project.testifySettings: TestifySettings
    get() {
        return this.extensions.getByName(EVALUATED_SETTINGS) as TestifySettings
    }