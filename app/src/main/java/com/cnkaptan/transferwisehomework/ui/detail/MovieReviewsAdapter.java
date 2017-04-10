package com.cnkaptan.transferwisehomework.ui.detail;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnkaptan.transferwisehomework.R;
import com.cnkaptan.transferwisehomework.data.Review;
import com.cnkaptan.transferwisehomework.util.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {

    @Nullable
    private List<Review> movieReviews;
    @Nullable
    private OnItemClickListener onItemClickListener;

    public MovieReviewsAdapter() {
        movieReviews = new ArrayList<>();
    }

    public void setMovieReviews(@Nullable List<Review> movieReviews) {
        this.movieReviews = movieReviews;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Nullable
    public List<Review> getMovieReviews() {
        return movieReviews;
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie_review, parent, false);
        return new MovieReviewViewHolder(itemView, onItemClickListener);
    }

    @Override
    @SuppressLint("PrivateResource")
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        if (movieReviews == null) {
            return;
        }
        Review review = movieReviews.get(position);
        holder.content.setText(review.getContent());
        holder.author.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        if (movieReviews == null) {
            return 0;
        }
        return movieReviews.size();
    }

    public Review getItem(int position) {
        if (movieReviews == null || position < 0 || position > movieReviews.size()) {
            return null;
        }
        return movieReviews.get(position);
    }

    public static class MovieReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_movie_review_content)
        TextView content;
        @BindView(R.id.text_movie_review_author)
        TextView author;

        @Nullable
        private OnItemClickListener onItemClickListener;

        public MovieReviewViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
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
