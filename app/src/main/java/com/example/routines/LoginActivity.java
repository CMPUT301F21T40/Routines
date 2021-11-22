package com.example.routines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This activity will allow user to input the email and password.
 * If the email is signed up and the password is correct, the user will be directed to HomeActivity
 * @author Shanshan wei/swei3
 * @see HomeActivity
 */

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseAuth myAuth;

    final String TAG = "Login";

    private TextView loginText;
    private TextView loginEmailText;
    private TextView loginPasswordText;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private FloatingActionButton loginExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide the title bar on welcome, login, signup
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_login);
        initializeView();

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        myAuth = FirebaseAuth.getInstance();

        /**
         * This sets a button listener.
         * if the input emails and passwords is correct, this will direct the user to HomeActivity
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = loginEmail.getText().toString();
                String inputPassword = loginPassword.getText().toString();
                if (inputEmail.length() == 0|| inputPassword.length() == 0){
                    Toast toast = Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT );
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    Login(inputEmail, inputPassword);
                }
            }
        });

        loginExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    /**
     * This will initialize views for all components like textview, buttons
     */
    public void initializeView(){

        loginText = findViewById(R.id.text_login_page);
        loginEmailText = findViewById(R.id.text_email_login);
        loginPasswordText = findViewById(R.id.text_password_login);

        loginEmail = findViewById(R.id.editText_email);
        loginPassword = findViewById(R.id.editText_password_login);

        loginButton = findViewById(R.id.button_login_page);
        loginExit = findViewById(R.id.floatingButton_login);

    }

    /**
     * This will allow user to log in to their account by checking with firebase auth
     * @param UserEmail
     * @param UserPassword
     */
    public void Login(String UserEmail, String UserPassword){
        myAuth.signInWithEmailAndPassword(UserEmail,UserPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toast toast = Toast.makeText(getApplicationContext(), "invalid Email/Password", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }
                    }
                });


    }
}