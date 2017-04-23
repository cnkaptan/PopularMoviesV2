package com.cnkaptan.transferwisehomework.presenter.grid;

import com.cnkaptan.transferwisehomework.BasePresenter;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.model.Movie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.cnkaptan.transferwisehomework.util.TestUtilities.getDummyMovieList;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by cnkaptan on 22/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MoviesGridPresenterTest {


    @Mock
    DataManager dataManager;
    @Mock
    MoviesGridFragmentContract.View view;

    MoviesGridPresenter moviesGridPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        moviesGridPresenter = new MoviesGridPresenter(dataManager, Schedulers.immediate(), Schedulers.immediate());
        moviesGridPresenter.attachView(view);
    }

    @Test
    public void getValidMovies_ReturnResults() throws Exception {
        //Arrange
        List<Movie> movieList = getDummyMovieList();
        when(dataManager.getDatasFromLocal()).thenReturn(Observable.just(movieList));

        //Action
        moviesGridPresenter.getDatasFromLocal();

        //Assert
        verify(view).showRefresh();
        verify(view).hideRefresh();
        verify(view).initDatas(movieList);
        verify(view, never()).onError(anyString());
    }

    @Test
    public void getValidMovies_SubscribtionError_ErrorMsg() throws Exception {
        String errorMsg = "No Local Data";
        when(dataManager.getDatasFromLocal()).thenReturn(Observable.error(new IOException(errorMsg)));

        moviesGridPresenter.getDatasFromLocal();

        verify(view).showRefresh();
        verify(view).hideRefresh();
        verify(view, never()).initDatas(anyList());
        verify(view).onError(errorMsg);

    }


    @Test
    public void loadMoreMoviesValidMovies_ReturnResults() throws Exception {
        //Arrange
        List<Movie> movieList = getDummyMovieList();
        when(dataManager.loadMoreMovies()).thenReturn(Observable.just(movieList));

        //Action
        moviesGridPresenter.loadMoreMovies();

        //Assert
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view).addNewMovies(movieList);
        verify(view, never()).onError(anyString());
    }

    @Test
    public void refreshMovies_SubscribtionError_ErrorMsg() throws Exception {
        String errorMsg = "No internet";
        when(dataManager.loadMoreMovies()).thenReturn(Observable.error(new IOException(errorMsg)));

        moviesGridPresenter.loadMoreMovies();

        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).addNewMovies(anyList());
        verify(view).onError(errorMsg);

    }

    @org.junit.Test(expected = BasePresenter.MvpViewNotAttachedException.class)
    public void presenter_NotAttached_ThrowsMvpException() {
        moviesGridPresenter.detachView();

        moviesGridPresenter.loadMoreMovies();

        verify(view, never()).showLoading();
        verify(view, never()).addNewMovies(anyList());
    }


}