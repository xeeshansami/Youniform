package com.youniform.android.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.youniform.android.Adapters.AllProductAdapter;
import com.youniform.android.Adapters.FiltersAdapter;
import com.youniform.android.Adapters.ProductTabAdapter;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.BaseClasses.WishListDataBase;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Interface.FilterClick;
import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Interface.RowClick;
import com.youniform.android.Interface.TabClick;
import com.youniform.android.Model.CartModel;
import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.Model.Filter.Attribute;
import com.youniform.android.Model.Filter.FilterModel;
import com.youniform.android.Model.GetProduct.GetProductModel;
import com.youniform.android.Model.WishListModel;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllProductActivity extends AppCompatActivity implements RowClick, ItemClick, TabClick, FilterClick {

    private final List<GetProductModel> getProductModelList = new ArrayList<>();
    private final List<CategoriesModel> categoriesModelList = new ArrayList<>();
    private final List<Attribute> attributeList = new ArrayList<>();
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.noProduct)
    TextView noProduct;
    @BindView(R.id.tv_catname)
    TextView tv_catname;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.rv_tabs)
    RecyclerView rv_tabs;
    @BindView(R.id.rv_filter)
    RecyclerView rv_filter;
    @BindView(R.id.tvCartCount)
    TextView tvCartCount;
    @BindView(R.id.filterView)
    ConstraintLayout filterView;
    int id = 0;
    private CartDataBase helper;
    private APIInterface apiInterface;
    private Dialog progressDialog;
    private AllProductAdapter allProductAdapter;
    private FiltersAdapter filtersAdapter;
    private WishListDataBase wishListDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        id = getIntent().getIntExtra("ID", 0);
        int PARENT_ID = getIntent().getIntExtra("PARENT_ID", 0);
        setupClients();
        setAdapter();
        getAllProducts(id);
        getchildCategory(PARENT_ID);
        getFilters();
        setFilterAdapter();
        autoSearch();
        tv_catname.setText(new PrefManager(this).getString("Tab"));
    }


    private void setupClients() {
        ButterKnife.bind(this);
        helper = new CartDataBase(AllProductActivity.this);
        List<CartModel> cartModelList = helper.getData();
        if (cartModelList.size() > 9) {
            tvCartCount.setText("9+");
        } else {
            tvCartCount.setText(String.valueOf(cartModelList.size()));
        }
        wishListDataBase = new WishListDataBase(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(AllProductActivity.this);
    }


    private void getAllProducts(int id) {
        Utils.showDialog(progressDialog);
        Call<List<GetProductModel>> call1 = apiInterface.allProducts(id);
        call1.enqueue(new Callback<List<GetProductModel>>() {
            @Override
            public void onResponse(Call<List<GetProductModel>> call, Response<List<GetProductModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    getProductModelList.clear();
                    if (response.body().size() > 0) {
                        noProduct.setVisibility(View.GONE);
                        getProductModelList.addAll(response.body());
                    } else {
                        noProduct.setVisibility(View.VISIBLE);
                    }
                    allProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<GetProductModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), AllProductActivity.this);
            }
        });
    }

    private void filterProducts(String attribute, int attribute_term) {
        Utils.showDialog(progressDialog);
        Call<List<GetProductModel>> call1 = apiInterface.filterProducts(attribute, attribute_term);
        call1.enqueue(new Callback<List<GetProductModel>>() {
            @Override
            public void onResponse(Call<List<GetProductModel>> call, Response<List<GetProductModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    getProductModelList.clear();
                    if (response.body().size() > 0) {
                        getProductModelList.addAll(response.body());
                    }
                    allProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<GetProductModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), AllProductActivity.this);
            }
        });
    }

    @OnClick(R.id.ib_bag)
    public void moveToBag() {
        startActivity(new Intent(AllProductActivity.this, CartActivity.class));
    }

    private void setAdapter() {
        allProductAdapter = new AllProductAdapter(getProductModelList, this, this, AllProductActivity.this);
        GridLayoutManager layoutManager = new GridLayoutManager(AllProductActivity.this, 2);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(allProductAdapter);
    }

    private void setTabAdapter() {
        ProductTabAdapter productTabAdapter = new ProductTabAdapter(categoriesModelList, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AllProductActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_tabs.setLayoutManager(layoutManager);
        rv_tabs.setAdapter(productTabAdapter);
    }

    private void setFilterAdapter() {
        filtersAdapter = new FiltersAdapter(attributeList, this, AllProductActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AllProductActivity.this);
        rv_filter.setLayoutManager(layoutManager);
        rv_filter.setAdapter(filtersAdapter);
        filtersAdapter.notifyDataSetChanged();
    }

    @Override
    public void rowPosition(int position) {
        Intent intent = new Intent(AllProductActivity.this, ProductDetailActivity.class);
        for (GetProductModel productModel : getProductModelList) {
            if (productModel.getId() == position) {
                intent.putExtra("PRODUCT_DETAIL", productModel);
                intent.putExtra("ID", id);
                Log.d("asadasad:ok", "" + productModel.getName());
            }
        }
        startActivity(intent);
    }

    private void filter(String text) {
        List<GetProductModel> filterdNames = new ArrayList<>();
        for (GetProductModel s : getProductModelList) {
            if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(s);
            }
        }
        allProductAdapter.filterList(filterdNames);
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

    @Override
    public void itemClick(int position) {
        boolean contains = true;
        List<WishListModel> wishListModelList = wishListDataBase.getData();
        for (GetProductModel productModel : getProductModelList) {
            if (productModel.getId() == position) {
                Gson gson = new Gson();
                String json = gson.toJson(productModel);
                for (WishListModel wishListModel : wishListModelList) {
                    if (wishListModel.getProduct().equals(json)) {
                        Utils.showToastMessage("Product Already in wishlist", AllProductActivity.this);
                    } else {
                        contains = false;
                    }
                }
                if (!contains) {
                    wishListDataBase.insertData(json);
                    Utils.showToastMessage("Product Added in wishlist", AllProductActivity.this);
                }
                if (wishListModelList.size() == 0) {
                    wishListDataBase.insertData(json);
                    Utils.showToastMessage("Product Added in wishlist", AllProductActivity.this);
                }
            }
        }
    }

    @Override
    public void itemClick2(int position, int id) {

    }

    private void getchildCategory(int PARENT_ID) {
        Utils.showDialog(progressDialog);
        Call<List<CategoriesModel>> call1 = apiInterface.subCategory(PARENT_ID);
        call1.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        categoriesModelList.addAll(response.body());
                    }
                    setTabAdapter();
                }
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), AllProductActivity.this);
            }
        });
    }

    private void getFilters() {
        Utils.showDialog(progressDialog);
        Call<FilterModel> call1 = apiInterface.getFilter();
        call1.enqueue(new Callback<FilterModel>() {
            @Override
            public void onResponse(Call<FilterModel> call, Response<FilterModel> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().getAttribute().size() > 0) {
                        attributeList.addAll(response.body().getAttribute());
                    }
                    filtersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FilterModel> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), AllProductActivity.this);
            }
        });
    }

    @Override
    public void tabClick(int id) {
        getAllProducts(id);
    }

    @OnClick(R.id.tv_openFilter)
    public void openFilter() {
        filterView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        onBackPressed();
    }

    @OnClick({R.id.ib_closeFilter, R.id.textView15, R.id.applyFilter})
    public void closeFilter() {
        setFilterAdapter();
        getAllProducts(id);
        filterView.setVisibility(View.GONE);
    }


    @Override
    public void filterClick(String attribute, int attribute_term) {
        filterProducts(attribute, attribute_term);
    }
}