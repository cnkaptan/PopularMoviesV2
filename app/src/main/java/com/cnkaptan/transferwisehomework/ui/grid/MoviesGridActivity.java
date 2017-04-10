package com.cnkaptan.transferwisehomework.ui.grid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cnkaptan.transferwisehomework.R;
import com.cnkaptan.transferwisehomework.data.pojos.Movie;
import com.cnkaptan.transferwisehomework.ui.detail.MovieDetailActivity;
import com.cnkaptan.transferwisehomework.utils.OnItemSelectedListener;


public class MoviesGridActivity extends AppCompatActivity implements OnItemSelectedListener{

    private static final String TAG = MoviesGridActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        Toolbar toolbar  = ((Toolbar)findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_grid_container, MoviesGridFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        startActivity(MovieDetailActivity.newInstent(this,movie));
    }
}
