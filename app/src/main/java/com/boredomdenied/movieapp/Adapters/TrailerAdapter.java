package com.boredomdenied.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boredomdenied.movieapp.Objects.Trailer;
import com.boredomdenied.movieapp.Utils.OnTrailerClickListener;
import com.boredomdenied.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.CustomViewHolder> {
    private List<Trailer> trailerList;
    private Context mContext;
    private OnTrailerClickListener onTrailerClickListener;

    public TrailerAdapter(Context context, List<Trailer> trailerList) {
        this.trailerList = trailerList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_trailer_items, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Trailer item = trailerList.get(i);

        if (!TextUtils.isEmpty(item.getKeyId())) {
            Picasso.get().load("https://img.youtube.com/vi/" + item.getKeyId() + "/mqdefault.jpg")
                    .error(R.drawable.placeholder)
                    .placeholder(R.color.colorPrimaryDark)
                    .into(customViewHolder.imageView);
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrailerClickListener.onItemClick(item);
            }
        };
        customViewHolder.imageView.setOnClickListener(listener);
    }


    @Override
    public int getItemCount() {
        return (null != trailerList ? trailerList.size() : 0);
    }

    public OnTrailerClickListener getOnTrailerClickListener() {
        return onTrailerClickListener;
    }

    public void setOnTrailerClickListener(OnTrailerClickListener OnTrailerClickListener) {
        this.onTrailerClickListener = OnTrailerClickListener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;


        public CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.poster);

        }
    }
}
