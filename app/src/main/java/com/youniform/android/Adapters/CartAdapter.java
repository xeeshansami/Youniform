package com.youniform.android.Adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.Interface.CartCountClick;
import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Model.CartModel;
import com.youniform.android.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ItemClick itemClick;
    private List<CartModel> cartModelList;
    private Context context;
    private CartDataBase helper;
    private CartCountClick cartCountClick;

    public CartAdapter(List<CartModel> cartModelList, Context context, CartCountClick cartCountClick, ItemClick itemClick) {
        this.cartModelList = cartModelList;
        this.context = context;
        this.itemClick = itemClick;
        this.cartCountClick = cartCountClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_bagProdname.setText(cartModelList.get(position).getName());
        holder.tv_quantity.setText(String.valueOf(cartModelList.get(position).getQuantity()));
        holder.tv_bagProdPrice.setText("RS: " + cartModelList.get(position).getPrice());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (cartModelList.get(position).getColor() == null && !(cartModelList.get(position).getSize() == null)) {
                holder.tv_bagProdPrice.setText(Html.fromHtml("<p><b>Rs:</b> " + cartModelList.get(position).getPrice() + "</p>"
                        + "<p><b> Size: </b>" + cartModelList.get(position).getSize() + "</p>", Html.FROM_HTML_MODE_COMPACT));
                Log.d("TAG", "onBindViewHolder:4 ");
            } else if (cartModelList.get(position).getSize() == null && !(cartModelList.get(position).getColor() == null)) {
                holder.tv_bagProdPrice.setText(Html.fromHtml("<p><b>Rs:</b> " + cartModelList.get(position).getPrice() + "</p>"
                        + "<p><b>Color: </b>" + cartModelList.get(position).getColor() + "</p>", Html.FROM_HTML_MODE_COMPACT));
                Log.d("TAG", "onBindViewHolder:3 ");
            } else if ((cartModelList.get(position).getColor() == null) && (cartModelList.get(position).getSize() == null)) {
                holder.tv_bagProdPrice.setText(Html.fromHtml("<p><b>Rs:</b> " + cartModelList.get(position).getPrice() + "</p>", Html.FROM_HTML_MODE_COMPACT));
                Log.d("TAG", "onBindViewHolder:2 ");
            } else {
                holder.tv_bagProdPrice.setText(Html.fromHtml(("<p><b>Rs:</b> " + cartModelList.get(position).getPrice() + "</p>"
                        + "<p><b>Color:</b> " + cartModelList.get(position).getColor() + "</p>" +
                        "<p><b>Size:</b> " + cartModelList.get(position).getSize() + "</p>"), Html.FROM_HTML_MODE_COMPACT));
                Log.d("TAG", "onBindViewHolder:1 ");
            }

        } else {
            if (cartModelList.get(position).getColor() == null) {
                holder.tv_bagProdPrice.setText(Html.fromHtml("<p><b>Rs:</b> " + cartModelList.get(position).getPrice() + "</p>"
                        + "<p><b> Size: </b>" + cartModelList.get(position).getSize() + "</p>"));
            } else if (cartModelList.get(position).getSize() == null) {
                holder.tv_bagProdPrice.setText(Html.fromHtml("<p><b>Rs:</b> " + cartModelList.get(position).getPrice() + "</p>"
                        + "<p><b>Color: </b>" + cartModelList.get(position).getColor() + "</p>"));
            } else if ((cartModelList.get(position).getColor() == null) && (cartModelList.get(position).getSize() == null)) {
                holder.tv_bagProdPrice.setText(Html.fromHtml("<p><b>Rs:</b> " + cartModelList.get(position).getPrice() + "</p>"));
            } else {
                holder.tv_bagProdPrice.setText(Html.fromHtml("<p><b>Rs:</b> " + cartModelList.get(position).getPrice() + "</p>"
                        + "<p><b>Color:</b> " + cartModelList.get(position).getColor() + "</p>" +
                        "<p><b>Size:</b> " + cartModelList.get(position).getSize() + "</p>"));

            }


        }


        holder.itemView.setOnClickListener(v -> {
            itemClick.itemClick2(position, cartModelList.get(position).getProducdid());

        });
        Glide.with(context).load(cartModelList.get(position).getImage()).into(holder.iv_bagimage);

        holder.ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = cartModelList.get(position).getQuantity();
                int newValue = oldValue + 1;
                cartCountClick.countClick(position, oldValue, newValue);
            }
        });
        holder.ib_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = cartModelList.get(position).getQuantity();
                int newValue = oldValue - 1;
                cartCountClick.countClick(position, oldValue, newValue);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_bagProdname, tv_bagProdPrice, tv_quantity, tv_color;
        private ImageView iv_bagimage;
        private ImageButton ib_add, ib_subtract;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_bagProdname = itemView.findViewById(R.id.tv_Prodname);
            tv_bagProdPrice = itemView.findViewById(R.id.tv_ProdPrice);
            iv_bagimage = itemView.findViewById(R.id.iv_image);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            ib_add = itemView.findViewById(R.id.ib_add);
            ib_subtract = itemView.findViewById(R.id.ib_subtract);
            helper = new CartDataBase(context);
        }
    }


}
