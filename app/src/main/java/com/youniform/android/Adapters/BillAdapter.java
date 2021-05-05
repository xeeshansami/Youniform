package com.youniform.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.Model.CartModel;
import com.youniform.android.R;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private List<CartModel> cartModelList;
    private Context context;
    private CartDataBase helper;
    // private CartCountClick cartCountClick;

    /*    public BillAdapter(List<CartModel> cartModelList, Context context, CartCountClick cartCountClick) {
            this.cartModelList = cartModelList;
            this.context = context;
            this.cartCountClick = cartCountClick;
        }*/
    public BillAdapter(List<CartModel> cartModelList, Context context) {
        this.cartModelList = cartModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_billing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvProductName.setText(cartModelList.get(position).getName() + " (" + cartModelList.get(position).getSize() + ") ");
        holder.tv_bagProdPrice.setText("RS: " + cartModelList.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tv_bagProdPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.itemProductName);
            tv_bagProdPrice = itemView.findViewById(R.id.itemProductPrice);

            helper = new CartDataBase(context);
        }
    }


}
