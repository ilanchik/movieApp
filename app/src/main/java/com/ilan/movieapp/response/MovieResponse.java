package com.ilan.movieapp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ilan.movieapp.models.MovieModel;

/**
 * Class for single movie request
 */
public class MovieResponse {
    // 1 - Find move object, serialize and deserialize results
    @SerializedName("results")
    @Expose
    private MovieModel movie;

    public void setMovie(MovieModel movie) {
        this.movie = movie;
    }

    public MovieModel getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
