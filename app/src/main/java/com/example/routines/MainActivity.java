package com.example.routines;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener{
    public static final String EXTRA_TEXT = "com.example.routines.EXTRA_TEXT";

    private ArrayAdapter<Habit> habitAdapter;

    FirebaseFirestore db;
    String userID;
    DocumentReference userDocumentRef;
    CollectionReference userHabitCollectionRef;

    BottomNavigationView BottomNavigator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Get user ID from either login page or sign up page
        Intent intent = getIntent();
        String loginUserID = intent.getStringExtra(LoginActivity.EXTRA_TEXT);
        String signUpUserID = intent.getStringExtra(SignupActivity.EXTRA_TEXT);
        if (loginUserID != null) {
            userID = loginUserID;
        } else {
            userID = signUpUserID;
        }

//        Create a Habits list under the current User
        db = FirebaseFirestore.getInstance();
        userDocumentRef = db.collection("Users").document(userID);
        userHabitCollectionRef = db.collection("Users").document(userID).collection("Habits");

        // Bottom Navigation Bar
        BottomNavigator = findViewById(R.id.bottom_navigation);
        BottomNavigator.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){

                    case R.id.home:
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return true;
            }
        });


        // creating a listview and the adapter so we can store all the habits in a list on the home screen
        ListView habitList = findViewById(R.id.habitList);
        ArrayList<Habit> habitDataList = new ArrayList<>();
        habitAdapter = new CustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

//        Add habits from Firestore to local habit list
        userHabitCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitAdapter.clear();

                for (QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
//                    Log.d(TAG, String.valueOf(doc.getData().get("Province Name")));

                    String habitName = doc.getId();
                    String habitReason = (String)doc.getData().get("Habit Reason");
                    String habitDate = (String)doc.getData().get("Start Date");

                    habitAdapter.add(new Habit(habitName, habitReason, habitDate));
                }
            }
        });


        // when the + at the bottom of the screen is pressed it will call AddHabitFragment
        final FloatingActionButton addHabitButton = findViewById(R.id.addHabitButton);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddHabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
            }
        });

    }
    
    // this is called from the AddHabitFragment so we can add a new Habit to the list
    /**
     * This is called when the + button is pressed and the info from the pop up fragment
     * is filled out. This will add the newly created habit into the medList
     * @param newHabit
     */
    public void onOkPressed(Habit newHabit){
        String habitName = newHabit.getName();
        String habitReason = newHabit.getReason();
        String habitDate = newHabit.getDate();

//        Add new habit to Firestore
        HashMap<String, String> data = new HashMap<>();
        data.put("Habit Reason", habitReason);
        data.put("Start Date", habitDate);
        userHabitCollectionRef.document(habitName)
                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.w("Update Successfully", "Error on writing documentation on Firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Update Failed", "Error on writing documentation on Firebase");
            }
        });

//        Add to local habit list
        habitAdapter.add(newHabit);

    }



}