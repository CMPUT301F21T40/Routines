package com.example.routines;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * A custom list to add the habits into
 */
public class HabitList extends ArrayAdapter<Habit> {
    /*
    Purpose: To create the list that all the habits will be stored in for the duration of program
    execution

    Outstanding Issues: None
*/
    private ArrayList<Habit> habits;
    private Context context;

    public HabitList(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.habit_list, parent, false);
        }
        // a habit list to store the habits
        Habit habit = habits.get(position);
        TextView habitName = view.findViewById(R.id.habitName);
        TextView habitDate = view.findViewById(R.id.habitDate);
        TextView habitReason = view.findViewById(R.id.habitReason);


        habitName.setText(habit.getName());
        habitDate.setText(habit.getDate());
        habitReason.setText(habit.getReason());


        return view;
    }

    /**
     * Gets the total count of habits in the Habit List
     * @return
     */
    public int habitCount(){
        return habits.size();
    }

    /**
     * adds a new habit to the HabitList
     * @param habit
     */
    public void addHabit(Habit habit){
        habits.add(habit);
    }

    /**
     * checks if a habit exists in the habit list
     * @param habit
     * @return
     */
    public boolean containsHabit(Habit habit){
        return habits.contains(habit);
    }
}

