package com.example.routines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    MarkerOptions marker;
    Button confirmLocationBtn;

    double currentLat;
    double currentLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        currentLat = intent.getDoubleExtra("currentLat", 0);
        currentLong = intent.getDoubleExtra("currentLong", 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        confirmLocationBtn = findViewById(R.id.confirm_location_btn);
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


    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        LatLng latLng = new LatLng(currentLat, currentLong);
        marker = new MarkerOptions().position(latLng).title("Current Location");
        map.addMarker(marker);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                map.clear();
                marker = new MarkerOptions().position(latLng).title("Selected");
                map.addMarker(marker);

                currentLat = latLng.latitude;
                currentLong = latLng.longitude;
            }
        });

    }
}