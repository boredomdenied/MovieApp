package com.boredomdenied.movieapp.Detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boredomdenied.movieapp.R;
import com.boredomdenied.movieapp.Utils.ReviewFeedItem;

import java.util.List;

public class RecyclerViewReviewAdapter extends RecyclerView.Adapter<RecyclerViewReviewAdapter.CustomViewHolder> {
    private List<ReviewFeedItem> feedItemList;
    private Context mContext;

    public RecyclerViewReviewAdapter(Context context, List<ReviewFeedItem> feedItemList) {
        this.feedItemList = feedItemList;
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
        final ReviewFeedItem feedItem = feedItemList.get(i);
        customViewHolder.textView.setText(feedItem.getReviewContent());

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;


        public CustomViewHolder(View view) {
            super(view);
            this.textView = view.findViewById(R.id.review);

        }
    }
}
