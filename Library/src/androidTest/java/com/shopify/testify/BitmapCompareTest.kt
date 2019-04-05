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

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.shopify.testify.internal.compare.FuzzyCompare
import com.shopify.testify.internal.compare.SameAsCompare
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BitmapCompareTest {

    @get:Rule var testActivityRule = ActivityTestRule(TestActivity::class.java)

    private val screenshotUtility = ScreenshotUtility()
    private lateinit var baselineBitmap: Bitmap
    private lateinit var activity: Activity

    @Before
    fun setUp() {
        activity = testActivityRule.activity
        baselineBitmap = screenshotUtility.loadBaselineBitmapForComparison(activity, "test")!!
    }

    @Test
    fun sameAsIdentical() {
        assertTrue(SameAsCompare().compareBitmaps(baselineBitmap, baselineBitmap))
    }

    @Test
    fun sameAsDifferent() {
        val rootView = activity.findViewById<View>(R.id.test_root_view)
        val capturedBitmap = screenshotUtility.createBitmapFromActivity(activity, "testing", rootView)!!

        assertFalse(SameAsCompare().compareBitmaps(capturedBitmap, baselineBitmap))
    }

    @Test
    fun fuzzy() {
        val similarBitmap = baselineBitmap.copy(baselineBitmap.config, true)!!
        similarBitmap.setPixel(0, 0, similarBitmap.getPixel(0, 0) + 1)

        assertFalse(FuzzyCompare(1.0f).compareBitmaps(similarBitmap, baselineBitmap))
        assertTrue(FuzzyCompare(0.99f).compareBitmaps(similarBitmap, baselineBitmap))
    }
}