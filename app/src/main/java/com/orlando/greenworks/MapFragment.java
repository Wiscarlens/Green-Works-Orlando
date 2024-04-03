package com.orlando.greenworks;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class MapFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1; // Added constant for location permission request code
    private final ArrayList<RecyclingCenter> recyclingCenterList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MainActivity.fragmentTitle.setText(R.string.map); // Set the title of the fragment in the toolbar.

        loadData();


        // Check if user was granted permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if it is not granted
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
        }


        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            mMap = googleMap;

            // Log the API key
            Log.d("API_KEY", BuildConfig.MAPS_API_KEY);

            LatLng orlando = new LatLng(28.5383, -81.3792); // Orlando, FL Map
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(orlando, 11)); // adjust default map zoom level

            // Enable zoom controls
            mMap.getUiSettings().setZoomControlsEnabled(true);

            // Check if user was granted permission
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true); // Enable location layer if permission is granted
            } else {
                // Request location permission if it is not granted
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }

            // Define the size of the marker icon
            int scaledSize = 90; // adjust this size as needed, default is 60

            // Create a scaled bitmap from the original drawable resource
            BitmapDrawable binImage = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.recycle_bin_1, null);
            assert binImage != null;
            Bitmap b = binImage.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, scaledSize, scaledSize, false);

            // Create a HashMap to store the association between markers and recycling centers
            HashMap<Marker, RecyclingCenter> markerRecyclingCenterMap = new HashMap<>();

            // Add a marker for each location
            for (RecyclingCenter center : recyclingCenterList) {
                String snippet = "Click Here To Navigate To This Location";
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(center.getLatLng()) // Set the position of the marker
                        .title(center.getAddress()) // Set the title of the marker to the address
                        .snippet(snippet) // Set the snippet of the marker to the additional text
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));


                // Store the association between the marker and the recycling center
                markerRecyclingCenterMap.put(marker, center);
            }

            // Set an InfoWindow click listener
            mMap.setOnInfoWindowClickListener(marker -> {
                // When the InfoWindow is clicked, start an intent to open Google Maps for navigation
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(marker.getTitle()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            });


            mMap.setOnMarkerClickListener(marker -> {
                // Get the recycling center associated with the clicked marker
                RecyclingCenter clickedCenter = markerRecyclingCenterMap.get(marker);
                // Show the bottom sheet dialog for the clicked recycling center
                assert clickedCenter != null;
                showBottomSheetDialog(clickedCenter);

                return false;
            });

        } catch (Exception e) {
            // Log the exception and handle it appropriately
            Log.e("MapFragment", "Error in onMapReady", e);
        }
    }

    // Added method to handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                assert mapFragment != null;
                mapFragment.getMapAsync(this);
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showBottomSheetDialog(RecyclingCenter recyclingCenter) {
        final Dialog bottomSheetDialog = new Dialog(requireContext());

        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.map_bottom_dialog);

        TextView recyclingCenterName = bottomSheetDialog.findViewById(R.id.recyclingCenterName);
        TextView recyclingCenterAddress = bottomSheetDialog.findViewById(R.id.recyclingCenterAddress);
        TextView recyclingCenterHours = bottomSheetDialog.findViewById(R.id.recyclingCenterHours);
        TextView recyclingCenterPhone = bottomSheetDialog.findViewById(R.id.recyclingCenterPhone);

        recyclingCenterName.setText(recyclingCenter.getName());
        recyclingCenterAddress.setText(recyclingCenter.getAddress());
        recyclingCenterHours.setText(recyclingCenter.getHours());
        recyclingCenterPhone.setText(recyclingCenter.getPhone());

        bottomSheetDialog.show();

        Objects.requireNonNull(bottomSheetDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    void loadData() {
        recyclingCenterList.add(new RecyclingCenter("Dover Shore Community Center", "1400 Gaston Foster Road", "Monday - Saturday\n9:00 AM - 9:00 PM\nSunday\n1:00 PM - 6:00PM", "407-246-4451", 28.52551, -81.32586));
        recyclingCenterList.add(new RecyclingCenter("Engelwood Neighborhood Center", "6123 Lacosta Drive", "Monday - Friday\n8:00 AM - 9:00 PM\nSaturday 9:00 AM - 9:00 PM\nSunday 1:00 PM - 6:00PM", "407-246-4453", 28.52894, -81.30075));
        recyclingCenterList.add(new RecyclingCenter("Northwest Community Center", "3955 WD Judge Road", "Monday - Saturday\n9:00 AM - 9:00 PM\nSunday\n1:00 PM - 9:00 PM", "407-246-4465", 28.56112, -81.42777));
        recyclingCenterList.add(new RecyclingCenter("Solid Waste Management Division", "1028 South Woods Avenue", "Monday - Friday\n8:00 AM - 5:00 PM\nSaturday and Sunday\nCLOSED", "407-246-2314", 28.52933, -81.39580));
    }

    @Override
    public void onStop() {
        super.onStop();

        MainActivity.fragmentTitle.setText(""); // Clear the title of the fragment in the toolbar.

    }

}