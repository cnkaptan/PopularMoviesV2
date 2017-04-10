package com.cnkaptan.transferwisehomework.data.database;

import com.cnkaptan.transferwisehomework.data.pojos.Movie;

import rx.Observable;

/**
 * Created by cnkaptan on 09/04/2017.
 */

public interface DataSource {

    void saveMovie(Movie movie);

    Observable<Movie> getAllMovies();

    void clearMovies();
}
