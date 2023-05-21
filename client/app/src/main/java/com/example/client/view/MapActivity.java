package com.example.client.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.client.R;
import com.example.client.databinding.ActivityMapBinding;
import com.example.client.model.LocationChoosen;
import com.example.client.model.LocationForPhoto;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityMapBinding mapBinding;
    private GoogleMap map;
    private List<Address> list;
    private Geocoder geocoder;
    private SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapBinding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(mapBinding.getRoot());


        mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(MapActivity.this);

        Places.initialize(getApplicationContext(), "API_KEY_HERE");
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
        autocompleteFragment.getView().setBackgroundColor(0x0000ff);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS));




        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                builder.setTitle("Locaiton selected");

                String locationName = place.getName() + ", ";
                
                for(AddressComponent component : place.getAddressComponents().asList()){
                    Log.d("logdev", component.getTypes().toString());
                    if(component.getTypes().contains("locality") || component.getTypes().contains("country")){
                        locationName += component.getName() + ", ";
                    }
                }
                locationName = locationName.substring(0, locationName.length()-2);

                builder.setMessage("Do you want to choose: \""+locationName+ "\" as a location for your photo?");

                Log.d("logdev", place.toString());
                String finalLocationName = locationName;
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code to handle "Yes" button click
                        // ...

                        LocationChoosen.setLocationForPhoto(new LocationForPhoto(finalLocationName, place.getId()));
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();




                Log.i("logdev", "Place: " + place.getName() + ", " + place.getId());
            }


            @Override
            public void onError(@NonNull Status status) {
                Log.i("logdev", "error: " + status);
            }
        });

    }
    private void geocode(String locationName) throws IOException {

        list = geocoder.getFromLocationName(locationName, 1);

        double latitude = list.get(0).getLatitude();
        double longitude = list.get(0).getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng , 10);
        map.moveCamera(cameraUpdate);

    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);


        LocationManager locationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            locationManager = (LocationManager) getSystemService(MapActivity.this.LOCATION_SERVICE);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        }

    }

}