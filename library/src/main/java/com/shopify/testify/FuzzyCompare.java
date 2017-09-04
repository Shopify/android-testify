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

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

class FuzzyCompare implements BitmapCompare {

    private static final int HSV_SIZE = 3;
    private static final int HUE = 0;
    private static final int SAT = 1;
    private static final int VAL = 2;

    private float exactness;

    FuzzyCompare(@FloatRange(from = 0.0, to = 1.0) float exactness) {
        this.exactness = exactness;
    }

    @VisibleForTesting
    static boolean isHueDifferent(float baseline, float current, float exactness) {
        float diff = hueDifference(baseline, current);
        float epsilon = 360.0f * (1.0f - exactness) + 0.0001f;
        return diff > epsilon;
    }

    @VisibleForTesting
    static boolean isValueDifferent(float baseline, float current, float exactness) {
        float diff = Math.abs(baseline - current);
        float epsilon = (1.0f - exactness) + 0.0001f;
        return diff > epsilon;
    }

    private static float hueDifference(float degree1, float degree2) {
        double angle1 = Math.toRadians(degree1);
        double angle2 = Math.toRadians(degree2);

        double x1 = Math.cos(angle1);
        double y1 = Math.sin(angle1);
        double x2 = Math.cos(angle2);
        double y2 = Math.sin(angle2);

        double dotProduct = x1 * x2 + y1 * y2;
        return (float) Math.toDegrees(Math.acos(dotProduct));
    }

    @Override
    public boolean compareBitmaps(@Nullable Bitmap baselineBitmap, @Nullable Bitmap currentBitmap) {
        if ((baselineBitmap == null) || (currentBitmap == null)) {
            return false;
        }

        if (baselineBitmap.getHeight() != currentBitmap.getHeight()) {
            return false;
        }

        if (baselineBitmap.getWidth() != currentBitmap.getWidth()) {
            return false;
        }

        int height = baselineBitmap.getHeight();
        int width = baselineBitmap.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                @ColorInt int baselineColor = baselineBitmap.getPixel(x, y);
                @ColorInt int currentColor = currentBitmap.getPixel(x, y);

                float[] baselineHsv = new float[HSV_SIZE];
                float[] currentHsv = new float[HSV_SIZE];

                /*
                 * hsv[0] is Hue [0 .. 360)
                 * hsv[1] is Saturation [0...1]
                 * hsv[2] is Value [0...1]
                 */

                Color.colorToHSV(baselineColor, baselineHsv);
                Color.colorToHSV(currentColor, currentHsv);

                if (isHueDifferent(baselineHsv[HUE], currentHsv[HUE], exactness)) {
                    return false;
                }
                if (isValueDifferent(baselineHsv[SAT], currentHsv[SAT], exactness)) {
                    return false;
                }
                if (isValueDifferent(baselineHsv[VAL], currentHsv[VAL], exactness)) {
                    return false;
                }
            }
        }
        return true;
    }

}
