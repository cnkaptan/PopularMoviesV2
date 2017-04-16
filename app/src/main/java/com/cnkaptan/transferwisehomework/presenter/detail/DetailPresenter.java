package com.cnkaptan.transferwisehomework.presenter.detail;

import android.support.annotation.NonNull;

import com.cnkaptan.transferwisehomework.BasePresenter;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.data.Review;
import com.cnkaptan.transferwisehomework.data.Trailer;
import com.cnkaptan.transferwisehomework.data.TrailerAndReviews;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

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
    public void getTrailerAndReviews(long movieId) {
        final Observable<List<Trailer>> trailerListObservable = mDatamanager.getMovieVideos(movieId);
        final Observable<List<Review>> reviewListObservable = mDatamanager.getMovieReviews(movieId);

        addSubscription(Observable.zip(trailerListObservable, reviewListObservable, TrailerAndReviews::new)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<TrailerAndReviews>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e.getMessage());
            }

            @Override
            public void onNext(TrailerAndReviews trailerAndReviews) {
                getView().initTrailerAndReviews(trailerAndReviews);
            }
        }));
    }
}
