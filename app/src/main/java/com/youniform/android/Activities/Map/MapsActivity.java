package com.youniform.android.Activities.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.youniform.android.Activities.SearchActivity;
import com.youniform.android.BaseClasses.LatLngInterpolator;
import com.youniform.android.BaseClasses.MarkerAnimation;
import com.youniform.android.Model.MakeBookings.LocationModel;
import com.youniform.android.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener {
    private static final String TAG = "MapsActivity";
    private static final int REQUEST_PICK_UP = 1;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    protected LocationRequest mlocationRequest;
    PlacesClient placesClient;
    LinearLayout startllc;
    TextView title;
    TextView description;
    int REQUEST_CHECK_SETTINGS = 100;
    LatLng latLng;
    String getPlaceAddress;
    Marker marker;
    int time = 1;
    private GoogleMap mMap;
    private double longitude, latitude, lat1, long1;
    private GoogleApiClient googleApiClient;
    private Location lastKnownLocation;
    private Button btnSetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast.makeText(getApplicationContext(), "Press and Hold to Set Pin Location", Toast.LENGTH_LONG).show();
        //Initializing googleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        btnSetLocation = findViewById(R.id.setlocation);
        startllc = findViewById(R.id.llStart);
        title = findViewById(R.id.tvTitle);
        description = findViewById(R.id.tvDescription);
        //LinearLayout LL = findViewById(R.id.LL2);
        Boolean bol = getIntent().getBooleanExtra("check", true);
        if (bol) {
            String apiKey = "AIzaSyD_pPOU0msOC8ToW0qKwEMOsvc091yGEc4";
            //  LL.setVisibility(View.VISIBLE);
            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), apiKey);
            }

        }

        startllc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startllc.setBackgroundColor(ContextCompat.getColor(MapsActivity.this, R.color.colorGrayLight));
                startActivityForResult(new Intent(MapsActivity.this, SearchActivity.class), REQUEST_PICK_UP);
            }
        });
        /*else {
            LL.setVisibility(View.GONE);
        }*/
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // googleMapOptions.mapType(googleMap.MAP_TYPE_HYBRID)
        //    .compassEnabled(true);

        // Add a marker in Sydney and move the camera

        LatLng mylocation = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(mylocation).title("Marker in Pakistan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
        mMap.setOnMarkerDragListener(this);

        mMap.setOnMapLongClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //moving the map to location
            moveMap();
        }
    }

    private void moveMap() {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        latLng = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        //    mMap.getUiSettings().setZoomControlsEnabled(true);


    }

    @Override
    public void onClick(View view) {
        Log.v(TAG, "view click event");

        if (view == btnSetLocation) {
            if ((lat1 != 0) && (long1 != 0)) {
                Intent returnIntent = new Intent();
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat1, long1, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                Log.d(TAG, "onClick: " + address + "\n" + city + "\n" + state + "\n" + country + "\n" + postalCode + "\n" + knownName);

                returnIntent.putExtra("address", address);
                returnIntent.putExtra("city", city);
                returnIntent.putExtra("state", state);
                returnIntent.putExtra("country", country);
                returnIntent.putExtra("postalCode", postalCode);
                returnIntent.putExtra("knownName", knownName);

                if (country.equals("Pakistan")) {
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Service Not Available", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(getApplicationContext(), "Select Location", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(3 * 1000);
        mlocationRequest.setFastestInterval(1000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mlocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (time == 1) {
                            updateLastLocation(true);
                            time++;
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        // mMap.clear();
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location").draggable(true));
        lat1 = latLng.latitude;
        long1 = latLng.longitude;
        btnSetLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(MapsActivity.this, "onMarkerDragStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Toast.makeText(MapsActivity.this, "onMarkerDrag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // getting the Co-ordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //move to current position
        moveMap();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(MapsActivity.this, "onMarkerClick", Toast.LENGTH_SHORT).show();


        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // setupGoogleApiClient();
                            }
                        }, 3000);
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_UP:
                    if (data.hasExtra("place")) {
                        LocationModel locationModel = (LocationModel) data.getSerializableExtra("place");
                        title.setText("" + locationModel.getPlaceName());
                        description.setText("" + locationModel.getAddress());
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationModel.getLat(), locationModel.getLng()), 15f));
//                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));

                        mMap.clear();

                        lat1 = locationModel.getLat();
                        long1 = locationModel.getLng();
                        latLng = new LatLng(lat1, long1);
                        LatLng latLng_ = new LatLng(lat1, long1);
                        marker = mMap.addMarker(new MarkerOptions().position(latLng_)
                                .anchor(0.5f, 0.5f).draggable(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng_)
                                .zoom(14.0f).build()));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        marker.setPosition(latLng);
                                        MarkerAnimation.animateMarkerToGB(marker, latLng, new LatLngInterpolator.Spherical());

                                    }
                                });

                            }
                        }, 100);
                        btnSetLocation.setVisibility(View.VISIBLE);
                    }
                    break;

            }
        }
    }

    private void updateLastLocation(boolean move) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mMap != null) {
            //  Utils.e("map", "mapp  NOT null");
        } else {
            //  Utils.e("map", "mapp null");
        }
        if (mMap != null) {
            mMap.setMyLocationEnabled(false);
        }
        if (lastKnownLocation != null) {
            if (move) {
                if (mMap != null) {
                    //    Utils.e("map sec", "mapp  NOT null");
                } else {
                    //   Utils.e("map sec", "mapp null");
                }
                if (mMap != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15f));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));

                    Intent returnIntent = new Intent();
                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String address = addresses.get(0).getAddressLine(0);
                    title.setText(address);
                    description.setText(address);
                    LatLng _latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    marker = mMap.addMarker(new MarkerOptions().position(_latLng)
                            .anchor(0.5f, 0.5f).draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(_latLng)
                            .zoom(14.0f).build()));

                }
            }
        } else {
            //  Utils.e("location", "location null");
        }
    }

}