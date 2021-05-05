package com.youniform.android.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Model.ResponseModel;
import com.youniform.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteReviewActivity extends AppCompatActivity {

    @BindView(R.id.tv_ProductName)
    TextView productName;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.et_tellusExperience)
    EditText et_tellusExperience;

    Button btn;
    int PRODUCT_ID = 0;
    float Rating = 0;
    boolean b;
    private APIInterface apiInterface;
    private Dialog progressDialog;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        setupClient();
        setData();
        btn = findViewById(R.id.btn_sendReview);
        b = getIntent().getBooleanExtra("check", true);
        if (b) {
            et_tellusExperience.setEnabled(false);
            ratingBar.setEnabled(false);
            et_tellusExperience.setHint("Please Buy Before Review");
            btn.setEnabled(false);

        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Rating = v;
            }
        });
    }

    private void setData() {
        productName.setText(getIntent().getStringExtra("PRODUCT_NAME"));
        PRODUCT_ID = getIntent().getIntExtra("PRODUCT_ID", 0);
    }

    private void setupClient() {
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(WriteReviewActivity.this);
        prefManager = new PrefManager(WriteReviewActivity.this);
    }

    @OnClick(R.id.btn_back)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.btn_sendReview)
    void setReview() {
        if (b) {

        }
        String review = et_tellusExperience.getText().toString();
        if (review.isEmpty()) {
            Utils.showToastMessage("Please type your review", WriteReviewActivity.this);
        } else if (Rating == 0) {
            Utils.showToastMessage("Please Select Rating", WriteReviewActivity.this);
        } else {
            Utils.showDialog(progressDialog);
            Call<ResponseModel> call1 = apiInterface.addReviews(PRODUCT_ID, review, prefManager.getString("FIRST_NAME") + " " +
                    prefManager.getString("LAST_NAME"), prefManager.getString("EMAIL"), Rating);
            call1.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    Utils.dismissDialog(progressDialog);
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Utils.showToastMessage(response.body().getMessage(), WriteReviewActivity.this);
                            onBackPressed();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Utils.dismissDialog(progressDialog);
                    Utils.showToastMessage(t.getLocalizedMessage(), WriteReviewActivity.this);
                }
            });

        }
    }
}