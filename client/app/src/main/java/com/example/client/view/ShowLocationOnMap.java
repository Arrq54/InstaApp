package com.example.client.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.client.R;
import com.example.client.databinding.ActivityShowLocationOnMapBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class ShowLocationOnMap extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityShowLocationOnMapBinding showLocationOnMapBinding;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showLocationOnMapBinding = ActivityShowLocationOnMapBinding.inflate(getLayoutInflater());

        setContentView(showLocationOnMapBinding.getRoot());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(this);


    }
    private void getPlaceById(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        Places.initialize(getApplicationContext(), "__API_KEY__");
        PlacesClient placesClient = Places.createClient(this);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            LatLng latLng =     place.getLatLng();
            map.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng , 10);
            map.moveCamera(cameraUpdate);
        }).addOnFailureListener((exception) -> {
            exception.printStackTrace();
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

        getPlaceById(getIntent().getStringExtra("id"));

    }
}
