package com.cnkaptan.transferwisehomework.data.database;

import com.cnkaptan.transferwisehomework.model.Movie;

import java.util.List;

import rx.Observable;

/**
 * Created by cnkaptan on 09/04/2017.
 */

public interface DataSource {

    void saveMovie(Movie movie);

    List<Movie> getAllMovies();

    Observable<Movie> getAllMoviesObserVable();

    void clearMovies();

    int getCurrentPage();
}
