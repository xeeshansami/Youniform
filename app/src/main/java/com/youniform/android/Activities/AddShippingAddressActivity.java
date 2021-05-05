package com.youniform.android.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.youniform.android.Adapters.ShippingAddresAdapter;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Model.ShiipingAddress.GetShippingAddressModel;
import com.youniform.android.Model.ShiipingAddress.Shippingaddress;
import com.youniform.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShippingAddressActivity extends AppCompatActivity implements ItemClick {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_noShipping)
    TextView tv_noShipping;
    @BindView(R.id.add_newAddress)
    Button add_newAddress;

    private ShippingAddresAdapter shippingAddresAdapter;
    private List<Shippingaddress> shippingaddressList = new ArrayList<>();

    private APIInterface apiInterface;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipping_address);
        setupClients();
        setAdapter();
        getAddress();
    }

    private void getAddress() {
        Utils.showDialog(progressDialog);
        Call<GetShippingAddressModel> call1 = apiInterface.getshipping(Utils.LOGIN_ID);
        call1.enqueue(new Callback<GetShippingAddressModel>() {
            @Override
            public void onResponse(Call<GetShippingAddressModel> call, Response<GetShippingAddressModel> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().getShippingaddress() != null) {
                        shippingaddressList.addAll(response.body().getShippingaddress());
                        shippingAddresAdapter.notifyDataSetChanged();
                    } else {
                        tv_noShipping.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetShippingAddressModel> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), AddShippingAddressActivity.this);
            }
        });
    }

    private void setAdapter() {
        if (getIntent().getBooleanExtra("GET_ADDRESS", false)) {
            add_newAddress.setVisibility(View.GONE);
            shippingAddresAdapter = new ShippingAddresAdapter(shippingaddressList, this, "SHIPPING", this);
        } else if ((getIntent().getBooleanExtra("GET_ADDRESS", false)) && (getIntent().getBooleanExtra("GET_Billing", false))) {
            add_newAddress.setVisibility(View.GONE);
            shippingAddresAdapter = new ShippingAddresAdapter(shippingaddressList, this, "BILLING", this);
        } else {
            shippingAddresAdapter = new ShippingAddresAdapter(shippingaddressList, this, "ACCOUNT");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(shippingAddresAdapter);
    }

    private void setupClients() {
        ButterKnife.bind(this);
        apiInterface = APIClient.thirdUrlClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(AddShippingAddressActivity.this);
    }


    @OnClick(R.id.add_newAddress)
    void newAddress() {
        if (isLocation()) {
            Intent intent = new Intent(AddShippingAddressActivity.this, NewAddressActivity.class);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(AddShippingAddressActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    public boolean isLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(AddShippingAddressActivity.this, NewAddressActivity.class);
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
    }

    @OnClick(R.id.btn_back)
    void back() {

        if (getIntent().getBooleanExtra("GET_ADDRESS", false) || getIntent().getBooleanExtra("GET_Billing", false)) {
            finish();
            Intent intent = new Intent(AddShippingAddressActivity.this, CheckOutTabActivity.class);
            startActivity(intent);
        } else {

            Intent intent = new Intent(AddShippingAddressActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if ((getIntent().getBooleanExtra("GET_ADDRESS", false)) || (getIntent().getBooleanExtra("GET_Billing", false))) {
            finish();
            Intent intent = new Intent(AddShippingAddressActivity.this, CheckOutTabActivity.class);

            startActivity(intent);
        } else {
            Intent intent = new Intent(AddShippingAddressActivity.this, HomeActivity.class);
            intent.putExtra("UPDATePROFILE", true);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    @Override
    public void itemClick(int position) {
        if ((getIntent().getBooleanExtra("GET_ADDRESS", false)) && (getIntent().getBooleanExtra("GET_Billing", false))) {
            Shippingaddress shippingaddress = shippingaddressList.get(position);
            Intent intent = new Intent("shippingaddress");
            String jsonInString = new Gson().toJson(shippingaddress);
            try {

                JSONObject object = new JSONObject(jsonInString);
                intent.putExtra("address1", object.getString("address_1"));
                intent.putExtra("address2", object.getString("address_2"));
                intent.putExtra("city", object.getString("city"));
                intent.putExtra("postcode", object.getString("postcode"));
                intent.putExtra("state", object.getString("state"));
                intent.putExtra("firstname", object.getString("first_name"));
                intent.putExtra("lastname", object.getString("last_name"));
                intent.putExtra("country", object.getString("country"));
                Log.d("BLACK", "itemClick: " + object.getString("country"));
                setResult(Activity.RESULT_OK, intent);
                finish();//finishing activity

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Shippingaddress shippingaddress = shippingaddressList.get(position);
            Intent intent = new Intent("shippingaddress");
            String jsonInString = new Gson().toJson(shippingaddress);

            intent.putExtra("ADDRESS", jsonInString);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            onBackPressed();
            Utils.showToastMessage(shippingaddress.getFirstName(), getApplicationContext());


        }
    }

    @Override
    public void itemClick2(int position, int id) {

    }


}