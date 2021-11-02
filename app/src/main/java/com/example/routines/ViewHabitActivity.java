package com.example.routines;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewHabitActivity extends AppCompatActivity implements EditHabitFragment.OnFragmentInteractionListener {
    private ImageView backImage;
    TextView nameView;
    TextView dateView;
    TextView reasonView;
    Habit habit;
    String name;
    String date;
    String reason;
    String habitName;
    String habitDate;
    String habitReason;
    ArrayList<String> habitFrequency;
    String habitPrivacy;
    Button add;
    Button view;
    Button delete;
    FloatingActionButton edit;
    FirebaseAuth myAuth;
    String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        backImage = (ImageView) findViewById(R.id.add_event_backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        name = (String) "Name:  "+getIntent().getStringExtra("habitName");
        myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        userId = user.getUid();
        habitName = getIntent().getStringExtra("habitName");
        nameView = findViewById(R.id.add_event_name);
        reasonView = findViewById(R.id.add_event_description);
        dateView = findViewById(R.id.habit_date);
        add = findViewById(R.id.add_event_button);
        view = findViewById(R.id.view_event_button);
        edit = findViewById(R.id.edit_habit_button);
        delete = findViewById(R.id.delete_habit_button);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference habitRef = db
                .collection("Habits")
                .document(userId)
                .collection("Habits")
                .document(habitName);
        habitRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                // concatenated strings we should to this in the XML later
                                date = (String) "Date Started:  "+document.getData().get("Start Date");
                                reason = (String) "Reason:  "+document.getData().get("Habit Reason");
                                habitFrequency = (ArrayList<String>) document.getData().get("Frequency");
                                habitPrivacy = (String) document.getData().get("Privacy");
                                habitDate = (String) document.getData().get("Start Date");
                                habitReason = (String) document.getData().get("Habit Reason");
                                nameView.setText(name);
                                reasonView.setText(reason);
                                dateView.setText(date);
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            }
                            else {
                                Log.d("TAG", "No such document ", task.getException());
                            }
                        }
                        else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabitActivity.this, AddEventActivity.class);
                intent.putExtra("habitName", name);
                startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabitActivity.this, EventListActivity.class);
                intent.putExtra("habitName", name);
                startActivity(intent);
            }
        });

        /**
         * Response when the edit button is clicked, it sends the user
         * to a page to edit a given habits details
         */
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditHabitFragment.newInstance(new Habit(habitName, habitReason, habitDate, habitFrequency, habitPrivacy)).show(getSupportFragmentManager(), "EDIT_MEDICINE");

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collRef = db.collection("Habits")
                        .document(userId)
                        .collection("Habits");
                DocumentReference docRef = collRef.document(habit.getName());

//      Delete old habit
                docRef
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        });

    }

    /**
     * When user clicks "OK" from EditFragment, this function runs.
     * It deletes the habit that was edited, and creates a new one
     * with the updated parameters
     * @param habit
     * @param newHabit
     */
    public void onEditPressed(Habit habit, Habit newHabit) {
        String habitName = newHabit.getName();
        String habitReason = newHabit.getReason();
        String habitDate = newHabit.getDate();
        String habitPrivacy = newHabit.getPrivacy();
        ArrayList<String> habitFrequency = (ArrayList<String>) newHabit.getFrequency();
        if (habitFrequency.isEmpty()) {
            habitFrequency.add("Null");
        }

//      Update habit in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("Habits")
                .document(userId)
                .collection("Habits");
        DocumentReference docRef = collRef.document(habit.getName());

//      Delete old habit
        docRef
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

//      Replace with new/updated habit
        HashMap<String, Object> data = new HashMap<>();
        data.put("Habit Name", habitName);
        data.put("Habit Reason", habitReason);
        data.put("Start Date", habitDate);
        data.put("Frequency", habitFrequency);
        data.put("Privacy", habitPrivacy);
        collRef.document(habitName)
                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.w("Update Successfully", "Error on writing documentation on Firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Update Failed", "Error on writing documentation on Firebase");
            }
        });

        Intent intent = new Intent(ViewHabitActivity.this, ViewHabitActivity.class);
        intent.putExtra("habitName", newHabit.getName());
        startActivity(intent);


    }


}
