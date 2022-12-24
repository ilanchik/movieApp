package com.ilan.movieapp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ilan.movieapp.models.MovieModel;

import java.util.List;

/**
 * Class is for getting multiple movies (Movies list) - popular movies
 */
public class MovieSearchResponse {

    @SerializedName("total_results")
    @Expose // serialize and deserialize
    private int total_count;

    @SerializedName("results")
    @Expose
    private List<MovieModel> movies;

    public int getTotal_count() {
        return total_count;
    }

    public List<MovieModel> getMovies() {
        return movies;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public void setMovies(List<MovieModel> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "total_count=" + total_count +
                ", movies=" + movies +
                '}';
    }
}
