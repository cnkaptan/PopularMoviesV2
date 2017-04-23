package com.cnkaptan.transferwisehomework.presenter.detail;

import android.support.annotation.NonNull;

import com.cnkaptan.transferwisehomework.BasePresenter;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.model.TrailerAndReviews;

import rx.Observer;
import rx.Scheduler;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public class DetailPresenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;
    @NonNull
    private DataManager mDatamanager;

    public DetailPresenter(@NonNull DataManager mDatamanager, Scheduler ioScheduler, Scheduler mainScheduler) {
        this.mDatamanager = mDatamanager;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void getTrailerAndReviews(long movieId) {
        addSubscription(mDatamanager.getTrailerAndReviews(movieId)
                .observeOn(mainScheduler).subscribe(new Observer<TrailerAndReviews>() {
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
