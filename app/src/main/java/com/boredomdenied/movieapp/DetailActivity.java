package com.boredomdenied.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView poster, hero;
    TextView mTitle, mOverview, mReleaseDate, mUserRating, mTextRating;
    RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String Poster = getIntent().getExtras().getString("poster");
        String heroBackdrop = getIntent().getExtras().getString("hero_backdrop");
        String movieName = getIntent().getExtras().getString("original_title");
        String movieDescription = getIntent().getExtras().getString("overview");
        String userRating = getIntent().getExtras().getString("vote_average");
        String releaseDate = getIntent().getExtras().getString("release_date");


        mTitle = findViewById(R.id.title);
        hero = findViewById(R.id.hero);
        poster = findViewById(R.id.poster);
        mOverview = findViewById(R.id.overview);

        mTextRating = findViewById(R.id.tv_rating);
        mUserRating = findViewById(R.id.rating);
        mReleaseDate = findViewById(R.id.release_date);
        mRatingBar = findViewById(R.id.movie_rating);

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
        mRatingBar.setRating(Float.valueOf(userRating)/2);
        mTextRating.setText("Rating:  ");
        mUserRating.setText(userRating);
        mReleaseDate.setText("Released:  " + releaseDate);


    }
}
