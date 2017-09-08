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
import android.support.annotation.LayoutRes;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import static junit.framework.Assert.assertNotNull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ScreenshotTest extends BaseScreenshotTest<ScreenshotTest> {

    private ActivityInstrumentationTestCase2 testCase;

    public ScreenshotTest(ActivityInstrumentationTestCase2 testCase) {
        this(testCase, NO_ID);
    }

    public ScreenshotTest(ActivityInstrumentationTestCase2 testCase, @LayoutRes int layoutId) {
        super(layoutId);
        this.testCase = testCase;
    }

    @Override
    protected String getTestName() {
        return testCase.getClass().getSimpleName() + "_" + testCase.getName();
    }

    @Override
    protected Context getTestContext() {
        return testCase.getInstrumentation().getContext();
    }

    @Override
    protected Activity getActivity() {
        return testCase.getActivity();
    }

    @Override
    protected ScreenshotTest getThis() {
        return this;
    }

    @IdRes
    @Override
    protected int getRootViewId() {
        if ((testCase instanceof ScreenshotTestCase) && (((ScreenshotTestCase) testCase).getRootViewId() != View.NO_ID)) {
            return ((ScreenshotTestCase) testCase).getRootViewId();
        } else {
            return super.getRootViewId();
        }
    }

    @Override
    public void assertSame() throws Exception {
        assertNotNull("You must specify an instrumentation test case.\n\n * Call ScreenshotAssert.instrumentation(this);\n", testCase);
        super.assertSame();
    }
}
