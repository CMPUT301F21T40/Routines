package com.example.routines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView BottomNavigator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Navigation Bar
        BottomNavigator = findViewById(R.id.bottom_navigation);
        BottomNavigator.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){

                    case R.id.home:
                        return true;

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
        habitAdapter.add(newHabit);
    }


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
}