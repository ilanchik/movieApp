package com.ilan.movieapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ilan.movieapp.models.MovieModel;
import com.ilan.movieapp.request.MovieApiClient;

import java.util.List;

// Note: Singleton pattern
public class MovieRepository {

    private static MovieRepository instance;

    private MovieApiClient movieApiClient;

    private String mQuery;
    private int mPageNumber;

    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository() {
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movieApiClient.getMovies();
    }
    public LiveData<List<MovieModel>> getPopularMovies() {
        return movieApiClient.getPopularMovies();
    }

    public void searchMovieApi(String query, int pageNumber) {
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApi(query, pageNumber);
    }

    public void searchPopularMovies(int pageNumber) {
        mPageNumber = pageNumber;
        movieApiClient.searchPopularMovies(pageNumber);
    }

    public void searchNextPage() {
        searchMovieApi(mQuery, mPageNumber + 1);
    }

}
