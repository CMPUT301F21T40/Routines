package com.example.routines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Response when the user clicks edit after selecting a habit.
 * User is shown the habit's information with the option to update it.
 */

public class editHabitFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onEditPressed(Habit habit, String name, String date, String reason);
    }

    static AddHabitFragment newInstance(Habit habit) {
        Bundle args = new Bundle();
        args.putSerializable("habit", (Serializable) habit);
        
        AddHabitFragment fragment = new AddHabitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view;
        Bundle bundle = getArguments();
        Habit original_habit = (Habit) bundle.getSerializable("habit");
        String current_name = original_habit.getName();
        String current_date = original_habit.getDate();
        String current_reason = original_habit.getReason();

        view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_fragment, null);

        EditText habitName = view.findViewById(R.id.edit_name_float);
        EditText habitDate = view.findViewById(R.id.edit_date_float);
        EditText habitReason = view.findViewById(R.id.edit_reason_float);
        habitName.setText(current_name);
        habitDate.setText(current_date);
        habitReason.setText(current_reason);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String new_name = habitName.getText().toString();
                        String new_date = habitDate.getText().toString();
                        String new_reason = habitReason.getText().toString();
                        listener.onEditPressed(original_habit, new_name, new_date, new_reason);
                    }
                }).create();
    }
}
