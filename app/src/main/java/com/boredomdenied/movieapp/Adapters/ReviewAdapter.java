package com.boredomdenied.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boredomdenied.movieapp.Objects.Review;
import com.boredomdenied.movieapp.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder> {
    private List<Review> reviewList;
    private Context mContext;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.reviewList = reviewList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_review_items, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Review review = reviewList.get(i);
        customViewHolder.textView.setText(review.getReviewContent());

    }

    @Override
    public int getItemCount() {
        return (null != reviewList ? reviewList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;


        public CustomViewHolder(View view) {
            super(view);
            this.textView = view.findViewById(R.id.review);

        }
    }
}
