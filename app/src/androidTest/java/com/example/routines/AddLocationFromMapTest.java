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
 * unit testing for us 06 01 02
 * @author lukas waschuk
 */
public class AddLocationFromMapTest {
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
        solo.sleep(1000);
    }

    public void deleteHabit(){
        solo.clickOnText("Test Habit");
        //solo.clickInList(0); cannot use anymore since there is 2 listviews in the activity, there is not abs reference
        solo.assertCurrentActivity("Needs to be ViewHabitActivity", ViewHabitActivity.class);
        // click the floating action button
        solo.clickOnView(solo.getView(R.id.delete_habit_button)); // click on the floating button
        solo.sleep(500);
        solo.clickOnButton("Confirm"); // click on the confirm
        solo.sleep(1000);
    }

    @Test
    public void addLocationFromMapTest(){
        login();
        addHabit();
        solo.clickOnText("Test Habit");
        solo.sleep(1000);
        solo.clickOnButton("ADD EVENT");
        solo.sleep(1000);
        solo.enterText((EditText) solo.getView(R.id.view_habit_name), "Habit Event");
        solo.enterText((EditText) solo.getView(R.id.view_habit_reason), "SOME KIND OF REASON");
        solo.clickOnButton("GET LOCATION FROM MAP");
        solo.sleep(3000);
        solo.enterText((EditText) solo.getView(R.id.address_search_editText), "116 St & 85 Ave, Edmonton, AB T6G 2R3");
        solo.sleep(1000);
        solo.typeText(0, "116 St & 85 Ave, Edmonton, AB T6G 2R3");
        solo.clickOnText("116 St & 85 Ave, Edmonton, AB T6G 2R3");
        solo.sleep(1000);
        solo.clickOnButton("CONFIRM");
        solo.sleep(1000);
        solo.clickOnButton("ADD");
        solo.sleep(1000);
        solo.clickOnButton("VIEW EVENTS");
        solo.sleep(3000);
        solo.goBack();
        deleteHabit();
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
