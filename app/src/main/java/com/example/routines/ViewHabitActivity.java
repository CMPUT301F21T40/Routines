package com.example.routines;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewHabitActivity extends AppCompatActivity implements EditHabitFragment.OnFragmentInteractionListener , DeleteHabitFragment.OnFragmentInteractionListener{
    TextView nameView;
    TextView dateView;
    TextView reasonView;
    TextView privacyView;
    TextView frequencyView;
    String frequency = "";
    String habitName;
    String habitDate;
    String habitReason;
    ArrayList<String> habitFrequency;
    String habitPrivacy;
    Button add;
    Button view;
    FloatingActionButton edit;
    FloatingActionButton delete;
    FirebaseAuth myAuth;
    String userId;
    String actualUserId;
    String habitId;
    long habitProgress;

    HomeFragment homeFragment;
    ViewHabitActivity viewActivity;
    Object ViewHabitActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        actualUserId = user.getUid();

        userId = (String) getIntent().getStringExtra("userId");
        Log.d("TAG", "user ID: " + userId);


        habitId = getIntent().getStringExtra("habitId");
        nameView = findViewById(R.id.view_habit_name);
        reasonView = findViewById(R.id.view_habit_reason);
        dateView = findViewById(R.id.view_habit_date);
        privacyView = findViewById(R.id.view_habit_privacy);
        frequencyView = findViewById(R.id.view_habit_frequency);
        add = findViewById(R.id.add_event_button);
        view = findViewById(R.id.view_event_button);
        edit = findViewById(R.id.edit_habit_button);
        delete = findViewById(R.id.delete_habit_button);

        //Boolean sameUser = Boolean.valueOf(getIntent().getStringExtra("sameUser"));
        Boolean sameUser = getIntent().getBooleanExtra("sameUser", true);

        if (!sameUser) {
            add.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
        } else {
            userId = actualUserId;
        }
/*
        if ((userId != null) && (userId != actualUserId)) {
            add.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
        } else {
            userId = actualUserId;
        }*/


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference habitRef = db
                .collection("Habits")
                .document(userId)
                .collection("Habits")
                .document(habitId);
        habitRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                // concatenated strings we should to this in the XML later
                                habitName = (String) document.getData().get("Habit Name");
                                habitDate = (String) document.getData().get("Start Date");
                                habitReason = (String) document.getData().get("Habit Reason");
                                habitFrequency = (ArrayList<String>) document.getData().get("Frequency");
                                habitProgress = (long)document.getData().get("Progress");
                                Collections.sort(habitFrequency,comparator);
                                habitPrivacy = (String) document.getData().get("Privacy");
                                for (int i = 0; i < habitFrequency.size(); i++){
                                    frequency += habitFrequency.get(i) + " ";
                                }

                                nameView.setText(habitName);
                                reasonView.setText(habitReason);
                                dateView.setText(habitDate);
                                privacyView.setText(habitPrivacy);
                                frequencyView.setText(frequency);
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
                Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                intent.putExtra("habitId", habitId);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabitActivity.this, EventListActivity.class);
                intent.putExtra("habitId", habitId);
                intent.putExtra("userId", userId);
                intent.putExtra("sameUser", sameUser);
                intent.putExtra("actualUserId", actualUserId);
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
                EditHabitFragment.newInstance(new Habit(habitName, habitReason, habitDate, habitFrequency, habitPrivacy, habitProgress)).show(getSupportFragmentManager(), "EDIT_HABIT");

            }
        });

        /**
         * Response when the delete button is clicked, it sends the user
         * to a page to confirm the deletion of the selected habit
         */
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteHabitFragment().newInstance(new Habit(habitName, habitReason, habitDate, habitFrequency, habitPrivacy, habitProgress)).show(getSupportFragmentManager(), "DELETE_HABIT");
            }
        });

    }

    /**
     * When the user clicks "CONFIRM" to delete the habit, this function runs.
     * It takes the habit as a parameter then deletes it from the firebase
     * @param habit
     * @author ipaterso
     * @see DeleteHabitFragment
     */
    public void onDeletePressed(Habit habit) {
//      Connect to Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("Habits")
                .document(userId)
                .collection("Habits");
        DocumentReference docRef = collRef.document(habitId);

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
        deleteEvent();
//      Update the Screen
        finish();
        Intent intent = getIntent();
        intent.setClass(ViewHabitActivity.this, HomeActivity.class);
        startActivity(intent);
        ViewGroup vg = findViewById(R.id.container);
    }

    /**
     * When user clicks "OK" from EditFragment, this function runs.
     * It updates the habit that was selected with the newly provided details
     * @param habit
     * @param newHabit
     * @author ipaterso
     * @see EditHabitFragment
     */
    public void onEditPressed(Habit habit, Habit newHabit) {
        habitName = newHabit.getName();
        habitReason = newHabit.getReason();
        habitDate = newHabit.getDate();
        habitPrivacy = newHabit.getPrivacy();
        habitFrequency = (ArrayList<String>) newHabit.getFrequency();
        if (habitFrequency.isEmpty()) {
            habitFrequency.add("Null");
        }
        if (!(habitFrequency.isEmpty()) && habitFrequency.contains("Null")) {
            habitFrequency.remove("Null");
        }

        if ((habitFrequency.size() > 1) && habitFrequency.contains("Null")) {
            habitFrequency.remove("Null");
        }

//      Connect to Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collRef = db.collection("Habits")
                .document(userId)
                .collection("Habits");
        DocumentReference docRef = collRef.document(habitId);

        nameView = findViewById(R.id.view_habit_name);
        reasonView = findViewById(R.id.view_habit_reason);
        dateView = findViewById(R.id.view_habit_date);
        privacyView = findViewById(R.id.view_habit_privacy);
        frequencyView = findViewById(R.id.view_habit_frequency);



//      Update the Details Screen
        nameView.setText(habitName);
        reasonView.setText(habitReason);
        String extendedDate = "Date: " + habitDate;
        dateView.setText(habitDate);
        privacyView.setText(habitPrivacy);
        String extendedFrequency = "";
        for (int i = 0; i < habitFrequency.size(); i++){
            extendedFrequency += habitFrequency.get(i) + " ";
        }
        frequencyView.setText(extendedFrequency);

//      Update Firebase
        docRef.update(
                "Habit Name", habitName,
                "Habit Reason", habitReason,
                "Start Date", habitDate,
                "Frequency", habitFrequency,
                "Privacy", habitPrivacy);

        Intent intent = getIntent();
        intent.putExtra("Habit Name", habitName);
        intent.putExtra("Start Date", habitDate);
        intent.putExtra("Habit Reason", habitReason);
        intent.putExtra("Frequency", habitFrequency);
        intent.putExtra("Privacy", habitPrivacy);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Comparator<String> comparator = new Comparator<String>() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public int compare(String day1, String day2) {
            return Integer.compare(DayOfWeek.valueOf(day1.toUpperCase()).getValue(),
                    DayOfWeek.valueOf(day2.toUpperCase()).getValue());
        }
    };

    /**
     * This method delete all the events of a habit
     * @author Shanshan Wei/swei3
     */
    public void deleteEvent(){
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Events");
        collectionReference.whereEqualTo("habitId", habitId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                collectionReference
                                        .document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Delete Events", "Succeed");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Delete Events", "Failure");
                                            }
                                        });
                            }
                        }
                    }
                });

    }

}
