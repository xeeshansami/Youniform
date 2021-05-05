package com.youniform.android.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Model.LoginModel;
import com.youniform.android.Model.ProfileModel.GetProfileModel;
import com.youniform.android.Model.ResponseModel;
import com.youniform.android.R;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUPActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.textInputLayout19)
    TextInputLayout et_number;

    @BindView(R.id.et_confirmpassword)
    EditText et_confirmpassword;

    @BindView(R.id.cb_robort)
    CheckBox cb_robort;


    private APIInterface apiInterface;
    private Dialog progressDialog;
    private PrefManager prefManager;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String verificationId = "";
    private CartDataBase helper;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeAutoRetrievalTimeOut(String verificationId) {
            Utils.dismissDialog(progressDialog);
            Toast.makeText(SignUPActivity.this, "Your Phone Number Verification is failed.Retry again!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Utils.dismissDialog(progressDialog);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Utils.dismissDialog(progressDialog);
            Log.w("onVerificationFailed", "onVerificationFailed", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.e("Exception:", "FirebaseAuthInvalidCredentialsException" + e);
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Log.e("Exception:", "FirebaseTooManyRequestsException" + e);
            }
            Toast.makeText(SignUPActivity.this, "Your Phone Number Verification is failed.Retry again!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken token) {
            Utils.dismissDialog(progressDialog);
            verificationId = s;
            showverificationDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupClient();

        TextWatcher();
    }

    private void setupClient() {
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(SignUPActivity.this);
        prefManager = new PrefManager(SignUPActivity.this);
        reference = FirebaseDatabase.getInstance().getReference("LoginRecords");
        mAuth = FirebaseAuth.getInstance();
        helper = new CartDataBase(this);

    }

    @OnClick(R.id.btn_back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.btn_signup)
    public void signup() {
        String strName = et_name.getText().toString();
        String strEmail = et_email.getText().toString();
        String strPassword = et_password.getText().toString();
        String strConfirmPassword = et_confirmpassword.getText().toString();
        String strNumber = et_number.getEditText().getText().toString();
        if (strName.isEmpty()) {
            Utils.showToastMessage("Please Enter Your Name", SignUPActivity.this);
        } else if (strEmail.isEmpty()) {
            Utils.showToastMessage("Please Enter Your Email", SignUPActivity.this);
        } else if (!Utils.isValidEmail(strEmail)) {
            Utils.showToastMessage("Please Enter valid Email", SignUPActivity.this);
        } else if (strNumber.isEmpty()) {
            Utils.showToastMessage("Please Enter Your Number", SignUPActivity.this);
        } else if (strNumber.length() < 12) {
            Utils.showToastMessage("Please Enter Valid Number", SignUPActivity.this);
        } else if (strPassword.isEmpty()) {
            Utils.showToastMessage("Please Enter Your Password", SignUPActivity.this);
        } else if (strConfirmPassword.isEmpty()) {
            Utils.showToastMessage("Please Enter Your Confirm Password", SignUPActivity.this);
        } else if (!strPassword.equals(strConfirmPassword)) {
            Utils.showToastMessage("Password Not Match", SignUPActivity.this);
        } else if (!cb_robort.isChecked()) {
            Utils.showToastMessage("Recaptcha unchecked", SignUPActivity.this);
        } else {
            checkNumber(strNumber, strEmail, strName);
        }
    }

    private void checkNumber(String number, String email, String name) {
        Utils.showDialog(progressDialog);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LoginRecords");
        ref.orderByChild("number").equalTo(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utils.dismissDialog(progressDialog);
                if (dataSnapshot.exists()) {
                    Toast.makeText(SignUPActivity.this, "Number Exists in database", Toast.LENGTH_SHORT).show();
                } else {
                    ref.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Utils.dismissDialog(progressDialog);
                            if (dataSnapshot.exists()) {
                                Toast.makeText(SignUPActivity.this, "User Name Exists in database", Toast.LENGTH_SHORT).show();
                            } else {
                                ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Utils.dismissDialog(progressDialog);
                                        if (dataSnapshot.exists()) {
                                            Toast.makeText(SignUPActivity.this, "Email Exists in database", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Utils.showDialog(progressDialog);
                                            PhoneAuthOptions options =
                                                    PhoneAuthOptions.newBuilder(mAuth)
                                                            .setPhoneNumber(number)       // Phone number to verify
                                                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                            .setActivity(SignUPActivity.this)                 // Activity (for callback binding)
                                                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                            .build();
                                            PhoneAuthProvider.verifyPhoneNumber(options);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Utils.dismissDialog(progressDialog);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Utils.dismissDialog(progressDialog);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Utils.dismissDialog(progressDialog);
            }
        });
    }

    public void showverificationDialog() {
        final Dialog dialog = new Dialog(SignUPActivity.this);
        dialog.setContentView(R.layout.activity_verification);
        dialog.setCancelable(false);
        dialog.show();
        Button declineButton = (Button) dialog.findViewById(R.id.btn_updateProfile);
        PinView pinView = dialog.findViewById(R.id.pinCode);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPinCode = pinView.getText().toString();
                if (strPinCode.isEmpty()) {
                    Utils.showToastMessage("Please Enter Your Pin Code", SignUPActivity.this);
                } else if (strPinCode.length() < 6) {
                    Utils.showToastMessage("Please Enter Your Compelete Code", SignUPActivity.this);
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, pinView.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                    dialog.dismiss();
                }
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Utils.showDialog(progressDialog);
        String strName = et_name.getText().toString();
        String strEmail = et_email.getText().toString();
        String strPassword = et_password.getText().toString();
        String strNumber = et_number.getEditText().getText().toString();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Call<ResponseModel> call1 = apiInterface.signup(strName, strEmail, strNumber, strPassword);
                            call1.enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    Utils.dismissDialog(progressDialog);
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            int status = response.body().getStatus();
                                            if (status == 200) {
                                                helper.deleteAll();
                                                FirebaseUser user = task.getResult().getUser();
                                                HashMap<String, Object> hashObj = new HashMap<>();
                                                hashObj.put("id", user.getUid());
                                                hashObj.put("number", strNumber);
                                                hashObj.put("email", strEmail);
                                                hashObj.put("name", strName);
                                                reference.child(user.getUid()).setValue(hashObj)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    ActionCodeSettings actionCodeSettings =
                                                                            ActionCodeSettings.newBuilder()
                                                                                    .setUrl("https://ndapak.org/uniform/")
                                                                                    .setHandleCodeInApp(true)
                                                                                    .setIOSBundleId("com.youniform.ios")
                                                                                    .setAndroidPackageName(
                                                                                            "com.youniform.android",
                                                                                            true, /* installIfNotAvailable */
                                                                                            "12"    /* minimumVersion */)
                                                                                    .build();
                                                                    sendSignInLink(strEmail, actionCodeSettings);
                                                                    getUserData(strEmail, strPassword);
                                                                } else {
                                                                    Toast.makeText(SignUPActivity.this, "Database Record not saved", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            }
                                            Utils.showToastMessage(response.body().getMessage(), SignUPActivity.this);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    Utils.dismissDialog(progressDialog);
                                    Utils.showToastMessage(t.getLocalizedMessage(), SignUPActivity.this);
                                }
                            });

                        } else {
                            Utils.dismissDialog(progressDialog);
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignUPActivity.this, "Invalid code.", Toast.LENGTH_LONG).show();
                            }
                            showverificationDialog();
                        }
                    }
                });
    }

    public void sendSignInLink(String email, ActionCodeSettings actionCodeSettings) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                        }
                    }
                });
    }


    private void getUserData(String strEmail, String strPassword) {
        Utils.showDialog(progressDialog);
        Call<LoginModel> call1 = apiInterface.login(strEmail, strPassword);
        call1.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
                            Utils.LOGIN_ID = response.body().getUserid();
                            prefManager.setInt("LOGIN_ID", response.body().getUserid());
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
                                            startActivity(new Intent(SignUPActivity.this, HomeActivity.class));
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<GetProfileModel>> call, Throwable t) {
                                    Utils.dismissDialog(progressDialog);
                                    Utils.showToastMessage(t.getLocalizedMessage(), SignUPActivity.this);
                                }
                            });
                        } else {
                            Utils.showToastMessage(response.body().getMessage(), SignUPActivity.this);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), SignUPActivity.this);
            }
        });
    }


    private void TextWatcher() {
        int maxLength = 13;
        et_number.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        et_number.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        et_number.setHint("Phone");

        et_number.getEditText().addTextChangedListener(new TextWatcher() {
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
                    String texts = et_number.getEditText().getText().toString();
                    texts = texts.substring(0, charIndex) + texts.substring(charIndex + 1);
                    et_number.getEditText().setText(texts);
                    et_number.getEditText().setSelection(3);
                }
            }
        });

    }
}