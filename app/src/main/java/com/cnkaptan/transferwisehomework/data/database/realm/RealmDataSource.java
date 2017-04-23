package com.cnkaptan.transferwisehomework.data.database.realm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cnkaptan.transferwisehomework.data.database.DataSource;
import com.cnkaptan.transferwisehomework.model.Movie;

import java.util.List;

import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by cnkaptan on 09/04/2017.
 */
@Singleton
public class RealmDataSource implements DataSource {
    @NonNull
    private Context mContext;
    @NonNull
    private RealmConfiguration mRealmConfiguration;
    private static final int PAGE_SIZE = 20;

    public RealmDataSource() {
    }

    public RealmDataSource(@NonNull Context context) {
        this.mContext = context;
        Realm.init(this.mContext);
        this.mRealmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(mRealmConfiguration);
    }


    @Override
    public void saveMovie(Movie movie) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(transactionRealm -> {
            final Movie realmMovie = realm.createObject(Movie.class, movie.getId());
            realmMovie.setMovieId(movie.getMovieId());
            realmMovie.setTitle(movie.getTitle());
            realmMovie.setOriginalTitle(movie.getOriginalTitle());
            realmMovie.setOverview(movie.getOverview());
            realmMovie.setReleaseDate(movie.getReleaseDate());
            realmMovie.setPosterPath(movie.getPosterPath());
            realmMovie.setPopularity(movie.getPopularity());
            realmMovie.setAverageVote(movie.getAverageVote());
            realmMovie.setVoteCount(movie.getVoteCount());
            realmMovie.setBackdropPath(movie.getBackdropPath());
            transactionRealm.insertOrUpdate(realmMovie);
        });
        realm.close();
    }

    @Override
    public List<Movie> getAllMovies() {
        final Realm realm = Realm.getDefaultInstance();
        List<Movie> allMovies = realm.copyFromRealm(realm.where(Movie.class).findAll());
        realm.close();
        return allMovies;
    }

    @Override
    public Observable<Movie> getAllMoviesObserVable() {
        return Observable.from(getAllMovies());
    }

    public int getCurrentPage() {
        List<Movie> allMovies = getAllMovies();
        int currentPage = 1;
        if (allMovies != null) {
            currentPage = allMovies.size() / PAGE_SIZE + 1;
        }
        return currentPage;
    }

    @Override
    public void clearMovies() {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(transactionRealm -> {
            RealmResults<Movie> result = transactionRealm.where(Movie.class).findAll();
            result.deleteAllFromRealm();
        });
        realm.close();
    }
}
