package com.example.myfirstapp;


import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import android.app.Activity;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.JMock1Matchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.example.myfirstapp.MainActivity;
import com.example.myfirstapp.controller.EventAdapter;
import com.example.myfirstapp.model.Event;

import java.util.Map;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {
    public static final String TEXT = "second";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void fabIsDisplayed() {
        onView(withId(R.id.add_event_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.add_event_btn)).perform(click());
        onView(withId(R.id.btn_save_event)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Create an event")));
    }

    @Test
    public void resViewIsDisplayed() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void clickingOnFabOpensNewActivity() {
        onView(withId(R.id.add_event_btn)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Create an event")));
    }
                     
    @Test
    public void checkIfDataInResViewIsEventAdapter() {
        onData(is(instanceOf(EventAdapter.class)));
    }

    @Test
    public void checkDataInAdapterIsOfTypeEvent() {
        onData(allOf(is(instanceOf(Event.class))));
    }

    /**
     * this test should fail
     */
    @Test
    public void ShouldFailCheckTextInListItemIsVisible() {
        onView(withId(R.id.event_title )).check(matches(isDisplayed()));
    }


}
