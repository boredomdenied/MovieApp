package com.boredomdenied.movieapp.Detail;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boredomdenied.movieapp.BuildConfig;
import com.boredomdenied.movieapp.Utils.FeedItem;
import com.boredomdenied.movieapp.Utils.OnItemClickListener;
import com.boredomdenied.movieapp.R;
import com.boredomdenied.movieapp.Utils.FeedItem;
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
    public List<FeedItem> feedsList;
    public List<FeedItem> feedList;
    ImageView poster, hero;
    TextView mTitle, mOverview, mReleaseDate, mUserRating, mTextRating;
    RatingBar mRatingBar;
    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;
    private RecyclerViewTrailerAdapter trailerAdapter;
    private RecyclerViewReviewAdapter reviewAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        String movieId = getIntent().getExtras().getString("movie_id");
        String Poster = getIntent().getExtras().getString("poster");
        String heroBackdrop = getIntent().getExtras().getString("hero_backdrop");
        String movieName = getIntent().getExtras().getString("original_title");
        String movieDescription = getIntent().getExtras().getString("overview");
        String userRating = getIntent().getExtras().getString("vote_average");
        String releaseDate = getIntent().getExtras().getString("release_date");

        final String MOVIE_URL = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + apiKey + "&language=en-US";
        final String REVIEW_URL = "https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" + apiKey + "&language=en-US";

               new MovieTask().execute(MOVIE_URL);
               new ReviewTask().execute(REVIEW_URL);

        Picasso.get()
                .load(Poster)
                .placeholder(R.color.colorPrimaryDark)
                .into(poster);

        Picasso.get()
                .load(heroBackdrop)
                .placeholder(R.color.colorPrimaryDark)
                .into(hero);

        mTitle.setText(movieName);
        mOverview.setText(movieDescription);
        mRatingBar.setRating(Float.valueOf(userRating) / 2);
        mTextRating.setText("Rating:  ");
        mUserRating.setText(userRating);
        mReleaseDate.setText("Released:  " + releaseDate);

    }

    private void parseTrailerResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
                item.setKeyId(post.optString("key"));

                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseReviewResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            feedList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
                item.setReviewContent(post.optString("content"));

                feedList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MovieTask extends AsyncTask<String, Void, Integer> {

        public FeedItem item;

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

                trailerAdapter = new RecyclerViewTrailerAdapter(DetailActivity.this, feedsList);
                trailerRecyclerView.setAdapter(trailerAdapter);
                trailerAdapter.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(FeedItem item) {

                        Intent yt_play = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + item.getKeyId()));
                        Intent yt_trailer = Intent.createChooser(yt_play, "Open With");
                        yt_play.putExtra("force_fullscreen", true);


                        if (yt_play.resolveActivity(getPackageManager()) != null) {
                            startActivity(yt_trailer);
                        } else {
                            Uri.parse("http://www.youtube.com/watch?v=" + item.getKeyId());

                        }

                    }
                });

            } else {
                Toast.makeText(DetailActivity.this, "Failed to Fetch Movies", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ReviewTask extends AsyncTask<String, Void, Integer> {

        public FeedItem item;

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

                reviewAdapter = new RecyclerViewReviewAdapter(DetailActivity.this, feedList);
                reviewRecyclerView.setAdapter(reviewAdapter);


            } else {
                Toast.makeText(DetailActivity.this, "not found", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
