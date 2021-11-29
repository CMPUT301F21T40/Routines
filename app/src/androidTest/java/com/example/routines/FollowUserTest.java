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
import java.util.Random;

/**
 * Intent testing for us 05 01 01 << Request to follow a user >>
 *     @author lukas waschuk
 *
 */
public class FollowUserTest {
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

    public String signUp(){
        String username = generateUSN();
        String email = concatEmail(username);
        solo.assertCurrentActivity("Wrong activity, needs to be welcome activity", WelcomeActivity.class);
        solo.sleep(500);
        solo.clickOnButton("SIGNUP");
        solo.sleep(500);
        solo.assertCurrentActivity("Wrong activity, needs to be signup activity ", SignupActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText_userName_signup), username);
        solo.enterText((EditText) solo.getView(R.id.editText_email_signup), email);
        solo.enterText((EditText) solo.getView(R.id.editText_password_signup), "123456");
        solo.enterText((EditText) solo.getView(R.id.editText_password_confirm), "123456");
        solo.sleep(500);
        solo.clickOnButton("SIGNUP");
        solo.assertCurrentActivity("Activity needs to be homeActivity", HomeActivity.class);
        View bottomBar= solo.getCurrentActivity().findViewById(R.id.bottom_navigation); // get the button inside the frame layout
        View profile = bottomBar.findViewById(R.id.profile);
        solo.clickOnView(profile);
        solo.sleep(500);
        // logout
        solo.assertCurrentActivity("Activity needs to be profile activity", ProfileActivity.class);
        solo.clickOnButton("LOG OUT");
        return username;
    }

    public void search(String userName){
        // get the profile button from the bottom menu
        View bottomBar= solo.getCurrentActivity().findViewById(R.id.bottom_navigation); // get the button inside the frame layout
        View search = bottomBar.findViewById(R.id.search);
        solo.clickOnView(search);
        solo.sleep(500);
        solo.assertCurrentActivity("Activity needs to be search activity", SearchActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_text), userName);
        solo.clickOnButton("SEARCH");
        solo.sleep(1000);
        solo.clickInList(0);
        solo.assertCurrentActivity("Activity needs to be SearchProfileActivity", SearchProfileActivity.class);
        solo.sleep(1000);
    }


    @Test
    public void followUser(){
        String username = signUp();
        login();
        search(username);
        solo.clickOnButton("FOLLOW");
        solo.sleep(3000);
        solo.goBack();
        View bottomBar= solo.getCurrentActivity().findViewById(R.id.bottom_navigation); // get the button inside the frame layout
        View profile = bottomBar.findViewById(R.id.profile);
        solo.clickOnView(profile);
        solo.sleep(500);
        // logout
        solo.assertCurrentActivity("Activity needs to be profile activity", ProfileActivity.class);
        solo.clickOnButton("LOG OUT");

//        removeTestUser();
        solo.sleep(1000);
    }

    public String generateUSN(){
        Random random = new Random();
        int upperbound = 1000000000;
        int usn = random.nextInt(upperbound);
        String userName = String.valueOf(usn);
        return userName;
    }

    public String concatEmail(String usn){
        return usn+"@gmail.com";
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
