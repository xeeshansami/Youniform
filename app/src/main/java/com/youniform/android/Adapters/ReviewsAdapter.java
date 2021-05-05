package com.youniform.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youniform.android.Model.ReviewsModel;
import com.youniform.android.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<ReviewsModel> reviewsModelList;
    private Context context;

    public ReviewsAdapter(List<ReviewsModel> reviewsModelList, Context context) {
        this.reviewsModelList = reviewsModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_reviews, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_UserName.setText(reviewsModelList.get(position).getReviewer());
        holder.tv_review.setText(reviewsModelList.get(position).getReview());
        holder.userRating.setRating(Float.parseFloat(String.valueOf(reviewsModelList.get(position).getRating())));
        Glide.with(context).load(reviewsModelList.get(position).getReviewerAvatar()).into(holder.iv_user);
    }

    @Override
    public int getItemCount() {
        return reviewsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_UserName, tv_review;
        private CircleImageView iv_user;
        private RatingBar userRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_UserName = itemView.findViewById(R.id.tv_UserName);
            tv_review = itemView.findViewById(R.id.tv_review);
            userRating = itemView.findViewById(R.id.userRating);
            iv_user = itemView.findViewById(R.id.iv_user);

        }
    }


}
