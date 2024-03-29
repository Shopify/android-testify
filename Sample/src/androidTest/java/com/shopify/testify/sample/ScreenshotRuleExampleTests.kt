/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Shopify Inc.
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

package com.shopify.testify.sample

import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.shopify.testify.ScreenshotRule
import com.shopify.testify.TestifyFeatures
import com.shopify.testify.annotation.ScreenshotInstrumentation
import com.shopify.testify.annotation.TestifyLayout
import com.shopify.testify.extensions.boundingBox
import com.shopify.testify.internal.exception.ScreenshotIsDifferentException
import com.shopify.testify.sample.test.TestHarnessActivity
import com.shopify.testify.sample.test.TestHarnessActivity.Companion.EXTRA_TITLE
import com.shopify.testify.sample.test.clientDetailsView
import com.shopify.testify.sample.test.getViewState
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class ScreenshotRuleExampleTests {

    /**
     * [rule] is set up to use a "harness activity". [TestHarnessActivity] is an empty Activity that
     * only exists in the Sample app's androidTest configuration. It is used to load arbitrary
     * [View] instances for testing.
     * [R.id.harness_root] is the topmost/root view in the hierarchy. Testify will load views in
     * this root.
     */
    @get:Rule
    var rule = ScreenshotRule(
        activityClass = TestHarnessActivity::class.java,
        launchActivity = false,
        rootViewId = R.id.harness_root
    )

    /**
     * Demonstrates the default Testify configuration for a simple layout test.
     *
     * This test uses the [TestifyLayout] annotation to load [R.layout.view_client_details] into
     * the [TestHarnessActivity].
     */
    @TestifyLayout(R.layout.view_client_details)
    @ScreenshotInstrumentation
    @Test
    fun default() {
        rule.setViewModifications { harnessRoot ->
            rule.activity.getViewState(name = "default").let {
                harnessRoot.clientDetailsView.render(it)
                rule.activity.title = it.name
            }
        }.assertSame()
    }

    /**
     * Demonstrates how to add a [Bundle] of extras to the [Intent] used to create the Activity
     * under test.
     *
     * You can provide a lambda to populate the provided Bundle with any user-declared extra values.
     * This example shows how to set the title bar text using an Intent extra.
     */
    @TestifyLayout(R.layout.view_client_details)
    @ScreenshotInstrumentation
    @Test
    fun addIntentExtras() {
        rule
            .addIntentExtras {
                it.putString(EXTRA_TITLE, "addIntentExtras")
            }
            .assertSame()
    }


    /**
     * Demonstrates the alternative of loading layouts by resource name.
     *
     * This approach is useful for library projects where the R.id values are not constant.
     */
    @TestifyLayout(layoutResName = "com.shopify.testify.sample:layout/view_client_details")
    @ScreenshotInstrumentation
    @Test
    fun usingLayoutResName() {
        rule.setViewModifications { harnessRoot ->
            rule.activity.getViewState(name = "usingLayoutResName").let {
                harnessRoot.clientDetailsView.render(it)
                rule.activity.title = it.name
            }
        }.assertSame()
    }

    /**
     * Demonstrates how to load a layout file programmatically using setTargetLayoutId
     */
    @ScreenshotInstrumentation
    @Test
    fun setTargetLayoutId() {
        rule
            .setTargetLayoutId(R.layout.view_client_details)
            .setViewModifications { harnessRoot ->
                rule.activity.getViewState(name = "setTargetLayoutId").let {
                    harnessRoot.clientDetailsView.render(it)
                    rule.activity.title = it.name
                }
            }.assertSame()
    }

    /**
     * Demonstrates Testify's ability to interoperate with Espresso actions.
     *
     * [ScreenshotRule.setEspressoActions] accepts a lambda of type [EspressoActions] in which you
     * may define any number of Espresso actions. These actions are executed after the activity is
     * fully inflated and any view modifications have been applied. Testify will synchronize with
     * the Espresso event loop and ensure that all Espresso actions are complete before capturing
     * a screenshot.
     *
     * Note that it's not generally recommended to use complex Espresso actions with your screenshot
     * tests. Espresso test are an order of magnitude slower to run and are more susceptible to
     * flakiness.
     */
    @TestifyLayout(R.layout.view_edit_text)
    @ScreenshotInstrumentation
    @Test
    fun setEspressoActions() {
        rule
            .setEspressoActions {
                onView(withId(R.id.edit_text)).perform(typeText("Testify"))
            }
            .assertSame()
    }

    /**
     * Demonstrates how to enable experimental features programmatically.
     *
     * In this example, CanvasCapture is enabled. If you compare to the [default] test case,
     * you will notice minor capture differences owning to the different capture methods used.
     */
    @TestifyLayout(R.layout.view_client_details)
    @ScreenshotInstrumentation
    @Test
    fun withExperimentalFeatureEnabled() {
        rule
            .setViewModifications { harnessRoot ->
                rule.activity.getViewState(name = "withExperimentalFeatureEnabled").let {
                    harnessRoot.clientDetailsView.render(it)
                    rule.activity.title = it.name
                }
            }
            .withExperimentalFeatureEnabled(TestifyFeatures.CanvasCapture)
            .assertSame()
    }

    /**
     * Demonstrates how to force the use of the software renderer.
     *
     * In some instances it may be desirable to use the software renderer, not Android's default
     * hardware renderer. Differences in GPU hardware from device to device (and emulators running
     * on different architectures) may cause flakiness in rendering.
     *
     * [@see https://developer.android.com/guide/topics/graphics/hardware-accel.html]
     */
    @TestifyLayout(R.layout.view_client_details)
    @ScreenshotInstrumentation
    @Test
    fun setUseSoftwareRenderer() {
        rule
            .setViewModifications { harnessRoot ->
                rule.activity.getViewState(name = "withExperimentalFeatureEnabled").let {
                    harnessRoot.clientDetailsView.render(it)
                    rule.activity.title = it.name
                }
            }
            .setUseSoftwareRenderer(true)
            .withExperimentalFeatureEnabled(TestifyFeatures.CanvasCapture)
            .assertSame()
    }

    /**
     * Demonstrates the fuzzy matching ability of testify
     *
     * By providing a value less than 1 to [setExactness], a test will be more tolerant to color
     * differences. The fuzzy matching algorithm maps the captured image into the HSV color space
     * and compares the Hue, Saturation and Lightness components of each pixel. If they are within
     * the provided tolerance, the images are considered to be the same.
     *
     * Note that the fuzzy matching is approximately 10x slower than the default matching.
     * Use sparingly.
     */
    @TestifyLayout(R.layout.view_client_details)
    @ScreenshotInstrumentation
    @Test
    fun setExactness() {
        rule
            .setScreenshotViewProvider {
                it.findViewById(R.id.info_card)
            }
            .setExactness(0.95f)
            .setViewModifications {
                val r = Integer.toHexString(Random.nextInt(0, 25) + 230).padStart(2, '0')
                it.findViewById<View>(R.id.info_card).setBackgroundColor(Color.parseColor("#${r}0000"))
            }
            .assertSame()
    }


    @TestifyLayout(R.layout.view_client_details)
    @ScreenshotInstrumentation
    @Test(expected = ScreenshotIsDifferentException::class)
    fun generateDiffs() {
        TestifyFeatures.GenerateDiffs.setEnabled(true)
        rule.setViewModifications { harnessRoot ->
            rule.activity.getViewState(name = "A Name").let {
                val state = it.copy(
                    name = "A Different Name",
                    avatar = R.drawable.avatar2,
                    heading = harnessRoot.context.getString(R.string.client_since, "2019")
                )
                harnessRoot.clientDetailsView.render(state)
                rule.activity.title = state.name
            }
        }.assertSame()
    }

    /**
     * Demonstrates Testify's ability to take a screenshot of a single view.
     *
     * Using [ScreenshotRule.setScreenshotViewProvider], you may return a [View] reference
     * which will be used by Testify to narrow the bitmap to only that View.
     * You can use this method to take a screenshot of a partial Activity or a single View.
     */
    @TestifyLayout(R.layout.view_client_details)
    @ScreenshotInstrumentation
    @Test
    fun setScreenshotViewProvider() {
        rule
            .setScreenshotViewProvider {
                it.findViewById(R.id.info_card)
            }
            .assertSame()
    }

    /**
     * Demonstrates how to change the orientation of your Activity to landscape.
     *
     * Note how the screenshot device key baseline has a longer width than height.
     * e.g. 22-800x480@240dp-en_US
     */
    @TestifyLayout(R.layout.view_client_details)
    @ScreenshotInstrumentation
    @Test
    fun setOrientation() {
        rule
            .setOrientation(requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE)
            .assertSame()
    }

    /**
     * Demonstrates how to exclude a rectangular region from the comparison.
     * This can be useful if some content of your View is dynamic or not repeatable under test.
     * Note that this comparison mechanism is slower than the default.
     */
    @TestifyLayout(R.layout.view_client_details)
    @ScreenshotInstrumentation
    @Test
    fun exclusions() {
        TestifyFeatures.PixelCopyCapture.setEnabled(true)
        TestifyFeatures.GenerateDiffs.setEnabled(true)
        rule
            .setExactness(0.9f)
            .setViewModifications {
                val r = Integer.toHexString(Random.nextInt(0, 255)).padStart(2, '0')
                val g = Integer.toHexString(Random.nextInt(0, 255)).padStart(2, '0')
                val b = Integer.toHexString(Random.nextInt(0, 255)).padStart(2, '0')
                it.findViewById<View>(R.id.info_card).setBackgroundColor(Color.parseColor("#$r$g$b"))
            }
            .defineExclusionRects { rootView, exclusionRects ->
                val card = rootView.findViewById<View>(R.id.info_card)
                exclusionRects.add(card.boundingBox)
            }
            .assertSame()
    }
}
