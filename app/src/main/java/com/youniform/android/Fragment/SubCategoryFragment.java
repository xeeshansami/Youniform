package com.youniform.android.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.youniform.android.Adapters.SubCategoryImageAdapter;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Interface.RowClick;
import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryFragment extends Fragment implements RowClick {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    OnPositionClick positionClick;
    int pos = 0;
    private int cat_id = 0;
    private String cat_name = "";
    private APIInterface apiInterface;
    private Dialog progressDialog;
    private SubCategoryImageAdapter subCategoryImageAdapter;
    private List<CategoriesModel> categoriesModelList = new ArrayList<>();

    public SubCategoryFragment(int cat_id, String cat_name, int position) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.pos = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        setupClients(view);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getsubCategory(cat_id);
            }
        });
        return view;
    }

    private void setupClients(View view) {
        ButterKnife.bind(this, view);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        positionClick = (OnPositionClick) context;
    }

    private void setAdapter() {
        subCategoryImageAdapter = new SubCategoryImageAdapter(categoriesModelList, this, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerview.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(recyclerview);
        recyclerview.setAdapter(subCategoryImageAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerview.setOnFlingListener(null);
        setAdapter();
        getsubCategory(cat_id);
    }

    private void getsubCategory(int cat_id) {
        pullToRefresh.setRefreshing(true);
        Call<List<CategoriesModel>> call1 = apiInterface.subCategory(cat_id);
        call1.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                pullToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        categoriesModelList.clear();
                        //              Integer id, String name, Integer parent, Image image, Boolean hassubcat)

                        for (CategoriesModel categoriesModel : response.body()) {
                            categoriesModelList.add(categoriesModel);
                        }
                        subCategoryImageAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void rowPosition(int position) {
        SharedPreferences prefs = getContext().getSharedPreferences(
                "MyTab", Context.MODE_PRIVATE);
        prefs.edit().putInt("Tab", pos).apply();
        positionClick.onsubItemClick(cat_id, categoriesModelList.get(position).getId());
    }

    public interface OnPositionClick {
        void onsubItemClick(int cat_id, int subId);
    }
}