package com.example.routines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * This activity allows the user to decide whether he/she wants to sign up or log in.
 * Then the button will direct the user to SignupActivity/LoginActivity
 * @author Shanshan Wei/swei3
 * @see LoginActivity
 * @see SignupActivity
 */

public class WelcomeActivity extends AppCompatActivity {

    private TextView appName;
    private TextView welcomeText;
    private TextView quoteText;
    private TextView quoteWriter;

    private Button loginButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide the title bar on welcome, login, signup
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_welcome);


        /**
         * This lines initialize the views of all components of this activity
         * @author Shanshan Wei/swei3
         */
        appName = findViewById(R.id.text_project_name);
        welcomeText = findViewById(R.id.text_welcome);
        quoteText = findViewById(R.id.text_quote);
        quoteWriter = findViewById(R.id.text_quote_writer);

        loginButton = findViewById(R.id.button_login);
        signupButton = findViewById(R.id.button_signup);

        /**
         * This sets the button listener and it will direct the user to LoginActivity if the user click on it
         * @author Shanshan Wei/swei3
         */

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /**
         * This sets the button listener and it will direct the user to SignupActivity if the user click on it
         * @author Shanshan Wei/swei3
         */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });
    }
}