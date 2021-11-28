package com.example.routines;

import static android.content.ContentValues.TAG;

import static com.google.firebase.firestore.FieldValue.arrayRemove;

import android.content.DialogInterface;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This activity display the details of a given event
 * It allows user to edit the details of the event
 * @author Zezhou Xiong
 * @see Event
 */
public class ViewEventActivity extends AppCompatActivity implements EditEventFragment.OnFragmentInteractionalListener, DeleteEventFragment.OnFragmentInteractionListener{

    //Text views for the details of given event
    TextView eventName;
    TextView eventComment;
    TextView eventDate;
    TextView eventLocation;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); //connect to firebase
    FloatingActionButton editButton;
    FloatingActionButton deleteButton;
    String eventId;
    String habitId;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        eventName = findViewById(R.id.view_event_name);
        eventComment = findViewById(R.id.view_event_comment);
        eventDate = findViewById(R.id.view_event_date);
        eventLocation = findViewById(R.id.view_event_location);
        editButton = findViewById(R.id.edit_event_button);
        deleteButton = findViewById(R.id.delete_habit_event_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enable the back button

        eventId = (String) getIntent().getStringExtra("eventId"); //fetch event id from last activity
        habitId = (String) getIntent().getStringExtra("habitId"); //fetch habit id from last activity



        FirebaseAuth myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        //String actualUserId = user.getUid();
        String actualUserId = getIntent().getStringExtra("actualUserId");
        String userId = getIntent().getStringExtra("userId");
        Log.d("ABCD", userId);
        Boolean sameUser = getIntent().getBooleanExtra("sameUser", true);

        if (!sameUser) {
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        }
        /*
        if (userId != actualUserId) {
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        } else {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }*/

        showInfo();


        /**
         * This sets a button listener and pops a fragment for the user to edit the event details
         * @author Shanshan Wei/swei3
         */
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameShowed = eventName.getText().toString();
                String commentShowed = eventComment.getText().toString();
                Event eventNow = new Event(nameShowed, commentShowed);
                EditEventFragment.newInstance(eventNow).show(getSupportFragmentManager(), "Edit_Event");
            }
        });


        /**
         * This sets a button listener and pops a dialog for the user to delete the event details
         * @auther Zezhou Xiong
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteEventFragment();
                DeleteEventFragment.newInstance(eventId).show(getSupportFragmentManager(),"Delete Event");
            }
        });


    }

    /**
     * This function receive a event id and delete the corresponding event in the firebase
     * @param eventId
     * @auther zezhou
     */
    public void onDeletePressedEvent(String eventId) {
        String userId = getIntent().getStringExtra("userId");
        String habitId = getIntent().getStringExtra("habitId");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("Events")
                .document(eventId);
        eventRef
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

        DocumentReference habitRef = db.collection("Habits")
                .document(userId)
                .collection("Habits")
                .document(habitId);

        habitRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        String completion = (String) document.getData().get("Completion Time");
                        String estimateCompletion = (String) document.getData().get("Estimate Completion Time");
                        completion = Integer.toString(Integer.parseInt(completion)-1);
                        int progress = (Integer.parseInt(completion) *100 / Integer.parseInt(estimateCompletion));


                        habitRef.update("Completion Time", completion,
                                "Progress", progress,
                                "eventList", arrayRemove(eventId))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        onBackPressed();



    }




    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This overrides the listener onOkPressed.
     * If the user edits the event details on EditEventFragment and clicks OK, this will start to work
     * It will update the local event and update the event doc on the firebase
     * @author Shanshan Wei/swei3
     * @param event
     */
    @Override
    public void onOkPressed(Event event) {
        String nameEdited = event.getEventName();
        String descriptionEdited = event.getDescription();
        Log.d("Edited event", nameEdited+descriptionEdited);
        db.collection("Events").document(eventId)
                .update("name", nameEdited, "description", descriptionEdited);
        showInfo();

    }

    /**
     * This shows the information of the event
     */
    public void showInfo(){
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
                                String location = (String) document.getData().get("location");

                                eventName.setText(name);
                                eventDate.setText(date);
                                eventComment.setText(comment);
                                eventLocation.setText(location);
                            } else
                                Log.d("Fail", "Error document noe exist: ", task.getException());

                        } else
                            Log.d("Fail", "Error fail to access documents ", task.getException());
                    }

                });
    }
}
