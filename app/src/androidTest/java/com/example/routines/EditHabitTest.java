package com.example.routines;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * INTENT TESTING FOR US
 */
public class EditHabitTest {
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

    public void addHabit(){
        // click the floating action button
        View floatingButtonView = solo.getCurrentActivity().findViewById(R.id.container); // get the frame layout
        View fab= floatingButtonView.findViewById(R.id.addHabitButton); // get the button inside the frame layout
        solo.clickOnView(fab);  // press the button
        // inside the fragment
        solo.clickOnButton("CONFIRM DATE");
        solo.enterText((EditText) solo.getView(R.id.habitNameEditText), "TEST HABIT");
        solo.enterText((EditText) solo.getView(R.id.habitReasonEditText), "TEST REASON");
        // test all the date switch's
        solo.clickOnButton("Private Habit");
        solo.clickOnButton("Monday");
        solo.clickOnButton("OK");
        solo.sleep(1000);
    }

    @Test
    public void changeHabitTest(){
        login();
        // have to add a habit every time to make sure there is one to edit, no need for this till deleteHabit is implemented
        //addHabit();
        solo.clickInList(0);
        solo.assertCurrentActivity("Needs to be ViewHabitActivity", ViewHabitActivity.class);
        // click the floating action button
        View floatingButtonView = solo.getCurrentActivity().findViewById(R.id.event_list_toolbar);
        View fab= floatingButtonView.findViewById(R.id.edit_habit_button); // get the button inside the frame layout
        solo.clickOnView(fab);
        solo.sleep(1000);
        // inside the fragment
        solo.clickOnButton("CONFIRM DATE");
        solo.clearEditText((EditText) solo.getView(R.id.habitNameEditText));
        solo.enterText((EditText) solo.getView(R.id.habitNameEditText), "EDITED TEST HABIT");
        solo.clearEditText((EditText) solo.getView(R.id.habitReasonEditText));
        solo.enterText((EditText) solo.getView(R.id.habitReasonEditText), "EDITED TEST REASON");
        // test all the date switch's
        solo.clickOnButton("Private Habit");
        solo.clickOnButton("Monday");
        solo.clickOnButton("Thursday");
        solo.clickOnButton("Friday");
        solo.clickOnButton("Saturday");
        solo.clickOnButton("Sunday");
        solo.clickOnButton("OK");
        solo.sleep(500);
    }
}
