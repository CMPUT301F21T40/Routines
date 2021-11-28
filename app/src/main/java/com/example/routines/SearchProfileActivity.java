package com.example.routines;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;

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
    FrameLayout fragmentLayout;
    String userId;
    SearchHabitsFragment searchHabitFragment;
    private HabitRecyclerAdapter habitAdapter;


    CollectionReference habitCollection;
    DocumentReference userDocument;
    CollectionReference currentUserHabitCol;

    TextView habitLabel;
    Boolean follow;
    String actualUserId;
    ArrayList<String> habitIdList;
    ArrayList<Habit> habitDataList;
    ArrayAdapter<Habit> habitArrayAdapter;

    String to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);

        fragmentLayout = findViewById(R.id.search_container);

        //enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        habitDataList = new ArrayList<>();
        //habitArrayAdapter = new HabitList(this, habitDataList);
        habitIdList = new ArrayList<>();
        follow = false;

        String id = (String) getIntent().getStringExtra("userId");


        //habitList = findViewById(R.id.search_event_list);
        userName = findViewById(R.id.search_profile_name);
        followButton = findViewById(R.id.follow);
        habitLabel = findViewById(R.id.habit_label);
        //habitList.setAdapter(habitArrayAdapter);
        //habitAdapter = new HabitRecyclerAdapter(habitDataList, (HabitRecyclerAdapter.OnHabitClickListener) this);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //habitView = rootView.findViewById(R.id.fragment_habitList);
        //habitView.setLayoutManager(layoutManager);
        //habitView.setAdapter(habitAdapter);

        myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        actualUserId = user.getUid();

        requestReference = db.collection("Notification");
        requestId = db.collection(String.valueOf(requestReference)).document().getId();

        //get user id from last activity

        //get user document by the given user id
        DocumentReference userRef = db
                .collection("Users")
                .document(id);

        habitCollection = db.collection("Habits");
        //userDocument = habitCollection.document(userId);
//        Sub-collection of Habit under the current user


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
                                habitLabel.setText("Follow this user to view their habits");
                                habitLabel.setTextSize(15);
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

        checkFollower(id);
        //updateFragment(id);



        /*
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewHabitActivity.class);
                String habitId = habitIdList.get(position);
                intent.putExtra("sameUser", false);
                intent.putExtra("habitId", habitId);
                intent.putExtra("userId", id);
                Log.d("Intent habit id", habitId);
                startActivity(intent);
            }
        });

         */

        getCurrentUserName();

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUserName == userName.getText().toString()) {
                    Toast.makeText(getApplicationContext(), "You can't follow yourself", Toast.LENGTH_SHORT).show();
                } else {
                    addPendingFollower();

                }
            }
        });

        Log.d("Lukas", "BEFORE BUTTON STATUS");
        buttonStatusPending(); //  check if pending request
        buttonStatusAccepted(); // check if already following

    }




/*
    public void showList(String id){
        // = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference collectionReference = db.collection("Habits")
                .document(id)
                .collection("Habits");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collectionReference.document(document.getId())
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if(value != null && value.exists()){
                                            String privacy = (String) value.getData().get("Privacy");
                                            if (privacy != null && privacy.equals("Public")) {                                                habitDataList.add(new Habit( (String)value.getData().get("Habit Name"),
                                                        (String)value.getData().get("Habit Reason"),
                                                        (String)value.getData().get("Start Date"),
                                                        (long)value.getData().get("Progress")));
                                                habitIdList.add((String) document.getId() );
                                                habitAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });

                        //Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
*/


