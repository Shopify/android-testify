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

import android.content.Context
import android.util.DisplayMetrics
import android.util.Pair
import android.view.WindowManager

import java.util.Locale

object DeviceIdentifier {

    @JvmStatic
    val DEFAULT_FOLDER_FORMAT = "a-wxh@d-l"
    @JvmStatic
    val DEFAULT_NAME_FORMAT = "c_n"

    private fun getDeviceDimensions(context: Context): Pair<Int, Int> {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        val display = windowManager.defaultDisplay

        display.getRealMetrics(metrics)
        val realWidth = metrics.widthPixels
        val realHeight = metrics.heightPixels

        return Pair(realWidth, realHeight)
    }

    fun getDescription(context: Context): String {
        return formatDeviceString(DeviceStringFormatter(context, null), DEFAULT_FOLDER_FORMAT)
    }

    @JvmStatic
    fun formatDeviceString(formatter: DeviceStringFormatter, format: String): String {
        /*
        a: API level (ex. 21)
        w: Device width (ex.720)
        h: Device height (ex. 1440)
        d: Device density (ex. 320dp)
        l: Language (ex. en)
        c: Test class (ex. OrderDetailTest)
        n: Test name (ex. testDefault)
         */
        val a = formatter.androidVersion
        val w = formatter.deviceWidth
        val h = formatter.deviceHeight
        val d = formatter.deviceDensity
        val l = formatter.language
        val c = formatter.testClass
        val n = formatter.getTestName()

        val stringBuilder = StringBuilder("")
        for (i in 0 until format.length) {
            val character = format[i]
            when (character) {
                'a' -> stringBuilder.append(a)
                'w' -> stringBuilder.append(w)
                'h' -> stringBuilder.append(h)
                'd' -> stringBuilder.append(d)
                'l' -> stringBuilder.append(l)
                'c' -> stringBuilder.append(c)
                'n' -> stringBuilder.append(n)
                else -> stringBuilder.append(character)
            }
        }

        return stringBuilder.toString()
    }

    open class DeviceStringFormatter(private val context: Context, private val testName: Pair<String, String>?) {

        internal open val dimensions: Pair<Int, Int>
            get() = getDeviceDimensions(context)

        internal open val androidVersion: String
            get() = Integer.toString(android.os.Build.VERSION.SDK_INT)

        internal open val deviceWidth: String
            get() = dimensions.first.toString()

        internal open val deviceHeight: String
            get() = dimensions.second.toString()

        internal open val deviceDensity: String
            get() = context.resources.displayMetrics.densityDpi.toString() + "dp"

        internal open val language: String
            get() = Locale.getDefault().language

        internal open val testClass: String
            get() = if (testName == null) "" else testName.first

        internal open fun getTestName(): String {
            return if (testName == null) "" else testName.second
        }
    }
}