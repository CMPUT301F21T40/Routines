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
 * INTENT TESTING FOR US.02.05.01
 */
public class EditHabitEventTest {
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

    public void addHabitEvent(){
        solo.clickOnText("TEST HABIT");
        solo.clickOnButton("ADD EVENT");
        solo.getView(R.id.add_habit_event);
        solo.enterText((EditText) solo.getView(R.id.view_habit_name), "TEST HABIT EVENT");
        solo.enterText((EditText) solo.getView(R.id.view_habit_reason), "SOME KIND OF REASON");
        solo.clickOnButton("ADD");
    }

    public void viewHabitEvent(){
        solo.clickOnText("TEST HABIT");
        addHabitEvent();
        solo.clickOnButton("VIEW EVENTS");
        solo.clickOnText("TEST HABIT EVENT");
    }
    @Test
    public void editHabitEventTest(){
        login();
        addHabit();
        viewHabitEvent();
        solo.clickOnView(solo.getView(R.id.edit_event_button)); // click on the floating button
        solo.clearEditText((EditText) solo.getView(R.id.editText_event_name));
        solo.clearEditText((EditText) solo.getView(R.id.editText_event_comment));
        solo.enterText((EditText) solo.getView(R.id.editText_event_name), " EDITED TEST HABIT EVENT");
        solo.enterText((EditText) solo.getView(R.id.editText_event_comment), "EDITED SOME KIND OF REASON");
        solo.sleep(2000);
        solo.clickOnText("Confirm");
        solo.sleep(2000);
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


