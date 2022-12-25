package com.ilan.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ilan.movieapp.AppExecutors;
import com.ilan.movieapp.models.MovieModel;
import com.ilan.movieapp.response.MovieSearchResponse;
import com.ilan.movieapp.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private MutableLiveData<List<MovieModel>> mMovies;              // Live Data for search
    private MutableLiveData<List<MovieModel>> mMoviesPopular;       // Live Data for popular
    private static MovieApiClient instance;
    private RetrieveMoviesRunnable retrieveMoviesRunnable;          // Global runnable request for search
    private RetrieveMoviesRunnablePopular retrieveMoviesRunnablePopular;   // Global runnable request for popular search

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPopular = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }
    public LiveData<List<MovieModel>> getPopularMovies() { return mMoviesPopular; }

    // These called through classes
    public void searchMoviesApi(String query, int pageNumber) {

        if (retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling Retrofit call
                myHandler.cancel(true);
            }
        }, 5000, TimeUnit.MILLISECONDS);

        // Retrieving data from RestAPI by runnable class

    }

    public void searchPopularMovies(int pageNumber) {

        if (retrieveMoviesRunnablePopular != null) {
            retrieveMoviesRunnablePopular = null;
        }

        retrieveMoviesRunnablePopular = new RetrieveMoviesRunnablePopular(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePopular);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling Retrofit call
                myHandler2.cancel(true);
            }
        }, 5000, TimeUnit.MILLISECONDS);

        // Retrieving data from RestAPI by runnable class

    }

    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response objects
            try {
                Response response = getMovies(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1) {

                        // Sending data to live data
                        // PostValue: used for background thread
                        // SetValue: not for background thread
                        mMovies.postValue(list);

                    } else {
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.d("TAG", "ERROR: " + error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return RequestService.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }

        private void cancelRequest() {
            Log.d("TAG", "Cancelling search request.");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnablePopular implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePopular(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response objects
            try {
                Response response2 = getPopular(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response2.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if (pageNumber == 1) {

                        // Sending data to live data
                        // PostValue: used for background thread
                        // SetValue: not for background thread
                        mMoviesPopular.postValue(list);

                    } else {
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMoviesPopular.postValue(currentMovies);
                    }
                } else {
                    String error = response2.errorBody().string();
                    Log.d("TAG", "ERROR: " + error);
                    mMoviesPopular.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPopular.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getPopular(int pageNumber) {
            return RequestService.getMovieApi().getPopularMovies(
                    Credentials.API_KEY,
                    pageNumber
            );
        }

        private void cancelRequest() {
            Log.d("TAG", "Cancelling search request.");
            cancelRequest = true;
        }
    }

}
