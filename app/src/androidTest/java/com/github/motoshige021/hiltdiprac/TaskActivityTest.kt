package com.github.motoshige021.hiltdiprac

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.motoshige021.hiltdiprac.data.TvProgram
import com.github.motoshige021.hiltdiprac.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import com.github.motoshige021.hiltdiprac.ui.detail.DetailFragmentArgs
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.testing.CustomTestApplication
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class TaskActivityTest {
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

    @Test
    fun detailDeleteTest() {
        repository.clearProgramList()
        repository.addProgramToList(TvProgram("title1", "desc1", true, "id-1"))
        repository.addProgramToList(TvProgram("title2", "desc2", false, "id-2"))

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withText("title1")).perform(click())
        onView(withId(R.id.titleTextView)).check(matches(withText("title1")))
        onView(withText(R.string.Detail_fragment_label)).check(matches(isDisplayed()))
        // move to FragmentTest
        //onView(withId(R.id.detailTextView)).check(matches(withText("desc1")))
        //onView(withId(R.id.complate_checkbox)).check(matches(isChecked()))

        onView(withId(R.id.menu_delete)).perform(click())
        onView(withText("title1")).check(doesNotExist())
        onView(withText("title2")).check(matches(isDisplayed()))
        onView(withText(R.string.main_fragment_label)).check(matches(isDisplayed()))
    }

    fun addTestData() {
        repository.clearProgramList()
        repository.addProgramToList(TvProgram("title1", "desc1", true, "id-1"))
        repository.addProgramToList(TvProgram("title2", "desc2", false, "id-2"))
        repository.addProgramToList(TvProgram("title3", "desc3", false, "id-3"))
        repository.addProgramToList(TvProgram("title4", "desc4", true, "id-4"))
        repository.addProgramToList(TvProgram("title5", "desc5", true, "id-5"))
    }

    @Test
    fun activeProgramTest() {
        addTestData()
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        for (i in 1..5) {
            var strTitle = "title" + i.toString()
            onView(withText(strTitle)).check(matches(isDisplayed()))
        }
        onView(withId(R.id.menu_filtering)).perform(click())
        onView(withText(R.string.menu_filter_active)).perform(click())
        Thread.sleep(500)
        onView(withText("title2")).check(matches(isDisplayed()))
        onView(withText("title3")).check(matches(isDisplayed()))
        onView(withText("title1")).check(doesNotExist())
        onView(withText("title4")).check(doesNotExist())
        onView(withText("title5")).check(doesNotExist())
    }

    @Test
    fun concluedProgramTest() {
        addTestData()
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        for (i in 1..5) {
            var strTitle = "title" + i.toString()
            onView(withText(strTitle)).check(matches(isDisplayed()))
        }
        onView(withId(R.id.menu_filtering)).perform(click())
        onView(withText(R.string.menu_filter_completed)).perform(click())
        Thread.sleep(500)
        onView(withText("title1")).check(matches(isDisplayed()))
        onView(withText("title4")).check(matches(isDisplayed()))
        onView(withText("title5")).check(matches(isDisplayed()))
        onView(withText("title2")).check(doesNotExist())
        onView(withText("title3")).check(doesNotExist())
    }

    @Test
    fun allProgramTest() {
        addTestData()
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.menu_filtering)).perform(click())
        onView(withText(R.string.menu_filter_completed)).perform(click())
        Thread.sleep(300)
        onView(withId(R.id.menu_filtering)).perform(click())
        onView(withText(R.string.menu_filter_all)).perform(click())
        Thread.sleep(500)
        for (i in 1..5) {
            var strTitle = "title" + i.toString()
            onView(withText(strTitle)).check(matches(isDisplayed()))
        }
    }
}