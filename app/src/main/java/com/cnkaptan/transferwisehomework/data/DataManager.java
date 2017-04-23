package com.cnkaptan.transferwisehomework.data;

import com.cnkaptan.transferwisehomework.model.Movie;
import com.cnkaptan.transferwisehomework.model.Review;
import com.cnkaptan.transferwisehomework.model.Trailer;
import com.cnkaptan.transferwisehomework.model.TrailerAndReviews;

import java.util.List;

import rx.Observable;

/**
 * Created by cnkaptan on 22/04/2017.
 */

public interface DataManager {

    Observable<List<Movie>> getMovies(Integer page);

    Observable<List<Trailer>> getMovieVideos(long movieId);

    Observable<List<Review>> getMovieReviews(long movieId);

    Observable<List<Movie>> getDatasFromLocal();

    Observable<List<Movie>> loadMoreMovies();

    Observable<TrailerAndReviews> getTrailerAndReviews(long movieId);
}
