package com.youniform.android.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.DividerDrawable;
import com.androidkun.xtablayout.XTabLayout;
import com.google.android.material.tabs.TabLayout;
import com.youniform.android.Activities.CartActivity;
import com.youniform.android.Activities.HomeActivity;
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


public class MenuFragment extends Fragment {

    @BindView(R.id.menupager)
    ViewPager viewpage;
    @BindView(R.id.menu_tabs)
    XTabLayout tabLayout;
    @BindView(R.id.tvCartCount)
    TextView tvCartCount;
    CartDataBase helper;
    int tab;
    private APIInterface apiInterface;
    private Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "MyTab", Context.MODE_PRIVATE);
        tab = prefs.getInt("Tab", 0);
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

                        adapter.addFragment(new MenuSubFragment(response.body().get(1).getId(), response.body().get(1).getName(), response.body().get(1).getHassubcat()), response.body().get(1).getName());
                        adapter.addFragment(new MenuSubFragment(response.body().get(2).getId(), response.body().get(2).getName(), response.body().get(2).getHassubcat()), "   "+response.body().get(2).getName());
                        adapter.addFragment(new MenuSubFragment(response.body().get(0).getId(), response.body().get(0).getName(), response.body().get(0).getHassubcat()), "   "+response.body().get(0).getName());


                  /*       for (CategoriesModel c: response.body()){
                            adapter.addFragment(new MenuSubFragment(c.getId(),c.getName(),c.getHassubcat()), c.getName());
                        }*/
                        viewpage.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewpage);
                        if (Utils.CAT_ID != 0) {
                            if (response.body().size() > 0) {
                                //    for (int i = 0; i < response.body().size(); i++) {
                                // if (response.body().get(i).getId().equals(Utils.CAT_ID)) {
                                tabLayout.setScrollPosition(tab, 0f, true);
                                tabLayout.setDividerGravity(DividerDrawable.CENTER);
                                viewpage.setCurrentItem(tab);
                                Utils.CAT_ID = 0;
                                //      }
                                //}
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
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
        Intent intent = new Intent(getContext(), SearchAllActivty.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_home)
    public void moveToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ib_bag)
    public void moveToBag() {
        startActivity(new Intent(getContext(), CartActivity.class));
    }
}