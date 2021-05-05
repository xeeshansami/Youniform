package com.youniform.android.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubMenuAdapter extends RecyclerView.Adapter<SubMenuAdapter.ViewHolder> {

    List<CategoriesModel> categoriesList = new ArrayList<>();
    private Context context;
    private List<CategoriesModel> categoriesModelList;
    private RecyclerView rv_notsub, rv_hassub;
    private APIInterface apiInterface;
    private Dialog progressDialog;
    private Animation animShow, animHide;

    private List<CategoriesModel> nohasSubcategoriesList = new ArrayList<>();
    private List<CategoriesModel> hasSubcategoriesList = new ArrayList<>();

    public SubMenuAdapter(Context context, List<CategoriesModel> categoriesModelList) {
        this.context = context;
        this.categoriesModelList = categoriesModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_menu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel categoriesModel = categoriesModelList.get(position);
        holder.tv_hassubCategoryName.setText(categoriesModel.getName());
    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    private void getchildCategory(int headerId) {
        Utils.showDialog(progressDialog);
        Call<List<CategoriesModel>> call1 = apiInterface.subCategory(headerId);
        call1.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        categoriesList.addAll(response.body());
                        nohasSubcategoriesList.clear();
                        hasSubcategoriesList.clear();
                        for (CategoriesModel categoriesModel : categoriesList) {
                            if (categoriesModel.getHassubcat()) {
                                hasSubcategoriesList.add(categoriesModel);
                                SubMenuAdapter menuAdapter = new SubMenuAdapter(context, hasSubcategoriesList);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                rv_hassub.setLayoutManager(layoutManager);
                                rv_hassub.setAdapter(menuAdapter);
                            } else {
                                nohasSubcategoriesList.add(categoriesModel);
                                NoSubMenuAdapter menuAdapter = new NoSubMenuAdapter(context, nohasSubcategoriesList);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                rv_notsub.setLayoutManager(layoutManager);
                                rv_notsub.setAdapter(menuAdapter);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), context);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_hassubCategoryName, tv_subCategoryName;
        private ImageView iv_dropDown;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            apiInterface = APIClient.getClient().create(APIInterface.class);
            progressDialog = Utils.progressDialog(context);
            tv_hassubCategoryName = itemView.findViewById(R.id.tv_hassubCategoryName);
            tv_subCategoryName = itemView.findViewById(R.id.tv_subCategoryName);
            rv_notsub = itemView.findViewById(R.id.rv_notsub);
            rv_hassub = itemView.findViewById(R.id.rv_hassub);
            iv_dropDown = itemView.findViewById(R.id.iv_dropDown);
            tv_subCategoryName.setVisibility(View.GONE);
            animShow = AnimationUtils.loadAnimation(context, R.anim.slid_up);
            animHide = AnimationUtils.loadAnimation(context, R.anim.slid_down);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (categoriesList.size() > 0) {
                        if (rv_hassub.getVisibility() == View.VISIBLE) {
                            tv_hassubCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
                            iv_dropDown.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down));
                            rv_hassub.startAnimation(animHide);
                            rv_hassub.setVisibility(View.GONE);
                        } else {
                            tv_hassubCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minimize, 0);

                            iv_dropDown.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_minimize));
                            rv_hassub.setVisibility(View.VISIBLE);
                            rv_hassub.startAnimation(animShow);
                        }
                        if (rv_notsub.getVisibility() == View.VISIBLE) {
                            tv_hassubCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);

                            iv_dropDown.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down));
                            rv_notsub.setVisibility(View.GONE);
                        } else {
                            tv_hassubCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minimize, 0);

                            iv_dropDown.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_minimize));
                            rv_notsub.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tv_hassubCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minimize, 0);

                        iv_dropDown.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_minimize));
                        getchildCategory(categoriesModelList.get(getAdapterPosition()).getId());
                    }
                }
            });
        }
    }
}
