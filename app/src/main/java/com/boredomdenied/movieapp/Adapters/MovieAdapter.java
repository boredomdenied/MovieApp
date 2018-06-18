package com.boredomdenied.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boredomdenied.movieapp.Objects.Movie;
import com.boredomdenied.movieapp.Utils.OnMovieClickListener;
import com.boredomdenied.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CustomViewHolder> {
    private List<Movie> movieList;
    private Context mContext;
    private OnMovieClickListener onMovieClickListener;

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.movieList = movieList;
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
        final Movie movie = movieList.get(i);

        if (!TextUtils.isEmpty(movie.getPoster())) {
            Picasso.get().load(movie.getPoster())
                    .resize(185,277)
                    .error(R.drawable.placeholder)
                    .placeholder(R.color.colorPrimaryDark)
                    .into(customViewHolder.imageView);
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMovieClickListener.onItemClick(movie);
            }
        };
        customViewHolder.imageView.setOnClickListener(listener);
    }


    @Override
    public int getItemCount() {
        return (null != movieList ? movieList.size() : 0);
    }

    public OnMovieClickListener getOnMovieClickListener() {
        return onMovieClickListener;
    }

    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;


        public CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.poster);

        }
    }
}
