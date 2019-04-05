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

package com.shopify.testify.internal

import com.shopify.testify.testifySettings
import org.gradle.api.Project
import java.io.File

internal fun Project.getDeviceImageDirectory(): String {
    return if (this.testifySettings.useSdCard)
        "/sdcard/testify_images/"
    else
        "/data/data/${this.testifySettings.testContextId}/app_images/"
}

internal fun Project.listFailedScreenshots(): List<String> {
    val src = getDeviceImageDirectory()
    val dst = getDestinationImageDirectory()

    val log = Adb()
        .argument("shell ls $src*.png")
        .argument("2>/dev/null")
        .execute()

    val files = ArrayList<String>()
    log.lines().forEach {
        if (it.isNotEmpty()) {
            files.add(it.replace(src, dst))
        }
    }

    return files
}

internal fun File.deleteOnDevice() {
    Adb()
        .argument("shell")
        .argument("rm")
        .argument(this.path)
        .stream(true)
        .execute()
}