package com.orlando.greenworks;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.maps.model.Marker;
import android.util.Log;


/*
___________________________________________________________________________________________
___________________________________________________________________________________________
|| IF MAP DOESN'T LOAD PLEASE MAKE SURE YOUR INDIVIDUAL API KEY IS CORRECTLY CONFIGURED  ||
|| AND UPDATED WITH NEW PACKAGE NAME AND YOUR SHA-1 FINGERPRINT                          ||
|| https://console.cloud.google.com/apis/credentials                                     ||
|| API CONFIG SHOULD LOOK LIKE THIS: https://imgur.com/a/DyQGiz5                         ||
|| ALSO SEE:                                                                             ||
|| https://developers.google.com/maps/documentation/android-sdk/start                    ||
|| https://developers.google.com/maps/documentation/android-sdk/get-api-key              ||
|| REMEMBER TO UPDATE YOUR secrets.properties FILE WITH YOUR API KEY                     ||
___________________________________________________________________________________________
___________________________________________________________________________________________
*/

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1; // Added constant for location permission request code


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            mMap = googleMap;

            LatLng orlando = new LatLng(28.5383, -81.3792); // Orlando, FL Map
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(orlando, 10)); // adjust default map zoom level

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
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.recycle_bin_1);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, scaledSize, scaledSize, false);

            // Define the recycling center locations
            LatLng[] locations = {
                    new LatLng(28.52551, -81.32586), // Dover Shore Community Center
                    new LatLng(28.52894, -81.30075), // Engelwood Neighborhood Center
                    new LatLng(28.56112, -81.42777), // Northwest Community Center
                    new LatLng(28.52933, -81.39580)  // Solid Waste Management Division
            };

            String[] addresses = {
                    "1400 Gaston Foster Road",
                    "6123 Lacosta Drive",
                    "3955 WD Judge Road",
                    "1028 South Woods Avenue"
            };

            // Add a marker for each location
            for (int i = 0; i < locations.length; i++) {
                LatLng location = locations[i];
                String address = addresses[i];
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(address) // Set the title of the marker to the address
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            }

            // Set an InfoWindow click listener
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {
                    // When the InfoWindow is clicked, start an intent to open Google Maps for navigation
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(marker.getTitle()));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
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
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true); // Enable location layer if permission is granted
                }
            } else {
                // Permission was denied. We can display an error message here.
            }
        }
    }
}