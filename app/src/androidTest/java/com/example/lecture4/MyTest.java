package com.example.lecture4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MyTest {

    @Rule
    ActivityScenarioRule<com.example.lecture4.MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(com.example.lecture4.MainActivity.class);

    @Test
    public void test1() {
        assert(true);
    }
}
