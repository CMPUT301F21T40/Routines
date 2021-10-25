package com.example.routines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private TextView AppName;
    private TextView WelcomeText;
    private TextView QuoteText;
    private TextView QuoteWriter;

    private Button LoginButton;
    private Button SignupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        AppName = findViewById(R.id.text_project_name);
        WelcomeText = findViewById(R.id.text_welcome);
        QuoteText = findViewById(R.id.text_quote);
        QuoteWriter = findViewById(R.id.text_quote_writer);

        LoginButton = findViewById(R.id.button_login);
        SignupButton = findViewById(R.id.button_signup);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });
    }
}