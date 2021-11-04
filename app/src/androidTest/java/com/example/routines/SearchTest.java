package com.example.routines;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * INTENT TESTING FOR US
 */
public class SearchTest {
    private Solo solo;
    @Rule // start testing on welcome page
    public ActivityTestRule<WelcomeActivity> rule =
            new ActivityTestRule<>(WelcomeActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instances
     * @throws Exception
     */
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
        solo.enterText((EditText) solo.getView(R.id.editText_email), "testEmail@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.editText_password_login), "testPassword123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Activity needs to be homeActivity", HomeActivity.class);
    }

    @Test
    public void searchTest(){
        login(); // login
        solo.sleep(500);
        // get the profile button from the bottom menu
        View bottomBar= solo.getCurrentActivity().findViewById(R.id.bottom_navigation); // get the button inside the frame layout
        View search = bottomBar.findViewById(R.id.search);
        solo.clickOnView(search);
        solo.sleep(500);
        solo.assertCurrentActivity("Activity needs to be search activity", SearchActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_text), "testUserName");
        solo.clickOnButton("SEARCH");
        solo.sleep(1000);
        solo.clickInList(0);
        solo.assertCurrentActivity("Activity needs to be SearchProfileActivity", SearchProfileActivity.class);
        solo.sleep(1000);
    }
    /**
     * Closes the activity after every test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
