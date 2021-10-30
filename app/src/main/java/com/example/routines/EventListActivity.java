package com.example.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class EventListActivity extends AppCompatActivity {
    private ImageView backImage;
    ListView eventList;
    ArrayAdapter<Event> eventArrayAdapter;
    ArrayList<Event> eventArrayList;
    ArrayList<String> habitIDList;
    private String habitID = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


        backImage = (ImageView) findViewById(R.id.event_list_backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Log.d("tag","event list displayed");
        eventList = findViewById(R.id.event_list_view);
        eventArrayList = new ArrayList<>();
        eventArrayAdapter = new EventCustomList(this, eventArrayList);
        eventList.setAdapter(eventArrayAdapter);

        String habitName = (String) getIntent().getStringExtra("habitName");


        CollectionReference eventRef = db
                .collection("Events");


        eventRef
                .whereEqualTo("habitID", habitName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
                                String name = (String) document.getData().get("name");
                                String date = (String) document.getData().get("date");
                                String habitID = (String) document.getData().get("habitID");
                                String description = (String) document.getData().get("description");
                                eventArrayList.add(new Event(name, description, habitID, date));
                                eventArrayAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Fail", "Error getting documents: ", task.getException());
                        }
                    }
                });

/*
        habitRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d("Success", "Task is successful");
                            DocumentSnapshot document = task.getResult();
                            if (document != null){
                                Log.d("Success","Document is not empty : "+task.getResult().getData());
                                habitIDList = (ArrayList<String>) document.get("eventList");
                                Log.d("aaaaaaaaaaaa", habitIDList.get(1));
                            }
                            else
                                Log.d("Fail", "Document is empty");
                        }
                        else
                            Log.d("Fail", "Task is failed");
                    }
                });

 */



    }

}
