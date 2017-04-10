package com.cnkaptan.transferwisehomework.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cnkaptan.transferwisehomework.data.api.MovieApi;
import com.cnkaptan.transferwisehomework.data.database.realm.RealmDataSource;
import com.cnkaptan.transferwisehomework.data.pojos.Movie;
import com.cnkaptan.transferwisehomework.data.pojos.MovieResponse;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by cnkaptan on 09/04/2017.
 */

public class DataManager {

    private static final String TAG = DataManager.class.getSimpleName();
    @NonNull
    private final MovieApi movieApi;

    @NonNull
    private final RealmDataSource realmDataSource;

    public DataManager(@NonNull MovieApi movieApi, @NonNull RealmDataSource realmDataSource) {
        this.movieApi = movieApi;
        this.realmDataSource = realmDataSource;
    }

    public Observable<List<Movie>> loadMoreMovies() {
        int page = realmDataSource.getCurrentPage();
        return getMovies(page);
    }

    public Observable<List<Movie>> getMovies(@Nullable Integer page){
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
        if (movieDiscoverResponse.getPage() == 1){
            realmDataSource.clearMovies();
        }
    }

    private void logResponse(MovieResponse<Movie> discoverMoviesResponse) {
        Log.d(TAG, "page == " + discoverMoviesResponse.getPage() + " " +
                discoverMoviesResponse.getResults().toString());
    }


    public Observable<List<Movie>> getDatasFromLocal(){
        return Observable.concat(realmDataSource.getAllMovies().toList(),getMovies(null))
                .filter(movies -> movies != null && movies.size()>0).first();
    }


}
