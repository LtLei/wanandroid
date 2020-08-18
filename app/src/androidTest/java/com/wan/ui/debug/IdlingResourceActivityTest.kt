package com.wan.ui.debug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.ViewModelUtil
import com.wan.*
import org.junit.After
import org.junit.Test
import org.mockito.Mockito.*

class IdlingResourceActivityTest {
    private val viewModel = mock(IdlingResourceViewModel::class.java)

    @Test
    fun fakeTest() {
        val fakeData = MutableLiveData<Int>()
        doReturn(fakeData).`when`(viewModel).fakeData
        `when`(viewModel.fetchFakeData()).then {
            fakeData.postValue(R.string.text_login_success)
        }
        IdlingResourceInjection.viewModel = viewModel

        val scenario = ActivityScenario.launch(IdlingResourceActivity::class.java)
        onView(withId(R.id.et_name)).perform(typeText("wanandroid"), closeSoftKeyboard())
        onView(withId(R.id.et_pwd)).perform(typeText("123456"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(R.id.tv_login_success)).check(matches(ViewMatchers.withText(R.string.text_login_success)))
    }

//    private lateinit var idlingResource: IdlingResource

//    @Test
//    fun testEspresso() {
//        val scenario = ActivityScenario.launch(IdlingResourceActivity::class.java)
//        scenario.onActivity {
//            idlingResource = it.getIdlingResource()
//            IdlingRegistry.getInstance().register(idlingResource)
//        }
//
//        onView(withId(R.id.et_name)).perform(typeText("wanandroid"), closeSoftKeyboard())
//        onView(withId(R.id.et_pwd)).perform(typeText("123456"), closeSoftKeyboard())
//        onView(withId(R.id.btn_login)).perform(click())
//
//        onView(withId(R.id.tv_login_success)).check(matches(ViewMatchers.withText(R.string.text_login_success)))
//    }
//
//    @After
//    fun release() {
//        IdlingRegistry.getInstance().unregister(idlingResource)
//    }
}
