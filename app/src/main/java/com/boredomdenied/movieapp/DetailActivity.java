package com.boredomdenied.movieapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boredomdenied.movieapp.Adapters.ReviewAdapter;
import com.boredomdenied.movieapp.Adapters.TrailerAdapter;
import com.boredomdenied.movieapp.Database.MovieDataModel;
import com.boredomdenied.movieapp.Objects.Movie;
import com.boredomdenied.movieapp.Objects.Review;
import com.boredomdenied.movieapp.Objects.Trailer;
import com.boredomdenied.movieapp.Utils.OnTrailerClickListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String apiKey = BuildConfig.MOVIE_API_KEY;
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final String TAG = "MovieApp";
    public List<Review> reviewList;
    public List<Trailer> trailerList;
    ImageView poster, hero;
    TextView mTitle, mOverview, mReleaseDate, mUserRating, mTextRating;
    RatingBar mRatingBar;
    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private ProgressBar progressBar;
    private MovieDataViewModel viewModel;
    private MovieDataModel pulledMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        viewModel = ViewModelProviders.of(DetailActivity.this).get(MovieDataViewModel.class);
        trailerRecyclerView = findViewById(R.id.trailer_recycler_view);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView = findViewById(R.id.review_recycler_view);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progress_bar);
        mTitle = findViewById(R.id.title);
        hero = findViewById(R.id.hero);
        poster = findViewById(R.id.poster);
        mOverview = findViewById(R.id.overview);
        mTextRating = findViewById(R.id.tv_rating);
        mUserRating = findViewById(R.id.rating);
        mReleaseDate = findViewById(R.id.release_date);
        mRatingBar = findViewById(R.id.movie_rating);

        final Movie  movie = getIntent().getParcelableExtra("myDataKey");

//        Toast.makeText(DetailActivity.this, (String.valueOf(movie.getMovieName())), Toast.LENGTH_SHORT).show();


        SpeedDialView speedDialView = findViewById(R.id.speedDialFavorite);

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_no_label, R.drawable
                        .ic_heart_black_24dp)
                        .setLabel("Save to Favorites")
                        .setLabelClickable(true)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
                        .create());

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_custom_color, R.drawable
                        .ic_heart_black_24dp)
                        .setLabel("Delete from Favorites")
                        .setLabelClickable(true)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
                        .create());


        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.fab_no_label:

                        viewModel.loadMovieById(movie.getMovieId()).observe(DetailActivity.this, new Observer<MovieDataModel>() {

                            @Override
                            public void onChanged(@Nullable MovieDataModel movie) {
                                if (movie != null) {
                                    Log.d(TAG, "onChanged: " + movie.getMovieId());
                                    pulledMovie = movie;
                                } else {
                                    Log.d(TAG, "onChanged: Null");
                                }
                            }
                        });

                        MovieDataModel movieDataModel = new MovieDataModel(movie.getMovieId(), movie.getPoster(), movie.getReleaseDate(),
                                movie.getUserRating(), movie.getMovieDescription(), movie.getHeroBackdrop(), movie.getMovieName());

                        viewModel.insertMovie(movieDataModel);

                        return false; // true to keep the Speed Dial open

                    case R.id.fab_custom_color:

                        viewModel.loadMovieById(movie.getMovieId()).observe(DetailActivity.this, new Observer<MovieDataModel>() {

                            @Override
                            public void onChanged(@Nullable MovieDataModel movie) {
                                if (movie != null) {
                                    Log.d(TAG, "onChanged: " + movie.getMovieId());
                                    pulledMovie = movie;
                                } else {
                                    Log.d(TAG, "onChanged: Null");
                                }
                            }
                        });

                        MovieDataModel movieDataModel2 = new MovieDataModel(movie.getMovieId(), movie.getPoster(), movie.getReleaseDate(),
                                movie.getUserRating(), movie.getMovieDescription(), movie.getHeroBackdrop(), movie.getMovieName());

                        viewModel.deleteMovie(movieDataModel2);

                        return false; // true to keep the Speed Dial open


                    default:
                        return false;
                }
            }
        });


        final String MOVIE_URL = "https://api.themoviedb.org/3/movie/" + movie.getMovieId() + "/videos?api_key=" + apiKey + "&language=en-US";
        final String REVIEW_URL = "https://api.themoviedb.org/3/movie/" + movie.getMovieId() + "/reviews?api_key=" + apiKey + "&language=en-US";

        new MovieTask().execute(MOVIE_URL);
        new ReviewTask().execute(REVIEW_URL);

        Picasso.get()
                .load(movie.getPoster())
                .placeholder(R.color.colorPrimaryDark)
                .into(poster);

        Picasso.get()
                .load(movie.getHeroBackdrop())
                .placeholder(R.color.colorPrimaryDark)
                .into(hero);

        mTitle.setText(movie.getMovieName());
        mOverview.setText(movie.getMovieDescription());
//        mRatingBar.setRating(Float.valueOf(movie.getUserRating()) / 2);
        mTextRating.setText("Rating:  ");
        mUserRating.setText(movie.getUserRating());
        mReleaseDate.setText("Released:  " + movie.getReleaseDate());

    }

    private void parseTrailerResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            trailerList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                Trailer item = new Trailer();
                item.setKeyId(post.optString("key"));

                trailerList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseReviewResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            reviewList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                Review item = new Review();
                item.setReviewContent(post.optString("content"));

                reviewList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MovieTask extends AsyncTask<String, Void, Integer> {

        public Trailer item;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseTrailerResult(response.toString());
                    result = 1;
                } else {
                    result = 0;
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {

                trailerAdapter = new TrailerAdapter(DetailActivity.this, trailerList);
                trailerRecyclerView.setAdapter(trailerAdapter);
                trailerAdapter.setOnTrailerClickListener(new OnTrailerClickListener() {

                    @Override
                    public void onItemClick(Trailer item) {

                        Intent yt_play = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + item.getKeyId()));
                        Intent yt_trailer = Intent.createChooser(yt_play, "Open With");
                        yt_play.putExtra("force_fullscreen", true);


                        if (yt_play.resolveActivity(getPackageManager()) != null) {
                            startActivity(yt_trailer);
                        } else {
                            Uri.parse(YOUTUBE_URL + item.getKeyId());

                        }

                    }
                });

            } else {
                Toast.makeText(DetailActivity.this, "Failed to Fetch Movies", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ReviewTask extends AsyncTask<String, Void, Integer> {

        public Movie item;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseReviewResult(response.toString());
                    result = 1;
                } else {
                    result = 0;
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {

                reviewAdapter = new ReviewAdapter(DetailActivity.this, reviewList);
                reviewRecyclerView.setAdapter(reviewAdapter);


            } else {
                Toast.makeText(DetailActivity.this, "not found", Toast.LENGTH_SHORT).show();

            }
        }
    }


}
