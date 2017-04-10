package com.cnkaptan.transferwisehomework.data.database.realm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cnkaptan.transferwisehomework.data.database.DataSource;
import com.cnkaptan.transferwisehomework.data.pojos.Movie;

import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by cnkaptan on 09/04/2017.
 */
@Singleton
public final class RealmDataSource implements DataSource {
    @NonNull
    private Context mContext;
    @NonNull
    private RealmConfiguration mRealmConfiguration;
    private static final int PAGE_SIZE = 20;
    private RealmDataSource(){

    }

    public RealmDataSource(@NonNull Context context) {
        this.mContext = context;
        Realm.init(this.mContext);
        this.mRealmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
    }


    @Override
    public void saveMovie(Movie movie) {
        final Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.beginTransaction();
        final Movie realmMovie = realm.createObject(Movie.class, UUID.randomUUID().toString());
        realmMovie.setMovideId(movie.getMovideId());
        realmMovie.setTitle(movie.getTitle());
        realmMovie.setOriginalTitle(movie.getOriginalTitle());
        realmMovie.setOverview(movie.getOverview());
        realmMovie.setReleaseDate(movie.getReleaseDate());
        realmMovie.setPosterPath(movie.getPosterPath());
        realmMovie.setPopularity(movie.getPopularity());
        realmMovie.setAverageVote(movie.getAverageVote());
        realmMovie.setVoteCount(movie.getVoteCount());
        realmMovie.setBackdropPath(movie.getBackdropPath());
        realm.copyToRealmOrUpdate(realmMovie);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public Observable<Movie> search(final long id) {
        return Observable.create(new Observable.OnSubscribe<Movie>() {
            @Override
            public void call(Subscriber<? super Movie> subscriber) {
                final Realm realm = Realm.getInstance(mRealmConfiguration);
                final Movie realmMovie = realm.where(Movie.class)
                        .equalTo("id",id)
                        .findFirst();

                if (realmMovie != null && realmMovie.isLoaded() && realmMovie.isValid()){
                    subscriber.onNext(realm.copyFromRealm(realmMovie));
                }else{
                    Observable.empty();
                }
                realm.close();
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Movie> getAllMovies() {
        final Realm realm = Realm.getInstance(mRealmConfiguration);
        final RealmResults<Movie> realmResults = realm.where(Movie.class)
                .findAll();
        List<Movie> allMovies = realm.copyFromRealm(realmResults);
        realm.close();
        return Observable.from(allMovies);
    }

    public int getCurrentPage() {
        final Realm realm = Realm.getInstance(mRealmConfiguration);
        final RealmResults<Movie> realmResults = realm.where(Movie.class).findAll();
        List<Movie> allMovies = realm.copyFromRealm(realmResults);
        realm.close();
        int currentPage = 1;
        if (allMovies != null) {
            currentPage = allMovies.size() / PAGE_SIZE + 1;
        }
        return currentPage;
    }

    public void clearMovies() {
        final Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.beginTransaction();
        RealmResults<Movie> result = realm.where(Movie.class).findAll();
        result.deleteAllFromRealm();
//        realm.delete(Movie.class);
        realm.commitTransaction();
        realm.close();
    }
}
