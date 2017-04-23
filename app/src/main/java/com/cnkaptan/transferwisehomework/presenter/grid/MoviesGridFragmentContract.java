package com.cnkaptan.transferwisehomework.presenter.grid;

import com.cnkaptan.transferwisehomework.MvpPresenter;
import com.cnkaptan.transferwisehomework.MvpView;
import com.cnkaptan.transferwisehomework.model.Movie;

import java.util.List;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public interface MoviesGridFragmentContract {

    interface View extends MvpView{
        void initDatas(List<Movie> movies);
        void onError(String message);
        void showLoading();
        void hideLoading();
        void hideRefresh();
        void showRefresh();
        void addNewMovies(List<Movie> movies);
    }

    interface Presenter extends MvpPresenter<View>{
        void getDatasFromLocal();
        void refreshMovies();
        void loadMoreMovies();
    }
}
