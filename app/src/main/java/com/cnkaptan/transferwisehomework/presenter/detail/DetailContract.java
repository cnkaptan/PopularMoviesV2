package com.cnkaptan.transferwisehomework.presenter.detail;

import com.cnkaptan.transferwisehomework.MvpPresenter;
import com.cnkaptan.transferwisehomework.MvpView;
import com.cnkaptan.transferwisehomework.model.TrailerAndReviews;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public interface DetailContract {
    interface View extends MvpView{
        void showError(String message);
        void initTrailerAndReviews(TrailerAndReviews trailerAndReviews);
    }

    interface Presenter extends MvpPresenter<View>{
        void getTrailerAndReviews(long movieId);
    }
}
