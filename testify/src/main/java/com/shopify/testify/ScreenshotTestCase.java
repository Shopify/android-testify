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
import android.support.annotation.IdRes;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.shopify.testify.annotation.ScreenshotInstrumentation;

/**
 * This class provides an ActivityInstrumentationTestCase2 subclass which handles screenshot testing.
 *
 * @param <T> An Activity subclass to test
 */
@SuppressWarnings({"unused", "deprecation", "WeakerAccess"})
@ScreenshotInstrumentation
public class ScreenshotTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    @IdRes private int rootViewId = View.NO_ID;

    public ScreenshotTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    public ScreenshotTestCase(Class<T> activityClass, @IdRes int rootViewId) {
        super(activityClass);
        this.rootViewId = rootViewId;
    }

    public void initMockito(Context context) {
        /*
         Required by Mockito
         Fixes "dexcache == null (and no default could be found; consider setting the 'dexmaker.dexcache' system property)"
         See https://code.google.com/p/dexmaker/issues/detail?id=2
         */
        System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
    }

    @IdRes
    protected int getRootViewId() {
        return rootViewId;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initMockito(getInstrumentation().getTargetContext());
    }
}
