package com.ilan.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ilan.movieapp.models.MovieModel;

public class MovieDetailsActivity extends AppCompatActivity {

    // Widgets
    private ImageView imageMovie;
    private TextView title, description;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageMovie = findViewById(R.id.imgMovieDetails);
        title = findViewById(R.id.txtMovieTitle);
        description = findViewById(R.id.txtMovieDescription);
        ratingBar = findViewById(R.id.ratingBar);

        GetDataFromIntent();
    }

    private void GetDataFromIntent() {
        String fixedImageUrl = "https://image.tmdb.org/t/p/w500/";
        if (getIntent().hasExtra("movie")) {
            MovieModel movieModel = getIntent().getParcelableExtra("movie");

            title.setText(movieModel.getTitle());
            description.setText(movieModel.getOverview());
            ratingBar.setRating(movieModel.getVote_average()/2);

            Glide
                    .with(this)
                    .load(fixedImageUrl + movieModel.getPoster_path())
                    .into(imageMovie);

        }
    }
}