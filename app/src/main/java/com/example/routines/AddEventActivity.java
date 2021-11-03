package com.example.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
/**
 * Purpose: An activity to allow user to complete a habit event of a habit
 * Outstanding Issues:None
 */
public class AddEventActivity extends AppCompatActivity {
    EditText eventName;
    EditText eventDescription;
    Button addButton;
    String habitId;
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);

        //allow user to return to last activity by press a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventName = findViewById(R.id.view_habit_name);
        eventDescription = findViewById(R.id.view_habit_reason);
        addButton = findViewById(R.id.add_event_button);


        habitId = (String) getIntent().getStringExtra("habitId");
        userId = (String) getIntent().getStringExtra("userId");

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        /**
         * This is called when the add button is pressed and all the info
         * is filled out. This will add the newly created event into the habit
         */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eventName.getText().toString();
                String description = eventDescription.getText().toString();

                if (name == "")
                    name = "null";

                if (description == "")
                    description = "null";
                //        Set current date as default
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);

                String date = String.format("%d-%02d-%d", year, month, day);
                HashMap<String, String> data = new HashMap<>();
                data.put("name", name);
                data.put("description", description);

                data.put("habitId", habitId);
                data.put("date", date);



                CollectionReference eventReference = db
                        .collection("Events");

                //generate an unique id for each event
                String eventID = eventReference.document().getId();
                eventReference
                        .document(eventID)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Success","Document added with ID: "+eventID);
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Fail", "Error adding document", e);
                            }
                        });

                //after successfully add a event, update the event list under the habit collection
                if (eventID != null) {
                    DocumentReference habitReference = db
                            .collection("Habits").document(userId)
                            .collection("Habits").document(habitId);
                    Log.d("Success", "field added with ID: " + eventID);
                    habitReference.update("eventList", FieldValue.arrayUnion(eventID));
                }

                //return to the last activity after everything is done
                eventName.setText("");
                eventDescription.setText("");
                onBackPressed();
            }
        });

    }

    /**
     * This is called when the up button is pressed. It changes the original
     * functionality of up button. It let user back to the last activity
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
