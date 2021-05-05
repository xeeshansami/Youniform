package com.youniform.android.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.youniform.android.Activities.Map.MapsActivity;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Model.CartModel;
import com.youniform.android.Model.LineItem;
import com.youniform.android.Model.PlaceOrder;
import com.youniform.android.Model.ProfileModel.Shipping;
import com.youniform.android.Model.ShiipingAddress.Shippingaddress;
import com.youniform.android.Model.ShippingLine;
import com.youniform.android.Model.UserModel.Billing;
import com.youniform.android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutTabActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_PLACE = 1000;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    @BindView(R.id.et_billingfName)
    TextInputEditText et_billingfName;
    @BindView(R.id.et_billinglastName)
    TextInputEditText et_billinglastName;
    @BindView(R.id.et_billingmail)
    TextInputEditText et_billingmail;
    @BindView(R.id.textInputLayout10)
    TextInputLayout et_billingnumber;
    @BindView(R.id.et_billingaddress1)
    TextInputEditText et_billingaddress1;
    /*  @BindView(R.id.et_billingaddress2)
      TextInputEditText et_billingaddress2;
      */
    /*   @BindView(R.id.et_billingcity)
    Spinner et_billingcity;*/
    @BindView(R.id.et_billingcity)
    TextInputEditText et_billingcity;
    @BindView(R.id.et_billingpostalcode)
    TextInputEditText et_billingpostalcode;
    /*@BindView(R.id.et_billingstate)
    Spinner et_billingstate;
    @BindView(R.id.et_billingcountry)
    Spinner et_billingcountry;
    */
    @BindView(R.id.et_billingstate)
    TextInputEditText et_billingstate;
    @BindView(R.id.et_billingcountry)
    TextInputEditText et_billingcountry;
    @BindView(R.id.et_name)
    TextInputEditText et_firstName;
    @BindView(R.id.et_lastName)
    TextInputEditText et_lastName;
    @BindView(R.id.et_address1)
    TextInputEditText et_address1;
    @BindView(R.id.et_address24)
    TextView et_address2;
    @BindView(R.id.et_city)
    TextInputEditText et_city;
    @BindView(R.id.et_postalcode)
    TextInputEditText et_postalcode;
    @BindView(R.id.et_state)
    TextInputEditText et_state;
    @BindView(R.id.et_country)
    TextInputEditText et_country;
    @BindView(R.id.sv_billing)
    ScrollView sv_billing;
    @BindView(R.id.sv_shipping)
    ScrollView sv_shipping;
    @BindView(R.id.state_progress)
    StateProgressBar state_progress;
    TextView tv_getPin, Tv_getPin2;
    String strAddress21;
    PlacesClient placesClient;
    CheckBox checkBox, fastDelivery, standardDelivery;
    String[] descriptionData = {"Billing Address", "Shipping Address"};
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
    String billingfName, billinglastName, billingmail, billingnumber, billingaddress1, billingaddress2, billingcity, billingpostalcode, billingstate, billingcountry;
    String strAddress2, deliveryType = "";
    int deliveryAmount;
    LocationManager nManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    BroadcastReceiver shippingaddress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Shippingaddress shippingaddress = new Gson().fromJson(intent.getStringExtra("ADDRESS"), Shippingaddress.class);
            et_firstName.setText(shippingaddress.getFirstName());
            et_lastName.setText(shippingaddress.getLastName());
            et_address1.setText(shippingaddress.getAddress1());
            et_address2.setText(shippingaddress.getAddress2());
            et_city.setText(shippingaddress.getCity());
            et_postalcode.setText(shippingaddress.getPostcode());
            et_state.setText(shippingaddress.getState());
            et_country.setText(shippingaddress.getCountry());
        }
    };
    String address;
    //    private Button btn_billing_add_address;
    private Billing billing;
    private APIInterface apiInterface;
    private Dialog progressDialog;
    private PrefManager prefManager;
    private List<CartModel> cartModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_tab);
        setupClients();
        checkBox = findViewById(R.id.cb_billing);
        tv_getPin = findViewById(R.id.tv_getPin1);
        TextWatcher();
        //     String apiKey = "AIzaSyBnySnbgwp1r3ZQOtKMl8nig3tPwR9j4To";
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
        standardDelivery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (standardDelivery.isChecked()) {
                fastDelivery.setChecked(false);
                deliveryType = "RS: 200 Delivery";
                deliveryAmount = 200;
            }
        });
        fastDelivery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (fastDelivery.isChecked()) {
                standardDelivery.setChecked(false);
                deliveryType = "RS: 2000 for Fast Delivery";
                deliveryAmount = 2000;
            }
        });
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    location.getLongitude();
                    Log.d("TAG", "onComplete: " + location.getLongitude() + "\n" + location.getLatitude());

                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(CheckOutTabActivity.this, Locale.getDefault());
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
                        et_billingcity.setText(city);
                        et_billingstate.setText(state);
                        et_billingcountry.setText(country);
                        et_billingpostalcode.setText(postalCode);

                        et_address2.setText(address);

                    } else {
                        Toast.makeText(getApplicationContext(), "Service Not Available in your Region", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
  /*      Location locationGPS = nManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();



        }
*/
        tv_getPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CheckOutTabActivity.this, MapsActivity.class);
                startActivityForResult(i, 1);
                // startActivity(intent);
            }
        });

        et_firstName.setText(new PrefManager(this).getString("FIRST_NAME"));
        et_lastName.setText(new PrefManager(this).getString("LAST_NAME"));


        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                et_firstName.setText(billingfName);
                et_lastName.setText(billinglastName);
                et_address1.setText(billingaddress1);
                et_address2.setText(tv_getPin.getText().toString());
                et_city.setText(billingcity);
                //et_state.setText(et_billingstate.getSelectedItem().toString());
                et_state.setText(Objects.requireNonNull(et_billingstate.getText()).toString());

                et_postalcode.setText(billingpostalcode);
                //et_country.setText(et_billingcountry.getSelectedItem().toString());
                et_country.setText(Objects.requireNonNull(et_billingcountry.getText()).toString());

            } else {
                et_firstName.setText("");
                et_lastName.setText("");
                et_address1.setText("");
                   /* et_address2.setText("");
                    et_city.setText("");
                    et_state.setText("");
                    et_postalcode.setText("");
                    et_country.setText("");
*/
            }

        });


        et_address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CheckOutTabActivity.this, MapsActivity.class);
                startActivityForResult(i, 2);
            }
        });


