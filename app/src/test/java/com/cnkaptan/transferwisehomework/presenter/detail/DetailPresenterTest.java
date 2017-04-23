package com.cnkaptan.transferwisehomework.presenter.detail;

import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.model.TrailerAndReviews;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.cnkaptan.transferwisehomework.util.TestUtilities.MOVIE_ID_FOR_DETAIL_PRESENTER;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by cnkaptan on 23/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailPresenterTest {

    @Mock
    DataManager dataManager;
    @Mock
    DetailContract.View view;

    DetailPresenter detailPresenter;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        detailPresenter = new DetailPresenter(dataManager, Schedulers.immediate(), Schedulers.immediate());
        detailPresenter.attachView(view);

    }

    @Test
    public void validReviewAndTrailerList() throws Exception {
        //Arrange
        when(dataManager.getTrailerAndReviews(MOVIE_ID_FOR_DETAIL_PRESENTER)).thenReturn(Observable.just(any(TrailerAndReviews.class)));

        //Action
        detailPresenter.getTrailerAndReviews(MOVIE_ID_FOR_DETAIL_PRESENTER);

        //Assert
        verify(view).initTrailerAndReviews(any(TrailerAndReviews.class));
    }

    @Test
    public void refreshMovies_SubscribtionError_ErrorMsg() throws Exception {
        String errorMsg = "No internet";
        when(dataManager.getTrailerAndReviews(MOVIE_ID_FOR_DETAIL_PRESENTER)).thenReturn(Observable.error(new IOException(errorMsg)));

        detailPresenter.getTrailerAndReviews(MOVIE_ID_FOR_DETAIL_PRESENTER);

        verify(view, never()).initTrailerAndReviews(any(TrailerAndReviews.class));
        verify(view).showError(errorMsg);

    }

}