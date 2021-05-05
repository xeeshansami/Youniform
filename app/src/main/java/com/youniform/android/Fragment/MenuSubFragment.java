package com.youniform.android.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Adapters.MenuAdapter;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Interface.MenuClick;
import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuSubFragment extends Fragment implements MenuClick {

    @BindView(R.id.rv_menu)
    RecyclerView menu;
    boolean hasSubCat = false;
    List<TreeModel> treeModelList = new ArrayList<>();
    List<CategoriesModel> categoriesModelList = new ArrayList<>();
    private APIInterface apiInterface;
    private Dialog progressDialog;
    private int catId = 0;
    private String strCatName = "";
    private MenuAdapter menuAdapter;

    public MenuSubFragment(int catId, String strCatName, boolean hasSubCat) {
        this.catId = catId;
        this.strCatName = strCatName;
        this.hasSubCat = hasSubCat;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_sub, container, false);
        setupClients(view);
        setAdapter();
        return view;
    }


    private void setupClients(View view) {
        ButterKnife.bind(this, view);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        getsubCategory(catId);
    }

    private void getsubCategory(int cat_id) {
        Utils.showDialog(progressDialog);
        Call<List<CategoriesModel>> call1 = apiInterface.subCategory(cat_id);
        call1.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        treeModelList.clear();
                        for (CategoriesModel categoriesModel : response.body()) {
                            treeModelList.add(new TreeModel(categoriesModel.getName(),
                                    categoriesModel.getId(), categoriesModel.getHassubcat(), null));

                            Log.d("BLACK", "onResponse:. Main " + categoriesModel.getId());
                        }
                        menuAdapter.notifyDataSetChanged();
                        if (Utils.SUB_ID != 0) {
                            for (int i = 0; i < response.body().size(); i++) {
                                if (Utils.SUB_ID == response.body().get(i).getId()) {
                                    getchildCategory(Utils.SUB_ID, i, treeModelList.get(i));
                                    Log.d("BLACK", "onResponse: Sub " + treeModelList.get(i).treeName);
                                }
                            }
                            Utils.SUB_ID = 0;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), getContext());
            }
        });
    }

    private void setAdapter() {
        menuAdapter = new MenuAdapter(treeModelList, this, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        menu.setLayoutManager(layoutManager);
        menu.setAdapter(menuAdapter);
    }

    @Override
    public void HeaderClick(int headerId, int position, TreeModel treeModel) {
        getchildCategory(headerId, position, treeModel);
    }

    private void getchildCategory(int headerId, int position, TreeModel treeModel) {
        Utils.showDialog(progressDialog);
        Call<List<CategoriesModel>> call1 = apiInterface.subCategory(headerId);
        call1.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        List<CategoriesModel> categoriesModelList = new ArrayList<>();
                        categoriesModelList.addAll(response.body());
                        treeModelList.set(position, new TreeModel(treeModel.getTreeName(), treeModel.getTreeId(), treeModel.isHassubcat(), categoriesModelList));
                        menu.setAdapter(menuAdapter);
                        menuAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
            }
        });
    }

    @Override
    public void SectionClick(int sectionId, int position, TreeModel treeModel) {

    }

    public class TreeModel {
        String treeName;
        int TreeId = 0;
        boolean hassubcat = false;
        List<CategoriesModel> categoriesModelList;

        public TreeModel(String treeName, int treeId, boolean hassubcat, List<CategoriesModel> categoriesModelList) {
            this.treeName = treeName;
            TreeId = treeId;
            this.hassubcat = hassubcat;
            this.categoriesModelList = categoriesModelList;
        }

        public TreeModel(List<CategoriesModel> categoriesModelList) {
            this.categoriesModelList = categoriesModelList;
        }

        public boolean isHassubcat() {
            return hassubcat;
        }

        public void setHassubcat(boolean hassubcat) {
            this.hassubcat = hassubcat;
        }

        public int getTreeId() {
            return TreeId;
        }

        public void setTreeId(int treeId) {
            TreeId = treeId;
        }

        public String getTreeName() {
            return treeName;
        }

        public void setTreeName(String treeName) {
            this.treeName = treeName;
        }

        public List<CategoriesModel> getCategoriesModelList() {
            return categoriesModelList;
        }

        public void setCategoriesModelList(List<CategoriesModel> categoriesModelList) {
            this.categoriesModelList = categoriesModelList;
        }
    }

}