//        btn_billing_add_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Utils.LOGIN_ID == 0) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckOutTabActivity.this);
//                    builder.setMessage("Login to Proceed")
//                            .setCancelable(false)
//                            .setPositiveButton("Go to Login", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    Intent intent = new Intent(CheckOutTabActivity.this, LoginActivity.class);
//                                    intent.putExtra("check", true);
//                                    startActivity(intent);
//                                }
//                            })
//                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                } else {
//                    if (isLocation()) {
//                        Intent intent = new Intent(CheckOutTabActivity.this, AddShippingAddressActivity.class);
//                        intent.putExtra("GET_ADDRESS", true);
//                        intent.putExtra("GET_Billing", true);
//                        startActivityForResult(intent, 5);
//                    } else {
//                        ActivityCompat.requestPermissions(CheckOutTabActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//                    }
//
//                }
//
//
//            }
//        });

    }

    private void setupClients() {
        ButterKnife.bind(this);
        fastDelivery = findViewById(R.id.fastDelivery);
        standardDelivery = findViewById(R.id.standardDelivery);
        /*

        @OnClick(R.id.tv_openCityPicker)
         void openCityPicker() {
    //        try {
    //            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
    //                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
    //                    .build();
    //            Intent intent = new PlaceAutocomplete.IntentBuilder
    //                    (PlaceAutocomplete.MODE_FULLSCREEN)
    //                    .setFilter(typeFilter)
    //                    .build(CheckOutTabActivity.this);
    //            startActivityForResult(intent, REQUEST_SELECT_PLACE);
    //        } catch (GooglePlayServicesRepairableException |
    //                GooglePlayServicesNotAvailableException e) {
    //            e.printStackTrace();
    //        }
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        }
    */
        CartDataBase helper = new CartDataBase(this);
        state_progress.setStateDescriptionData(descriptionData);
        prefManager = new PrefManager(CheckOutTabActivity.this);
        cartModelList = helper.getData();
        apiInterface = APIClient.thirdUrlClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(CheckOutTabActivity.this);
//        btn_billing_add_address = findViewById(R.id.billing_add_address);
        LocalBroadcastManager.getInstance(this).registerReceiver(shippingaddress, new IntentFilter("shippingaddress"));
        Places.initialize(getApplicationContext(), "AIzaSyDDPHT6uQjWmRRs0OqqGKmeSnxwW35xlus");
        setData();
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);
    }

    private void setData() {
        et_billingfName.setText(prefManager.getString("FIRST_NAME"));
        et_firstName.setText(prefManager.getString("FIRST_NAME"));
        et_billinglastName.setText(prefManager.getString("LAST_NAME"));
        et_lastName.setText(prefManager.getString("LAST_NAME"));
        et_billingmail.setText(prefManager.getString("EMAIL"));
        et_billingnumber.getEditText().setText(prefManager.getString("BNUMBER"));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(shippingaddress);
        super.onDestroy();
    }

    @OnClick(R.id.btn_billingsave)
    void billingSave() {
        billingfName = et_billingfName.getText().toString();
        billinglastName = et_billinglastName.getText().toString();
        billingmail = et_billingmail.getText().toString();
        billingnumber = et_billingnumber.getEditText().getText().toString();
        billingaddress1 = et_billingaddress1.getText().toString();
        billingaddress2 = strAddress21;
        //billingcity = et_billingcity.getSelectedItem().toString();
        billingcity = et_billingcity.getText().toString();
        billingpostalcode = et_billingpostalcode.getText().toString();
        /*     billingstate = et_billingstate.getSelectedItem().toString();
               billingcountry = et_billingcountry.getSelectedItem().toString();
        */
        billingstate = et_billingstate.getText().toString();
        billingcountry = et_billingcountry.getText().toString();

        if (billingfName.isEmpty() || billinglastName.isEmpty() || billingmail.isEmpty() || billingnumber.isEmpty() || billingaddress1.isEmpty()
                || billingcity.isEmpty() || billingstate.isEmpty() || billingcountry.isEmpty()) {
            Utils.showToastMessage("Kindly provide complete information", CheckOutTabActivity.this);
        } else if (!Utils.isValidEmail(billingmail)) {
            Utils.showToastMessage("Email is not valid", CheckOutTabActivity.this);
        } else {
            billing = new Billing(billingfName, billinglastName, billingaddress1, billingaddress2, billingcity, billingpostalcode, billingcountry,
                    billingstate, billingmail, billingnumber);
            state_progress.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            sv_shipping.setVisibility(View.VISIBLE);
            sv_billing.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_save)
    void save() {
        String strFirtName = et_firstName.getText().toString();
        String strLastName = et_lastName.getText().toString();
        String strAddress1 = et_address1.getText().toString();
        strAddress2 = et_address2.getText().toString();
        String strCity = et_city.getText().toString();
        String strState = et_state.getText().toString();
        String strPostalCode = et_postalcode.getText().toString();
        String strCountry = et_country.getText().toString();
        if (strFirtName.isEmpty() || strLastName.isEmpty() || strAddress1.isEmpty()
                || strCity.isEmpty() || strState.isEmpty() || strCountry.isEmpty() || deliveryType.equals("")) {
            Utils.showToastMessage("All Fields Are Required", CheckOutTabActivity.this);
        } else if (Utils.LOGIN_ID == 0) {
            loginAlert();
        } else {
            state_progress.setAllStatesCompleted(true);
            Shipping shipping = new Shipping(strFirtName, strLastName, strAddress1, strAddress2, strCity, strPostalCode, strCountry, strState);
            List<LineItem> lineItemslist = new ArrayList<>();
            for (CartModel cartModel : cartModelList) {
                lineItemslist.add(new LineItem(cartModel.getProducdid(), cartModel.getQuantity(), 0));
            }
            List<ShippingLine> shippingLines = new ArrayList<>();
            shippingLines.add(new ShippingLine("flat_rate", "Flat Rate", ""));

            PlaceOrder placeOrder = new PlaceOrder(Utils.LOGIN_ID,
                    "cod",
                    "Cash On Delivery",
                    false,
                    billing,
                    shipping,
                    lineItemslist, shippingLines);
            Utils.showDialog(progressDialog);
            String jsonInString = new Gson().toJson(placeOrder);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString);
            Call<ResponseBody> call1 = apiInterface.placeOrder(body);
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Utils.dismissDialog(progressDialog);
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Utils.showToastMessage("Order Placed", CheckOutTabActivity.this);

                            Intent intent = new Intent(CheckOutTabActivity.this, BillAfterCheckoutActivity.class);
                            intent.putExtra("Courier", deliveryType);
                            intent.putExtra("CourierPrice", deliveryAmount);
                            startActivity(intent);
                            //       helper.deleteAll();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Utils.dismissDialog(progressDialog);
                    Utils.showToastMessage(t.getLocalizedMessage(), CheckOutTabActivity.this);
                }
            });
        }
    }

    @OnClick(R.id.btn_shippingBack)
    void shippingBack() {
        sv_billing.setVisibility(View.VISIBLE);
        sv_shipping.setVisibility(View.GONE);
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(CheckOutTabActivity.this, AddShippingAddressActivity.class);
            intent.putExtra("GET_ADDRESS", true);
            startActivity(intent);
            Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Please Grant Permission for Further Proceed", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            Log.v("TAG", "DENIED: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }*/

    @OnClick(R.id.ib_back)
    void back() {
        onBackPressed();
    }

