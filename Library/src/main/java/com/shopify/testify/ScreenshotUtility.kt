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
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Debug
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.test.platform.app.InstrumentationRegistry
import com.shopify.testify.internal.DeviceIdentifier
import com.shopify.testify.internal.capture.Capture
import com.shopify.testify.internal.capture.createBitmapFromDrawingCache
import com.shopify.testify.internal.exception.ScreenshotDirectoryNotFoundException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

internal class ScreenshotUtility {
    private var locale: Locale? = null

    private val localeIdentifier: String
        get() = if (locale != null) {
            "-" + locale!!.language
        } else ""

    private val preferredBitmapOptions: BitmapFactory.Options
        get() {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            return options
        }

    @Throws(Exception::class)
    private fun saveBitmapToFile(context: Context, bitmap: Bitmap?, outputFilePath: String): Boolean {
        if (bitmap == null) {
            return false
        }
        if (assureScreenshotDirectory(context)) {
            Log.d(LOG_TAG, "Writing screenshot to {$outputFilePath}")
            val outputStream = FileOutputStream(outputFilePath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return true
        } else {
            throw ScreenshotDirectoryNotFoundException(useSdCard(), getOutputDirectoryPath(context).absolutePath)
        }
    }

    private fun assureScreenshotDirectory(context: Context): Boolean {
        var created = true
        val outputDirectory = getOutputDirectoryPath(context)
        if (!outputDirectory.exists()) {
            Log.d(LOG_TAG, "Trying to make the directory")
            created = outputDirectory.mkdirs()
        }
        return created
    }

    private fun getOutputDirectoryPath(context: Context): File {
        val path: File
        path = if (useSdCard()) {
            val sdCard = Environment.getExternalStorageDirectory()
            File(sdCard.absolutePath + SDCARD_DESTINATION_DIR)
        } else {
            context.getDir(DATA_DESTINATION_DIR, Context.MODE_PRIVATE)
        }
        return path
    }

    fun getOutputFilePath(context: Context, fileName: String): String {
        return getOutputDirectoryPath(context).path + File.separator + fileName + localeIdentifier + PNG_EXTENSION
    }

    @Throws(Exception::class)
    private fun loadBitmapFromAsset(context: Context, filePath: String): Bitmap? {
        val assetManager = context.assets
        var inputStream: InputStream? = null
        var bitmap: Bitmap?
        try {
            inputStream = assetManager.open(filePath)
            bitmap = BitmapFactory.decodeStream(inputStream, null, preferredBitmapOptions)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Unable to decode bitmap file.", e)
            bitmap = null
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    Log.e(LOG_TAG, "Unable to close input stream.", e)
                    bitmap = null
                }
            }
        }
        return bitmap
    }

    /**
     * Load a baseline bitmap from the androidTest assets directory.
     */
    fun loadBaselineBitmapForComparison(context: Context, testName: String): Bitmap? {
        val filePath = SOURCE_DIR + DeviceIdentifier.getDescription(context) + "/" + testName + PNG_EXTENSION
        return loadBitmapFromAsset(context, filePath)
    }

    private val createBitmapFromView: Capture
        get() {
            return ::createBitmapFromDrawingCache
        }

    /**
     * Capture a bitmap from the given Activity and save it to the screenshots directory.
     */
    fun createBitmapFromActivity(activity: Activity, fileName: String, screenshotView: View?): Bitmap? {
        val currentActivityBitmap = arrayOfNulls<Bitmap>(1)
        val latch = CountDownLatch(1)
        activity.runOnUiThread {
            currentActivityBitmap[0] = createBitmapFromView(activity, screenshotView)
            latch.countDown()
        }

        try {
            if (Debug.isDebuggerConnected()) {
                latch.await()
            } else if (!latch.await(2, TimeUnit.SECONDS)) {
                return null
            }
        } catch (e: InterruptedException) {
            Log.e(LOG_TAG, "createBitmapFromView interrupted.", e)
            return null
        }

        val outputPath = getOutputFilePath(activity, fileName)
        saveBitmapToFile(activity, currentActivityBitmap[0], outputPath)
        return BitmapFactory.decodeFile(outputPath, preferredBitmapOptions)
    }

    fun deleteBitmap(context: Context, fileName: String): Boolean {
        val file = File(getOutputFilePath(context, fileName))
        return file.delete()
    }

    fun setLocale(locale: Locale?) {
        this.locale = locale
    }

    companion object {

        private val LOG_TAG = ScreenshotUtility::class.java.simpleName
        private const val PNG_EXTENSION = ".png"
        private const val DATA_DESTINATION_DIR = "images"
        private const val SDCARD_DESTINATION_DIR = "/testify_images"
        private const val SOURCE_DIR = "screenshots/"

        fun useSdCard(): Boolean {
            val extras = InstrumentationRegistry.getArguments()
            return extras.containsKey("useSdCard") && extras.get("useSdCard") == "true"
        }
    }
}
