package com.example.routines;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** Purpose: A map activity that allows user to place a marker on map or search address,
 *  the result location will be returned to AddEventActivity or EditEventActivity
 * @author yyang13
 */

public class Map extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    MarkerOptions userMarker;
    MarkerOptions searchMarker;
    Button confirmLocationBtn;

    Place place;
    EditText addressEditText;


    double currentLat;
    double currentLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        get latitude and longitude from the class that called Map activity
        Intent intent = getIntent();
        currentLat = intent.getDoubleExtra("currentLat", 0);
        currentLong = intent.getDoubleExtra("currentLong", 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

//        Confirm button returns data back to the parent activity
        confirmLocationBtn = findViewById(R.id.confirm_location_btn);
        confirmLocationBtn.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
        confirmLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("currentLat", currentLat);
                resultIntent.putExtra("currentLong", currentLong);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


        addressEditText = findViewById(R.id.address_search_editText);

//        Initialize Place from Google Map API
        Places.initialize(getApplicationContext(), "AIzaSyCvHbudRqKKcUArzx2lnA_nS16vZJfLYi4");
        addressEditText.setFocusable(false);
        addressEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(Map.this);
                startActivityForResult(intent, 100);
            }
        });


    }


    /**
     * Get result from activity
     * @param requestCode
     * @param resultCode
     * @param data
     * @return void
     * @author yyang13
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            place = Autocomplete.getPlaceFromIntent(data);
            addressEditText.setText(place.getAddress());

            currentLat = place.getLatLng().latitude;
            currentLong = place.getLatLng().longitude;

            map.clear();
            LatLng latLng2 = new LatLng(currentLat, currentLong);
            searchMarker = new MarkerOptions().position(latLng2).title("Selected");
            map.addMarker(searchMarker);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLong), 5), 500, null);


        }else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Create Map
     * @param googleMap
     * @return void
     * @author yyang13
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(53.4397, -113.625);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

//        Allows user to put a marker on map, when return to parent activity,
//        Latitude and longitude will be translated into address
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                addressEditText.setText("");
                map.clear();
                userMarker = new MarkerOptions().position(latLng).title("Selected");
                map.addMarker(userMarker);

                currentLat = latLng.latitude;
                currentLong = latLng.longitude;
            }
        });


    }


}