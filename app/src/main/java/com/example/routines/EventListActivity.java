package com.example.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This activity display a list of historical events of a habits
 * It allows user to check the details of an event
 */
public class EventListActivity extends AppCompatActivity {
    ListView eventList;
    ArrayAdapter<Event> eventArrayAdapter;
    ArrayList<Event> eventArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d("tag","event list displayed");
        eventList = findViewById(R.id.event_list_view);
        eventArrayList = new ArrayList<>();
        eventArrayAdapter = new EventCustomList(this, eventArrayList);
        eventList.setAdapter(eventArrayAdapter);

        String habitId = (String) getIntent().getStringExtra("habitId");


        //fetch all the events which stores corresponding habit id
        CollectionReference eventRef = db
                .collection("Events");

        eventRef
                .whereEqualTo("habitId", habitId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
                                String name = (String) document.getData().get("name");
                                String date = (String) document.getData().get("date");
                                String habitID = (String) document.getData().get("habitId");
                                String description = (String) document.getData().get("description");
                                eventArrayList.add(new Event(name, description, habitID, date));
                                eventArrayAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Fail", "Error getting documents: ", task.getException());
                        }
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
