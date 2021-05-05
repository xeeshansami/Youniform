package com.youniform.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.youniform.android.Interface.PlaceAutoCompleteInterface;
import com.youniform.android.Model.Searched.PlaceAutocomplete;
import com.youniform.android.R;

import java.util.ArrayList;


public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.PlaceViewHolder> {

    ArrayList<PlaceAutocomplete> mResultList;
    Context mContext;
    PlaceAutoCompleteInterface mListener;
    private int layout;

    public PlaceAutocompleteAdapter(ArrayList<PlaceAutocomplete> mResultList, Context mContext, int layout) {
        this.mResultList = mResultList;
        this.mContext = mContext;
        this.layout = layout;
        this.mListener = (PlaceAutoCompleteInterface) mContext;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, viewGroup, false);
        PlaceViewHolder mPredictionHolder = new PlaceViewHolder(convertView);
        return mPredictionHolder;
    }


    @Override
    public void onBindViewHolder(PlaceViewHolder mPredictionHolder, final int i) {
        mPredictionHolder.tvDescription.setText(mResultList.get(i).description);
        mPredictionHolder.tvTitle.setText(mResultList.get(i).placename);
        mPredictionHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlaceClick(mResultList, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        public CardView mParentLayout;
        public TextView tvTitle, tvDescription;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            mParentLayout = itemView.findViewById(R.id.predictedRow);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

    }


}