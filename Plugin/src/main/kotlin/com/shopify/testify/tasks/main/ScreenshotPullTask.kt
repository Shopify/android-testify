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

import com.shopify.testify.internal.Adb
import com.shopify.testify.internal.AnsiFormat
import com.shopify.testify.internal.Device
import com.shopify.testify.internal.assurePath
import com.shopify.testify.internal.getDestinationImageDirectory
import com.shopify.testify.internal.getDeviceImageDirectory
import com.shopify.testify.internal.listFailedScreenshots
import com.shopify.testify.internal.println
import com.shopify.testify.tasks.internal.TaskNameProvider
import com.shopify.testify.tasks.internal.TestifyDefaultTask
import com.shopify.testify.testifySettings
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.util.regex.Pattern

open class ScreenshotPullTask : TestifyDefaultTask() {

    override fun getDescription() = "Pull screenshots from the device and wait for all files to be committed to disk"

    override fun taskAction() {
        println("  Pulling screenshots:")

        if (!Device.hasRootAccess) {
            println(AnsiFormat.Red, "  Permission denied trying to access device directory. Try running 'adb root'.")
            return
        }

        val failedScreenshots = project.listFailedScreenshots()
        if (failedScreenshots.isEmpty()) {
            println(AnsiFormat.Green, "  No failed screenshots found")
            return
        }

        println("  ${failedScreenshots.size} images to be pulled")

        pullScreenshots()
        distributeLocalizedScreenshots()
        syncScreenshots()

        println("  Ready")
    }

    private fun pullScreenshots() {
        val src = "${project.getDeviceImageDirectory()}."
        val dst = project.getDestinationImageDirectory()
        File(dst).assurePath()

        println()
        println("  Source               = $src")
        println("  Destination          = $dst")
        println()

        Adb()
            .argument("pull")
            .argument(src)
            .argument(dst)
            .execute()

        Thread.sleep(project.testifySettings.pullWaitTime)
    }

    private fun syncScreenshots() {
        val failedScreenshots = project.listFailedScreenshots()
        failedScreenshots.forEach {
            println(AnsiFormat.Green, "    Copying ${File(it).nameWithoutExtension}...")
            Thread.sleep(SYNC_SLEEP)
        }

        println("")
    }

    private fun distributeLocalizedScreenshots() {
        val pattern = Pattern.compile(".*-([a-z]{2})\\.png")
        project.listFailedScreenshots().forEach {
            val matcher = pattern.matcher(it)
            if (matcher.matches()) {
                val language = matcher.group(1)
                val destinationImageDirectory = project.getDestinationImageDirectory(language)
                val destinationFilePath = "$destinationImageDirectory${it.substring(it.lastIndexOf("/") + 1, matcher.start(1) - 1)}.png"
                if (File(destinationImageDirectory).exists()) {
                    File(destinationImageDirectory).mkdir()
                }
                Files.move(Paths.get(it), Paths.get(destinationFilePath), REPLACE_EXISTING)
            }
        }
    }

    companion object : TaskNameProvider {
        override fun taskName() = "screenshotPull"
        const val SYNC_SLEEP = 125L
    }
}