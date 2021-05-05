package com.youniform.android.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Adapters.CartAdapter;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Interface.CartCountClick;
import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Model.CartModel;
import com.youniform.android.Model.GetProduct.GetProductModel;
import com.youniform.android.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartCountClick, ItemClick {

    public static int i = 0;
    private final List<GetProductModel> getProductModelList = new ArrayList<>();
    @BindView(R.id.tv_noBagItems)
    TextView tv_noBagItems;
    @BindView(R.id.tv_totalBagPrice)
    TextView tv_totalBagPrice;
    @BindView(R.id.rv_bag)
    RecyclerView rv_bag;
    int price = 0;
    APIInterface apiInterface;
    private CartDataBase helper;
    private Dialog loading;
    private CartAdapter cartAdapter;
    private List<CartModel> cartModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        loading = Utils.progressDialog(this);
        setAdapter();
        setupClients();
    }


    private void setAdapter() {
        cartAdapter = new CartAdapter(cartModelList, this, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_bag.setLayoutManager(layoutManager);
        rv_bag.setAdapter(cartAdapter);
    }

    private void setupClients() {
        helper = new CartDataBase(this);
        cartModelList = helper.getData();
        if (cartModelList.size() > 0) {
            cartAdapter = new CartAdapter(cartModelList, this, this, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rv_bag.setLayoutManager(layoutManager);
            rv_bag.setAdapter(cartAdapter);
            price = 0;
            for (CartModel cartModel : cartModelList) {
                if (!cartModel.getPrice().isEmpty()) {
                    price = price + Integer.parseInt(cartModel.getPrice()) * cartModel.getQuantity();
                }
            }
            tv_totalBagPrice.setText("RS: " + price);
        } else {
            tv_noBagItems.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.ib_bagBack)
    void bagBack() {
        startActivity(new Intent(CartActivity.this, HomeActivity.class));
    }

    @OnClick(R.id.btn_bagCheckout)
    void bagCheckout() {
        if (cartModelList.size() > 0) {
            startActivity(new Intent(CartActivity.this, CheckOutTabActivity.class));
        } else {
            Utils.showToastMessage("No Items Available For CheckOut", CartActivity.this);
        }
    }

    @Override
    public void countClick(int position, int oldValue, int newValue) {
        if (newValue == 0) {
            helper.delete(String.valueOf(cartModelList.get(position).get_id()));
        }
        if (newValue > 0) {
            helper.update(String.valueOf(cartModelList.get(position).get_id()), newValue);
        }
        cartModelList = helper.getData();
        cartAdapter = new CartAdapter(cartModelList, this, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_bag.setLayoutManager(layoutManager);
        rv_bag.setAdapter(cartAdapter);
        price = 0;
        for (CartModel cartModel : cartModelList) {
            price = price + Integer.parseInt(cartModel.getPrice()) * cartModel.getQuantity();
        }
        tv_totalBagPrice.setText("RS: " + price);
    }


    @Override
    public void itemClick(int position) {

    }

    @Override
    public void itemClick2(int position, int id) {
        loading.show();
        Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
        int id2 = helper.getData().get(position).getCatId();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<GetProductModel>> call1 = apiInterface.allProducts(id2);
        call1.enqueue(new Callback<List<GetProductModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<GetProductModel>> call, @NotNull Response<List<GetProductModel>> response) {
                if (response.isSuccessful()) {
                    getProductModelList.clear();
                    if (Objects.requireNonNull(response.body()).size() > 0) {
                        getProductModelList.addAll(response.body());
                        for (GetProductModel productModel : getProductModelList) {
                            if (productModel.getId() == id) {
                                intent.putExtra("PRODUCT_DETAIL", productModel);
                                intent.putExtra("ID", id2);
                                loading.dismiss();
                                startActivity(intent);
                            }
                        }

                    }
                } else
                    Toast.makeText(CartActivity.this, "unsuccess: " + response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<List<GetProductModel>> call, @NotNull Throwable t) {
                Toast.makeText(CartActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}