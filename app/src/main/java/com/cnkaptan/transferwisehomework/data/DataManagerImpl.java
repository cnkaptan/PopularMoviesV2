package com.cnkaptan.transferwisehomework.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cnkaptan.transferwisehomework.data.api.MovieApi;
import com.cnkaptan.transferwisehomework.data.database.DataSource;
import com.cnkaptan.transferwisehomework.model.Movie;
import com.cnkaptan.transferwisehomework.model.MovieResponse;
import com.cnkaptan.transferwisehomework.model.Review;
import com.cnkaptan.transferwisehomework.model.ReviewResponse;
import com.cnkaptan.transferwisehomework.model.Trailer;
import com.cnkaptan.transferwisehomework.model.TrailerAndReviews;
import com.cnkaptan.transferwisehomework.model.TrailerResponse;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cnkaptan on 09/04/2017.
 */
@Singleton
public class DataManagerImpl implements DataManager {


    private static final String TAG = DataManagerImpl.class.getSimpleName();
    @NonNull
    private final MovieApi movieApi;

    @NonNull
    private final DataSource datasource;

    private final HashMap<Long, List<Trailer>> trailerCache;
    private final HashMap<Long, List<Review>> reviewCache;


    public DataManagerImpl(@NonNull MovieApi movieApi, @NonNull DataSource dataSource) {
        this.movieApi = movieApi;
        this.datasource = dataSource;
        this.trailerCache = new HashMap<>();
        this.reviewCache = new HashMap<>();
    }

    public Observable<List<Movie>> loadMoreMovies() {
        final int page = datasource.getCurrentPage();
        return getMovies(page);
    }

    @Override
    public Observable<TrailerAndReviews> getTrailerAndReviews(long movieId) {
        Observable<List<Trailer>> trailerListObservable = getMovieVideos(movieId);
        Observable<List<Review>> reviewListObservable = getMovieReviews(movieId);
        return Observable.zip(trailerListObservable, reviewListObservable, TrailerAndReviews::new);
    }

    public Observable<List<Movie>> getMovies(@Nullable Integer page) {
        Observable<Movie> movieObservable = movieApi.discoverMovies(page)
                .subscribeOn(Schedulers.io())
                .doOnNext((movieDiscoverResponse) -> clearMoviesSortTableIfNeeded(movieDiscoverResponse))
                .map((movieDiscoverResponse) -> movieDiscoverResponse.getResults())
                .flatMap(movies -> Observable.from(movies))
                .doOnNext(movie ->{
                    movie.setId(UUID.randomUUID().toString());
                    datasource.saveMovie(movie);
                });

        return movieObservable.toList();
    }

    private void clearMoviesSortTableIfNeeded(MovieResponse<Movie> movieDiscoverResponse) {
        if (movieDiscoverResponse.getPage() == 1) {
            datasource.clearMovies();
        }
    }

    public Observable<List<Movie>> getDatasFromLocal() {
        return Observable.concat(datasource.getAllMoviesObserVable().toList(), getMovies(null))
                .filter(movies -> movies != null && movies.size() > 0).first();
    }


    public Observable<List<Trailer>> getMovieVideos(long movieId) {
        final List<Trailer> videos = getTrailerCache().get(movieId);
        if (videos != null){
            return Observable.from(videos).toList();
        }
        return movieApi.getMovieVideos(movieId)
                .subscribeOn(Schedulers.io())
                .map((Func1<TrailerResponse, List<Trailer>>) TrailerResponse::getResults)
                .doOnNext(trailers -> trailerCache.put(movieId, trailers));

    }

    public Observable<List<Review>> getMovieReviews(long movieId) {
        final List<Review> cachedReview = getReviewCache().get(movieId);
        if (cachedReview != null){
            return Observable.from(cachedReview).toList();
        }
        return movieApi.getMovieReviews(movieId)
                .subscribeOn(Schedulers.io())
                .map((Func1<ReviewResponse, List<Review>>) ReviewResponse::getResults)
                .doOnNext(reviews -> reviewCache.put(movieId, reviews));
    }

    public HashMap<Long, List<Trailer>> getTrailerCache() {
        return trailerCache;
    }

    public HashMap<Long, List<Review>> getReviewCache() {
        return reviewCache;
    }
}
