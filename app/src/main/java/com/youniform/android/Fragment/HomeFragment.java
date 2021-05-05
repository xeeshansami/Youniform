package com.youniform.android.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.google.android.material.tabs.TabLayout;
import com.youniform.android.Activities.CartActivity;
import com.youniform.android.Activities.SearchAllActivty;
import com.youniform.android.Adapters.ViewPagerAdapter;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Model.CartModel;
import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {


    @BindView(R.id.viewpager)
    ViewPager viewpage;
    @BindView(R.id.tabs)
    XTabLayout tabLayout;
    @BindView(R.id.tvCartCount)
    TextView tvCartCount;
    CartDataBase helper;
    private APIInterface apiInterface;
    private Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupClients(view);
        getCategories();
        return view;
    }


    private void getCategories() {
        Utils.showDialog(progressDialog);
        Call<List<CategoriesModel>> call1 = apiInterface.Categories();
        call1.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<CategoriesModel>> call, @NotNull Response<List<CategoriesModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).size() > 0) {
                        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

                        //     for (CategoriesModel c: response.body()){

                        //  adapter.addFragment(new SubCategoryFragment(c.getId(), c.getName()), c.getName());
                        adapter.addFragment(new SubCategoryFragment(response.body().get(1).getId(), response.body().get(1).getName(), 0), response.body().get(1).getName());
                        adapter.addFragment(new SubCategoryFragment(response.body().get(2).getId(), response.body().get(2).getName(), 1), response.body().get(2).getName());
                        adapter.addFragment(new SubCategoryFragment(response.body().get(0).getId(), response.body().get(0).getName(), 2), response.body().get(0).getName());

                        //}
                        viewpage.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewpage);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<CategoriesModel>> call, @NotNull Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), getContext());
            }
        });
    }

    private void setupClients(View view) {
        ButterKnife.bind(this, view);
        helper = new CartDataBase(getContext());

        List<CartModel> cartModelList = helper.getData();
        if (cartModelList.size() > 9) {
            tvCartCount.setText("9+");
        } else {
            tvCartCount.setText(String.valueOf(cartModelList.size()));
        }
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(getContext());
    }

    @OnClick(R.id.tv_Search)
    public void moveToSearch() {
        startActivity(new Intent(getContext(), SearchAllActivty.class));
    }

    @OnClick(R.id.iv_home)
    public void moveToHome() {
    }

    @OnClick(R.id.ib_bag)
    public void moveToBag() {
        startActivity(new Intent(getContext(), CartActivity.class));
    }
}