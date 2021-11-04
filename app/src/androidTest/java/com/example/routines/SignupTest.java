package com.example.routines;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * INTENT TESTING US
 */
public class SignupTest {
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

    @Test
    public void signUpTest(){
        solo.assertCurrentActivity("Wrong activity, needs to be welcome activity", WelcomeActivity.class);
        solo.sleep(500);
        solo.clickOnButton("SIGNUP");
        solo.sleep(500);
        solo.assertCurrentActivity("Wrong activity, needs to be signup activity ", SignupActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText_userName_signup), "testUserNameWithDelete");
        solo.enterText((EditText) solo.getView(R.id.editText_email_signup), "testEmailWithDelete@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.editText_password_signup), "testPassword123456");
        solo.enterText((EditText) solo.getView(R.id.editText_password_confirm), "testPassword123456");
        solo.sleep(500);
        solo.clickOnButton("SIGNUP");
        solo.assertCurrentActivity("Activity needs to be homeActivity", HomeActivity.class);
        solo.sleep(500);
        removeTestUser();

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
        // deletes the user name too
        DocumentReference docRef = db.collection("User Names")
                .document("testUserNameWithDelete");
        //      Delete old habit
        docRef
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
        // deletes the authentication associated with the user
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
