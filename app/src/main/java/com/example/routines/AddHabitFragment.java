package com.example.routines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *     To respond to the user clicking the + button at the bottom of the screen.
 *     This will pop up a fragment where the user can add all the habit info and it will
 *     add the habit to the habit list on the home page
 * @see HomeActivity class
 * @author lwaschuk 
 */
public class AddHabitFragment extends DialogFragment {
    /*
    Purpose: To respond to the user clicking the + button at the bottom of the screen.
    This will pop up a fragment where the user can add all the habit info and it will
    add the habit to the habit list on the home page

    Outstanding Issues:
    -XML formatting, need to make it look better
    -Several text formatting errors, need to format text properly, find out how to do it

     */
    private EditText habitName;
    private EditText habitDate;
    private EditText habitReason;
    private DatePicker datePicker;
    private Button confirmDateButton;
    private TextView frequencySelector;

    private int day;
    private int month;
    private int year;

    private Switch privacySwtich;
    private Switch monSwitch;
    private Switch tueSwitch;
    private Switch wedSwitch;
    private Switch thurSwitch;
    private Switch friSwitch;
    private Switch satSwitch;
    private Switch sunSwitch;

    private String privacy;
    private ArrayList<String> frequencyList;

    private OnFragmentInteractionListener listener;

    // create a interface to call a function in mainActivity

    /**
     * A interface to connect the fragment with Main activity
     * @see HomeActivity class
     * @author lwaschuk
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed(Habit habit);
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
        privacySwtich = view.findViewById(R.id.private_switch);

        datePicker = view.findViewById(R.id.date_picker);
        confirmDateButton = view.findViewById(R.id.confirm_button);

        frequencyList = new ArrayList<>();
        monSwitch = view.findViewById(R.id.mon_switch);
        tueSwitch = view.findViewById(R.id.tue_switch);
        wedSwitch = view.findViewById(R.id.wed_switch);
        thurSwitch = view.findViewById(R.id.thur_switch);
        friSwitch = view.findViewById(R.id.fri_switch);
        satSwitch = view.findViewById(R.id.sat_switch);
        sunSwitch = view.findViewById(R.id.sun_switch);


//        Create Date picker
        createDatePicker();

//        Create Frequency switches
        createFrequencySwitch();

//        Create Privacy Switch
        createPrivacySwitch();

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
                        long progress = 50; // init progress to 0 every time
                        listener.onOkPressed(new Habit(name, reason, date, frequencyList, privacy, progress));
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
    
    /**
     * create privacy switch
     * @return void
     * @author yyang13
     */
    public void createPrivacySwitch() {
        privacy = "Public";
        privacySwtich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !privacy.equals("Private")) {
                    privacy = "Private";
                }
                else if (!isChecked && !privacy.equals("Public")) {
                    privacy = "Public";
                }
            }
        });
    }

    /**
     * create DatePicker
     * @return void
     */
    public void createDatePicker() {
        //        Set current date as default
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
        habitDate.setText(String.format("%d-%02d-%d", year, month, day));

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
    }

    /**
     * create Frequency Switches
     * @return void
     */
    public void createFrequencySwitch() {
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
    }


}
