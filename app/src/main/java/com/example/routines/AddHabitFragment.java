package com.example.routines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddHabitFragment extends DialogFragment {
    /*
    Purpose: To open a fragment that lets the user add a new medication when the button in the
    bottom left is clicked

    Outstanding Issues: Data checking for the Name (to make sure a name is actually submitted, i did
    error checking for it and if the user leaves it blank it will assign null to the name) and a
    dropdown for the unit type (mg, mcg, drop)

     */
    private EditText habitName;
    private EditText habitDate;
    private EditText habitReason;

    private OnFragmentInteractionListener listener;

    // create a interface to call a function in mainActivity
    public interface OnFragmentInteractionListener {
        void onOkPressed(Habit newHabit);

    }
    // error checking for OnFragment listener
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_fragment, null);
        habitName = view.findViewById(R.id.habitNameEditText);
        habitDate = view.findViewById(R.id.habitDateEditText);
        habitReason = view.findViewById(R.id.habitReasonEditText);


        // create a pop up with a calendar and multiple areas for the user to fill in
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add New Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String date = habitDate.getText().toString();
                        date = check(date);
                        String name = habitName.getText().toString();
                        name = check(name);
                        String reason = habitReason.getText().toString();
                        reason = check(reason);

                        listener.onOkPressed(new Habit(name, reason, date));
                    }
                }).create();
    }
    // to check if the user entered anything, if he entered nothing it will add NULL
    public String check(String str){
        if (str.length() == 0){
            str = "Null";
        }
        return str;
    }
}