package com.cnkaptan.transferwisehomework.presenter.detail;

import com.cnkaptan.transferwisehomework.BasePresenter;
import com.cnkaptan.transferwisehomework.data.api.MovieApi;
import com.cnkaptan.transferwisehomework.data.pojos.ReviewResponse;
import com.cnkaptan.transferwisehomework.data.pojos.TrailerResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public class DetailPresenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {
    private MovieApi movieApi;

    public DetailPresenter(MovieApi movieApi) {
        this.movieApi = movieApi;
    }

    @Override
    public void getTrailersList(long movieId) {
        addSubscription(movieApi.getMovieVideos(movieId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<TrailerResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e.getMessage());
            }

            @Override
            public void onNext(TrailerResponse trailerResponse) {
                getView().initTrailersData(trailerResponse.getResults());
            }
        }));
    }

    @Override
    public void getReviewsList(long movieId) {
        addSubscription(movieApi.getMovieReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReviewResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ReviewResponse reviewResponse) {
                        getView().initReviewsData(reviewResponse.getResults());
                    }
                }));
    }
}
