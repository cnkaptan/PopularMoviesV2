package com.cnkaptan.transferwisehomework.presenter.detail;

import android.support.annotation.NonNull;

import com.cnkaptan.transferwisehomework.BasePresenter;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.data.Review;
import com.cnkaptan.transferwisehomework.data.Trailer;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public class DetailPresenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {
    @NonNull
    private DataManager mDatamanager;

    public DetailPresenter(DataManager dataManager) {
        this.mDatamanager = dataManager;
    }

    @Override
    public void getTrailersList(long movieId) {
        addSubscription(mDatamanager.getMovieVideos(movieId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Trailer>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e.getMessage());
            }

            @Override
            public void onNext(List<Trailer> trailers) {
                getView().initTrailersData(trailers);
            }
        }));
    }

    @Override
    public void getReviewsList(long movieId) {
        addSubscription(mDatamanager.getMovieReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Review>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Review> reviews) {
                        getView().initReviewsData(reviews);
                    }
                }));
    }
}
