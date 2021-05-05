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


public class WishtListAdapter extends RecyclerView.Adapter<WishtListAdapter.ViewHolder> {

    private List<GetProductModel> getProductModelList;
    private Context context;
    private RowClick rowClick;
    private ItemClick itemClick;

    public WishtListAdapter(List<GetProductModel> getProductModelList, Context context, RowClick rowClick, ItemClick itemClick) {
        this.getProductModelList = getProductModelList;
        this.context = context;
        this.rowClick = rowClick;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_singlerow, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_Prodname.setText(getProductModelList.get(position).getName());
        holder.tv_Status.setText(getProductModelList.get(position).getStockStatus());
        holder.tv_ProdPrice.setText("RS: " + getProductModelList.get(position).getPrice());
        Glide.with(context).load(getProductModelList.get(position).getImages().get(0).getSrc()).into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return getProductModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Prodname, tv_ProdPrice, tv_Status;
        private ImageView iv_image;
        private ImageButton ib_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Prodname = itemView.findViewById(R.id.tv_Prodname);
            tv_Status = itemView.findViewById(R.id.tv_Status);
            tv_ProdPrice = itemView.findViewById(R.id.tv_ProdPrice);
            iv_image = itemView.findViewById(R.id.iv_image);
            ib_delete = itemView.findViewById(R.id.ib_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowClick.rowPosition(getAdapterPosition());
                }
            });
            ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.itemClick(getAdapterPosition());
                }
            });

        }
    }


}
