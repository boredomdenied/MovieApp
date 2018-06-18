package com.boredomdenied.movieapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<MovieDataModel>> loadAllMovies();

    @Insert(onConflict = REPLACE)
    void insertMovie(MovieDataModel movieDataModel);

    @Delete
    void deleteMovie(MovieDataModel movieDataModel);

    @Query("SELECT * FROM movie WHERE movieId = :movieId")
    LiveData<MovieDataModel> loadMovieById(int movieId);
}
