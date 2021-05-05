package com.youniform.android.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Fragment.MenuSubFragment;
import com.youniform.android.Interface.MenuClick;
import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<MenuSubFragment.TreeModel> treeModelList;
    private MenuClick menuClick;
    private Context context;
    private List<CategoriesModel> nohasSubcategoriesList = new ArrayList<>();
    private List<CategoriesModel> hasSubcategoriesList = new ArrayList<>();

    public MenuAdapter(List<MenuSubFragment.TreeModel> treeModelList, MenuClick menuClick, Context context) {
        this.treeModelList = treeModelList;
        this.menuClick = menuClick;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_menu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuSubFragment.TreeModel treeModel = treeModelList.get(position);
        if (treeModel.isHassubcat()) {
            Typeface typeface = ResourcesCompat.getFont(context, R.font.poppins_bold);
            holder.tv_hassubCategoryName.setTypeface(typeface, Typeface.NORMAL);


            holder.tv_hassubCategoryName.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tv_hassubCategoryName.setText(Html.fromHtml("<b>" + treeModel.getTreeName() + "</b> "));
            holder.tv_subCategoryName.setVisibility(View.GONE);
            holder.hasSubLayout.setVisibility(View.VISIBLE);
            holder.rv_hassub.setVisibility(View.VISIBLE);
            if (treeModel.getCategoriesModelList() != null) {
                if (treeModel.getCategoriesModelList().size() > 0) {
                    nohasSubcategoriesList.clear();
                    hasSubcategoriesList.clear();
                    for (CategoriesModel categoriesModel : treeModel.getCategoriesModelList()) {
                        if (categoriesModel.getHassubcat()) {
                            hasSubcategoriesList.add(categoriesModel);
                            SubMenuAdapter menuAdapter = new SubMenuAdapter(context, hasSubcategoriesList);
                            Log.d("TAG", "onBindViewHolder: 111");
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                            holder.rv_hassub.setLayoutManager(layoutManager);
                            holder.tv_hassubCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);

                            holder.rv_hassub.setAdapter(menuAdapter);
                        } else {
                            nohasSubcategoriesList.add(categoriesModel);

                            holder.tv_hassubCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minimize, 0);
                            NoSubMenuAdapter menuAdapter = new NoSubMenuAdapter(context, nohasSubcategoriesList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                            holder.rv_notsub.setLayoutManager(layoutManager);
                            holder.rv_notsub.setAdapter(menuAdapter);
                        }
                    }
                }
            }
            holder.dropdownlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (treeModel.getCategoriesModelList() != null) {
                        if (treeModel.getCategoriesModelList().size() > 0) {
                            if (holder.rv_notsub.getVisibility() == View.VISIBLE) {
                                holder.tv_hassubCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
                                holder.iv_dropDown.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down));
                                holder.rv_notsub.setVisibility(View.GONE);
                            } else {
                                holder.tv_hassubCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minimize, 0);
                                holder.iv_dropDown.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_minimize));
                                holder.rv_notsub.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        holder.iv_dropDown.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_minimize));
                        menuClick.HeaderClick(treeModel.getTreeId(), position, treeModel);
                    }
                }
            });
        } else {
            Log.d("TAG", "onBindViewHolder: " + treeModel.getTreeName());
            holder.tv_subCategoryName.setText(treeModel.getTreeName());
            holder.hasSubLayout.setVisibility(View.GONE);
            holder.rv_hassub.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return treeModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_subCategoryName, tv_hassubCategoryName;
        private LinearLayout hasSubLayout;
        private ImageView iv_dropDown;
        private LinearLayout dropdownlayout;
        private RecyclerView rv_notsub, rv_hassub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subCategoryName = itemView.findViewById(R.id.tv_subCategoryName);
            tv_hassubCategoryName = itemView.findViewById(R.id.tv_hassubCategoryName);
            hasSubLayout = itemView.findViewById(R.id.hasSubLayout);
            rv_notsub = itemView.findViewById(R.id.rv_notsub);
            rv_hassub = itemView.findViewById(R.id.rv_hassub);
            dropdownlayout = itemView.findViewById(R.id.dropdownlayout);
            iv_dropDown = itemView.findViewById(R.id.iv_dropDown);

        }
    }


}