/*
    /**
     * This function will write the user's public habits if and only
     * if the current user is following the user
     *
     * @param id
     * @author ipaterso
     */
    /*
    public void showHabits(String id) {
        CollectionReference collectionReference = db.collection("Habits")
                .document(id)
                .collection("Habits");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String docId = document.getId();
                        collectionReference.document(docId)
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if (value != null && value.exists()) {
                                            String privacy = (String) value.getData().get("Privacy");
                                            if (privacy != null && privacy.equals("Public")) {
                                                habitDataList.add(new Habit((String) value.getData().get("Habit Name"),
                                                        (String) value.getData().get("Habit Reason"),
                                                        (String) value.getData().get("Start Date"),
                                                        (ArrayList<String>) value.getData().get("Frequency"),
                                                        (String) value.getData().get("Privacy"),
                                                        (long) value.getData().get("Progress")));
                                                habitIdList.add(docId);
                                                habitArrayAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }


     */

    /**
     * Function to set the string value of the button to it will display the correct values
     * (Pending)
     *
     * @author Lukas Waschuk
     * Todo: If there is time we can add a remove function here
     * @see SearchProfileActivity
     */
    public void buttonStatusPending() {
        db.collection("Users")
                .whereEqualTo("User Name", userName.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                requestReceiver = document.getId();
                                Log.d("requestReceiver", requestReceiver);
                            }
                            requestReference
                                    .whereEqualTo("Receiver Name", userName.getText().toString())
                                    .whereEqualTo("Sender Name", currentUserName)
                                    .whereEqualTo("Status", "pending")
                                    .limit(1)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            Log.d("Lukas", "inside onComplete1");
                                            Log.d("Lukas", currentUserName);
                                            Log.d("Lukas", userName.getText().toString());
                                            if (task.isSuccessful()) {
                                                Log.d("Lukas", "Inside task successful");
                                                boolean isEmpty = task.getResult().isEmpty();
                                                if (!isEmpty) {
                                                    followButton.setText("Request Sent");
                                                    //followButton.setBackgroundColor(0xff0000);
                                                    Log.d("Lukas", "Inside !ifEmpty statement");
                                                }
                                            } else {
                                                Log.d("Lukas", "Need to redo, not successful");
                                                onComplete(task);
                                            } // wait for firebase, DO NOT LEAVE YET
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * Function to set the string value of the button to it will display the correct values
     * (Accepted)
     *
     * @author Lukas Waschuk
     * Todo: If there is time we can add a remove function here
     * @see SearchProfileActivity
     */
    public void buttonStatusAccepted() {
        db.collection("Users")
                .whereEqualTo("User Name", userName.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Lukas", "Inside onComplete1 on accepted");
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                requestReceiver = document.getId();
                                Log.d("requestReceiver", requestReceiver);
                            }


                            // check for accepted flag and change the value
                            requestReference
                                    .whereEqualTo("Receiver Name", userName.getText().toString())
                                    .whereEqualTo("Sender Name", currentUserName)
                                    .whereEqualTo("Status", "accepted")
                                    .limit(1)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            Log.d("Lukas", "Inside onComplete2 on accepted");
                                            Log.d("Lukas", currentUserName);
                                            Log.d("Lukas", userName.getText().toString());
                                            if (task.isSuccessful()) {
                                                Log.d("Lukas", "Inside task successful on accepted");
                                                boolean isEmpty = task.getResult().isEmpty();
                                                if (!isEmpty) {
                                                    followButton.setText("Accepted");
//                                            followButton.setBackgroundColor(Color.RED); // to change color but i dont think we need to
                                                    Log.d("Lukas", "Inside If statement on accepted ");
                                                }
                                            } else {
                                                Log.d("Lukas", "Redo");
                                                onComplete(task);
                                            }// wait for firebase, DO NOT LEAVE YET
                                        }
                                    });
                        }
                    }
                });
    }

    public void getCurrentUserName() {
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

    public void checkFollower(String id) {
        db.collection("Notification")
                .whereEqualTo("Receiver", id)
                .whereEqualTo("Sender", actualUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Found a document--------------");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", "Got a task ----------------");
                                String status = (String) document.getData().get("Status");
                                Log.d("TAG", "Status : " + status);
                                if (status.equals("accepted")) {
                                    Log.d("TAG", "print habits here -------------");
                                    //showList(id);
                                    habitLabel.setText("User's Habits");
                                    habitLabel.setTextSize(25);
                                    searchHabitFragment = SearchHabitsFragment.newInstance(id);
                                    FragmentManager manager = getSupportFragmentManager();
                                    FragmentTransaction transaction = manager.beginTransaction();
                                    transaction.replace(R.id.search_container, searchHabitFragment);
                                    transaction.commit();
                                }
                            }

                        }
                    }
                });
        //updateFragment(id);
    }

    public void updateFragment(String id) {
        searchHabitFragment = SearchHabitsFragment.newInstance(id);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.search_container, searchHabitFragment);
        transaction.commit();

    }


    public void addPendingFollower() {
        db.collection("Users")
                .whereEqualTo("User Name", userName.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                requestReceiver = document.getId();
                                Log.d("requestReceiver", requestReceiver);
                            }
                            requestReference
                                    .whereEqualTo("Receiver", requestReceiver)
                                    .whereEqualTo("Sender", userId)
                                    .limit(1)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                boolean isEmpty = task.getResult().isEmpty();
                                                if (!isEmpty) {
                                                    Log.d("Follow", "There is request doc");
                                                    Toast.makeText(getApplicationContext(),
                                                            "You have already sent a request to this user", Toast.LENGTH_SHORT)
                                                            .show();
                                                } else {
                                                    HashMap<String, Object> data = new HashMap<>();
                                                    data.put("Sender", userId);
                                                    data.put("Receiver", requestReceiver);
                                                    data.put("Status", "pending");
                                                    data.put("Sender Name", currentUserName);
                                                    data.put("Receiver Name", userName.getText().toString());
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
                                                    Toast.makeText(getApplicationContext(),
                                                            "Request sent successfully", Toast.LENGTH_SHORT)
                                                            .show();
                                                    followButton.setText("Request Sent"); // change button after req sent
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });

    }
}

