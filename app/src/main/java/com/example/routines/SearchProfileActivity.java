package com.example.routines;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;

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
    TextView habitLabel;
    Button followButton;
    //String userId;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // connect to firebase

    FrameLayout fragmentLayout;
    HabitRecyclerAdapter habitAdapter;
    ArrayList<String> habitIdList;
    ArrayList<Habit> habitDataList;
    ArrayAdapter<Habit> habitArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);

        habitDataList = new ArrayList<>();
        //habitArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, habitDataList);
        habitArrayAdapter = new SearchHabitList(this, habitDataList);

        habitIdList = new ArrayList<>();

        //enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        habitList= findViewById(R.id.search_event_list);
        habitList.setAdapter(habitArrayAdapter);
        userName = findViewById(R.id.search_profile_name);
        habitLabel = findViewById(R.id.habit_label);
        followButton = findViewById(R.id.follow);
        //userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
                                habitLabel.setText(name + "'s Habits");
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

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
                                                habitDataList.add(new Habit( (String) value.getData().get("Habit Name"),
                                                        (String) value.getData().get("Habit Reason"),
                                                        (String) value.getData().get("Start Date"),
                                                        (ArrayList<String>) value.getData().get("Frequency"),
                                                        (String) value.getData().get("Privacy")));
                                                habitIdList.add(docId);
                                                habitArrayAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
}
