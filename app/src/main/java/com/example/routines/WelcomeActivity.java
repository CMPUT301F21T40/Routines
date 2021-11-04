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
        setContentView(R.layout.activity_welcome);

        appName = findViewById(R.id.text_project_name);
        welcomeText = findViewById(R.id.text_welcome);
        quoteText = findViewById(R.id.text_quote);
        quoteWriter = findViewById(R.id.text_quote_writer);

        loginButton = findViewById(R.id.button_login);
        signupButton = findViewById(R.id.button_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });
    }
}