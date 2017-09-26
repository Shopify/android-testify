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
import android.support.annotation.LayoutRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.util.Pair;
import android.view.View;

import com.shopify.testify.annotation.BitmapComparisonExactness;
import com.shopify.testify.annotation.TestifyLayout;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

// Consider subclassing ActivityTestRule
@SuppressWarnings({"unused", "WeakerAccess"})
public class ScreenshotTestRule<T extends Activity> extends ActivityTestRule<T> implements TestRule {

    @LayoutRes
    private int layoutId;
    private String testMethodNode;
    private String testSimpleClassName;
    private String testClass;
    private Throwable throwable;
    private BaseScreenshotTest.EspressoActions espressoActions;
    private BaseScreenshotTest.ViewModification viewModification;
    private Float exactness = null;

    public ScreenshotTestRule(Class<T> activityClass) {
        super(activityClass);
    }

    public ScreenshotTestRule(Class<T> activityClass, boolean initialTouchMode) {
        super(activityClass, initialTouchMode);
    }

    public ScreenshotTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity) {
        super(activityClass, initialTouchMode, launchActivity);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        throwable = null;
        espressoActions = null;
        testSimpleClassName = description.getTestClass().getSimpleName();
        testMethodNode = description.getMethodName();
        testClass = description.getTestClass().getCanonicalName() + "#" + description.getMethodName();
        final TestifyLayout testifyLayout = description.getAnnotation(TestifyLayout.class);
        layoutId = (testifyLayout != null) ? testifyLayout.layoutId() : View.NO_ID;
        final BitmapComparisonExactness bitmapComparison = description.getAnnotation(BitmapComparisonExactness.class);
        exactness = (bitmapComparison != null) ? bitmapComparison.exactness() : null;
        return new ScreenshotStatement(super.apply(new BaseInterceptorStatement(base), description));
    }

    public ScreenshotTestRule setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public ScreenshotTestRule setEspressoActions(BaseScreenshotTest.EspressoActions espressoActions) {
        this.espressoActions = espressoActions;
        return this;
    }

    public ScreenshotTestRule setViewModifications(BaseScreenshotTest.ViewModification viewModification) {
        this.viewModification = viewModification;
        return this;
    }

    private void afterTestStatement() {
        ScreenshotTest screenshotTest = new ScreenshotTest(layoutId);
        try {
            screenshotTest.setViewModifications(viewModification);
            screenshotTest.setEspressoActions(espressoActions);
            if (exactness != null) {
                screenshotTest.assertSame(exactness);
            } else {
                screenshotTest.assertSame();
            }
            int a = 0;
        } catch (Throwable throwable) {
            this.throwable = throwable;
        }
    }

    private class BaseInterceptorStatement extends Statement {

        private final Statement base;

        BaseInterceptorStatement(Statement base) {
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            base.evaluate();
            afterTestStatement();
        }
    }

    private class ScreenshotStatement extends Statement {

        private final Statement base;

        ScreenshotStatement(Statement base) {
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                base.evaluate();
            } catch (Throwable ignored) {
            } finally {
                if (throwable != null) {
                    throw throwable;
                }
            }
        }
    }

    private class ScreenshotTest extends BaseScreenshotTest<ScreenshotTest> {

        ScreenshotTest(@LayoutRes int layoutId) {
            super(layoutId);
        }

        @Override
        protected Pair<String, String> getTestNameComponents() {
            return new Pair<>(testSimpleClassName, testMethodNode);
        }

        @Override
        protected String getFullyQualifiedTestPath() {
            return testClass;
        }

        @Override
        protected Context getTestContext() {
            return InstrumentationRegistry.getContext();
        }

        @Override
        protected Activity getActivity() {
            return ScreenshotTestRule.this.getActivity();
        }

        @Override
        protected ScreenshotTest getThis() {
            return this;
        }
    }
}