package com.youniform.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Activities.AllProductActivity;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.R;

import java.util.List;


public class NoSubMenuAdapter extends RecyclerView.Adapter<NoSubMenuAdapter.ViewHolder> {

    private Context context;
    private List<CategoriesModel> categoriesModelList;


    public NoSubMenuAdapter(Context context, List<CategoriesModel> categoriesModelList) {
        this.context = context;
        this.categoriesModelList = categoriesModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_sub_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel categoriesModel = categoriesModelList.get(position);
        holder.tv_subcat_name.setText(categoriesModel.getName());

        Log.d("TAG", "onBindViewHolder: " + categoriesModel.getName());
    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_subcat_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subcat_name = itemView.findViewById(R.id.tv_subcat_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AllProductActivity.class);
                    intent.putExtra("ID", categoriesModelList.get(getAdapterPosition()).getId());
                    intent.putExtra("PARENT_ID", categoriesModelList.get(getAdapterPosition()).getParent());
                    new PrefManager(context).setString("Tab", tv_subcat_name.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
    }


}
