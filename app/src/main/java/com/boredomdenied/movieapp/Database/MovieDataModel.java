package com.boredomdenied.movieapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "movie")
public class MovieDataModel implements Parcelable{

    @NonNull
    @PrimaryKey
    private int movieId;
    @ColumnInfo(name="moviePoster")
    private String moviePoster;
    @ColumnInfo(name="releaseDate")
    private String releaseDate;
    @ColumnInfo(name="userRating")
    private String userRating;
    @ColumnInfo(name="movieDescription")
    private String movieDescription;
    @ColumnInfo(name="heroBackdrop")
    private String heroBackdrop;
    @ColumnInfo(name="movieName")
    private String movieName;

    public MovieDataModel(@NonNull int movieId, String moviePoster, String releaseDate, String userRating, String movieDescription, String heroBackdrop, String movieName) {
        this.movieId = movieId;
        this.moviePoster = moviePoster;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
        this.movieDescription = movieDescription;
        this.heroBackdrop = heroBackdrop;
        this.movieName = movieName;
    }

    @Ignore
    public MovieDataModel() {

    }


    protected MovieDataModel(Parcel in) {
        movieId = in.readInt();
        moviePoster = in.readString();
        releaseDate = in.readString();
        userRating = in.readString();
        movieDescription = in.readString();
        heroBackdrop = in.readString();
        movieName = in.readString();
    }

    public static final Creator<MovieDataModel> CREATOR = new Creator<MovieDataModel>() {
        @Override
        public MovieDataModel createFromParcel(Parcel in) {
            return new MovieDataModel(in);
        }

        @Override
        public MovieDataModel[] newArray(int size) {
            return new MovieDataModel[size];
        }
    };

    @NonNull
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull int movieId) {
        this.movieId = movieId;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getHeroBackdrop() {
        return heroBackdrop;
    }

    public void setHeroBackdrop(String heroBackdrop) {
        this.heroBackdrop = heroBackdrop;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(moviePoster);
        parcel.writeString(releaseDate);
        parcel.writeString(userRating);
        parcel.writeString(movieDescription);
        parcel.writeString(heroBackdrop);
        parcel.writeString(movieName);
    }
}

