package com.youniform.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Interface.TabClick;
import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.R;

import java.util.List;

public class ProductTabAdapter extends RecyclerView.Adapter<ProductTabAdapter.ViewHolder> {

    private List<CategoriesModel> categoriesModelList;
    private Context context;
    private TabClick tabClick;

    public ProductTabAdapter(List<CategoriesModel> categoriesModelList, Context context, TabClick tabClick) {
        this.categoriesModelList = categoriesModelList;
        this.context = context;
        this.tabClick = tabClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_tab, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_tabName.setText(categoriesModelList.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_tabName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tabName = itemView.findViewById(R.id.tv_tabName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tabClick.tabClick(categoriesModelList.get(getAdapterPosition()).getId());
                }
            });
        }
    }


}
