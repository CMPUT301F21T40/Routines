package com.example.routines;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ViewHabitActivity extends AppCompatActivity implements EditHabitFragment.OnFragmentInteractionListener {
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
    FirebaseAuth myAuth;
    String userId;
    String habitId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        userId = user.getUid();

        habitId = getIntent().getStringExtra("habitId");
        nameView = findViewById(R.id.view_habit_name);
        reasonView = findViewById(R.id.view_habit_reason);
        dateView = findViewById(R.id.view_habit_date);
        privacyView = findViewById(R.id.view_habit_privacy);
        frequencyView = findViewById(R.id.view_habit_frequency);
        add = findViewById(R.id.add_event_button);
        view = findViewById(R.id.view_event_button);
        edit = findViewById(R.id.edit_habit_button);

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
                                Collections.sort(habitFrequency,comparator);
                                habitPrivacy = (String) document.getData().get("Privacy");
                                for (int i = 0; i < habitFrequency.size(); i++){
                                    frequency += habitFrequency.get(i) + " ";
                                }

                                nameView.setText("Name:  " + habitName);
                                reasonView.setText("Reason:  " + habitReason);
                                dateView.setText("Date:  " + habitDate);
                                privacyView.setText("Privacy:  " + habitPrivacy);
                                frequencyView.setText("Frequency:  " + frequency);
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
                EditHabitFragment.newInstance(new Habit(habitName, habitReason, habitDate, habitFrequency, habitPrivacy)).show(getSupportFragmentManager(), "EDIT_HABIT");

            }
        });

    }

    /**
     * When user clicks "OK" from EditFragment, this function runs.
     * It updates the habit that was selected with the newly provided details
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
        if (!(habitFrequency.isEmpty()) && habitFrequency.contains("Null")) {
            habitFrequency.remove("Null");
        }

//      Update habit in Firestore
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

        String extendedName = "Name: "+habitName;
        nameView.setText(extendedName);
        String extendedReason = "Reason: " + habitReason;
        reasonView.setText(extendedReason);
        String extendedDate = "Date: " + habitDate;
        dateView.setText(extendedDate);
        String extendedPrivacy = "Privacy: " + habitPrivacy;
        privacyView.setText(extendedPrivacy);
        String extendedFrequency = "Frequency: ";
        for (int i = 0; i < habitFrequency.size(); i++){
            extendedFrequency += habitFrequency.get(i) + " ";
        }
        frequencyView.setText(extendedFrequency);

        docRef.update(
                "Habit Name", habitName,
                "Habit Reason", habitReason,
                "Start Date", habitDate,
                "Frequency", habitFrequency,
                "Privacy", habitPrivacy);

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

}
