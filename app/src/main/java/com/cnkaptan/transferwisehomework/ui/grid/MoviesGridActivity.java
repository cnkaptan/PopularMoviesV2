package com.cnkaptan.transferwisehomework.ui.grid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.cnkaptan.transferwisehomework.R;
import com.cnkaptan.transferwisehomework.data.pojos.Movie;
import com.cnkaptan.transferwisehomework.ui.detail.MovieDetailActivity;
import com.cnkaptan.transferwisehomework.ui.detail.MovieDetailFragment;
import com.cnkaptan.transferwisehomework.utils.OnItemSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesGridActivity extends AppCompatActivity implements OnItemSelectedListener {

    private static final String TAG = MoviesGridActivity.class.getSimpleName();
    private static final String SELECTED_MOVIE_KEY = "MovieSelected";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movies_grid_container)
    FrameLayout moviesGridContainer;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Nullable
    @BindView(R.id.movie_detail_container)
    ScrollView movieDetailContainer;
    private boolean twoPaneMode;
    private Movie selectedMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        ButterKnife.bind(this);
        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_grid_container, MoviesGridFragment.newInstance())
                    .commit();
        }

        twoPaneMode = movieDetailContainer != null;
        if (twoPaneMode && selectedMovie == null) {
            movieDetailContainer.setVisibility(View.GONE);
        }

        setupToolbar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_MOVIE_KEY, selectedMovie);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            selectedMovie = savedInstanceState.getParcelable(SELECTED_MOVIE_KEY);
        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (twoPaneMode && movieDetailContainer != null) {
            movieDetailContainer.setVisibility(View.VISIBLE);
            selectedMovie = movie;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, MovieDetailFragment.newInstance(movie))
                    .commit();
        } else {
            startActivity(MovieDetailActivity.newInstent(this, movie));
        }

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

}
