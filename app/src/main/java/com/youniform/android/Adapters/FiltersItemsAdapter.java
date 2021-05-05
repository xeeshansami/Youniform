package com.youniform.android.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Interface.FilterClick;
import com.youniform.android.Model.Filter.Term;
import com.youniform.android.R;

import java.util.List;


public class FiltersItemsAdapter extends RecyclerView.Adapter<FiltersItemsAdapter.ViewHolder> {

    private List<Term> termList;
    private FilterClick filterClick;

    public FiltersItemsAdapter(List<Term> termList, FilterClick filterClick) {
        this.termList = termList;
        this.filterClick = filterClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_checkfilter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cb_items.setText(termList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return termList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb_items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb_items = itemView.findViewById(R.id.cb_items);
            cb_items.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    filterClick.filterClick(termList.get(getAdapterPosition()).getTaxonomy(), termList.get(getAdapterPosition()).getTermId());
                }
            });

        }
    }


}
