package com.youniform.android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.youniform.android.Adapters.WishtListAdapter;
import com.youniform.android.BaseClasses.WishListDataBase;
import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Interface.RowClick;
import com.youniform.android.Model.GetProduct.GetProductModel;
import com.youniform.android.Model.WishListModel;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WishListActivity extends AppCompatActivity implements RowClick, ItemClick {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_emptyWishlist)
    TextView tv_emptyWishlist;

    private WishtListAdapter wishtListAdapter;
    private List<GetProductModel> getProductModelList = new ArrayList<>();
    private WishListDataBase wishListDataBase;
    private List<WishListModel> wishListModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        setupClients();
    }

    private void setupClients() {
        ButterKnife.bind(this);
        wishListDataBase = new WishListDataBase(this);
        wishListModelList = wishListDataBase.getData();
        for (WishListModel wishListModel : wishListModelList) {
            GetProductModel productModel = new Gson().fromJson(wishListModel.getProduct(), GetProductModel.class);
            getProductModelList.add(productModel);
        }
        if (wishListModelList.size() == 0) {
            tv_emptyWishlist.setVisibility(View.VISIBLE);
        }
        wishtListAdapter = new WishtListAdapter(getProductModelList, this, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(wishtListAdapter);
    }

    @OnClick(R.id.btn_back)
    void backClick() {
        onBackPressed();
    }

    @Override
    public void rowPosition(int position) {
        Intent intent = new Intent(WishListActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_DETAIL", getProductModelList.get(position));
        intent.putExtra("ID", getProductModelList.get(0).getCategories().get(0).getId());
        startActivity(intent);
    }

    @Override
    public void itemClick(int position) {
        wishListDataBase.delete(String.valueOf(wishListModelList.get(position).getId()));
        wishListModelList.clear();
        getProductModelList.clear();
        wishListModelList = wishListDataBase.getData();
        for (WishListModel wishListModel : wishListModelList) {
            GetProductModel productModel = new Gson().fromJson(wishListModel.getProduct(), GetProductModel.class);
            getProductModelList.add(productModel);
        }
        if (wishListModelList.size() == 0) {
            tv_emptyWishlist.setVisibility(View.VISIBLE);
        }
        wishtListAdapter = new WishtListAdapter(getProductModelList, this, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(wishtListAdapter);
    }

    @Override
    public void itemClick2(int position, int id) {

    }
}