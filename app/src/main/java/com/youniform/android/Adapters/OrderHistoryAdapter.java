package com.youniform.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.ItemClick;
import com.youniform.android.Model.OrderHistory.GetOrderHistory;
import com.youniform.android.Model.OrderHistory.LineItem;
import com.youniform.android.R;

import java.util.List;


public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private List<GetOrderHistory> orderHistoryList;
    private Context context;
    private ItemClick itemClick;

    public OrderHistoryAdapter(List<GetOrderHistory> orderHistoryList, Context context, ItemClick itemClick) {
        this.orderHistoryList = orderHistoryList;
        this.context = context;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_track_record, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Utils.changeFormate(holder.tv_orderDate, orderHistoryList.get(position).getDateCreated());
        holder.tv_singleOrderStatus.setText(orderHistoryList.get(position).getStatus());
        holder.tv_finalPrice.setText("RS: " + orderHistoryList.get(position).getTotal());
        holder.tv_orderId.setText("OD - " + String.valueOf(orderHistoryList.get(position).getId()));
        int count = 0;
        if (orderHistoryList.get(position).getLineItems().size() > 0) {
            for (int i = 0; i < orderHistoryList.get(position).getLineItems().size(); i++) {
                LineItem lineItem = orderHistoryList.get(position).getLineItems().get(i);
                if (i == 0) {
                    holder.cardView3.setVisibility(View.VISIBLE);
                    Picasso.get().load(lineItem.getImage()).into(holder.image1);
                }
                if (i == 1) {
                    holder.cardView4.setVisibility(View.VISIBLE);
                    Picasso.get().load(lineItem.getImage()).into(holder.image2);
                }
                if (i == 2) {
                    holder.cardView5.setVisibility(View.VISIBLE);
                    Picasso.get().load(lineItem.getImage()).into(holder.image3);
                }
                if (i == 3) {
                    holder.card6.setVisibility(View.VISIBLE);
                    Picasso.get().load(lineItem.getImage()).into(holder.image4);
                }
                if (i > 3) {
                    holder.image4.setVisibility(View.GONE);
                    count = count + 1;
                    holder.tv_count.setVisibility(View.VISIBLE);
                    holder.tv_count.setText(String.valueOf(count));
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_orderDate, tv_orderId, tv_finalPrice, tv_singleOrderStatus, tv_count;
        private ImageView image1, image2, image3, image4;
        private CardView cardView3, cardView4, cardView5, card6;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_orderDate = itemView.findViewById(R.id.tv_orderDate);
            tv_singleOrderStatus = itemView.findViewById(R.id.tv_singleOrderStatus);
            tv_orderId = itemView.findViewById(R.id.tv_orderhistoryId);
            tv_finalPrice = itemView.findViewById(R.id.tv_finalPrice);
            tv_count = itemView.findViewById(R.id.tv_count);
            cardView3 = itemView.findViewById(R.id.cardView3);
            cardView4 = itemView.findViewById(R.id.cardView4);
            cardView5 = itemView.findViewById(R.id.cardView5);
            card6 = itemView.findViewById(R.id.card6);
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            image3 = itemView.findViewById(R.id.image3);
            image4 = itemView.findViewById(R.id.image4);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.itemClick(getAdapterPosition());
                }
            });
        }
    }


}
