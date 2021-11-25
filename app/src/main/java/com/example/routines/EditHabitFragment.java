package com.example.routines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Response when the user clicks edit after selecting a habit.
 * User has the ability to update the information of a given habit.
 * @author ipaterso
 * @see ViewHabitActivity
 */

public class EditHabitFragment extends DialogFragment {
    private Habit originalHabit;
    private String currentName;
    private String currentDate;
    private String currentReason;
    private String currentPrivacy;
    private EditText habitName;
    private EditText habitDate;
    private EditText habitReason;
    private DatePicker datePicker;
    private Button confirmDateButton;

    private int day;
    private int month;
    private int year;
    long defaultProgress;

    private Switch privacySwitch;
    private Switch monSwitch;
    private Switch tueSwitch;
    private Switch wedSwitch;
    private Switch thurSwitch;
    private Switch friSwitch;
    private Switch satSwitch;
    private Switch sunSwitch;

    private ArrayList<String> frequencyList;

    private EditHabitFragment.OnFragmentInteractionListener listener;

    //private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        //void onOkPressed(Habit habit);
        void onEditPressed(Habit habit, Habit newHabit);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof EditHabitFragment.OnFragmentInteractionListener) {
            listener = (EditHabitFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * An instance of the habit that is to be edited
     * @param habit
     * @return
     */
    static EditHabitFragment newInstance(Habit habit) {
        Bundle args = new Bundle();
        args.putSerializable("habit", habit);
        
        EditHabitFragment fragment = new EditHabitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_fragment, null);
        Bundle bundle = getArguments();
        originalHabit = (Habit) bundle.getSerializable("habit");
        currentName = originalHabit.getName();
        currentDate = originalHabit.getDate();
        currentReason = originalHabit.getReason();
        currentPrivacy = originalHabit.getPrivacy();
        defaultProgress = originalHabit.getProgress();

//      Set the attributes to their actual values
        habitName = view.findViewById(R.id.habitNameEditText);
        habitDate = view.findViewById(R.id.habitDateEditText);
        habitReason = view.findViewById(R.id.habitReasonEditText);
        habitName.setText(currentName);
        habitDate.setText(currentDate);
        if (currentReason == "Null") {
            currentReason = "";
        }
        habitReason.setText(currentReason);
        datePicker = view.findViewById(R.id.date_picker);
        confirmDateButton = view.findViewById(R.id.confirm_button);

//        Set Privacy switch to its actual value
        privacySwitch = view.findViewById(R.id.private_switch);
        if (currentPrivacy.contentEquals("Private")) {
            privacySwitch.setChecked(true);
        }

//      Set the Frequency details to their actual values
        frequencyList = (ArrayList<String>) originalHabit.getFrequency();
        if (frequencyList == null) {
            frequencyList = new ArrayList<String>();
        }
        monSwitch = view.findViewById(R.id.mon_switch);
        if (frequencyList.size() != 0 && frequencyList.contains("Monday")) {
            monSwitch.setChecked(true);
        }
        tueSwitch = view.findViewById(R.id.tue_switch);
        if (frequencyList.size() != 0 && frequencyList.contains("Tuesday")) {
            tueSwitch.setChecked(true);
        }
        wedSwitch = view.findViewById(R.id.wed_switch);
        if (frequencyList.size() != 0 && frequencyList.contains("Wednesday")) {
            wedSwitch.setChecked(true);
        }
        thurSwitch = view.findViewById(R.id.thur_switch);
        if (frequencyList.size() != 0 && frequencyList.contains("Thursday")) {
            thurSwitch.setChecked(true);
        }
        friSwitch = view.findViewById(R.id.fri_switch);
        if (frequencyList.size() != 0 && frequencyList.contains("Friday")) {
            friSwitch.setChecked(true);
        }
        satSwitch = view.findViewById(R.id.sat_switch);
        if (frequencyList.size() != 0 && frequencyList.contains("Saturday")) {
            satSwitch.setChecked(true);
        }
        sunSwitch = view.findViewById(R.id.sun_switch);
        if (frequencyList.size() != 0 && frequencyList.contains("Sunday")) {
            sunSwitch.setChecked(true);
        }

//        Set current date as default
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);

//        Create Date picker
        confirmDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day = datePicker.getDayOfMonth();
                month = (datePicker.getMonth() + 1);
                year = datePicker.getYear();
//                Format entered date
                habitDate.setText(String.format("%d-%02d-%d", year, month, day));
            }
        });


//        Create Privacy switch
        privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !currentPrivacy.equals("Private")) {
                    currentPrivacy = "Private";
                }
                else if (!isChecked && !currentPrivacy.equals("Public")) {
                    currentPrivacy = "Public";
                }
            }
        });


//        Create Frequency switches
        monSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !frequencyList.contains("Monday")) {
                    frequencyList.add("Monday");
                }
                else if (!isChecked && frequencyList.contains("Monday")){
                    frequencyList.remove("Monday");
                }
            }
        });

        tueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !frequencyList.contains("Tuesday")) {
                    frequencyList.add("Tuesday");
                }
                else if (!isChecked && frequencyList.contains("Tuesday")){
                    frequencyList.remove("Tuesday");
                }
            }
        });

        wedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !frequencyList.contains("Wednesday")) {
                    frequencyList.add("Wednesday");
                }
                else if (!isChecked && frequencyList.contains("Wednesday")){
                    frequencyList.remove("Wednesday");
                }
            }
        });

        thurSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !frequencyList.contains("Thursday")) {
                    frequencyList.add("Thursday");
                }
                else if (!isChecked && frequencyList.contains("Thursday")){
                    frequencyList.remove("Thursday");
                }
            }
        });

        friSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !frequencyList.contains("Friday")) {
                    frequencyList.add("Friday");
                }
                else if (!isChecked && frequencyList.contains("Friday")){
                    frequencyList.remove("Friday");
                }
            }
        });

        satSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !frequencyList.contains("Saturday")) {
                    frequencyList.add("Saturday");
                }
                else if (!isChecked && frequencyList.contains("Saturday")){
                    frequencyList.remove("Saturday");
                }
            }
        });

        sunSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !frequencyList.contains("Sunday")) {
                    frequencyList.add("Sunday");
                }
                else if (!isChecked && frequencyList.contains("Sunday")){
                    frequencyList.remove("Sunday");
                }
            }
        });

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
                        String newPrivacy = currentPrivacy;
                        newPrivacy = check(newPrivacy);
                        listener.onEditPressed(originalHabit, new Habit(newName, newReason, newDate, frequencyList, newPrivacy, defaultProgress));
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
        if (str.length() == 0){
            str = "Null";
        }
        return str;
    }
}
