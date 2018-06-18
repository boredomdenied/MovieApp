package com.boredomdenied.movieapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.boredomdenied.movieapp.Database.MovieDataModel;
import com.boredomdenied.movieapp.Database.MovieDatabase;

import java.util.List;

public class MovieDataViewModel extends AndroidViewModel {

    private static final String TAG = MovieDataViewModel.class.getSimpleName();
    private final LiveData<List<MovieDataModel>> mAllMovies;
    private MovieDatabase movieDatabase;

    public MovieDataViewModel(Application application) {
        super(application);

        movieDatabase = MovieDatabase.getInstance(this.getApplication());
        mAllMovies = movieDatabase.movieDao().loadAllMovies();
        Log.d(TAG, "MovieDataViewModel: Room Db loaded");
    }

    public LiveData<List<MovieDataModel>> loadAllMovies() {
        return mAllMovies;
    }

    public LiveData<MovieDataModel> loadMovieById(int movieId) {
        return movieDatabase.movieDao().loadMovieById(movieId);
    }

    public void insertMovie(MovieDataModel MovieDataModel) {
        Log.d(TAG, "insertMovie being accessed.");
        new insertAsyncTask(movieDatabase).execute(MovieDataModel);
    }

    public void deleteMovie(MovieDataModel MovieDataModel) {
        Log.d(TAG, "deleteMovie being accessed.");
        new deleteAsyncTask(movieDatabase).execute(MovieDataModel);
    }

    private static class insertAsyncTask extends AsyncTask<MovieDataModel, Void, Void> {
        private MovieDatabase db;

        insertAsyncTask(MovieDatabase movieDatabase) {
            db = movieDatabase;
        }

        @Override
        protected Void doInBackground(final MovieDataModel... params) {
            db.movieDao().insertMovie(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<MovieDataModel, Void, Void> {
        private MovieDatabase db;

        deleteAsyncTask(MovieDatabase movieDatabase) {
            db = movieDatabase;
        }

        @Override
        protected Void doInBackground(final MovieDataModel... params) {
            db.movieDao().deleteMovie(params[0]);
            return null;
        }
    }
}
