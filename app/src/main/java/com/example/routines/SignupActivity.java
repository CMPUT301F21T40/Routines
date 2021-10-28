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

    private TextView SignUpText;
    private TextView SignUserText;
    private TextView SignEmailText;
    private TextView SignPasswordText;
    private TextView SignConfirmText;

    private EditText SignUser;
    private EditText SignEmail;
    private EditText SignPassword;
    private EditText SignConfirm;

    private FloatingActionButton SignUpExit;
    private Button SignUpButton;

    private String InputName;
    private String InputEmail;
    private String InputPassword;
    private String InputConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        userNames = db.collection("User Names");


        initializeView();




        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputName = SignUser.getText().toString();
                InputEmail = SignEmail.getText().toString();
                InputPassword = SignPassword.getText().toString();
                InputConfirm =SignConfirm.getText().toString();


                boolean isValid = isValidEmail(SignEmail.getText().toString());
                if(!(isValid)){
                    SignEmail.setText("");
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                }

                if(InputName.length()==0 ||InputEmail.length()==0|| InputPassword.length()==0|| InputConfirm.length()==0){
                    Toast.makeText(getApplicationContext(), "Information Missing", Toast.LENGTH_SHORT).show();
                }else if(!(InputPassword.equals(InputConfirm))) {
                    Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                    SignConfirm.setText("");
                    SignPassword.setText("");
                }else if(InputPassword.length() < 6){
                    Toast.makeText(getApplicationContext(),"Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    SignPassword.setText("");

                }else{
                    if(isValidUserName(InputName)) {
                        buildFile(InputEmail, InputPassword);
                    }else{
                        SignUser.setText("");
                    }

                }

            }
        });

        SignUpExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void initializeView(){
        SignUpText = findViewById(R.id.text_signup_page);
        SignUserText = findViewById(R.id.text_user);
        SignEmailText = findViewById(R.id.text_Email_signup);
        SignPasswordText = findViewById(R.id.text_password_signup);
        SignConfirmText = findViewById(R.id.text_password_confirm);

        SignUser = findViewById(R.id.editText_userName_signup);
        SignEmail = findViewById(R.id.editText_email_signup);
        SignPassword = findViewById(R.id.editText_password_signup);
        SignConfirm = findViewById(R.id.editText_password_confirm);

        SignUpButton = findViewById(R.id.button_signup_page);
        SignUpExit = findViewById(R.id.floatingButton_Signup);
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
                        Toast.makeText(getApplicationContext(),"User name used", Toast.LENGTH_SHORT).show();
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
                            data.put("User Name", InputName );
                            data.put("Email", InputEmail);
                            data.put("Password", InputPassword);
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
                            userNames.document(InputName)
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
                            Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                            SignConfirm.setText("");
                            SignPassword.setText("");
                            SignEmail.setText("");

                        }
                    }
                });


    }


}