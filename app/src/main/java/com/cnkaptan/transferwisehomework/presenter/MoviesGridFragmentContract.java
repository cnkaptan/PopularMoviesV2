package com.cnkaptan.transferwisehomework.presenter;

import com.cnkaptan.transferwisehomework.MvpPresenter;
import com.cnkaptan.transferwisehomework.MvpView;
import com.cnkaptan.transferwisehomework.data.pojos.Movie;

import java.util.List;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public interface MoviesGridFragmentContract {

    interface View extends MvpView{
        void initDatas(List<Movie> movies);
        void onError(String message);

    }

    interface Presenter extends MvpPresenter<View>{
        void getDatasFromLocal();
    }
}
