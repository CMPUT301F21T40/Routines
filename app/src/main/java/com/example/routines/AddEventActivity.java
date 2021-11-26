package com.example.routines;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Purpose: An activity to allow user to complete a habit event of a habit
 * Outstanding Issues:None
 * @author Zezhou Xiong
 */
public class AddEventActivity extends AppCompatActivity implements LocationListener {
    EditText eventName;
    EditText eventDescription;
    EditText eventLocation;
    Button addButton;
    Button getLocationBtn;
    String habitId;
    String userId;

    LocationManager locationManager;

    Button openMap;
    double currentLatitude = 0;
    double currentLongitude = 0;
    double latFromMap;
    double longFromMap;

    boolean loadingLocation = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);

        //allow user to return to last activity by press a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventName = findViewById(R.id.view_habit_name);
        eventDescription = findViewById(R.id.view_habit_reason);
        eventLocation = findViewById(R.id.event_location_editText);
        addButton = findViewById(R.id.add_event_button);
        getLocationBtn = findViewById(R.id.get_location_btn);
        openMap = findViewById(R.id.open_map);


        habitId = (String) getIntent().getStringExtra("habitId");
        userId = (String) getIntent().getStringExtra("userId");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        Check location permission
        checkLocationPermission();

//        Get location
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                loadingLocation = true;
                if (currentLongitude == 0 || currentLatitude == 0) {
                    Toast.makeText(getApplicationContext(), "Loading Location", Toast.LENGTH_SHORT ).show();
                }
            }
        });

//        Open map
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(AddEventActivity.this, Map.class);
                    intent.putExtra("currentLat", currentLatitude);
                    intent.putExtra("currentLong", currentLongitude);

                    startActivityForResult(intent, 1);
            }
        });


        /**
         * This is called when the add button is pressed and all the info
         * is filled out. This will add the newly created event into the habit
         */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eventName.getText().toString();
                String description = eventDescription.getText().toString();
                String locaiton = eventLocation.getText().toString();

                CollectionReference eventReference = db.collection("Events");

                if (name == "")
                    name = "null";

                if (description == "")
                    description = "null";

                if (locaiton == "") {
                    locaiton = "null";
                }

                //        Set current date as default
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);


//                If get current location button is pressed, add event will be diabled until information is loaded
                if (currentLongitude == 0 && currentLatitude == 0 && loadingLocation == true) {
                    Toast.makeText(getApplicationContext(), "Loading Location", Toast.LENGTH_SHORT ).show();
                } else {
                    String date = String.format("%d-%02d-%d", year, month, day);
                    HashMap<String, String> data = new HashMap<>();
                    data.put("name", name);
                    data.put("description", description);
                    data.put("location", locaiton);
                    data.put("habitId", habitId);
                    data.put("date", date);

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
                    eventLocation.setText("");
                    updateCompletion(habitId, userId, db);
                    onBackPressed();
                }


            }
        });

    }

    /**
     * Get result back from Map activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                latFromMap = data.getDoubleExtra("currentLat", 0);
                longFromMap = data.getDoubleExtra("currentLong", 0);

                if (latFromMap != 0 || longFromMap != 0){
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        List<Address> addresses = geocoder.getFromLocation(latFromMap, longFromMap, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        eventLocation.setText(address);

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


    /**
     * Check Location Permission
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(AddEventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddEventActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }

    /**
     * Get Location Service
     */
    @SuppressLint("MissingPermission")
    public void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, AddEventActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    /**
     * Get Address From Location
     * @param location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(AddEventActivity.this, Locale.getDefault());
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            eventLocation.setText(address);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateCompletion(String habitId, String userId, FirebaseFirestore db){
        DocumentReference habitRef = db
                .collection("Habits")
                .document(userId)
                .collection("Habits")
                .document(habitId);
        habitRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String completion = (String) document.getData().get("Completion Time");
                        
                        completion = Integer.toString(Integer.parseInt(completion + 1));
                        habitRef.update("Completion Time", completion)
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

    }


}
