package com.example.routines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        setContentView(R.layout.activity_login);

        initializeView();

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        myAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String InputEmail = loginEmail.getText().toString();
                String InputPassword = loginPassword.getText().toString();
                if (InputEmail.length() == 0|| InputPassword.length() == 0){
                    Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT ).show();
                }else{
                    Login(InputEmail, InputPassword);
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


    public void initializeView(){

        loginText = findViewById(R.id.text_login_page);
        loginEmailText = findViewById(R.id.text_email_login);
        loginPasswordText = findViewById(R.id.text_password_login);

        loginEmail = findViewById(R.id.editText_email);
        loginPassword = findViewById(R.id.editText_password_login);

        loginButton = findViewById(R.id.button_login_page);
        loginExit = findViewById(R.id.floatingButton_login);

    }

    public void Login(String UserEmail, String UserPassword){
        myAuth.signInWithEmailAndPassword(UserEmail,UserPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "invalid Email/Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}