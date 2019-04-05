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
package com.shopify.testify.internal.capture

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.view.PixelCopy
import android.view.View
import java.util.concurrent.CountDownLatch

@Suppress("unused")
@TargetApi(Build.VERSION_CODES.O)
fun createBitmapUsingPixelCopy(activity: Activity, targetView: View?): Bitmap {
    val window = activity.window!!
    val view: View = targetView ?: activity.window.decorView
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

    val locationInWindow = IntArray(2)
    view.getLocationInWindow(locationInWindow)
    val latch = CountDownLatch(1)
    val handlerThread = HandlerThread("TestifyPixelCopy").apply { start() }

    PixelCopy.request(
        window,
        Rect(locationInWindow[0], locationInWindow[1], locationInWindow[0] + view.width, locationInWindow[1] + view.height),
        bitmap,
        { copyResult ->
            if (copyResult == PixelCopy.SUCCESS) {
                latch.countDown()
            } else {
                throw RuntimeException("Failed to capture bitmap")
            }
            handlerThread.quitSafely()
        },
        Handler(handlerThread.looper)
    )

    latch.await()

    return bitmap
}