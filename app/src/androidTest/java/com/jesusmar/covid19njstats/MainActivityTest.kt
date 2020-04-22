package com.jesusmar.covid19njstats

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.jesusmar.covid19njstats.activities.EssexHistory
import com.jesusmar.covid19njstats.activities.MainActivity
import com.jesusmar.covid19njstats.activities.StateHistory
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var  mainActivity: ActivityTestRule<MainActivity> =  ActivityTestRule(
        MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }
    @After
    fun cleanUp() {
        Intents.release()
    }

    @Test
    fun checkUILabel() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_essex)).check(
            ViewAssertions.matches(ViewMatchers.withText("Essex")))
    }

    @Test
    fun callsEssexActivity(){
        Espresso.onView(ViewMatchers.withId(R.id.btn_essex)).perform(ViewActions.click())
        intended(hasComponent(EssexHistory::class.java.getName()))
    }

    @Test
    fun callsStateActivity(){
        Espresso.onView(ViewMatchers.withId(R.id.btn_state)).perform(ViewActions.click())
        intended(hasComponent(StateHistory::class.java.getName()))
    }

}

