package com.shopify.testify

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import android.view.View

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.io.File

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@RunWith(AndroidJUnit4::class)
class ScreenshotUtilityTest {

    @Rule
    var testActivityRule = ActivityTestRule(TestActivity::class.java)

    @Test
    fun loadBaselineBitmapForComparison() {
        val context = testActivityRule.activity
        val screenshotUtility = ScreenshotUtility()

        val baselineBitmap = screenshotUtility.loadBaselineBitmapForComparison(context, "test")
        assertNotNull(baselineBitmap)
    }

    @Test
    fun createBitmapFromActivity() {
        val activity = testActivityRule.activity
        val rootView = activity.findViewById<View>(R.id.test_root_view)
        val screenshotUtility = ScreenshotUtility()
        val outputFilePath = screenshotUtility.getOutputFilePath(activity, "testing")
        val bitmapFile = File(outputFilePath)

        val capturedBitmap = screenshotUtility.createBitmapFromActivity(activity, "testing", rootView)
        assertNotNull(capturedBitmap)
        assertTrue(bitmapFile.exists())
    }

    @Test
    fun compareBitmaps() {
        val activity = testActivityRule.activity
        val rootView = activity.findViewById<View>(R.id.test_root_view)
        val screenshotUtility = ScreenshotUtility()

        val capturedBitmap = screenshotUtility.createBitmapFromActivity(activity, "testing", rootView)
        val baselineBitmap = screenshotUtility.loadBaselineBitmapForComparison(activity, "test")

        val compareDifferentBitmaps = screenshotUtility.compareBitmaps(capturedBitmap, baselineBitmap)
        val compareSameBitmaps = screenshotUtility.compareBitmaps(baselineBitmap, baselineBitmap)
        assertFalse(compareDifferentBitmaps)
        assertTrue(compareSameBitmaps)
    }

    @Test
    fun deleteBitmap() {
        val activity = testActivityRule.activity
        val screenshotUtility = ScreenshotUtility()

        createBitmapFromActivity()

        val fileName = "testing"
        val outputFilePath = screenshotUtility.getOutputFilePath(activity, fileName)

        screenshotUtility.deleteBitmap(activity, fileName)
        val fileThatShouldBeDeleted = File(outputFilePath)
        assertFalse(fileThatShouldBeDeleted.exists())
    }

}