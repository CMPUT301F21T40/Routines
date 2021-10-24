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

public class CustomList extends ArrayAdapter<Habit> {
    /*
    Purpose: To create the list that all the habits will be stored in for the duration of program
    execution

    Outstanding Issues: None
*/
    private ArrayList<Habit> habits;
    private Context context;

    public CustomList(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.custom_list, parent, false);
        }

        Habit habit = habits.get(position);
        TextView habitName = view.findViewById(R.id.habitName);
        TextView habitDate = view.findViewById(R.id.habitDate);
        TextView habitReason = view.findViewById(R.id.habitReason);


        habitName.setText(habit.getName());
        habitDate.setText(habit.getDate());
        habitReason.setText(habit.getReason());


        return view;
    }

}