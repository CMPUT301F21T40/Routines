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
 * User has the ability to update the information of a given habit.
 */

public class EditHabitFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onEditPressed(Habit habit, String name, String date, String reason);
    }

    /**
     * An instance of the habit that is to be edited
     * @param habit
     * @return
     */
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
        Habit originalHabit = (Habit) bundle.getSerializable("habit");
        String currentName = originalHabit.getName();
        String currentDate = originalHabit.getDate();
        String currentReason = originalHabit.getReason();

        view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_fragment, null);

        EditText habitName = view.findViewById(R.id.edit_name_float);
        EditText habitDate = view.findViewById(R.id.edit_date_float);
        EditText habitReason = view.findViewById(R.id.edit_reason_float);
        habitName.setText(currentName);
        habitDate.setText(currentDate);
        habitReason.setText(currentReason);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newName = habitName.getText().toString();
                        newName = check(newName);
                        String newDate = habitDate.getText().toString();
                        newDate = check(newDate);
                        String newReason = habitReason.getText().toString();
                        newReason = check(newReason);
                        listener.onEditPressed(originalHabit, newName, newDate, newReason);
                    }
                }).create();
    }

    /**
     * If the string length is 0, set the string to NULL and return
     * Same function from AddHabitFragment
     * @param str
     * @return
     */
    public String check(String str) {
        if (str.length() == 0) {
            str = "Null";
        }
        return str;
    }
}
