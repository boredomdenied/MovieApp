package com.boredomdenied.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {
    private List<FeedItem> feedItemList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_items, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final FeedItem feedItem = feedItemList.get(i);

        if (!TextUtils.isEmpty(feedItem.getPoster())) {
            Picasso.get().load(feedItem.getPoster())
                    .error(R.drawable.placeholder)
                    .placeholder(R.color.colorPrimaryDark)
                    .into(customViewHolder.imageView);
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(feedItem);
            }
        };
        customViewHolder.imageView.setOnClickListener(listener);
    }


    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;


        public CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.poster);

        }
    }
}
