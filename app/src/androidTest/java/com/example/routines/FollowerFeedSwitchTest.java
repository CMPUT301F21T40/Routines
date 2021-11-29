package com.example.routines;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * INTENT TESTING FOR EXTRA METHOD FOLLOWER FEED
 * @author lukas waschuk
 */
public class FollowerFeedSwitchTest {
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

    public void login(){
        solo.assertCurrentActivity("Wrong activity, needs to be Welcome", WelcomeActivity.class);
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Activity needs to be loginActivity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText_email), "testUserNew@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.editText_password_login), "123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Activity needs to be homeActivity", HomeActivity.class);
    }

    @Test
    public void todaySwitchTest(){
        login(); // login
        solo.sleep(500);
        // test the switch at the top of the screen
        solo.clickOnButton("Following");   // this will have to change when we change the UI
        solo.sleep(1000);
        solo.clickOnButton("All Habits");
        solo.clickOnButton("Following");
        solo.clickOnButton("All Habits");
        solo.sleep(500);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
