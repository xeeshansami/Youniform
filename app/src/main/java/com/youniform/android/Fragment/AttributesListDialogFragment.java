package com.youniform.android.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

public class AttributesListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    List<String> stringList = new ArrayList<>();
    RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    public static AttributesListDialogFragment newInstance() {
        return new AttributesListDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attributes_list_dialog, container, false);
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            stringList = getArguments().getStringArrayList("ATTRIBUTES_LIST");
            recyclerView.setAdapter(new ItemAdapter(stringList));
        }

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();

        super.onCreate(savedInstanceState);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.attributes_list_item, parent, false));
            text = itemView.findViewById(R.id.text);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<String> stringList;

        public ItemAdapter(List<String> stringList) {
            this.stringList = stringList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(stringList.get(position));
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

    }

}