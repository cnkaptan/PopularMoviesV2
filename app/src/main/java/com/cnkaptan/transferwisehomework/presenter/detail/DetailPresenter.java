package com.cnkaptan.transferwisehomework.presenter.detail;

import com.cnkaptan.transferwisehomework.BasePresenter;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.data.pojos.Review;
import com.cnkaptan.transferwisehomework.data.pojos.Trailer;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public class DetailPresenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {
    //TODO datamanagerden alip , cachleyebilirsin Review ve Trailer icin
    private DataManager dataManager;

    public DetailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void getTrailersList(long movieId) {
        addSubscription(dataManager.getMovieVideos(movieId)
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
        addSubscription(dataManager.getMovieReviews(movieId)
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
