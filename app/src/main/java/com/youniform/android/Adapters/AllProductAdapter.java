package com.youniform.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Interface.RowClick;
import com.youniform.android.Model.GetProduct.GetProductModel;
import com.youniform.android.R;

import java.util.List;


public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.ViewHolder> {

    private List<GetProductModel> getProductModelList;
    private RowClick rowClick;
    private ItemClick itemClick;
    private Context context;

    public AllProductAdapter(List<GetProductModel> getProductModelList, RowClick rowClick, Context context) {
        this.getProductModelList = getProductModelList;
        this.rowClick = rowClick;
        this.context = context;
    }

    public AllProductAdapter(List<GetProductModel> getProductModelList, RowClick rowClick, ItemClick itemClick, Context context) {
        this.getProductModelList = getProductModelList;
        this.rowClick = rowClick;
        this.itemClick = itemClick;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvProductName.setText(getProductModelList.get(position).getName());
        holder.tvPrice.setText("RS: " + getProductModelList.get(position).getPrice());
        if (getProductModelList.get(position).getImages() != null) {
            Glide.with(context).load(getProductModelList.get(position).getImages().get(0).getSrc()).into(holder.ivProductImage);
        }
    }

    @Override
    public int getItemCount() {
        return getProductModelList.size();
    }

    public void filterList(List<GetProductModel> filterdNames) {
        this.getProductModelList = filterdNames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvPrice;
        private ImageView ivProductImage;
        private ImageButton ib_like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            ib_like = itemView.findViewById(R.id.ib_like);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowClick.rowPosition(getProductModelList.get(getAdapterPosition()).getId());
                }
            });
            ib_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.itemClick(getProductModelList.get(getAdapterPosition()).getId());
                }
            });
        }
    }


}
