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

object Device {

    fun version(): Int {
        val result = Adb().arguments(
            "shell",
            "getprop",
            "ro.build.version.sdk")
            .execute()
        return result.trim().toInt()
    }

    fun language(): String {
        return if (version() <= 21) {
            Adb().arguments(
                "shell",
                "getprop",
                "ro.product.locale.language")
                .execute().trim()
        } else {
            Adb().arguments(
                "shell",
                "getprop",
                "ro.product.locale")
                .execute().trim().split("-").first()
        }
    }

    fun locale(): String {
        return if (version() <= 21) {
            val language = Adb().arguments(
                "shell",
                "getprop",
                "ro.product.locale.language")
                .execute().trim()
            val region = Adb().arguments(
                "shell",
                "getprop",
                "ro.product.locale.region")
                .execute().trim()
            "$language-$region"
        } else {
            Adb().arguments(
                "shell",
                "getprop",
                "ro.product.locale")
                .execute()
        }
    }

    fun timeZone(): String {
        val result = Adb().arguments(
            "shell",
            "getprop",
            "persist.sys.timezone")
            .execute()
        return result.trim()
    }

    fun displayDensity(): Int {
        val densityLine = Adb().arguments(
            "shell",
            "wm",
            "density")
            .execute().trim()
        return densityLine.substring("Physical density: ".length).trim().toInt()
    }

    fun displaySize(): String {
        val sizeLine = Adb().arguments(
            "shell",
            "wm",
            "size")
            .execute().trim()
        return sizeLine.substring("Physical size: ".length).trim()
    }

    fun deviceKey(language: String = Device.language()): String {
        return "${version()}-${displaySize()}@${displayDensity()}dp-$language"
    }

    val hasRootAccess: Boolean
        get() = Adb().arguments(
            "shell",
            "ls",
            "data/data")
            .execute().trim().isNotEmpty()
}