/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Shopify Inc.
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

package com.shopify.testify;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.view.View;

import com.shopify.testify.exception.ScreenshotDirectoryNotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
class ScreenshotUtility implements BitmapCompare {

    private static final String LOG_TAG = ScreenshotUtility.class.getSimpleName();
    private static final String PNG_EXTENSION = ".png";
    private static final String DATA_DESTINATION_DIR = "images";
    private static final String SDCARD_DESTINATION_DIR = "/testify_images";
    private static final String SOURCE_DIR = "screenshots/";
    @Nullable
    private Locale locale = null;

    private Bitmap createBitmapFromView(@NonNull final Activity activity, @Nullable final View targetView) {
        View v = targetView;
        if (v == null) {
            v = activity.getWindow().getDecorView();
        }
        v.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private boolean saveBitmapToFile(@NonNull Context context, @NonNull Bitmap bitmap, String outputFilePath) throws Exception {
        if (assureScreenshotDirectory(context)) {
            Log.d(LOG_TAG, "Writing screenshot to " + outputFilePath);
            OutputStream outputStream = new FileOutputStream(outputFilePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return true;
        } else {
            throw new ScreenshotDirectoryNotFoundException(outputFilePath);
        }
    }

    private boolean assureScreenshotDirectory(@NonNull Context context) {
        boolean created = true;
        File outputDirectory = getOutputDirectoryPath(context);
        if (!outputDirectory.exists()) {
            created = outputDirectory.mkdirs();
        }
        return created;
    }

    @NonNull
    private File getOutputDirectoryPath(@NonNull Context context) {
        Bundle extras = InstrumentationRegistry.getArguments();

        if (extras.containsKey("useSdCard") && extras.get("useSdCard").equals("true")) {
            File sdCard = Environment.getExternalStorageDirectory();
            return new File(sdCard.getAbsolutePath() + SDCARD_DESTINATION_DIR);

        }
        return context.getDir(DATA_DESTINATION_DIR, Context.MODE_PRIVATE);
    }

    private String getOutputFilePath(@NonNull final Context context, final String fileName) {
        return getOutputDirectoryPath(context).getPath() + File.separator + fileName + getLocaleIdentifier() + PNG_EXTENSION;
    }

    private String getLocaleIdentifier() {
        if (locale != null) {
            return "-" + locale.getLanguage();
        }
        return "";
    }

    private BitmapFactory.Options getPreferredBitmapOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return options;
    }

    @Nullable
    private Bitmap loadBitmapFromAsset(Context context, String filePath) throws Exception {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            inputStream = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(inputStream, null, getPreferredBitmapOptions());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to decode bitmap file.", e);
            bitmap = null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Unable to close input stream.", e);
                    bitmap = null;
                }
            }
        }
        return bitmap;
    }

    /**
     * Load a baseline bitmap from the androidTest assets directory.
     */
    @Nullable
    Bitmap loadBaselineBitmapForComparison(@NonNull Context context, String testName) throws Exception {
        String filePath = SOURCE_DIR + DeviceIdentifier.getDescription(context) + "/" + testName + PNG_EXTENSION;
        return loadBitmapFromAsset(context, filePath);
    }

    /**
     * Capture a bitmap from the given Activity and save it to the screenshots directory.
     */
    @Nullable
    Bitmap createBitmapFromActivity(final Activity activity, String testName, @Nullable final View screenshotView) throws Exception {
        final Bitmap[] currentActivityBitmap = new Bitmap[1];
        final CountDownLatch latch = new CountDownLatch(1);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentActivityBitmap[0] = createBitmapFromView(activity, screenshotView);
                latch.countDown();
            }
        });

        try {
            if (Debug.isDebuggerConnected()) {
                latch.await();
            } else if (!latch.await(2, TimeUnit.SECONDS)) {
                return null;
            }
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "createBitmapFromView interrupted.", e);
            return null;
        }

        String outputPath = getOutputFilePath(activity, testName);
        saveBitmapToFile(activity, currentActivityBitmap[0], outputPath);
        return BitmapFactory.decodeFile(outputPath, getPreferredBitmapOptions());
    }

    /**
     * Compare two bitmaps using {@link Bitmap#sameAs(Bitmap)}
     */
    @Override
    public boolean compareBitmaps(@Nullable Bitmap baselineBitmap, @Nullable Bitmap currentBitmap) {
        return !(baselineBitmap == null || currentBitmap == null) && baselineBitmap.sameAs(currentBitmap);
    }

    boolean deleteBitmap(Context context, String testName) {
        final File file = new File(getOutputFilePath(context, testName));
        return file.delete();
    }

    void setLocale(@Nullable Locale locale) {
        this.locale = locale;
    }
}
