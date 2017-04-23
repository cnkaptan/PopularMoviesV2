package com.cnkaptan.transferwisehomework.presenter.grid;

import android.support.annotation.NonNull;

import com.cnkaptan.transferwisehomework.BasePresenter;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.model.Movie;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public class MoviesGridPresenter extends BasePresenter<MoviesGridFragmentContract.View> implements MoviesGridFragmentContract.Presenter {

    @NonNull
    final private DataManager dataManagerImpl;
    @NonNull
    private final Scheduler mainScheduler, ioScheduler;
    public MoviesGridPresenter(@NonNull DataManager dataManagerImpl, Scheduler ioScheduler, Scheduler mainScheduler) {
        this.dataManagerImpl = dataManagerImpl;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void getDatasFromLocal() {
        checkViewAttached();
        getView().showRefresh();
        addSubscription(dataManagerImpl.getDatasFromLocal().subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
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
        checkViewAttached();
        getView().showRefresh();
        addSubscription(dataManagerImpl.getMovies(null).subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
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
        checkViewAttached();
        getView().showLoading();
        addSubscription(dataManagerImpl.loadMoreMovies().subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
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
