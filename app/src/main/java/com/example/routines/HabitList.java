package com.example.routines;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

/**
 * A custom list to add the habits into
 * @see HomeActivity class
 * @author lwaschuk
 */
public class HabitList extends ArrayAdapter<Habit> {
    /*
    Purpose: To create the list that all the habits will be stored in for the duration of program
    execution

    Outstanding Issues: None
*/
    private ArrayList<Habit> habits;
    private Context context;

    /**
     * constructor for the habit list
     * @param context Context
     * @param habits Habit
     * @see HabitList Class
     * @author lwaschuk
     */
    public HabitList(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     * gets the view of the habitlist to add habits into
     * @param position int
     * @param convertView View
     * @param parent View
     * @see HomeActivity class
     * @author lwaschuk
     * @return view View
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
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

        // this is testing for progress bar
        ProgressBar progress = view.findViewById(R.id.progressBar3);


        habitName.setText(habit.getName());
        progress.setProgress(0);
        progress.setMax(100);
        progress.setProgress(60);

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

