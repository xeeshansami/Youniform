package com.youniform.android.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.youniform.android.Activities.Map.MapsActivity;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Model.ResponseModel;
import com.youniform.android.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAddressActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    TextInputEditText et_firstName;
    @BindView(R.id.et_lastName)
    TextInputEditText et_lastName;
    @BindView(R.id.et_address1)
    TextInputEditText et_address1;
    @BindView(R.id.et_city)
    TextInputEditText et_city;
    @BindView(R.id.et_postalcode)
    TextInputEditText et_postalcode;
    @BindView(R.id.et_state)
    TextInputEditText et_state;
    @BindView(R.id.et_country)
    TextInputEditText et_country;
    @BindView(R.id.tv_getPin)
    TextView tv_getPin;
    Spinner places;
    String strAddress2;
    LocationManager nManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    private APIInterface apiInterface;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        setupClients();
        places = findViewById(R.id.places);
        String apiKey = "AIzaSyBnySnbgwp1r3ZQOtKMl8nig3tPwR9j4To";
        nManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.d("TAG", "onCreate: " + new PrefManager(this).getString("FIRST_NAME"));
        et_firstName.setText(new PrefManager(this).getString("FIRST_NAME"));
        et_lastName.setText(new PrefManager(this).getString("LAST_NAME"));

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
        Location locationGPS = nManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
   /*     if (locationGPS != null) {
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();


            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {

                e.printStackTrace();
            }
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.d("TAG", "onClick: " + address + "\n" + city + "\n" + state + "\n" + country + "\n" + postalCode + "\n" + knownName);
            if (country.equals("Pakistan")) {
                tv_getPin.setText(address);
                et_postalcode.setText(postalCode);
                et_country.setText(country);
                et_state.setText(state);
                et_city.setText(city);
            } else {
                Toast.makeText(getApplicationContext(), "Service Not Available in your Region", Toast.LENGTH_SHORT).show();
            }


*/
        tv_getPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewAddressActivity.this, MapsActivity.class);
                i.putExtra("check", true);
                startActivityForResult(i, 1);
                // startActivity(intent);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    location.getLongitude();
                    Log.d("TAG", "onComplete: " + location.getLongitude() + "\n" + location.getLatitude());

                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(NewAddressActivity.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    Log.d("TAG", "onClick: " + address + "\n" + city + "\n" + state + "\n" + country + "\n" + postalCode + "\n" + knownName);
                    if (country.equals("Pakistan")) {
                        tv_getPin.setText(address);
                        et_postalcode.setText(postalCode);
                        et_country.setText(country);
                        et_state.setText(state);
                        et_city.setText(city);

                    } else {
                        Toast.makeText(getApplicationContext(), "Service Not Available in your Region", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


    private void setupClients() {
        ButterKnife.bind(this);
        apiInterface = APIClient.thirdUrlClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(NewAddressActivity.this);
    }

    @OnClick(R.id.btn_back)
    void backClick() {
        onBackPressed();
    }

    @OnClick(R.id.btn_save)
    void save() {
        String strFirtName = et_firstName.getText().toString();
        String strLastName = et_lastName.getText().toString();
        String strAddress1 = et_address1.getText().toString();
        //   String strAddress2 = et_address2.getText().toString();
        String strCity = et_city.getText().toString();
        String strState = et_state.getText().toString();
        String strPostalCode = et_postalcode.getText().toString();
        String strCountry = et_country.getText().toString();
        if (strFirtName.isEmpty() && strLastName.isEmpty() && strAddress1.isEmpty()
                && strCity.isEmpty() && strState.isEmpty() && strCountry.isEmpty()) {
            Utils.showToastMessage("All Feilds Are Required", NewAddressActivity.this);
        } else {
            if (strPostalCode.isEmpty()) {
                if (strAddress1.isEmpty() || strCity.isEmpty() || strState.isEmpty() || strCountry.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (et_postalcode.getText().toString().isEmpty()) {
                        Utils.showDialog(progressDialog);
                        strPostalCode = "0";
                        Call<ResponseModel> call1 = apiInterface.addshipping(Utils.LOGIN_ID, strFirtName, strLastName, strAddress1, tv_getPin.getText().toString(),
                                strCity, strState, Long.valueOf(strPostalCode), strCountry, places.getSelectedItem().toString());
                        call1.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                Utils.dismissDialog(progressDialog);
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        if (response.body().getStatus() == 200) {
                                            startActivity(new Intent(NewAddressActivity.this, AddShippingAddressActivity.class));
                                        }
                                        Utils.showToastMessage(response.body().getMessage(), NewAddressActivity.this);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Utils.dismissDialog(progressDialog);
                                startActivity(new Intent(NewAddressActivity.this, AddShippingAddressActivity.class));
                                // Utils.showToastMessage(t.getLocalizedMessage(), NewAddressActivity.this);
                            }
                        });
                    }
                }
            }

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                String address = data.getStringExtra("address");
                String city = data.getStringExtra("city");
                String state = data.getStringExtra("state");
                String country = data.getStringExtra("country");
                String postalCode = data.getStringExtra("postalCode");
                String knownName = data.getStringExtra("knownName");
                tv_getPin.setText(address);
                et_city.setText(city);
                et_state.setText(state);
                et_country.setText(country);
                et_postalcode.setText(postalCode);
                tv_getPin.setText(address);

                // strAddress2 = data.getStringExtra("result");
            }
        }
       /* if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                tv_getPin.setText(result);
                strAddress2 = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }*/
    }//onActivityResult


}