package com.example.routines;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

/**
 * This activity display the details of a user
 * Outstanding issues: allows user to follow all the habit
 * @author Zezhou Xiong
 * @see User
 */
public class SearchProfileActivity extends AppCompatActivity {
    //initialize the layout
    ListView habitList;
    TextView userName;
    Button followButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // connect to firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);

        //enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        habitList= findViewById(R.id.search_event_list);
        userName = findViewById(R.id.search_profile_name);
        followButton = findViewById(R.id.follow);

        //get user id from last activity
        String id = (String) getIntent().getStringExtra("userId");

        //get user document by the given user id
        DocumentReference userRef = db
                .collection("Users")
                .document(id);

        //fetch field from the document and set text to text view
        userRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = (String) document.getData().get("User Name");
                                userName.setText(name);
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

    }
}
