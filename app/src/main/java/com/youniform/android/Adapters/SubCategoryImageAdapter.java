package com.youniform.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.youniform.android.Interface.RowClick;
import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.R;

import java.util.List;


public class SubCategoryImageAdapter extends RecyclerView.Adapter<SubCategoryImageAdapter.ViewHolder> {

    private List<CategoriesModel> categoriesModelList;
    private RowClick rowClick;
    private Context context;

    public SubCategoryImageAdapter(List<CategoriesModel> categoriesModelList, RowClick rowClick, Context context) {
        this.categoriesModelList = categoriesModelList;
        this.rowClick = rowClick;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_categoryimage, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.subName.setText(categoriesModelList.get(position).getName());
        if (categoriesModelList.get(position).getImage() != null) {
            Picasso.get().load(categoriesModelList.get(position).getImage().getSrc()).into(holder.iv_subImage);
        }
    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button subName;
        private ImageView iv_subImage;

        public ViewHolder(View itemView) {
            super(itemView);
            subName = itemView.findViewById(R.id.subName);
            iv_subImage = itemView.findViewById(R.id.iv_subImage);
            subName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    rowClick.rowPosition(getAdapterPosition());
                }
            });
        }
    }
}
