package com.shopify.testify.sample

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.shopify.testify.sample.clients.MockClientData
import com.shopify.testify.sample.clients.details.ClientDetailsActivity
import com.shopify.testify.sample.clients.index.ClientListActivity
import org.hamcrest.CoreMatchers.endsWith
import org.junit.Test

/**
 * Intention behind this setup is to share typical use cases utilizing ActivityScenario:
 * https://developer.android.com/reference/androidx/test/core/app/ActivityScenario
 */
class ActivityScenarioExampleTest {
    @Test
    fun activityScenarioNoIntentUseCase() {
        launchActivity<ClientListActivity>().use {
            onView(ViewMatchers.withClassName(endsWith("RecyclerView")))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(THE_CLIENT_POSITION, scrollTo()))
                .check(matches(hasDescendant(withText(THE_CLIENT.name))))
        }
    }

    @Test
    fun activityScenarioIntentUseCase() {
        launch<ClientDetailsActivity>(
            ClientDetailsActivity.createClientDetailsActivityIntent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                THE_CLIENT
            )
        ).use {
            onView(withId(R.id.address)).check(matches(withText(R.string.mock_address)))
        }
    }

    companion object {
        const val THE_CLIENT_POSITION = 13
        val THE_CLIENT = MockClientData.CLIENTS[THE_CLIENT_POSITION]
    }
}
