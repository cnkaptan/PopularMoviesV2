package com.cnkaptan.transferwisehomework.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;

import com.cnkaptan.transferwisehomework.data.api.MovieApi;
import com.cnkaptan.transferwisehomework.data.database.realm.RealmDataSource;

import java.util.List;

import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cnkaptan on 09/04/2017.
 */
@Singleton
public class DataManager {


    private static final String TAG = DataManager.class.getSimpleName();
    @NonNull
    private final MovieApi movieApi;

    @NonNull
    private final RealmDataSource realmDataSource;

    private final ArrayMap<Long, List<Trailer>> trailerCache;
    private final ArrayMap<Long, List<Review>> reviewCache;


    public DataManager(@NonNull MovieApi movieApi, @NonNull RealmDataSource realmDataSource) {
        this.movieApi = movieApi;
        this.realmDataSource = realmDataSource;
        this.trailerCache = new ArrayMap<>();
        this.reviewCache = new ArrayMap<>();
    }

    public Observable<List<Movie>> loadMoreMovies() {
        final int page = realmDataSource.getCurrentPage();
        return getMovies(page);
    }

    public Observable<List<Movie>> getMovies(@Nullable Integer page) {
        Observable<Movie> movieObservable = movieApi.discoverMovies(page)
                .subscribeOn(Schedulers.io())
                .doOnNext((movieDiscoverResponse) -> clearMoviesSortTableIfNeeded(movieDiscoverResponse))
                .doOnNext((discoverMoviesResponse) -> logResponse(discoverMoviesResponse))
                .map((movieDiscoverResponse) -> movieDiscoverResponse.getResults())
                .flatMap(movies -> Observable.from(movies))
                .doOnNext(movie -> realmDataSource.saveMovie(movie));

        return movieObservable.toList();
    }

    private void clearMoviesSortTableIfNeeded(MovieResponse<Movie> movieDiscoverResponse) {
        if (movieDiscoverResponse.getPage() == 1) {
            realmDataSource.clearMovies();
        }
    }

    private void logResponse(MovieResponse<Movie> discoverMoviesResponse) {
        Log.d(TAG, "page == " + discoverMoviesResponse.getPage() + " " +
                discoverMoviesResponse.getResults().toString());
    }


    public Observable<List<Movie>> getDatasFromLocal() {
        return Observable.concat(realmDataSource.getAllMovies().toList(), getMovies(null))
                .filter(movies -> movies != null && movies.size() > 0).first();
    }


    public Observable<List<Trailer>> getMovieVideos(long movieId) {
        final List<Trailer> videos = trailerCache.get(movieId);
        if (videos != null){
            return Observable.from(videos).toList();
        }
        return movieApi.getMovieVideos(movieId)
                .subscribeOn(Schedulers.io())
                .map((Func1<TrailerResponse, List<Trailer>>) TrailerResponse::getResults)
                .doOnNext(trailers -> trailerCache.put(movieId, trailers));

    }

    public Observable<List<Review>> getMovieReviews(long movieId) {
        final List<Review> cachedReview = reviewCache.get(movieId);
        if (cachedReview != null){
            return Observable.from(cachedReview).toList();
        }
        return movieApi.getMovieReviews(movieId)
                .subscribeOn(Schedulers.io())
                .map((Func1<ReviewResponse, List<Review>>) ReviewResponse::getResults)
                .doOnNext(reviews -> reviewCache.put(movieId, reviews));
    }
}
