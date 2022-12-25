package com.ilan.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ilan.movieapp.MovieListActivity;
import com.ilan.movieapp.R;
import com.ilan.movieapp.models.MovieModel;
import com.ilan.movieapp.utils.Credentials;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;
    private static final int DISPLAY_POPULAR_MOVIES = 1;
    private static final int DISPLAY_SEARCH_MOVIES = 2;

    public MovieAdapter(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == DISPLAY_SEARCH_MOVIES) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
            return new MovieViewHolder(view, onMovieListener);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_movies, parent, false);
            return new PopularMovieViewHolder(view, onMovieListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (Credentials.POPULAR) {
            return DISPLAY_POPULAR_MOVIES;
        } else {
            return DISPLAY_SEARCH_MOVIES;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //((MovieViewHolder) holder).title.setText(mMovies.get(position).getTitle());
        //((MovieViewHolder) holder).releaseDate.setText(mMovies.get(position).getRelease_date());
        //((MovieViewHolder) holder).duration.setText(String.valueOf(mMovies.get(position).getRuntime()));

        String fixedImageUrl = "https://image.tmdb.org/t/p/w500/";
        int itemViewType = getItemViewType(position);

        switch (itemViewType) {
            case DISPLAY_SEARCH_MOVIES:
                // Vote average is over 10 and rating bar is out of 5 stars -- dividing by 2
                ((MovieViewHolder) holder).ratingBar.setRating((mMovies.get(position).getVote_average()/2));

                // ImageView -- Using Glide

                Glide
                        .with(holder.itemView.getContext())
                        .load(fixedImageUrl + mMovies.get(position).getPoster_path())
                        .into(((MovieViewHolder) holder).movieImage);
                break;

            case DISPLAY_POPULAR_MOVIES:
                // Vote average is over 10 and rating bar is out of 5 stars -- dividing by 2
                ((PopularMovieViewHolder) holder).ratingBar.setRating((mMovies.get(position).getVote_average()/2));

                // ImageView -- Using Glide
                Glide
                        .with(holder.itemView.getContext())
                        .load(fixedImageUrl + mMovies.get(position).getPoster_path())
                        .into(((PopularMovieViewHolder) holder).movieImage);
        }


    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
    }

    public List<MovieModel> getmMovies() {
        return mMovies;
        //notifyDataSetChanged();
    }

    // Getting the ID of the movie clicked
    public MovieModel getSelectedMovie(int position) {
        if (mMovies != null) {
            if (mMovies.size() > 0) {
                return mMovies.get(position);
            }
        }
        return null;
    }
}
