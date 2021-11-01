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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class SignupActivity extends AppCompatActivity {

    FirebaseFirestore db;
    CollectionReference collectionReference;
    CollectionReference userNames;
    FirebaseAuth myAuth;


    private String UserId;
    final String TAG = "SignUp";
    private int flag;

    private TextView signUpText;
    private TextView signUserText;
    private TextView signEmailText;
    private TextView signPasswordText;
    private TextView signConfirmText;

    private EditText signUser;
    private EditText signEmail;
    private EditText signPassword;
    private EditText signConfirm;

    private FloatingActionButton signUpExit;
    private Button signUpButton;

    private String inputName;
    private String inputEmail;
    private String inputPassword;
    private String inputConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        userNames = db.collection("User Names");

        initializeView();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputName = signUser.getText().toString();
                inputEmail = signEmail.getText().toString();
                inputPassword = signPassword.getText().toString();
                inputConfirm = signConfirm.getText().toString();


                boolean isValid = isValidEmail(signEmail.getText().toString());
                if(!(isValid)){
                    signEmail.setText("");
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                }

                if(inputName.length()==0 || inputEmail.length()==0|| inputPassword.length()==0|| inputConfirm.length()==0){
                    Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT).show();
                }else if(!(inputPassword.equals(inputConfirm))) {
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    signConfirm.setText("");
                    signPassword.setText("");
                }else if(inputPassword.length() < 6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 digits", Toast.LENGTH_SHORT).show();
                    signPassword.setText("");

                }else{
                    if(isValidUserName(inputName)) {
                        buildFile(inputEmail, inputPassword);
                    }else{
                        signUser.setText("");
                    }

                }

            }
        });

        signUpExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void initializeView(){
        signUpText = findViewById(R.id.text_signup_page);
        signUserText = findViewById(R.id.text_user);
        signEmailText = findViewById(R.id.text_Email_signup);
        signPasswordText = findViewById(R.id.text_password_signup);
        signConfirmText = findViewById(R.id.text_password_confirm);

        signUser = findViewById(R.id.editText_userName_signup);
        signEmail = findViewById(R.id.editText_email_signup);
        signPassword = findViewById(R.id.editText_password_signup);
        signConfirm = findViewById(R.id.editText_password_confirm);

        signUpButton = findViewById(R.id.button_signup_page);
        signUpExit = findViewById(R.id.floatingButton_Signup);
    }


    // Resource: https://www.javatpoint.com/java-email-validation
    public static boolean isValidEmail(String inputEmail){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (inputEmail.matches(regex)){
            return true;
        }else {
            return false;
        }
    }

    public boolean isValidUserName(String strName){
        DocumentReference docRef = userNames.document(strName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(getApplicationContext(),"User name already exists.", Toast.LENGTH_SHORT).show();
                        flag = 1;
                    } else {
                        assert true;
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        if (flag == 0){
            return true;
        }else{
            return false;
        }

    }



    public void buildFile(String UserEmail, String UserPassword){
        myAuth.createUserWithEmailAndPassword(UserEmail, UserPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = myAuth.getCurrentUser();
                            UserId = user.getUid();
                            HashMap<String, String> data = new HashMap<>();
                            data.put("User ID", UserId);
                            data.put("User Name", inputName);
                            data.put("Email", inputEmail);
                            data.put("Password", inputPassword);
                            collectionReference.document(UserId)
                                    .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Update Failed", "Error on writing documentation on Firebase");
                                }
                            });
                            userNames.document(inputName)
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Update Success", "User Information updated to User Names collection");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Update Failure",  "User information fails to update in User Names collection");
                                }
                            });
                        }else{
                            Log.w(TAG,"createUserWithEmail:failure" );
                            Toast.makeText(getApplicationContext(), "Authentication failed, user not added.", Toast.LENGTH_SHORT).show();
                            signConfirm.setText("");
                            signPassword.setText("");
                            signEmail.setText("");

                        }
                    }
                });


    }


}