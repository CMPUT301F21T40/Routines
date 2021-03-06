package com.example.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
 * @author Zezhou Xiong
 * @see Event
 */
public class EventListActivity extends AppCompatActivity {
    ListView eventList;
    ArrayAdapter<Event> eventArrayAdapter;
    ArrayList<Event> eventArrayList;
    ArrayList<String> eventIdList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        eventList = findViewById(R.id.event_list_view);
        eventArrayList = new ArrayList<>();
        eventIdList = new ArrayList<>();
        eventArrayAdapter = new EventCustomList(this, eventArrayList);
        eventList.setAdapter(eventArrayAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewEventActivity.class);
                String eventId = eventIdList.get(i);
                intent.putExtra("eventId", eventId);
                startActivity(intent);
            }
        });


    }


    /**
     * Override the onResume to allow APP refresh event list if the user delete any
     * event in the event list
     */
    @Override
    public void onResume() {
        super.onResume();
        String habitId = (String) getIntent().getStringExtra("habitId");
        String userId = (String) getIntent().getStringExtra("userId");
        String actualUserId = (String) getIntent().getStringExtra("actualUserId");
        Boolean sameUser = getIntent().getBooleanExtra("sameUser", true);

        //fetch all the events which stores corresponding habit id
        CollectionReference eventRef = db.collection("Events");
        eventArrayList.clear();
        eventArrayAdapter.notifyDataSetChanged();
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
                                String location = (String) document.getData().get("location");
                                String habitId = (String) document.getData().get("habitId");
                                String description = (String) document.getData().get("description");
                                String eventId = (String) document.getId();

                                eventArrayList.add(new Event(name, description, habitId, date, location));
                                eventIdList.add(eventId);
                                eventArrayAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Fail", "Error getting documents: ", task.getException());
                        }
                    }
                });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewEventActivity.class);
                String eventId = eventIdList.get(i);
                intent.putExtra("userId", userId);
                intent.putExtra("eventId", eventId);
                intent.putExtra("sameUser", sameUser);
                intent.putExtra("actualUserId", actualUserId);
                intent.putExtra("habitId", habitId);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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