//    @OnClick(R.id.add_address)
//    void addAddress() {
//        if (Utils.LOGIN_ID == 0) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Login to Proceed")
//                    .setCancelable(false)
//                    .setPositiveButton("Go to Login", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Intent intent = new Intent(CheckOutTabActivity.this, LoginActivity.class);
//                            intent.putExtra("check", true);
//                            startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.show();
//        } else {
//            if (isLocation()) {
//                Intent intent = new Intent(CheckOutTabActivity.this, AddShippingAddressActivity.class);
//                intent.putExtra("GET_ADDRESS", true);
//                startActivity(intent);
//            } else {
//                ActivityCompat.requestPermissions(CheckOutTabActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            }
//
//        }
//
//    }

    public boolean isLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                // Intent i = new Intent(CheckOutTabActivity.this, MapsActivity.class);
                //  startActivityForResult(i, 7);
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 7);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private void loginAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Login to checkout")
                .setCancelable(false)
                .setPositiveButton("Go to Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(CheckOutTabActivity.this, LoginActivity.class);
                        intent.putExtra("check", true);
                        startActivityForResult(intent, 1);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
/*        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }*/
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                String address = data.getStringExtra("address");
                String city = data.getStringExtra("city");
                String state = data.getStringExtra("state");
                String country = data.getStringExtra("country");
                String postalCode = data.getStringExtra("postalCode");
                String knownName = data.getStringExtra("knownName");
                tv_getPin.setText(knownName);
                et_billingcity.setText(city);
                et_billingstate.setText(state);
                et_billingcountry.setText(country);
                et_billingpostalcode.setText(postalCode);
                tv_getPin.setText(knownName + " " + address);

                // strAddress2 = data.getStringExtra("result");
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                String address = data.getStringExtra("address");
                String city = data.getStringExtra("city");
                String state = data.getStringExtra("state");
                String country = data.getStringExtra("country");
                String postalCode = data.getStringExtra("postalCode");
                String knownName = data.getStringExtra("knownName");


                et_address2.setText(address);

                et_city.setText(city);
                //   et_state.setText(et_billingstate.getSelectedItem().toString());
                et_state.setText(state);

                et_postalcode.setText(postalCode);
                //                    et_country.setText(et_billingcountry.getSelectedItem().toString());
                et_country.setText(country);

                // strAddress2 = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 5) {

            try {
                address = data.getStringExtra("address1");

            } catch (Exception ex) {
                address = "";
            }
            String address2 = data.getStringExtra("address2");
            String city = data.getStringExtra("city");
            String state = data.getStringExtra("state");
            String country = data.getStringExtra("country");
            String postalCode = data.getStringExtra("postcode");
            String knownName = data.getStringExtra("knownName");
            String firstname = data.getStringExtra("firstname");
            String lastname = data.getStringExtra("lastname");
            tv_getPin.setText(address2);
            et_billingcity.setText(city);
            et_billingstate.setText(state);
            et_billingcountry.setText(country);
            et_billingpostalcode.setText(postalCode);
            et_billingfName.setText(firstname);
            et_billinglastName.setText(lastname);
            et_billingaddress1.setText(address);
            Log.d("BLACK2", "itemClick: " + data.getStringExtra("country"));

            // strAddress2 = data.getStringExtra("result");
            if (resultCode == Activity.RESULT_CANCELED) {
                //do nothing
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void TextWatcher() {
        int maxLength = 13;
        et_billingnumber.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        et_billingnumber.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        et_billingnumber.setHint("Phone");

        et_billingnumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable text) {
                if (text.length() == 1) {
                    text.append("+92");
                    int charIndex = 0;
                    String texts = et_billingnumber.getEditText().getText().toString();
                    texts = texts.substring(0, charIndex) + texts.substring(charIndex + 1);
                    et_billingnumber.getEditText().setText(texts);
                    et_billingnumber.getEditText().setSelection(3);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (sv_billing.getVisibility() != View.VISIBLE || sv_shipping.getVisibility() == View.VISIBLE) {
            sv_billing.setVisibility(View.VISIBLE);
            sv_shipping.setVisibility(View.GONE);

            // Either gone or invisible
        } else {
            // Its visible
            finish();
        }

        super.onBackPressed();
    }
}//onActivityResult


//}