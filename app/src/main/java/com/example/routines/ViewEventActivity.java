package com.example.routines;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This activity display the details of a given event
 * It allows user to edit the details of the event
 */
public class ViewEventActivity extends AppCompatActivity {

    //Text views for the details of given event
    TextView eventName;
    TextView eventComment;
    TextView eventDate;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); //connect to firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        eventName = findViewById(R.id.view_event_name);
        eventComment = findViewById(R.id.view_event_comment);
        eventDate = findViewById(R.id.view_event_date);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enable the back button

        String eventId = (String) getIntent().getStringExtra("eventId"); //fetch event id from last activity

        //fetch document from firebase with given event id
        DocumentReference eventRef = db
                .collection("Events")
                .document(eventId);

        //get fields of document and set the text to text view
        eventRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                                String name = (String) document.getData().get("name");
                                String date = (String) document.getData().get("date");
                                String comment = (String) document.getData().get("description");

                                eventName.setText(name);
                                eventDate.setText(date);
                                eventComment.setText(comment);
                            } else
                                Log.d("Fail", "Error document noe exist: ", task.getException());

                        } else
                            Log.d("Fail", "Error fail to access documents ", task.getException());
                    }

                });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
