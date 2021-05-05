package com.youniform.android.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Model.ShiipingAddress.Shippingaddress;
import com.youniform.android.R;

import java.util.List;

import static com.youniform.android.R.drawable.ic_work;


public class ShippingAddresAdapter extends RecyclerView.Adapter<ShippingAddresAdapter.ViewHolder> {

    private List<Shippingaddress> shippingaddressList;
    private Context context;
    private String vcType;
    private ItemClick itemClick;

    public ShippingAddresAdapter(List<Shippingaddress> shippingaddressList, Context context, String vcType, ItemClick itemClick) {
        this.shippingaddressList = shippingaddressList;
        this.context = context;
        this.vcType = vcType;
        this.itemClick = itemClick;
    }

    public ShippingAddresAdapter(List<Shippingaddress> shippingaddressList, Context context, String vcType) {
        this.shippingaddressList = shippingaddressList;
        this.context = context;
        this.vcType = vcType;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_shipping_address, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_addressName.setText(shippingaddressList.get(position).getFirstName());
        Log.d("TAG", "onBindViewHolder:1 " + shippingaddressList.get(position).getCategory());
        Log.d("TAG", "onBindViewHolder: 2" + shippingaddressList.get(position).getAddress1());
        Log.d("TAG", "onBindViewHolder: 3" + shippingaddressList.get(position).getAddress2());
        Log.d("TAG", "onBindViewHolder: 4" + shippingaddressList.get(position).getCity());
        Log.d("TAG", "onBindViewHolder: 5" + shippingaddressList.get(position).getPostcode());
        Log.d("TAG", "onBindViewHolder: 6" + shippingaddressList.get(position).getState());
        Log.d("TAG", "onBindViewHolder: 7" + shippingaddressList.get(position).getFirstName());
        Log.d("TAG", "onBindViewHolder: 8" + shippingaddressList.get(position).getLastName());

        if (shippingaddressList.get(position).getCategory().equals("Home")) {
            holder.iv_category.setImageResource(R.drawable.ic_home);
        } else {
            holder.iv_category.setImageResource(ic_work);
        }
        if (shippingaddressList.get(position).getAddress2() != null) {
            holder.tv_address.setText(shippingaddressList.get(position).getAddress1() + " " + shippingaddressList.get(position).getAddress2());
        } else {
            holder.tv_address.setText(shippingaddressList.get(position).getAddress1() + " " +
                    "" + shippingaddressList.get(position).getCity() + " " + shippingaddressList.get(position).getCity() + " " +
                    "" + shippingaddressList.get(position).getCountry() + "" +
                    " " + shippingaddressList.get(position).getAddress2());
        }
        if (vcType.endsWith("ACCOUNT")) {
            holder.rb_selectAddress.setVisibility(View.INVISIBLE);
        }
        if (vcType.endsWith("SHIPPING")) {
            holder.rb_selectAddress.setVisibility(View.VISIBLE);
        }
        if (vcType.endsWith("BILLING")) {
            holder.rb_selectAddress.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return shippingaddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_category;
        private TextView tv_addressName, tv_address;
        private RadioButton rb_selectAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_addressName = itemView.findViewById(R.id.tv_addressName);
            tv_address = itemView.findViewById(R.id.tv_address);
            rb_selectAddress = itemView.findViewById(R.id.rb_selectAddress);
            iv_category = itemView.findViewById(R.id.iv_category);
            rb_selectAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        itemClick.itemClick(getAdapterPosition());
                    }
                }
            });
        }
    }


}
