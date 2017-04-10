package com.cnkaptan.transferwisehomework.data.api;

import com.cnkaptan.transferwisehomework.data.MovieResponse;
import com.cnkaptan.transferwisehomework.data.Movie;
import com.cnkaptan.transferwisehomework.data.ReviewResponse;
import com.cnkaptan.transferwisehomework.data.TrailerResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cnkaptan on 08/04/2017.
 */

public interface MovieApi {
    @GET("movie/{id}/videos")
    Observable<TrailerResponse> getMovieVideos(@Path("id") long movieId);

    @GET("movie/{id}/reviews")
    Observable<ReviewResponse> getMovieReviews(@Path("id") long movieId);

    @GET("movie/popular")
    Observable<MovieResponse<Movie>> discoverMovies(@Query("page") Integer page);
}
