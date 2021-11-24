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
 * Unit testing for US 02.04.01
 */
public class ViewHabitEvent {
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
    public void deleteHabit(){
        solo.clickOnText("TEST HABIT");
        //solo.clickInList(0); cannot use anymore since there is 2 listviews in the activity, there is not abs reference
        solo.assertCurrentActivity("Needs to be ViewHabitActivity", ViewHabitActivity.class);
        // click the floating action button
        solo.clickOnView(solo.getView(R.id.delete_habit_button)); // click on the floating button
        solo.sleep(500);
        solo.clickOnButton("Confirm"); // click on the confirm
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
    @Test
    public void viewHabitEventTest(){
        login();
        addHabit();
        solo.clickOnText("TEST HABIT");
        addHabitEvent();
        solo.clickOnButton("VIEW EVENTS");
        solo.clickOnText("TEST HABIT EVENT");
        solo.sleep(3000);
        solo.goBack();
        solo.goBack();
        deleteHabit(); // just to delete it so its not cluttered 
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

