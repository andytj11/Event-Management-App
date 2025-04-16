package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.example.assignment1.databinding.ActivityGoogleMapBinding;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapBinding binding;
    private SupportMapFragment mapFragment;
    private String selectedCategoryLocationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Getting the selectedCategoryLocation after clicking an category in FragmentListCategory
        selectedCategoryLocationName = getIntent().getExtras().getString("categoryLocation", "Malaysia");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocationName(selectedCategoryLocationName, 1); //last param means only return the first address object
        }
        catch (IOException e) {
        }

        LatLng selectedCategoryLocation = findCategoryLocation(addresses);

        // Add a marker on the map coordinates.
        mMap.addMarker(
                new MarkerOptions()
                        .position(selectedCategoryLocation)
                        .title(selectedCategoryLocationName)
        );

        // Move the camera to the map coordinates and zoom level to 10
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedCategoryLocation));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));


        // When we click anywhere on the map, return a msg about the country
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                String msg;

                List<Address> latlongToAddressList;

                try {
                    latlongToAddressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if(latlongToAddressList.isEmpty())
                    msg = "No country at this location! Sorry!";
                else{
                    android.location.Address address = latlongToAddressList.get(0);
                    msg = "The selected country is " + address.getCountryName() + "!";
                }
                Toast.makeText(mapFragment.getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public LatLng findCategoryLocation(List<Address> addresses){
        // if addresses is empty, means we cant find any location associated with the selectedCategoryLocationName
        if(addresses.isEmpty()) {
            Toast.makeText(this, "Category address not found", Toast.LENGTH_SHORT).show();
            // Set default location to Malaysia
            selectedCategoryLocationName = "Malaysia";
            return new LatLng(3.6926495795158947, 102.01061343260308); // Latitude and Longitude of Malaysia
        } else { // we found an address associated with selectedCategoryLocationName
            Toast.makeText(this, selectedCategoryLocationName, Toast.LENGTH_SHORT).show();
            Address topResult = addresses.get(0);
            return new LatLng(topResult.getLatitude(), topResult.getLongitude());
        }
    }
}