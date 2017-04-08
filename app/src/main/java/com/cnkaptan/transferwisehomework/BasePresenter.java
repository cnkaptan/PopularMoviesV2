package com.cnkaptan.transferwisehomework;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by cnkaptan on 08/04/2017.
 */

public class BasePresenter<T extends MvpView> implements MvpPresenter<T> {

    private T view;
    private CompositeSubscription compositeSubscription;


    @Override
    public void attachView(T view) {
        this.view = view;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        compositeSubscription.clear();
        compositeSubscription = null;
        view = null;
    }

    public T getView() {
        return view;
    }

    private boolean isViewAttached() {
        return view != null;
    }

    public void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }
}