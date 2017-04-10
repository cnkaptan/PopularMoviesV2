package com.cnkaptan.transferwisehomework.presenter;

import android.support.annotation.NonNull;

import com.cnkaptan.transferwisehomework.BasePresenter;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.data.pojos.Movie;

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
        addSubscription(dataManager.getDatasFromLocal().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Movie>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        getView().initDatas(movies);
                    }
                }));
    }
}
