package com.cnkaptan.transferwisehomework.ui.grid;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnkaptan.transferwisehomework.R;
import com.cnkaptan.transferwisehomework.data.Movie;
import com.cnkaptan.transferwisehomework.util.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {
    private static final String POSTER_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String POSTER_IMAGE_SIZE = "w780";
    private final OnItemClickListener onItemClickListener;
    private List<Movie> movies;

    public MoviesAdapter(List<Movie> movies, OnItemClickListener onItemClickListener) {
        this.movies = movies;
        this.onItemClickListener  = onItemClickListener;
    }

    @Nullable
    public Movie getItem(int position){
        if (movies != null && movies.size() > 0){
            return movies.get(position);
        }
        return null;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_movie, parent, false);
        return new MovieHolder(itemView,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        final Movie movie = getItem(position);
        if (movie != null) {
            holder.moviePoster.setContentDescription(movie.getTitle());
            Glide.with(holder.moviePoster.getContext())
                    .load(POSTER_IMAGE_BASE_URL + POSTER_IMAGE_SIZE + movie.getPosterPath())
                    .placeholder(new ColorDrawable(ContextCompat.getColor(holder.moviePoster.getContext(),R.color.accent_material_light)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .crossFade()
                    .into(holder.moviePoster);
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null){
            return movies.size();
        }
        return 0;
    }

    public void setDatas(List<Movie> movies) {
        if (this.movies == null){
            this.movies = new ArrayList<>();
        }
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void addDatas(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.image_movie_poster)
        ImageView moviePoster;

        private OnItemClickListener onItemClickListener;

        public MovieHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
