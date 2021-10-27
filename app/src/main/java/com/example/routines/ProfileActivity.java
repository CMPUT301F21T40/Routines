package com.example.routines;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    final String TAG = "Profile";

    FirebaseFirestore db;
    CollectionReference Users;
    CollectionReference userNames;
    FirebaseAuth myAuth;
    private String UserId;
    BottomNavigationView BottomNavigator;

    private TextView NameText;
    private TextView EmailText;
    private TextView UserName;
    private TextView UserEmail;
    private ImageView UserPhoto;
    private Uri ImageUri;
    Button LogOutButton;
    ActivityResultLauncher<Intent> someActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Users = db.collection("Users");

        initializeData();
        switchActivity();
        showInformation();

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            UserPhoto.setImageBitmap(bitmap);
                        }
                    }
                });



    }

    public void initializeData(){
        EmailText = findViewById(R.id.text_email_lable_profile);
        NameText = findViewById(R.id.text_user_lable_profile);
        UserEmail = findViewById(R.id.text_email_profile);
        UserName = findViewById(R.id.text_user_profile);
        UserPhoto = findViewById(R.id.image_profile);
        LogOutButton = findViewById(R.id.button_profile);
        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        UserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                choosePicture();
            }
        });

    }

    public void switchActivity(){
        // The bottom Navigation bar
        BottomNavigator = findViewById(R.id.bottom_navigation);
        BottomNavigator.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        return true;

                }
                return true;
            }
        });
    }

    public void showInformation(){
        FirebaseUser User = myAuth.getCurrentUser();
        if(User != null){
            UserId = User.getUid();
            Users.document(UserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if (value != null && value.exists()) {
                        Log.d(TAG, "Current data: " + value.getData());
                        String Name = (String) value.getData().get("User Name");
                        String Email = (String) value.getData().get("Email");
                        UserName.setText(Name);
                        UserEmail.setText(Email);

                    } else {
                        Log.d(TAG, "Current data: null");
                    }

                }
            });

        }
    }

    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            someActivityResultLauncher.launch(intent);
        } else {
            Toast.makeText(this, "There is no app that support this action",
                    Toast.LENGTH_SHORT).show();
        }
    }
}