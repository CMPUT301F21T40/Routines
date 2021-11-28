package com.example.routines;

import static android.app.appsearch.AppSearchResult.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This fragment shows the event details with its name and comment
 * The user can also edit the event by inputting a new name and a new comment
 * @author Shanshan Wei/swei3
 * @see ViewEventActivity
 * @see Event
 */

public class EditEventFragment extends DialogFragment implements LocationListener {

    private Event event;
    private EditText eventName;
    private EditText eventComment;
    private EditText eventDate;
    private EditText eventLocation;
    private OnFragmentInteractionalListener listener;

    private DatePicker datePicker;
    private Button confirmDateButton;

    private FragmentActivity myContext;

    LocationManager locationManager;

    Button openMap;
    double currentLatitude = 0;
    double currentLongitude = 0;
    double latFromMap;
    double longFromMap;

    boolean loadingLocation = false;

    private int day;
    private int month;
    private int year;


    public interface OnFragmentInteractionalListener{
        void onOkPressed(Event event);
    }

    /**
     * This is used to transfer data of the object Event from ViewEventActivity
     * @param event
     * @return fragment
     * @author Shanshan Wei/swei3
     */
    public static EditEventFragment newInstance(Event event){
        Bundle args = new Bundle();
        args.putSerializable("Event", event);
        EditEventFragment fragment = new EditEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * The start of fragments' lifecycle
     * It attaches the fragment
     * @param context
     * @author Shanshan Wei/swei3
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof  OnFragmentInteractionalListener){
            listener = (OnFragmentInteractionalListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionalListener");
        }
    }



    /**
     * This builds a dialog fragment to show details of the event and allows them to edit the details.
     * @param saveInstanceState
     * @return Dialog
     * @author Shanshan Wei/swei3
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle saveInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_event, null);
        eventName = view.findViewById(R.id.editText_event_name);
        eventComment = view.findViewById(R.id.editText_event_comment);
        eventDate = view.findViewById(R.id.editText_event_date);
        eventLocation = view.findViewById(R.id.editText_event_location);

        datePicker = view.findViewById(R.id.event_date_picker);
        confirmDateButton = view.findViewById(R.id.confirm_eventDate_button);
        openMap = view.findViewById(R.id.editEvent_open_map);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        event = (Event) getArguments().getSerializable("Event");
        eventName.setText(event.getEventName());
        eventComment.setText(event.getDescription());
        eventDate.setText(event.getEventDate());
        eventLocation.setText(event.getEventLocation());


//        Create Date picker
        createDatePicker();

//        Check location permission
        checkLocationPermission();

//        Open map
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Map.class);
                intent.putExtra("currentLat", currentLatitude);
                intent.putExtra("currentLong", currentLongitude);

                startActivityForResult(intent, 101);
            }
        });


        return builder
                .setView(view)
                .setTitle("Edit Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputName = eventName.getText().toString();
                        String inputComment = eventComment.getText().toString();
                        String inputDate = eventDate.getText().toString();
                        String inputLocation = eventLocation.getText().toString();
                        Log.d("EditEvent fragment", inputName+inputComment);
                        event.setEventName(inputName);
                        event.setDescription(inputComment);
                        event.setEventDate(inputDate);
                        event.setEventLocation(inputLocation);
                        listener.onOkPressed(new Event(inputName, inputComment, inputDate, inputLocation));
                    }
                }).create();

    }

    /**
     * Get result back from Map activity
     * @param requestCode
     * @param resultCode
     * @param data
     * @return void
     * @author yyang13
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            latFromMap = data.getDoubleExtra("currentLat", 0);
            longFromMap = data.getDoubleExtra("currentLong", 0);

            if (latFromMap != 0 || longFromMap != 0){
                try {
                    Geocoder geocoder = new Geocoder(getContext());
                    List<Address> addresses = geocoder.getFromLocation(latFromMap, longFromMap, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    eventLocation.setText(address);

                }catch (Exception e) {
                    System.out.println("HEeerrerereererer");
                    System.out.println("HEeerrerereererer");
                    System.out.println("HEeerrerereererer");
                    System.out.println(e.toString());
                }
            }


        }

    }

    /**
     * create DatePicker
     * @return void
     * @author yyang13
     *
     */
    public void createDatePicker() {
//        Create Date picker
        confirmDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day = datePicker.getDayOfMonth();
                month = (datePicker.getMonth() + 1);
                year = datePicker.getYear();
//                Format entered date
                eventDate.setText(String.format("%d-%02d-%d", year, month, day));
            }
        });
    }


    /**
     * Check Location Permission
     * @return void
     * @author yyang13
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }


    /**
     * Get Address From Location
     * @param location
     * @return void
     * @author yyang13
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            eventLocation.setText(address);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }



}
