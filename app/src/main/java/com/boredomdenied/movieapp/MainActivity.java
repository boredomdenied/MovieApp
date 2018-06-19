package com.boredomdenied.movieapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boredomdenied.movieapp.Adapters.MovieAdapter;
import com.boredomdenied.movieapp.Adapters.MovieDataModelAdapter;
import com.boredomdenied.movieapp.Database.MovieDataModel;
import com.boredomdenied.movieapp.Objects.Movie;
import com.boredomdenied.movieapp.Utils.OnMovieClickListener;
import com.facebook.stetho.Stetho;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {


    private static final String apiKey = BuildConfig.MOVIE_API_KEY;
    private static final String TAG = "MovieApp";
    private static final String POSTER_URL = "http://image.tmdb.org/t/p/w185";
    private static final String BACKDROP_URL = "http://image.tmdb.org/t/p/original";
    final String topRatedUrl = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + apiKey + "&language=en-US";
    public ArrayList<Movie> movieList;
    public boolean useFavorites;
    String mostPopularUrl = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&language=en-US";
    private MovieDataModelAdapter mMovieDataModelAdapter;
    private TextView nullTextView;
    private RecyclerView mRecyclerView;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private MovieDataViewModel viewModel;
    private NetworkInfo networkInfo;
    private ConnectivityManager conMan;
    public Parcelable mListState;
    private String noInternet = "No internet connectivity. Only displaying your Favorie Movies.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate called.");
        //       Stetho.initializeWithDefaults(this);

        int spanCount;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 6;
        } else {
            spanCount = 3;
        }

        viewModel = ViewModelProviders.of(MainActivity.this).get(MovieDataViewModel.class);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        progressBar = findViewById(R.id.progress_bar);
        nullTextView = findViewById(R.id.null_view);
        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = conMan.getActiveNetworkInfo();


        final String nowPlayingUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + apiKey + "&language=en-US";

        if (savedInstanceState != null && savedInstanceState.getBoolean("useFavorites")) {
            progressBar.setVisibility(View.GONE);
            getFavoriteMovies();
            useFavorites = true;
        } else if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                new MovieTask().execute(nowPlayingUrl);
            } else {
                Toast.makeText(getApplicationContext(), noInternet, Toast.LENGTH_LONG).show();
                getFavoriteMovies();
                useFavorites = true;
        }



        SpeedDialView speedDialView = findViewById(R.id.speedDial);

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_no_label, R.drawable
                        .ic_heart_black_24dp)
                        .setLabel("My Favorites")
                        .setLabelClickable(true)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
                        .create());

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_custom_color, R.drawable
                        .ic_heart_black_24dp)
                        .setLabel("Popular Movies")
                        .setLabelClickable(true)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
                        .create());

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_long_label, R.drawable
                        .ic_heart_black_24dp)
                        .setLabel("Top Rated")
                        .setLabelClickable(true)
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
                        .create());


        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                Log.d(TAG, "speedDialActionItem called");
                switch (speedDialActionItem.getId()) {
                    case R.id.fab_no_label:
                        useFavorites = true;
                        getFavoriteMovies();
                        return false; // true to keep the Speed Dial open
                    case R.id.fab_custom_color:
                        useFavorites = false;
                        new MovieTask().execute(mostPopularUrl);
                        return false; // true to keep the Speed Dial open
                    case R.id.fab_long_label:
                        useFavorites = false;
                        new MovieTask().execute(topRatedUrl);
                        return false; // true to keep the Speed Dial open
                    default:
                        return false;
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called.");
        if (mListState != null)
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume called.");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called.");
        if (mListState != null)
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);

    }

    public void getFavoriteMovies() {
        Log.d(TAG, "getFavoriteMovies called.");

        MovieDataViewModel viewModel = ViewModelProviders.of(MainActivity.this).get(MovieDataViewModel.class);
        viewModel.loadAllMovies().observe(MainActivity.this, new Observer<List<MovieDataModel>>() {
            @Override
            public void onChanged(@Nullable List<MovieDataModel> allMovies) {

                mMovieDataModelAdapter = new MovieDataModelAdapter(MainActivity.this, allMovies);
                if (allMovies != null) {
                    if (allMovies.size() == 0) {
                        nullTextView.setVisibility(View.VISIBLE);
                        nullTextView.setText(R.string.app_name);
                    }
                }

                mRecyclerView.setAdapter(mMovieDataModelAdapter);

            }
        });
    }

    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            movieList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                Movie item = new Movie();
                item.setPoster(POSTER_URL + post.optString("poster_path"));
                item.setHeroBackdrop(BACKDROP_URL + post.optString("backdrop_path"));
                item.setMovieName(post.optString("original_title"));
                item.setMovieDescription(post.optString("overview"));
                item.setUserRating(post.optString("vote_average"));
                item.setReleaseDate(post.optString("release_date"));
                item.setMovieId(post.optInt("id"));


                movieList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState Called." + useFavorites);
        if (!useFavorites) {
            savedInstanceState.putBoolean("useFavorites", false);
        } else {
            savedInstanceState.putBoolean("useFavorites", true);
        }

        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable("mRecyclerView", mListState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    if (mListState != null)
        mListState = savedInstanceState.getParcelable("mRecyclerView");

    }

    public class MovieTask extends AsyncTask<String, Void, Integer> {

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
                    parseResult(response.toString());
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

                adapter = new MovieAdapter(MainActivity.this, movieList);
                mRecyclerView.setAdapter(adapter);
                adapter.setOnMovieClickListener(new OnMovieClickListener() {
                    @Override
                    public void onItemClick(Movie item) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("poster", item.getPoster());
                        intent.putExtra("hero_backdrop", item.getHeroBackdrop());
                        intent.putExtra("original_title", item.getMovieName());
                        intent.putExtra("overview", item.getMovieDescription());
                        intent.putExtra("vote_average", item.getUserRating());
                        intent.putExtra("release_date", item.getReleaseDate());
                        intent.putExtra("movie_id", item.getMovieId());
                        startActivity(intent);

                    }
                });

            } else {
                Toast.makeText(MainActivity.this, "Failed to Fetch Movies", Toast.LENGTH_SHORT).show();
            }
        }
    }
}