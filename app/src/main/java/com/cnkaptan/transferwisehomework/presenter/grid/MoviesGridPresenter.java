package com.cnkaptan.transferwisehomework.presenter.grid;

import android.support.annotation.NonNull;

import com.cnkaptan.transferwisehomework.BasePresenter;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.data.Movie;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public class MoviesGridPresenter extends BasePresenter<MoviesGridFragmentContract.View> implements MoviesGridFragmentContract.Presenter {

    @NonNull
    final private DataManager dataManager;

    public MoviesGridPresenter(@NonNull DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void getDatasFromLocal() {
        getView().showRefresh();
        addSubscription(dataManager.getDatasFromLocal().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideRefresh();
                        getView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        getView().hideRefresh();
                        getView().initDatas(movies);
                    }
                }));
    }

    @Override
    public void refreshMovies() {
        getView().showRefresh();
        addSubscription(dataManager.getMovies(null).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(e.getMessage());
                        getView().hideRefresh();
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        getView().initDatas(movies);
                        getView().hideRefresh();
                    }
                }));

    }

    @Override
    public void loadMoreMovies() {
        getView().showLoading();
        addSubscription(dataManager.loadMoreMovies().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(e.getMessage());
                        getView().hideLoading();
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        getView().addNewMovies(movies);
                        getView().hideLoading();
                    }
                }));
    }
}
