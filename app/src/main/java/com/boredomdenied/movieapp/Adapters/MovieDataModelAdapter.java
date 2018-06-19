package com.boredomdenied.movieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boredomdenied.movieapp.Database.MovieDataModel;
import com.boredomdenied.movieapp.DetailActivity;
import com.boredomdenied.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDataModelAdapter extends RecyclerView.Adapter<MovieDataModelAdapter.CustomViewHolder> {
    private List<MovieDataModel> movieList;
    private Context mContext;
    private static final String TAG = "MovieDataModelAdapter";


    public MovieDataModelAdapter(Context context, List<MovieDataModel> movieList) {
        this.movieList = movieList;
        this.mContext = context;
    }

    @Override
    public MovieDataModelAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_items, null);
        MovieDataModelAdapter.CustomViewHolder viewHolder = new MovieDataModelAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieDataModelAdapter.CustomViewHolder customViewHolder, int i) {
        final MovieDataModel movie = movieList.get(i);

        if (!TextUtils.isEmpty(movie.getMoviePoster())) {
            Log.d(TAG, "onBindViewHolder called");

            Picasso.get().load(movie.getMoviePoster())
                    .resize(185,277)
                    .error(R.drawable.placeholder)
                    .placeholder(R.color.colorPrimaryDark)
                    .into(customViewHolder.imageView);
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("poster", movie.getMoviePoster());
                intent.putExtra("hero_backdrop", movie.getHeroBackdrop());
                intent.putExtra("original_title", movie.getMovieName());
                intent.putExtra("overview", movie.getMovieDescription());
                intent.putExtra("vote_average", movie.getUserRating());
                intent.putExtra("release_date", movie.getReleaseDate());
                intent.putExtra("movie_id", movie.getMovieId());
                mContext.startActivity(intent);
            }
        };
        customViewHolder.imageView.setOnClickListener(listener);
    }


    @Override
    public int getItemCount() {
        return (null != movieList ? movieList.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;


        public CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.poster);

        }
    }
}
