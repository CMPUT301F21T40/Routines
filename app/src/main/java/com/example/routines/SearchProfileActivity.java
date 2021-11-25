package com.example.routines;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

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
    CollectionReference requestReference;
    FirebaseAuth myAuth;
    String requestId;
    String requestReceiver;
    String currentUserName;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);

        //enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        habitList= findViewById(R.id.search_event_list);
        userName = findViewById(R.id.search_profile_name);
        followButton = findViewById(R.id.follow);

        requestReference = db.collection("Notification");
        requestId = db.collection(String.valueOf(requestReference)).document().getId();


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

        getCurrentUserName();
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUserName == userName.getText().toString()){
                    Toast.makeText(getApplicationContext(),"You can't follow yourself", Toast.LENGTH_SHORT).show();
                }else{
                    addPendingFollower();
                }
            }
        });



    }

    public void getCurrentUserName(){
        myAuth = FirebaseAuth.getInstance();
        userId = myAuth.getCurrentUser().getUid();

        Log.d("requestid 1", requestId);

        db.collection("Users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Current user name", "Listen failed.", error);
                            return;
                        }
                        if (value != null && value.exists()) {
                            Log.d("Current user name", "Current data: " + value.getData());
                            currentUserName = (String) value.getData().get("User Name");
                        } else {
                            Log.d("Current user name", "Current data: null");
                        }

                    }
                });
    }




    public void addPendingFollower(){
        db.collection("Users")
                .whereEqualTo("User Name", userName.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                requestReceiver = document.getId();
                                Log.d("requestReceiver", requestReceiver);
                            }
                            requestReference
                                    .whereEqualTo("Receiver", requestReceiver )
                                    .whereEqualTo("Sender", userId)
                                    .limit(1)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                boolean isEmpty = task.getResult().isEmpty();
                                                if(!isEmpty){
                                                    Log.d("Follow", "There is request doc");
                                                    Toast.makeText(getApplicationContext(),
                                                            "You have send request to this user", Toast.LENGTH_SHORT)
                                                            .show();
                                                }else{
                                                    HashMap<String, Object> data = new HashMap<>();
                                                    data.put("Sender", userId);
                                                    data.put("Receiver", requestReceiver);
                                                    data.put("Status", "pending");
                                                    data.put("Sender Name", currentUserName);
                                                    data.put("Receiver Name", userName.getText().toString() );
                                                    requestReference.document(requestId)
                                                            .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.w("Add Request", "Success on writing documentation on Firebase");
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("Add Request Failed", "Error on writing documentation on Firebase");
                                                        }
                                                    });

                                                }

                                            }

                                        }
                                    });

                        }
                    }
                });

    }
}
