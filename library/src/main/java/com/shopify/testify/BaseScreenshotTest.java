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
import android.graphics.Bitmap;
import android.os.Debug;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.test.espresso.Espresso;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.shopify.testify.exception.ScreenshotBaselineNotDefinedException;
import com.shopify.testify.exception.ScreenshotIsDifferentException;
import com.shopify.testify.modification.HidePasswordViewModification;
import com.shopify.testify.modification.HideScrollbarsViewModification;
import com.shopify.testify.modification.HideTextSuggestionsViewModification;
import com.shopify.testify.modification.SoftwareRenderViewModification;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@SuppressWarnings("unused")
abstract class BaseScreenshotTest<T> {

    static final int NO_ID = -1;
    private static final long INFLATE_TIMEOUT_SECONDS = 5;
    private static final String LOG_TAG = BaseScreenshotTest.class.getName();
    @Nullable
    private ViewModification viewModification;
    @Nullable
    private EspressoActions espressoActions;
    @Nullable
    private ViewProvider screenshotViewProvider;
    @LayoutRes
    private int layoutId;
    private Locale defaultLocale = null;
    private boolean hideSoftKeyboard = true;
    private HidePasswordViewModification hidePasswordViewModification = new HidePasswordViewModification();
    private HideScrollbarsViewModification hideScrollbarsViewModification = new HideScrollbarsViewModification();
    private HideTextSuggestionsViewModification hideTextSuggestionsViewModification = new HideTextSuggestionsViewModification();
    private SoftwareRenderViewModification softwareRenderViewModification = new SoftwareRenderViewModification();
    private BitmapCompare bitmapCompare = null;

    BaseScreenshotTest(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    protected abstract String getTestName();

    protected abstract Context getTestContext();

    protected abstract Activity getActivity();

    protected abstract T getThis();

    @SuppressWarnings("WeakerAccess")
    public T setViewModifications(ViewModification viewModification) {
        this.viewModification = viewModification;
        return getThis();
    }

    @SuppressWarnings("WeakerAccess")
    public T setScreenshotViewProvider(ViewProvider viewProvider) {
        this.screenshotViewProvider = viewProvider;
        return getThis();
    }

    @SuppressWarnings("WeakerAccess")
    public T setEspressoActions(EspressoActions espressoActions) {
        this.espressoActions = espressoActions;
        return getThis();
    }

    public T setHideSoftKeyboard(boolean hideSoftKeyboard) {
        this.hideSoftKeyboard = hideSoftKeyboard;
        return getThis();
    }

    public T setHideScrollbars(boolean hideScrollbars) {
        hideScrollbarsViewModification.setEnabled(hideScrollbars);
        return getThis();
    }

    public T setHidePasswords(boolean hidePasswords) {
        hidePasswordViewModification.setEnabled(hidePasswords);
        return getThis();
    }

    public T setHideTextSuggestions(boolean hideTextSuggestions) {
        hideTextSuggestionsViewModification.setEnabled(hideTextSuggestions);
        return getThis();
    }

    public T setUseSoftwareRenderer(boolean useSoftwareRenderer) {
        softwareRenderViewModification.setEnabled(useSoftwareRenderer);
        return getThis();
    }

    /**
     * Sets the default locale for this test case.
     * {@link #assertSame()} will reset the locale on completion.
     *
     * @param newLocale the new default locale
     * @return ScreenshotTest instance builder
     */
    public T setLocale(Locale newLocale) {
        defaultLocale = Locale.getDefault();
        LocaleHelper.setTestLocale(newLocale);
        return getThis();
    }

    @IdRes
    protected int getRootViewId() {
        return android.R.id.content;
    }

    private ViewGroup getRootView(Activity activity) {
        return (ViewGroup) activity.findViewById(getRootViewId());
    }

    private void initializeView(final Activity activity) throws Exception {
        final ViewGroup parentView = getRootView(activity);
        final CountDownLatch latch = new CountDownLatch(1);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (layoutId != NO_ID) {
                    activity.getLayoutInflater().inflate(layoutId, parentView, true);
                }
                if (viewModification != null) {
                    viewModification.modifyView(parentView);
                }

                hideScrollbarsViewModification.modify(parentView);
                hideTextSuggestionsViewModification.modify(parentView);
                hidePasswordViewModification.modify(parentView);
                softwareRenderViewModification.modify(parentView);

                latch.countDown();
            }
        });
        if (Debug.isDebuggerConnected()) {
            latch.await();
        } else {
            assertTrue(latch.await(INFLATE_TIMEOUT_SECONDS, TimeUnit.SECONDS));
        }
    }

    /**
     * @param exactness 0.0: matches against anything(no differences)
     *                  1.0: exact matches only
     * @throws Exception same as assertSame()
     */
    public void assertSame(@FloatRange(from = 0.0, to = 1.0) float exactness) throws Exception {
        bitmapCompare = new FuzzyCompare(exactness);
        assertSame();
    }

    public void assertSame() throws Exception {
        try {
            final Activity activity = getActivity();
            initializeView(activity);

            if (espressoActions != null) {
                espressoActions.performEspressoActions();
            }

            if (hideSoftKeyboard) {
                Espresso.closeSoftKeyboard();
            }

            final String testName = getTestName();
            final ScreenshotUtility screenshotUtility = new ScreenshotUtility();
            screenshotUtility.setLocale(defaultLocale != null ? Locale.getDefault() : null);

            View screenshotView = null;
            if (screenshotViewProvider != null) {
                screenshotView = screenshotViewProvider.getView(getRootView(activity));
            }
            Bitmap currentBitmap = screenshotUtility.createBitmapFromActivity(activity, testName, screenshotView);
            assertNotNull("Failed to capture bitmap from activity", currentBitmap);

            Bitmap baselineBitmap = screenshotUtility.loadBaselineBitmapForComparison(getTestContext(), testName);
            if (baselineBitmap == null) {
                throw new ScreenshotBaselineNotDefinedException(testName);
            }

            if (bitmapCompare == null) {
                bitmapCompare = screenshotUtility;
            }

            if (bitmapCompare.compareBitmaps(baselineBitmap, currentBitmap)) {
                assertTrue("Could not delete cached bitmap " + testName, screenshotUtility.deleteBitmap(activity, testName));
            } else {
                throw new ScreenshotIsDifferentException();
            }
        } catch (ScreenshotIsDifferentException | ScreenshotBaselineNotDefinedException exception) {
            if (hasCloudScreenshotter()) {
                Log.d(LOG_TAG, "Invoking Firebase ScreenShotter");
                ScreenShotter.takeScreenshot(getTestName(), getActivity());
            }
            throw exception;
        } finally {
            if (defaultLocale != null) {
                LocaleHelper.setTestLocale(defaultLocale);
                defaultLocale = null;
            }
        }
    }

    private boolean hasCloudScreenshotter() {
        try {
            Class.forName("com.google.android.libraries.cloudtesting.screenshots.ScreenShotter");
        } catch (ClassNotFoundException ignored) {
            return false;
        }
        return true;
    }

    // TODO: Move to top-level to simplify import
    @SuppressWarnings("WeakerAccess")
    public interface ViewModification {

        void modifyView(ViewGroup rootView);
    }

    @SuppressWarnings("WeakerAccess")
    public interface EspressoActions {

        void performEspressoActions();
    }

    @SuppressWarnings("WeakerAccess")
    public interface ViewProvider {
        View getView(ViewGroup rootView);
    }
}
