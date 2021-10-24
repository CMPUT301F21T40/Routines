package com.example.routines;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener{
    private ArrayAdapter<Habit> habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creating a listview and the adapter so we can store all the habits in a list on the home screen
        ListView habitList = findViewById(R.id.habitList);
        ArrayList<Habit> habitDataList = new ArrayList<>();
        habitAdapter = new CustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        // adding some habits into the list for now so we can easily test them, we will remove these after
        // edit / delete have been tested
        Habit habit1 = new Habit("Soccer", "For fun", "2021-04-12");
        Habit habit2 = new Habit("Gym", "Lose weight", "2012-06-11");
        Habit habit3 = new Habit("University", "I hate myself", "2019-01-07");
        habitAdapter.add(habit1);
        habitAdapter.add(habit2);
        habitAdapter.add(habit3);

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
        habitAdapter.add(newHabit);
    }



}