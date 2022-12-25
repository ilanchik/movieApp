package com.ilan.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.ilan.movieapp.adapters.MovieAdapter;
import com.ilan.movieapp.adapters.OnMovieListener;
import com.ilan.movieapp.models.MovieModel;
import com.ilan.movieapp.request.RequestService;
import com.ilan.movieapp.response.MovieSearchResponse;
import com.ilan.movieapp.utils.Credentials;
import com.ilan.movieapp.utils.MovieApi;
import com.ilan.movieapp.viewModels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {
    // Add Network security config

    // Recycler View
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    // ViewModel
    private MovieListViewModel movieListViewModel;

    Boolean isPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // SearchView
        setupSearchView();
        
        recyclerView = findViewById(R.id.recyclerView);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        configureRecyclerView();
        observeChanges();
        observePopularMovies();

        // Get data for popular movies
        movieListViewModel.searchPopularMovies(1);

    }

    private void observePopularMovies() {
        movieListViewModel.getPopularMovies().observe(this, movieModels -> {
            if (movieModels != null) {
                for (MovieModel movieModel : movieModels) {
                    movieAdapter.setmMovies(movieModels);
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Get data from searchView & query the api to get the results
    private void setupSearchView() {
        final SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        // The search string from searchView
                        query,
                        1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(v -> {
            isPopular = false;
        });
    }

    // Observing any data changes
    private void observeChanges() {
        movieListViewModel.getMovies().observe(this, movieModels -> {
            if (movieModels != null) {
                for (MovieModel movieModel : movieModels) {
                    Log.d("TAG", "onChange: " + movieModel.getTitle());

                    movieAdapter.setmMovies(movieModels);
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Initialize recyclerview and adding data to it
    private void configureRecyclerView() {
        // Live data cannot be passed via constructor
        movieAdapter = new MovieAdapter(this);

        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Pagination
        // Loading next page of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    // display next search results on next page of api
                    movieListViewModel.searchNextPage();
                }
            }
        });
    }

    private void getRetrofitResponse() {
        MovieApi movieApi = RequestService.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi
                .searchMovie(Credentials.API_KEY, "Action", 1);

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if (response.code() == 200) {
                    Log.v("TAG", "The response: " + response.body().toString());
                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
                    for (MovieModel movie : movies) {
                        Log.v("TAG", movie.getRelease_date());
                    }
                } else {
                    Log.v("TAG", "ERROR " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });

    }

    private void getRetrofitResponseById() {
        MovieApi movieApi = RequestService.getMovieApi();
        Call<MovieModel> responseCall = movieApi.getMovie(550, Credentials.API_KEY);

        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {
                    MovieModel movie = response.body();
                    Log.d("TAG", "The Response: " + movie.getTitle());
                } else {
                    Log.d("TAG", "ERROR: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        // Toast.makeText(this, "You clicked me", Toast.LENGTH_SHORT).show();
        // Need ID of movie to get details

        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", movieAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

    }
}