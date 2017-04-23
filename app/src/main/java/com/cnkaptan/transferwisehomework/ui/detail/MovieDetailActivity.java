package com.cnkaptan.transferwisehomework.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnkaptan.transferwisehomework.R;
import com.cnkaptan.transferwisehomework.model.Movie;
import com.cnkaptan.transferwisehomework.util.Constants;
import com.cnkaptan.transferwisehomework.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetailActivity extends AppCompatActivity {
    private static final String MOVIE = "movie";

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backdrop_image)
    ImageView movieBackdropImage;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    private Movie movie;

    public static Intent newInstent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        movie = getIntent().getParcelableExtra(MOVIE);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movies_grid_container, MovieDetailFragment.newInstance(movie))
                    .commit();
        }
        initToolbar();
        final Resources resources = getResources();
        ViewCompat.setElevation(nestedScrollView,
                Utils.convertDpToPixel(getResources().getInteger(R.integer.movie_detail_content_elevation_in_dp),resources));
        ViewCompat.setElevation(fab,
                Utils.convertDpToPixel(getResources().getInteger(R.integer.movie_detail_fab_elevation_in_dp),resources));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }
        collapsingToolbarLayout.setTitle(movie.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        setTitle("");
        Glide.with(this)
                .load(Constants.POSTER_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_SIZE + movie.getBackdropPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(movieBackdropImage);
    }
}
