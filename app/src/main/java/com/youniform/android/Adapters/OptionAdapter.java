package com.youniform.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Interface.OptionClickListener;
import com.youniform.android.R;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {

    private Context context;
    private List<String> optionList;
    private OptionClickListener listener;

    public OptionAdapter(Context mContext, List<String> mOptionList, OptionClickListener mListener) {
        this.context = mContext;
        this.optionList = mOptionList;
        this.listener = mListener;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OptionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_options, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        String item = optionList.get(position);

        holder.tvProductName.setText(item);

        holder.itemView.setOnClickListener(v -> listener.onOptionItemClick(position));
        holder.clickable.setOnClickListener(v -> listener.onOptionItemClick(position));
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName;
        LinearLayout clickable;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tv_option);
            clickable = itemView.findViewById(R.id.clickable);
        }
    }
}

