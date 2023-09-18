package com.github.motoshige021.hiltdiprac

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.motoshige021.hiltdiprac.data.TvProgram
import com.github.motoshige021.hiltdiprac.ui.detail.DetailFragmentArgs
import com.github.motoshige021.hiltdiprac.util.EspressoIdlingResource
import com.github.motoshige021.hiltdiprac.ui.detail.DetailFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class DetailFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository :TaskRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.coutingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.coutingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    fun addTestData() {
        repository.clearProgramList()
        repository.addProgramToList(TvProgram("title1", "desc1", true, "id-1"))
        repository.addProgramToList(TvProgram("title2", "desc2", false, "id-2"))
    }

    @Test
    fun activeProgramTest() {
        addTestData()
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withText("title2")).perform(click())
        onView(withText("title2")).check(matches(isDisplayed()))
        onView(withText("desc2")).check(matches(isDisplayed()))
        onView(withId(R.id.complate_checkbox)).check(matches(isNotChecked()))
    }

    @Test
    fun cocludProgramTest() {
        addTestData()
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withText("title1")).perform(click())
        onView(withText("title1")).check(matches(isDisplayed()))
        onView(withText("desc1")).check(matches(isDisplayed()))
        onView(withId(R.id.complate_checkbox)).check(matches(isChecked()))
    }
}