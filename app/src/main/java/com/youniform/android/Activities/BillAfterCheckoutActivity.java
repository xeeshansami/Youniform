package com.youniform.android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Adapters.BillAdapter;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.Model.CartModel;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

public class BillAfterCheckoutActivity extends AppCompatActivity {
    CardView backCardView;
    TextView tvPaymentMeth, tvShipping, tvTotal;
    RecyclerView BillRv;
    BillAdapter billAdapter;
    int price = 0,price2 = 0;
    private List<CartModel> cartModelList = new ArrayList<>();
    private CartDataBase helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_after_checkout);
        UIinit();

        tvShipping.setText(getIntent().getStringExtra("Courier"));
        price2 = getIntent().getIntExtra("CourierPrice",0);
        setupClients();
        setAdapter();

    }


    private void UIinit() {
        BillRv = findViewById(R.id.bill_rv);
        backCardView = findViewById(R.id.cardbacktomenu);
        tvPaymentMeth = findViewById(R.id.tvPaymentMethod);
        tvShipping = findViewById(R.id.tv_shipping);
        tvTotal = findViewById(R.id.tvTotalPrice);
    }

    public void onClick(View view) {
        if (view == backCardView) {
            onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        helper.deleteAll();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        helper.deleteAll();
        startActivity(new Intent(BillAfterCheckoutActivity.this, HomeActivity.class));
    }


    private void setAdapter() {
        billAdapter = new BillAdapter(cartModelList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        BillRv.setLayoutManager(layoutManager);
        BillRv.setAdapter(billAdapter);
    }

    private void setupClients() {
        helper = new CartDataBase(this);
        cartModelList = helper.getData();
        if (cartModelList.size() > 0) {
            billAdapter = new BillAdapter(cartModelList, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            BillRv.setLayoutManager(layoutManager);
            BillRv.setAdapter(billAdapter);
            price = 0;
            for (CartModel cartModel : cartModelList) {
                if (!cartModel.getPrice().isEmpty()) {
                    price = price + Integer.parseInt(cartModel.getPrice()) * cartModel.getQuantity();
                }
            }
            price = price + price2;
            tvTotal.setText("RS: " + price);
        }
    }


}