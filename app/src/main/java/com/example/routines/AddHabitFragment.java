package com.example.routines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 *     To respond to the user clicking the + button at the bottom of the screen.
 *     This will pop up a fragment where the user can add all the habit info and it will
 *     add the habit to the habit list on the home page
 */
public class AddHabitFragment extends DialogFragment {
    /*
    Purpose: To respond to the user clicking the + button at the bottom of the screen.
    This will pop up a fragment where the user can add all the habit info and it will
    add the habit to the habit list on the home page

    Outstanding Issues:
    -XML formatting, need to make it look better
    -Several text formatting errors, need to format text properly, rushed through it
    -need date picker to choose the date (just have normal text right now)

     */
    private EditText habitName;
    private EditText habitDate;
    private EditText habitReason;

    private OnFragmentInteractionListener listener;

    // create a interface to call a function in mainActivity

    /**
     * A interface to connect the fragment with Main activity
     */
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
        // get the user input and send it to the "onOkPressed listener"
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_fragment, null);
        habitName = view.findViewById(R.id.habitNameEditText);
        habitDate = view.findViewById(R.id.habitDateEditText);
        habitReason = view.findViewById(R.id.habitReasonEditText);
        habitReason.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

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

    /**
     * If the string len is 0 set the string to NULL and return
     * @param str
     * @return str
     * returns either the original string or the NULL
     */
    public String check(String str){
        if (str.length() == 0){
            str = "Null";
        }
        return str;
    }
}