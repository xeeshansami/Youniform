package com.youniform.android.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.youniform.android.Adapters.AllProductAdapter;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.BaseClasses.WishListDataBase;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Interface.RowClick;
import com.youniform.android.Model.GetProduct.GetProductModel;
import com.youniform.android.Model.WishListModel;
import com.youniform.android.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAllActivty extends AppCompatActivity implements RowClick, ItemClick {

    private final List<GetProductModel> getProductModelList = new ArrayList<>();
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.noSearchProduct)
    TextView noProduct;
    @BindView(R.id.searchProductview)
    SearchView searchview;
    private APIInterface apiInterface;
    private Dialog progressDialog;
    private AllProductAdapter allProductAdapter;
    private WishListDataBase wishListDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all_activty);
        setupClients();
        setAdapter();
        getProducts("");
        autoSearch();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchAllActivty.this, HomeActivity.class));
    }

    private void setupClients() {
        ButterKnife.bind(this);
        wishListDataBase = new WishListDataBase(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(SearchAllActivty.this);
    }

    private void setAdapter() {
        allProductAdapter = new AllProductAdapter(getProductModelList, this, this, SearchAllActivty.this);
        GridLayoutManager layoutManager = new GridLayoutManager(SearchAllActivty.this, 2);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(allProductAdapter);
    }


    public void autoSearch() {
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });


    }

    private void getProducts(String newText) {
        Utils.showDialog(progressDialog);
        Call<List<GetProductModel>> call1 = apiInterface.searchProducts(newText, "100");
        call1.enqueue(new Callback<List<GetProductModel>>() {
            @Override
            public void onResponse(Call<List<GetProductModel>> call, Response<List<GetProductModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        getProductModelList.clear();
                        noProduct.setVisibility(View.GONE);
                        getProductModelList.addAll(response.body());
                        allProductAdapter.notifyDataSetChanged();
                    } else {
                        noProduct.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetProductModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), SearchAllActivty.this);
            }
        });
    }

    private void filter(String text) {
        List<GetProductModel> filterdNames = new ArrayList<>();
        if (!text.isEmpty()) {
            for (GetProductModel s : getProductModelList) {
                if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                    filterdNames.add(s);
                }
            }
            allProductAdapter.filterList(filterdNames);
        }
    }


    @Override
    public void rowPosition(int position) {
        Intent intent = new Intent(SearchAllActivty.this, ProductDetailActivity.class);
        for (GetProductModel productModel : getProductModelList) {
            if (productModel.getId() == position) {
                intent.putExtra("PRODUCT_DETAIL", productModel);
                intent.putExtra("ID", getProductModelList.get(0).getCategories().get(0).getId());
            }
        }
        startActivity(intent);
    }

    @OnClick(R.id.ib_Searchback)
    public void searchBack() {
        onBackPressed();
    }


    @Override
    public void itemClick(int position) {
//        boolean contains = true;
//        for (GetProductModel productModel: getProductModelList){
//            if (productModel.getId() == position){
//                Gson gson = new Gson();
//                String json = gson.toJson(productModel);
//                List<WishListModel> wishListModelList = wishListDataBase.getData();
//                for (WishListModel wishListModel : wishListModelList){
//                    if (wishListModel.getProduct().equals(json)){
//                        contains = true;
//                        Utils.showToastMessage("Product Already in wishlist",SearchAllActivty.this);
//                    }else {
//                        contains = false;
//                    }
//                }
//                if (wishListModelList.size() == 0) {
//                    wishListDataBase.insertData(json);
//                    Utils.showToastMessage("Product Added in wishlist",SearchAllActivty.this);
//                }
//                if (!contains){
//                    wishListDataBase.insertData(json);
//                    Utils.showToastMessage("Product Added in wishlist",SearchAllActivty.this);
//                }
//            }
//        }

        boolean contains = true;
        List<WishListModel> wishListModelList = wishListDataBase.getData();
        for (GetProductModel productModel : getProductModelList) {
            if (productModel.getId() == position) {
                Gson gson = new Gson();
                String json = gson.toJson(productModel);
                for (WishListModel wishListModel : wishListModelList) {
                    if (wishListModel.getProduct().equals(json)) {
                        Utils.showToastMessage("Product Already in wishlist", SearchAllActivty.this);
                    } else {
                        contains = false;
                    }
                }
                if (!contains) {
                    wishListDataBase.insertData(json);
                    Utils.showToastMessage("Product Added in wishlist", SearchAllActivty.this);
                }
                if (wishListModelList.size() == 0) {
                    wishListDataBase.insertData(json);
                    Utils.showToastMessage("Product Added in wishlist", SearchAllActivty.this);
                }
            }
        }
    }

    @Override
    public void itemClick2(int position, int id) {

    }
}