package com.cnkaptan.transferwisehomework.data;

import com.cnkaptan.transferwisehomework.data.api.MovieApi;
import com.cnkaptan.transferwisehomework.data.database.DataSource;
import com.cnkaptan.transferwisehomework.model.Movie;
import com.cnkaptan.transferwisehomework.model.MovieResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static com.cnkaptan.transferwisehomework.util.TestUtilities.MOVIE1_TITLE;
import static com.cnkaptan.transferwisehomework.util.TestUtilities.MOVIE2_TITLE;
import static com.cnkaptan.transferwisehomework.util.TestUtilities.getDummyMovieList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by cnkaptan on 23/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerImplTest {
    @Mock
    MovieApi movieApi;
    @Mock
    DataSource dataSource;

    DataManagerImpl dataManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dataManager = new DataManagerImpl(movieApi, dataSource);
    }

    @Test
    public void testLoadMorename() throws Exception {
        final int CURRENT_PAGE = 1;
        when(dataSource.getCurrentPage()).thenReturn(CURRENT_PAGE);
        when(movieApi.discoverMovies(CURRENT_PAGE)).thenReturn(getMovieResponse());

        //Action
        TestSubscriber<List<Movie>> subscriber = new TestSubscriber<>();
        Observable<List<Movie>> listObservable = dataManager.loadMoreMovies();
        listObservable.subscribe(subscriber);
        //Assert
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        testMovieList(subscriber);

        verify(dataSource, times(2)).saveMovie(any(Movie.class));
    }

    @Test
    public void testDataSourceAndMovieApiValidData() throws Exception {
        when(dataSource.getAllMoviesObserVable()).thenReturn(Observable.from(getDummyMovieList()));
        when(movieApi.discoverMovies(null)).thenReturn(getMovieResponse());
        when(dataManager.getMovies(null)).thenReturn(getMovieListObservable());

        //Action
        TestSubscriber<List<Movie>> subscriber = new TestSubscriber<>();
        Observable<List<Movie>> listObservable = dataManager.getDatasFromLocal();
        listObservable.subscribe(subscriber);
        //Assert
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        testMovieList(subscriber);

    }

    @Test
    public void testDataSourceValidDataButApiError() throws Exception {
        when(dataSource.getAllMoviesObserVable()).thenReturn(Observable.from(getDummyMovieList()));
        when(movieApi.discoverMovies(null)).thenReturn(Observable.error(new IllegalArgumentException()));
        when(dataManager.getMovies(null)).thenReturn(Observable.error(new IllegalArgumentException()));

        //Action
        TestSubscriber<List<Movie>> subscriber = new TestSubscriber<>();
        Observable<List<Movie>> listObservable = dataManager.getDatasFromLocal();
        listObservable.subscribe(subscriber);
        //Assert
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        testMovieList(subscriber);

    }

    @Test
    public void testEmptyDataSourceAndMovieApiErrorData() throws Exception {
        when(dataSource.getAllMoviesObserVable()).thenReturn(Observable.from(getEmptyMockData()));
        when(movieApi.discoverMovies(null)).thenReturn(Observable.error(new IllegalArgumentException()));
        when(dataManager.getMovies(null)).thenReturn(Observable.error(new IllegalArgumentException()));

        //Action
        TestSubscriber<List<Movie>> subscriber = new TestSubscriber<>();
        Observable<List<Movie>> listObservable = dataManager.getDatasFromLocal();
        listObservable.subscribe(subscriber);
        //Assert
        subscriber.awaitTerminalEvent();
        subscriber.assertError(IllegalArgumentException.class);

    }

    private List<Movie> getEmptyMockData() {
        return new ArrayList<>();
    }

    private void testMovieList(TestSubscriber<List<Movie>> subscriber) {
        List<List<Movie>> onNextEvents = subscriber.getOnNextEvents();
        List<Movie> movieList = onNextEvents.get(0);
        final Movie movie1 = movieList.get(0);
        final Movie movie2 = movieList.get(1);
        Assert.assertEquals(MOVIE1_TITLE, movie1.getTitle());
        Assert.assertEquals(MOVIE2_TITLE, movie2.getTitle());
    }


    private Observable<List<Movie>> getMovieListObservable() {
        return Observable.from(getDummyMovieList()).toList();
    }

    private Observable<MovieResponse<Movie>> getMovieResponse() {
        MovieResponse<Movie> movieResponse = new MovieResponse<>(1, getDummyMovieList(), 1, 2);
        return Observable.just(movieResponse);
    }


}