package com.example.routines;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * INTENT TESTING for the welcome screen
 * @author lukas waschuk
 */
public class WelcomeActivityTest {
    private Solo solo;

    @Rule // start testing on welcome page
    public ActivityTestRule<WelcomeActivity> rule =
            new ActivityTestRule<>(WelcomeActivity.class, true, true);

    @Before
    public void setup() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void welcomeActivityTest() {
        // testing when you start the app it is opens to the welcome page like intended
        solo.assertCurrentActivity("Wrong activity", WelcomeActivity.class);
        solo.sleep(1000);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
