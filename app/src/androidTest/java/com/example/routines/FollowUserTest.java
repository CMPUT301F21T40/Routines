package com.example.routines;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Intent testing for us 05 01 01
 *
 * notes: to actually test this right now you have to enter a new username and email on line 74 75
 * have to change line 99 to that username
 * change line 112 to that new email
 * change line 159 to the new user id
 */
public class FollowUserTest {
    FirebaseFirestore db;
    CollectionReference collectionReference;
    CollectionReference userNames;
    FirebaseAuth myAuth;
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

    public void signUp(){
        solo.assertCurrentActivity("Wrong activity, needs to be welcome activity", WelcomeActivity.class);
        solo.sleep(500);
        solo.clickOnButton("SIGNUP");
        solo.sleep(500);
        solo.assertCurrentActivity("Wrong activity, needs to be signup activity ", SignupActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText_userName_signup), "newUser0000");
        solo.enterText((EditText) solo.getView(R.id.editText_email_signup), "newUser0000@gmail.com");
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
    }

    public void search(){
        // get the profile button from the bottom menu
        View bottomBar= solo.getCurrentActivity().findViewById(R.id.bottom_navigation); // get the button inside the frame layout
        View search = bottomBar.findViewById(R.id.search);
        solo.clickOnView(search);
        solo.sleep(500);
        solo.assertCurrentActivity("Activity needs to be search activity", SearchActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_text), "newUser0000"); // just find myself for now
        solo.clickOnButton("SEARCH");
        solo.sleep(1000);
        solo.clickInList(0);
        solo.assertCurrentActivity("Activity needs to be SearchProfileActivity", SearchProfileActivity.class);
        solo.sleep(1000);
    }

    public void loginDeleteAccount(){
        solo.assertCurrentActivity("Wrong activity, needs to be Welcome", WelcomeActivity.class);
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Activity needs to be loginActivity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText_email), "newUser0000@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.editText_password_login), "123456");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Activity needs to be homeActivity", HomeActivity.class);
    }


    @Test
    public void followUser(){
        signUp();
        login();
        search();
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
        loginDeleteAccount();
        removeTestUser();
        solo.sleep(1000);
    }



    /**
     * Deletes the test user so the test can be reused after every iteration
     */
    public void removeTestUser(){
        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        userNames = db.collection("User Names");
        // gets the user
        myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        String userId = user.getUid();


        // delete the user name from suer name collection
        db.collection("User Names").document("newUser0000")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        // delete the user ID from user id collection
        db.collection("Users").document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        user.delete();

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
