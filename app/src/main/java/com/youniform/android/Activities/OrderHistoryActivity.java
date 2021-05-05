package com.youniform.android.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Adapters.OrderHistoryAdapter;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Model.OrderHistory.GetOrderHistory;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity implements ItemClick {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_noOrders)
    TextView tv_noOrders;

    private APIInterface apiInterface;
    private Dialog progressDialog;
    private List<GetOrderHistory> getOrderHistoryList = new ArrayList<>();
    private OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        setupClients();
        getAdapter();
        getAllOrders();
    }

    private void getAdapter() {
        orderHistoryAdapter = new OrderHistoryAdapter(getOrderHistoryList, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(orderHistoryAdapter);
    }

    private void setupClients() {
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(OrderHistoryActivity.this);
    }


    private void getAllOrders() {
        Utils.showDialog(progressDialog);
        Call<List<GetOrderHistory>> call1 = apiInterface.allOrders(Utils.LOGIN_ID);
        call1.enqueue(new Callback<List<GetOrderHistory>>() {
            @Override
            public void onResponse(Call<List<GetOrderHistory>> call, Response<List<GetOrderHistory>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        tv_noOrders.setVisibility(View.GONE);
                        getOrderHistoryList.addAll(response.body());

                        orderHistoryAdapter.notifyDataSetChanged();
                    } else {
                        tv_noOrders.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetOrderHistory>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), OrderHistoryActivity.this);
            }
        });
    }

    @OnClick(R.id.btn_back)
    void back() {
        onBackPressed();
    }

    @Override
    public void itemClick(int position) {
        Intent intent = new Intent(OrderHistoryActivity.this, SingleOrderStatusActivity.class);
        intent.putExtra("ORDER_STATUS", getOrderHistoryList.get(position).getStatus());
        startActivity(intent);
    }

    @Override
    public void itemClick2(int position, int id) {

    }
}