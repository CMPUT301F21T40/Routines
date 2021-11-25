package com.example.routines;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchHabitList extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habits;
    private Context context;

    public SearchHabitList(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.search_habit_list, parent, false);
        }
        // a habit list to store the habits
        Habit habit = habits.get(position);
        TextView habitName = view.findViewById(R.id.search_habit_name);


        habitName.setText(habit.getName());

        return view;
    }

    /**
     * Gets the total count of habits in the Habit List
     * @return habits.size() int
     * @see HabitList class
     * @author lwaschuk
     */
    public int habitCount(){
        return habits.size();
    }

    /**
     * adds a new habit to the HabitList
     * @param habit Habit
     * @see HabitList class
     * @author lwsachuk
     */
    public void addHabit(Habit habit){
        habits.add(habit);
    }

    public void removeHabit(Habit habit) { habits.remove(habit);}

    /**
     * checks if a habit exists in the habit list
     * @param habit Habit
     * @return boolean
     * @see HabitList class
     * @author lwaschuk
     */
    public boolean containsHabit(Habit habit){
        return habits.contains(habit);
    }

}