package com.youniform.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youniform.android.Activities.ProductDetailActivity;
import com.youniform.android.Model.GetProduct.GetProductModel;
import com.youniform.android.R;

import java.util.List;

public class SuggestionProductsAdapter extends RecyclerView.Adapter<SuggestionProductsAdapter.SuggestionProductsViewHolder> {
    private Context context;
    private List<GetProductModel> productList;

    public SuggestionProductsAdapter(Context mContext, List<GetProductModel> mProductList) {
        this.context = mContext;
        this.productList = mProductList;
    }


    @NonNull
    @Override
    public SuggestionProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SuggestionProductsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_suggestion_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionProductsViewHolder holder, int position) {
        GetProductModel item = productList.get(position);
        if (item.getImages() != null) {
            Glide.with(context).load(item.getImages().get(0).getSrc()).into(holder.ivProduct);
        }

        if (!item.getStockStatus().equals("instock")) {
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        holder.tvProductName.setText(item.getName());
        holder.ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_DETAIL", productList.get(position));
                intent.putExtra("ID", productList.get(0).getCategories().get(0).getId());
                Toast.makeText(context, "CAT ID: "+productList.get(0).getCategories().get(0).getId(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class SuggestionProductsViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct;
        TextView tvProductName;

        public SuggestionProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.iv_suggestion_product);
            tvProductName = itemView.findViewById(R.id.tv_suggestion_product);
        }
    }
}
