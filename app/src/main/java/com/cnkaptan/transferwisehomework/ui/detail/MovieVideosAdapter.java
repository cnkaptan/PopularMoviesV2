package com.cnkaptan.transferwisehomework.ui.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnkaptan.transferwisehomework.R;
import com.cnkaptan.transferwisehomework.data.Trailer;
import com.cnkaptan.transferwisehomework.util.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.MovieVideoViewHolder> {

    private static final String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/mqdefault.jpg";
    private final Context context;

    @Nullable
    private List<Trailer> movieVideos;
    @Nullable
    private OnItemClickListener onItemClickListener;

    public MovieVideosAdapter(Context context) {
        this.context = context;
        movieVideos = new ArrayList<>();
    }

    public void setMovieVideos(@Nullable List<Trailer> movieVideos) {
        this.movieVideos = movieVideos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Nullable
    public List<Trailer> getMovieVideos() {
        return movieVideos;
    }

    @Override
    public MovieVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_video, parent, false);
        return new MovieVideoViewHolder(itemView, onItemClickListener);
    }

    @Override
    @SuppressLint("PrivateResource")
    public void onBindViewHolder(MovieVideoViewHolder holder, int position) {
        if (movieVideos == null) {
            return;
        }
        Trailer video = movieVideos.get(position);
        if (video.isYoutubeVideo()) {
            Glide.with(context)
                    .load(String.format(YOUTUBE_THUMBNAIL, video.getKey()))
                    .placeholder(new ColorDrawable(context.getResources().getColor(R.color.accent_material_light)))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .crossFade()
                    .into(holder.movieVideoThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        if (movieVideos == null) {
            return 0;
        }
        return movieVideos.size();
    }

    public Trailer getItem(int position) {
        if (movieVideos == null || position < 0 || position > movieVideos.size()) {
            return null;
        }
        return movieVideos.get(position);
    }


    public static class MovieVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_video_thumbnail)
        ImageView movieVideoThumbnail;

        @Nullable
        private OnItemClickListener onItemClickListener;

        public MovieVideoViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
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
