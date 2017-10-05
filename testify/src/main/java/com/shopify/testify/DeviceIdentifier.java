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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.Locale;

public class DeviceIdentifier {

    private DeviceIdentifier() {
    }

    private static Pair<Integer, Integer> getDeviceDimensions(@NonNull Context context) {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();

        int realWidth = 0;
        int realHeight = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(metrics);
            realWidth = metrics.widthPixels;
            realHeight = metrics.heightPixels;
        } else {
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");

                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                Log.e(DeviceIdentifier.class.getSimpleName(), "Could not retrieve real device metrics", e);
            }
        }

        return new Pair<>(realWidth, realHeight);
    }

    public static String getDescription(@NonNull Context context) {
        return formatDeviceString(context, null, "a-wxh@d-l");
    }

    public static String formatDeviceString(@NonNull Context context, @Nullable Pair<String, String> testName, String format) {
        /*
        a: API level (ex. 21)
        w: Device width (ex.720)
        h: Device height (ex. 1440)
        d: Device density (ex. 320dp)
        l: Language (ex. en)
        c: Test class (ex. OrderDetailTest)
        n: Test name (ex. testDefault)
         */
        final Pair<Integer, Integer> dimensions = getDeviceDimensions(context);

        final String a = Integer.toString(android.os.Build.VERSION.SDK_INT);
        final String w = dimensions.first.toString();
        final String h = dimensions.second.toString();
        final String d = context.getResources().getDisplayMetrics().densityDpi + "dp";
        final String l = Locale.getDefault().getLanguage();
        final String c = (testName == null) ? "" : testName.first;
        final String n = (testName == null) ? "" : testName.second;

        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < format.length(); i++) {
            char character = format.charAt(i);
            switch (character) {
                case 'a':
                    stringBuilder.append(a);
                    break;
                case 'w':
                    stringBuilder.append(w);
                    break;
                case 'h':
                    stringBuilder.append(h);
                    break;
                case 'd':
                    stringBuilder.append(d);
                    break;
                case 'l':
                    stringBuilder.append(l);
                    break;
                case 'c':
                    stringBuilder.append(c);
                    break;
                case 'n':
                    stringBuilder.append(n);
                    break;
                default:
                    stringBuilder.append(character);
                    break;
            }
        }

        return stringBuilder.toString();
    }
}