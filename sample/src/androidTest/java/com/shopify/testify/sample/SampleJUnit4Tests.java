package com.shopify.testify.sample;

import android.support.test.runner.AndroidJUnit4;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.shopify.testify.ScreenshotTestRule;
import com.shopify.testify.annotation.BitmapComparisonExactness;
import com.shopify.testify.annotation.ScreenshotInstrumentation;
import com.shopify.testify.annotation.TestifyLayout;
import com.shopify.testify.interfaces.EspressoActions;
import com.shopify.testify.interfaces.ViewModification;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SampleJUnit4Tests {

    @Rule
    public final ScreenshotTestRule<TestHarnessActivity> screenshotTestRule = new ScreenshotTestRule<>(TestHarnessActivity.class);

    @Test
    @TestifyLayout(layoutId = R.layout.test_sample)
    @ScreenshotInstrumentation
    public void ruleUsingAnnotation() throws Exception {
    }

    @Test
    @ScreenshotInstrumentation
    public void ruleUsingSetter() throws Exception {
        screenshotTestRule.setLayoutId(R.layout.activity_main);
    }

    @Test
    @TestifyLayout(layoutId = R.layout.test_sample)
    @ScreenshotInstrumentation
    public void usingEspresso() throws Exception {
        screenshotTestRule.setEspressoActions(new EspressoActions() {
            @Override
            public void performEspressoActions() {
                onView(withId(R.id.checkBox)).perform(click());
            }
        });
    }

    @Test
    @TestifyLayout(layoutId = R.layout.test_sample)
    @ScreenshotInstrumentation
    public void withViewModifications() throws Exception {
        screenshotTestRule.setViewModifications(new ViewModification() {
            @Override
            public void modifyView(ViewGroup rootView) {
                ((RadioButton) rootView.findViewById(R.id.radioButton)).setChecked(true);
            }
        });
    }

    /**
     * This test randomly assigns a grayscale color with a 5% variance to the specified layout.
     * With our traditional image comparison, this test will fail every time.
     * Using the BitmapComparisonExactness, we specify that we require only a 95% match
     */
    @Test
    @TestifyLayout(layoutId = R.layout.activity_color)
    @BitmapComparisonExactness(exactness = 0.95f)
    @ScreenshotInstrumentation
    public void withFuzzyMatching() throws Exception {
        screenshotTestRule.setViewModifications(new ViewModification() {
            @Override
            public void modifyView(ViewGroup rootView) {

                int shade = (int) (0xff * (0.95f + (0.05f * new Random().nextFloat())));
                int color = 0xff000000 | (shade << 16) | (shade << 8) | (shade);

                rootView.setBackgroundColor(color);
            }
        });
    }

    @Test
    @TestifyLayout(layoutId = R.layout.content_editable)
    @ScreenshotInstrumentation
    public void noBlinkingCursor() {
    }

    @Test
    @TestifyLayout(layoutId = R.layout.content_editable)
    @ScreenshotInstrumentation
    public void allowBlinkingCursor() {
        screenshotTestRule.getTestModifications().setHideCursor(false);
    }
}
