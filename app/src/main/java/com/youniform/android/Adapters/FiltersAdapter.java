package com.youniform.android.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Interface.FilterClick;
import com.youniform.android.Model.Filter.Attribute;
import com.youniform.android.Model.Filter.Term;
import com.youniform.android.R;

import java.util.List;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> implements FilterClick {

    private List<Attribute> attributeList;

    private FilterClick filterClick;
    private Context context;


    public FiltersAdapter(List<Attribute> attributeList, FilterClick filterClick, Context context) {
        this.attributeList = attributeList;
        this.filterClick = filterClick;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_filter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_filterType.setText(attributeList.get(position).getName());
        List<Term> termList = attributeList.get(position).getTerms();
        FiltersItemsAdapter filtersItemsAdapter = new FiltersItemsAdapter(termList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.rv_filterItems.setLayoutManager(layoutManager);
        holder.rv_filterItems.setAdapter(filtersItemsAdapter);
        holder.itemView.setOnClickListener(v -> {
            if (holder.rv_filterItems.getVisibility() == View.VISIBLE) {
                holder.rv_filterItems.setVisibility(View.GONE);
                holder.iv_expend.setImageResource(R.drawable.ic_add);
            } else {
                holder.rv_filterItems.setVisibility(View.VISIBLE);
                holder.iv_expend.setImageResource(R.drawable.ic_minimize);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attributeList.size();
    }

    @Override
    public void filterClick(String attribute, int attribute_term) {
        filterClick.filterClick(attribute, attribute_term);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_filterType;
        private RecyclerView rv_filterItems;
        private ImageView iv_expend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_filterType = itemView.findViewById(R.id.tv_filterType);
            rv_filterItems = itemView.findViewById(R.id.rv_filterItems);
            iv_expend = itemView.findViewById(R.id.iv_expend);
        }
    }


}
