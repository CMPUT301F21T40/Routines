package com.example.routines;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore db;
    CollectionReference Users;
    CollectionReference userNames;
    FirebaseAuth myAuth;
    private String UserId;
    BottomNavigationView BottomNavigator;

    TextView NameText;
    TextView EmailText;
    TextView UserName;
    TextView UserEmail;
    ImageView UserPhoto;
    Button LogOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Users = db.collection("Users");

        initializeData();
        switchActivity();





    }

    public void initializeData(){
        EmailText = findViewById(R.id.text_email_lable_profile);
        NameText = findViewById(R.id.text_user_lable_profile);
        UserEmail = findViewById(R.id.text_email_profile);
        UserName = findViewById(R.id.text_user_profile);
        UserPhoto = findViewById(R.id.image_profile);
        LogOutButton = findViewById(R.id.button_profile);

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

                }
            });

        }
    }
}