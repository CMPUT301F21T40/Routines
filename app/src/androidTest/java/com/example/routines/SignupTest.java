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
 * US
 * Signing up for the app
 * ISSUES: NEED TO DELETE THE USER FROM FIREBASE FOR RE-USABILITY
 */
public class SignupTest {
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
        solo.sleep(500);
        solo.assertCurrentActivity("Activity needs to be loginActivity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText_email), "a@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.editText_password_login), "123456");
        solo.sleep(500);
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Activity needs to be homeActivity", HomeActivity.class);
        solo.sleep(500);
    }

    @Test
    public void signUpTest(){
        solo.assertCurrentActivity("Wrong activity, needs to be welcome activity", WelcomeActivity.class);
        solo.sleep(500);
        solo.clickOnButton("SIGNUP");
        solo.sleep(500);
        solo.assertCurrentActivity("Wrong activity, needs to be signup activity ", SignupActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText_userName_signup), "testUserName");
        solo.enterText((EditText) solo.getView(R.id.editText_email_signup), "testEmail@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.editText_password_signup), "testPassword123456");
        solo.enterText((EditText) solo.getView(R.id.editText_password_confirm), "testPassword123456");
        solo.sleep(500);
        solo.clickOnButton("SIGNUP");
        solo.assertCurrentActivity("Activity needs to be homeActivity", HomeActivity.class);
        solo.sleep(500);
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
