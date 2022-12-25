package com.ilan.movieapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilan.movieapp.R;

public class PopularMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Widgets
    ImageView movieImage;
    RatingBar ratingBar;

    // Listeners
    OnMovieListener onMovieListener;

    public PopularMovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);
        this.onMovieListener = onMovieListener;

        movieImage = itemView.findViewById(R.id.imgMovie);
        ratingBar = itemView.findViewById(R.id.ratingBar);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
