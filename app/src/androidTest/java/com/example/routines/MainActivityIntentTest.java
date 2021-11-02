package com.example.routines;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.google.firebase.FirebaseApp;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {
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

    @Test
    public void welcomeActivityTest() {
        solo.assertCurrentActivity("Wrong activity", WelcomeActivity.class);
    }

    //@Test   // not included right now, waiting on TAS response. It will only add once because of the username conflicts
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

    @Test
    public void loginTest(){
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
    public void addHabitTest(){
        loginTest(); // login
        solo.sleep(500);
        // click the floating action button
        View floatingButtonView = solo.getCurrentActivity().findViewById(R.id.container); // get the frame layout
        View fab= floatingButtonView.findViewById(R.id.addHabitButton); // get the button inside the frame layout
        solo.clickOnView(fab);  // press the button
        // inside the fragment
        solo.sleep(500);
        solo.clickOnButton("CONFIRM DATE");
        solo.enterText((EditText) solo.getView(R.id.habitNameEditText), "TEST HABIT");
        solo.enterText((EditText) solo.getView(R.id.habitReasonEditText), "TEST REASON");
        solo.enterText((EditText) solo.getView(R.id.habitPrivacyEditText), "Private");
        // test all the date switch's
        solo.clickOnButton("Monday");
        solo.clickOnButton("Tuesday");
        solo.clickOnButton("Wednesday");
        solo.clickOnButton("Thursday");
        solo.clickOnButton("Friday");
        solo.clickOnButton("Saturday");
        solo.clickOnButton("Sunday");
        solo.clickOnButton("OK");
        solo.sleep(500);
    }

    @Test
    public void addHabitFragmentBackButton(){ // test canceling adding a habit
        loginTest();
        // click floating action button
        View floatingButtonView = solo.getCurrentActivity().findViewById(R.id.container); // get the frame layout
        View fab= floatingButtonView.findViewById(R.id.addHabitButton); // get the button inside the frame layout
        solo.clickOnView(fab);  // press the button
        solo.sleep(500);
        solo.clickOnButton("Cancel");
        solo.sleep(500);
    }

    @Test
    public void todaySwitchTest(){
        loginTest(); // login
        solo.sleep(500);
        // test the switch at the top of the screen
        solo.clickOnButton("Today");
        solo.clickOnButton("All Habits");
        solo.clickOnButton("Today");
        solo.clickOnButton("All Habits");
        solo.sleep(500);
    }

    @Test
    public void logoutTest(){
        loginTest(); // login
        solo.sleep(500);
        // get the profile button from the bottom menu
        View bottomBar= solo.getCurrentActivity().findViewById(R.id.bottom_navigation); // get the button inside the frame layout
        View profile = bottomBar.findViewById(R.id.profile);
        solo.clickOnView(profile);
        solo.sleep(500);
        // logout
        solo.assertCurrentActivity("Activity needs to be profile activity", ProfileActivity.class);
        solo.clickOnButton("LOG OUT");
        // after logging out we should be back at welcome activity
        solo.assertCurrentActivity("Activity needs to be welcome activity", WelcomeActivity.class);
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
