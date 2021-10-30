package com.example.routines;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
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
public class HomeActivity extends AppCompatActivity{

    private ArrayAdapter<Habit> habitAdapter;
    private ArrayList<Habit> habitDataList;
    AppCompatRadioButton switchHabits;
    AppCompatRadioButton switchTodayHabits;
    FrameLayout fragmentLayout;
    TodayFilterFragment filterFragment;
    HomeFragment homeFragment;

    BottomNavigationView bottomNavigator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentLayout = findViewById(R.id.container);

        switchActivity();
        switchRadioButton();

        switchHabits.setTextColor(Color.WHITE);
        switchTodayHabits.setTextColor(Color.BLACK);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, homeFragment);
        transaction.commit();


        // when the + at the bottom of the screen is pressed it will call AddHabitFragment
        final FloatingActionButton addHabitButton = findViewById(R.id.addHabitButton);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddHabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
            }
        });



    }

    public void switchActivity(){
        // The bottom Navigation bar
        bottomNavigator = findViewById(R.id.bottom_navigation);
        bottomNavigator.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
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
    }

    public void switchRadioButton(){
        switchHabits = findViewById(R.id.switch_habits);
        switchHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(view);
            }
        });
        switchTodayHabits = findViewById(R.id.switch_today);
        switchTodayHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(view);
            }
        });
    }

    public void onClickButton(View view){
        boolean isSelected = ((AppCompatRadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.switch_habits:
                if(isSelected) {
                    switchHabits.setTextColor(Color.WHITE);
                    switchTodayHabits.setTextColor(Color.BLACK);
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, homeFragment);
                    transaction.commit();
                    Toast.makeText(getApplicationContext(), "all habits", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.switch_today:
                if(isSelected){
                    switchTodayHabits.setTextColor(Color.WHITE);
                    switchHabits.setTextColor(Color.BLACK);
                    filterFragment = TodayFilterFragment.newInstance();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, filterFragment);
                    transaction.commit();
                    Toast.makeText(getApplicationContext(), "Today filter", Toast.LENGTH_SHORT).show();
                }
        }
    }




}