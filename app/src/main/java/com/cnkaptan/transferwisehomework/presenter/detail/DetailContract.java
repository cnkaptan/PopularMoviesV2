package com.cnkaptan.transferwisehomework.presenter.detail;

import com.cnkaptan.transferwisehomework.MvpPresenter;
import com.cnkaptan.transferwisehomework.MvpView;
import com.cnkaptan.transferwisehomework.data.Review;
import com.cnkaptan.transferwisehomework.data.Trailer;

import java.util.List;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public interface DetailContract {
    interface View extends MvpView{
        void initTrailersData(List<Trailer> trailers);
        void initReviewsData(List<Review> reviews);
        void showError(String message);
    }

    interface Presenter extends MvpPresenter<View>{
        void getTrailersList(long movieId);
        void getReviewsList(long movieId);
    }
}
