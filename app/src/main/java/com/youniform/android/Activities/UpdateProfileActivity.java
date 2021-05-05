package com.youniform.android.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Model.ProfileModel.GetProfileModel;
import com.youniform.android.Model.UpdateImageModel;
import com.youniform.android.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {


    @BindView(R.id.iv_user)
    CircleImageView profileImage;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_lastName)
    EditText et_lastName;
    @BindView(R.id.et_email)
    EditText et_email;


    private PrefManager prefManager;
    private APIInterface apiInterface;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        setupClients();
    }

    private void setupClients() {
        ButterKnife.bind(this);
        prefManager = new PrefManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDialog = Utils.progressDialog(UpdateProfileActivity.this);
        Glide.with(this).load(prefManager.getString("PROFILE_IMAGE")).placeholder(R.drawable.account).into(profileImage);
        String fname = prefManager.getString("FIRST_NAME");
        String lname = prefManager.getString("LAST_NAME");
        String uname = prefManager.getString("USER_NAME");
        if (fname.isEmpty() || lname.isEmpty()) {
            et_name.setText(uname);
        } else {
            et_name.setText(fname);
            et_lastName.setText(lname);
        }
        et_email.setText(prefManager.getString("EMAIL"));
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @OnClick(R.id.btn_back)
    void backPress() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(UpdateProfileActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_update_profile)
    void updateProfile() {
        String fname = et_name.getText().toString();
        String lname = et_lastName.getText().toString();
        if (fname.isEmpty()) {
            Utils.showToastMessage("Please Enter First Name", UpdateProfileActivity.this);
        } else if (lname.isEmpty()) {
            Utils.showToastMessage("Please Enter Last Name", UpdateProfileActivity.this);
        } else {
            Utils.showDialog(progressDialog);
            Call<GetProfileModel> call1 = apiInterface.updateProfile(fname, lname, Utils.LOGIN_ID);
            call1.enqueue(new Callback<GetProfileModel>() {
                @Override
                public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                    Utils.dismissDialog(progressDialog);
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            prefManager.setString("FIRST_NAME", response.body().getFirstName());
                            prefManager.setString("LAST_NAME", response.body().getLastName());
                            prefManager.setString("EMAIL", response.body().getEmail());
                            prefManager.setString("PROFILE_IMAGE", response.body().getAvatarUrl());
                            Intent intent = new Intent(UpdateProfileActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileModel> call, Throwable t) {
                    Utils.dismissDialog(progressDialog);
                    Utils.showToastMessage(t.getLocalizedMessage(), UpdateProfileActivity.this);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Location Allowed", "onRequestPermissionsResult");
        boolean permissionGranted = false;
        switch (requestCode) {
            case 9:
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                permissionGranted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (permissionGranted) {
            Pix.start(UpdateProfileActivity.this, Options.init().setRequestCode(100));


        } else {
            Utils.showToastMessage("You don't assign permission.Assign Permission Manually", UpdateProfileActivity.this);
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            Pix.start(UpdateProfileActivity.this, Options.init().setRequestCode(100));
        } else {
            if (ActivityCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            ) {
                Pix.start(UpdateProfileActivity.this, Options.init().setRequestCode(100));
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                };
                requestPermissions(PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @OnClick(R.id.iv_user)
    void updateImage() {
        checkPermission();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("val", "requestCode ->  " + requestCode + "  resultCode " + resultCode);
        switch (requestCode) {
            case (100): {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> returnresult = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    File file = new File(returnresult.get(0));

                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                    RequestBody loginId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Utils.LOGIN_ID));
                    Utils.showDialog(progressDialog);
                    APIInterface getResponse = APIClient.getRetrofit().create(APIInterface.class);
                    Call<UpdateImageModel> call1 = getResponse.updateImage(fileToUpload, loginId);
                    call1.enqueue(new Callback<UpdateImageModel>() {
                        @Override
                        public void onResponse(Call<UpdateImageModel> call, Response<UpdateImageModel> response) {
                            Utils.dismissDialog(progressDialog);
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    //      Utils.showToastMessage("Image Uploded Successfully",UpdateProfileActivity.this);
                                    Utils.dismissDialog(progressDialog);
                                    profileImage.setImageURI(Uri.parse(returnresult.get(0)));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateImageModel> call, Throwable t) {
                            Utils.dismissDialog(progressDialog);
                            Utils.showToastMessage(t.getLocalizedMessage(), UpdateProfileActivity.this);
                        }
                    });
                }
                Utils.dismissDialog(progressDialog);
            }
            break;
        }
    }
}