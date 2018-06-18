package com.boredomdenied.movieapp.Objects;


public class Movie {

    private String title;
    private String poster;
    private String heroBackdrop;
    private int movieId;
    private String movieName;
    private String movieDescription;
    private String userRating;
    private String releaseDate;

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