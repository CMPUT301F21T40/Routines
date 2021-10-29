package com.example.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;

public class AddEventActivity extends AppCompatActivity {
    EditText eventName;
    EditText eventDescription;
    Button addButton;
    String habitID;
    private ImageView backImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);

        eventName = findViewById(R.id.add_event_name);
        eventDescription = findViewById(R.id.add_event_description);
        addButton = findViewById(R.id.add_event_button);

        Intent intent = getIntent();
        habitID = (String) getIntent().getStringExtra("habitName");
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        backImage = (ImageView) findViewById(R.id.add_event_backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eventName.getText().toString();
                String description = eventDescription.getText().toString();
                String date = String.valueOf(android.text.format.DateFormat.format("dd-mm-yyyy", new java.util.Date()));
                HashMap<String, String> data = new HashMap<>();
                data.put("name", name);
                data.put("description", description);
                data.put("habitID", habitID);
                data.put("date", date);



                CollectionReference eventReference = db
                        .collection("Events");
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

                if (eventID != null) {
                    DocumentReference habitReference = db
                            .collection("Habits").document(habitID);
                    Log.d("Success", "field added with ID: " + eventID);
                    habitReference.update("eventList", FieldValue.arrayUnion(eventID));
                }

                eventName.setText("");
                eventDescription.setText("");
                onBackPressed();
            }
        });

    }
}
