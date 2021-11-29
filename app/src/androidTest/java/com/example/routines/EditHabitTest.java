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
 * INTENT TESTING FOR US 01.04.01
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
        solo.enterText((EditText) solo.getView(R.id.editText_email), "testUserNew@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.editText_password_login), "123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Activity needs to be homeActivity", HomeActivity.class);
    }

    public void deleteHabit(){
        solo.assertCurrentActivity("Needs to be ViewHabitActivity", ViewHabitActivity.class);
        // click the floating action button
        solo.clickOnView(solo.getView(R.id.delete_habit_button)); // click on the floating button
        solo.sleep(500);
        solo.clickOnButton("Confirm"); // click on the confirm
    }

    public void addHabit(){
        // click the floating action button
        View floatingButtonView = solo.getCurrentActivity().findViewById(R.id.container); // get the frame layout
        View fab= floatingButtonView.findViewById(R.id.addHabitButton); // get the button inside the frame layout
        solo.clickOnView(fab);  // press the button
        // inside the fragment
        solo.clickOnButton("CONFIRM DATE");
        solo.enterText((EditText) solo.getView(R.id.habitNameEditText), "Test Habit");
        solo.enterText((EditText) solo.getView(R.id.habitReasonEditText), "Test Reason");
        // test all the date switch's
        solo.clickOnButton("Private Habit");
        solo.clickOnButton("Monday");
        solo.clickOnButton("Tuesday");
        solo.clickOnButton("Wednesday");
        solo.clickOnButton("Thursday");
        solo.clickOnButton("Friday");
        solo.clickOnButton("Saturday");
        solo.clickOnButton("Sunday");
        solo.clickOnButton("OK");
    }

    @Test
    public void editHabit(){
        login();
        // have to add a habit every time to make sure there is one to edit, no need for this till deleteHabit is implemented
        addHabit();
        solo.clickOnText("Test Habit");
        //solo.clickInList(0); cannot use anymore since there is 2 listviews in the activity, there is not abs reference
        solo.assertCurrentActivity("Needs to be ViewHabitActivity", ViewHabitActivity.class);
        solo.clickOnView(solo.getView(R.id.edit_habit_button)); // click on the floating button
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
        deleteHabit();
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
