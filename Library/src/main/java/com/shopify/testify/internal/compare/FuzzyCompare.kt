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
package com.shopify.testify.internal.compare

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting

internal class FuzzyCompare(private val exactness: Float) : BitmapCompare {

    override fun compareBitmaps(baselineBitmap: Bitmap, currentBitmap: Bitmap): Boolean {
        if (baselineBitmap.height != currentBitmap.height) {
            return false
        }

        if (baselineBitmap.width != currentBitmap.width) {
            return false
        }

        val height = baselineBitmap.height
        val width = baselineBitmap.width

        for (y in 0 until height) {
            for (x in 0 until width) {
                @ColorInt val baselineColor = baselineBitmap.getPixel(x, y)
                @ColorInt val currentColor = currentBitmap.getPixel(x, y)

                val baselineHsv = FloatArray(HSV_SIZE)
                val currentHsv = FloatArray(HSV_SIZE)

                /*
                 * hsv[0] is Hue [0 .. 360)
                 * hsv[1] is Saturation [0...1]
                 * hsv[2] is Value [0...1]
                 */

                Color.colorToHSV(baselineColor, baselineHsv)
                Color.colorToHSV(currentColor, currentHsv)

                if (isHueDifferent(baselineHsv[HUE], currentHsv[HUE], exactness)) {
                    return false
                }
                if (isValueDifferent(baselineHsv[SAT], currentHsv[SAT], exactness)) {
                    return false
                }
                if (isValueDifferent(baselineHsv[VAL], currentHsv[VAL], exactness)) {
                    return false
                }
            }
        }
        return true
    }

    companion object {

        private const val HSV_SIZE = 3
        private const val HUE = 0
        private const val SAT = 1
        private const val VAL = 2

        @VisibleForTesting
        fun isHueDifferent(baseline: Float, current: Float, exactness: Float): Boolean {
            val diff = hueDifference(baseline, current)
            val epsilon = 360.0f * (1.0f - exactness) + 0.0001f
            return diff > epsilon
        }

        @VisibleForTesting
        fun isValueDifferent(baseline: Float, current: Float, exactness: Float): Boolean {
            val diff = Math.abs(baseline - current)
            val epsilon = 1.0f - exactness + 0.0001f
            return diff > epsilon
        }

        private fun hueDifference(degree1: Float, degree2: Float): Float {
            val angle1 = Math.toRadians(degree1.toDouble())
            val angle2 = Math.toRadians(degree2.toDouble())

            val x1 = Math.cos(angle1)
            val y1 = Math.sin(angle1)
            val x2 = Math.cos(angle2)
            val y2 = Math.sin(angle2)

            val dotProduct = x1 * x2 + y1 * y2
            return Math.toDegrees(Math.acos(dotProduct)).toFloat()
        }
    }
}
