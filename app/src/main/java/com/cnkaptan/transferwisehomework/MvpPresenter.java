package com.cnkaptan.transferwisehomework;

/**
 * Created by cnkaptan on 08/04/2017.
 */

public interface MvpPresenter<V extends MvpView> {
    void attachView(V view);

    void detachView();
}