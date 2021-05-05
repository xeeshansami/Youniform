package com.youniform.android.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Model.LoginModel;
import com.youniform.android.Model.ProfileModel.GetProfileModel;
import com.youniform.android.Model.ResponseModel;
import com.youniform.android.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText email;
    @BindView(R.id.et_password)
    EditText password;
    Boolean aBoolea;
    private APIInterface changeUrlApiInterface;
    private APIInterface apiInterface;
    private Dialog progressDialog;
    private PrefManager prefManager;
    private CartDataBase helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpClients();
        aBoolea = getIntent().getBooleanExtra("check", false);
    }

    private void setUpClients() {
        ButterKnife.bind(this);
        changeUrlApiInterface = APIClient.changeUrlClient().create(APIInterface.class);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(LoginActivity.this);
        prefManager = new PrefManager(LoginActivity.this);
        helper = new CartDataBase(this);
    }

    @OnClick(R.id.tv_signUp)
    public void moveToSignUPPage() {
        Intent intent = new Intent(LoginActivity.this, SignUPActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_forgetPassword)
    public void forgetPassword() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.forgetpassword_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView tv_signin = dialogView.findViewById(R.id.tv_signin);
        EditText et_email = dialogView.findViewById(R.id.et_email);
        Button btn_forgetPassword = dialogView.findViewById(R.id.btn_forgetPassword);
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isValidEmail(et_email.getText().toString())) {
                    forgetRequest(et_email.getText().toString());
                    alertDialog.dismiss();
                } else {
                    Utils.showToastMessage("Please Enter Valid Email", LoginActivity.this);
                }
            }
        });
    }


    @OnClick(R.id.btn_signin)
    public void signIn() {
        String strEmail = email.getText().toString();
        String strPassword = password.getText().toString();
        if (strEmail.isEmpty()) {
            Utils.showToastMessage("Please Enter Email", LoginActivity.this);
        } else if (!Utils.isValidEmail(strEmail)) {
            Utils.showToastMessage("Please Enter Valid Email", LoginActivity.this);
        } else if (strPassword.isEmpty()) {
            Utils.showToastMessage("Please Enter Password", LoginActivity.this);
        } else {
            Utils.showDialog(progressDialog);
            Call<LoginModel> call1 = apiInterface.login(strEmail, strPassword);
            call1.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    Utils.dismissDialog(progressDialog);
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                // helper.deleteAll();
                                Utils.LOGIN_ID = response.body().getUserid();
                                prefManager.setInt("LOGIN_ID", response.body().getUserid());
                                getUserData();
                            }
                            Utils.showToastMessage(response.body().getMessage(), LoginActivity.this);
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Utils.dismissDialog(progressDialog);
                    Utils.showToastMessage(t.getLocalizedMessage(), LoginActivity.this);
                }
            });
        }
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
                        prefManager.setString("USER_NAME", response.body().get(0).getUsername());
                        prefManager.setString("LAST_NAME", response.body().get(0).getLastName());
                        prefManager.setString("EMAIL", response.body().get(0).getEmail());
                        prefManager.setString("BNUMBER", response.body().get(0).getNumber());
                        prefManager.setString("PROFILE_IMAGE", response.body().get(0).getAvatarUrl());
                        if (aBoolea) {
                            Intent intent = new Intent();
                            setResult(1, intent);
                            finish();//finishing activity
                        } else {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetProfileModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), LoginActivity.this);
            }
        });
    }


    private void forgetRequest(String email) {
        Utils.showDialog(progressDialog);
        Call<ResponseModel> call1 = changeUrlApiInterface.resetpassword(email);
        call1.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
                            verificationPopup(email);
                        }
                        Utils.showToastMessage(response.body().getMessage(), LoginActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), LoginActivity.this);
            }
        });
    }

    private void verificationPopup(String email) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.otp_verification_dailog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        PinView pinCode = dialogView.findViewById(R.id.pinCode);
        Button btn_verify = dialogView.findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showDialog(progressDialog);
                Call<ResponseModel> call1 = changeUrlApiInterface.validateCode(email, pinCode.getText().toString());
                call1.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        Utils.dismissDialog(progressDialog);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    setPasswordPopup(email, pinCode.getText().toString());
                                    alertDialog.dismiss();
                                }
                                Utils.showToastMessage(response.body().getMessage(), LoginActivity.this);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Utils.dismissDialog(progressDialog);
                        Utils.showToastMessage(t.getLocalizedMessage(), LoginActivity.this);
                    }
                });
            }
        });
    }

    private void setPasswordPopup(String email, String code) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.setpassword_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        EditText et_newPassword = dialogView.findViewById(R.id.et_newPassword);
        Button btn_setPassword = dialogView.findViewById(R.id.btn_setPassword);
        btn_setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_newPassword.getText().toString();
                if (password.isEmpty()) {
                    Utils.showToastMessage("Please Enter New Passwor", LoginActivity.this);
                } else {
                    Utils.showDialog(progressDialog);
                    Call<ResponseModel> call1 = changeUrlApiInterface.setpassword(email, password, code);
                    call1.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            Utils.dismissDialog(progressDialog);
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        alertDialog.dismiss();
                                    }
                                    Utils.showToastMessage(response.body().getMessage(), LoginActivity.this);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Utils.dismissDialog(progressDialog);
                            Utils.showToastMessage(t.getLocalizedMessage(), LoginActivity.this);
                        }
                    });
                }
            }
        });
    }


}