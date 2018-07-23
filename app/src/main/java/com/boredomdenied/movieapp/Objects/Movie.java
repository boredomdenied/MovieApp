package com.boredomdenied.movieapp.Objects;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String title;
    private String poster;
    private String heroBackdrop;
    private int movieId;
    private String movieName;
    private String movieDescription;
    private String userRating;
    private String releaseDate;

    public Movie(Parcel in) {
        title = in.readString();
        poster = in.readString();
        heroBackdrop = in.readString();
        movieId = in.readInt();
        movieName = in.readString();
        movieDescription = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }

    public Movie(String title, String poster, String heroBackdrop, int movieId, String movieName, String movieDescription, String userRating, String releaseDate) {
        this.title = title;
        this.poster = poster;
        this.heroBackdrop = heroBackdrop;
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public Movie() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster);

        dest.writeString(heroBackdrop);
        dest.writeInt(movieId);
        dest.writeString(movieName);
        dest.writeString(movieDescription);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getHeroBackdrop() {
        return heroBackdrop;
    }

    public void setHeroBackdrop(String heroBackdrop) { this.heroBackdrop = heroBackdrop; }

    public String getMovieName() { return movieName; }

    public void setMovieName(String movieName) { this.movieName = movieName; }

    public String getMovieDescription() { return movieDescription; }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription; }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getMovieId() { return movieId; }

    public void setMovieId(int movieId) { this.movieId = movieId; }
    
    
}