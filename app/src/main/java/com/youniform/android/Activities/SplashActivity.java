package com.youniform.android.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Model.ProfileModel.GetProfileModel;
import com.youniform.android.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private PrefManager prefManager;
    private APIInterface apiInterface;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupclients();
        if (isSMSPermission()) {
            Handler();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 1);

        }


    }


    public boolean isSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                return true;
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private void setupclients() {
        prefManager = new PrefManager(SplashActivity.this);
        Utils.LOGIN_ID = prefManager.getInt("LOGIN_ID");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(SplashActivity.this);
    }

    private void getUserData() {
        Utils.showDialog(progressDialog);
        Call<List<GetProfileModel>> call1 = apiInterface.getProfile(Utils.LOGIN_ID);
        call1.enqueue(new Callback<List<GetProfileModel>>() {
            @Override
            public void onResponse(Call<List<GetProfileModel>> call, Response<List<GetProfileModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        prefManager.setString("FIRST_NAME", response.body().get(0).getFirstName());
                        prefManager.setString("LAST_NAME", response.body().get(0).getLastName());
                        prefManager.setString("EMAIL", response.body().get(0).getEmail());
                        prefManager.setString("PROFILE_IMAGE", response.body().get(0).getAvatarUrl());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetProfileModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), SplashActivity.this);
            }
        });
    }

    private void Handler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.LOGIN_ID != 0) {
                    getUserData();
                }
                Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(mainIntent);
            }
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Handler();
            Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Please Grant Permission for Further Proceed", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            Log.v("TAG", "DENIED: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }
}





