package com.ilan.movieapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilan.movieapp.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Widgets
    TextView title, releaseDate, duration;
    ImageView movieImage;
    RatingBar ratingBar;

    // Click listener
    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;

        title = itemView.findViewById(R.id.txtMovieTitle);
        releaseDate = itemView.findViewById(R.id.txtMovieCategory);
        duration = itemView.findViewById(R.id.txtMovieDuration);
        movieImage = itemView.findViewById(R.id.imgMovie);
        ratingBar = itemView.findViewById(R.id.ratingBar);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onMovieListener.onMovieClick(getAdapterPosition()); // getBindingAdapterPosition()
    }
}
