package com.cnkaptan.transferwisehomework.data.api;

import com.cnkaptan.transferwisehomework.model.Movie;
import com.cnkaptan.transferwisehomework.model.MovieResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.observers.TestSubscriber;

import static com.cnkaptan.transferwisehomework.util.TestUtilities.MOVIE1_TITLE;
import static com.cnkaptan.transferwisehomework.util.TestUtilities.MOVIE2_TITLE;
import static com.cnkaptan.transferwisehomework.util.TestUtilities.getDummyMovieList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by cnkaptan on 22/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieApiTest {
    @Mock
    MovieApi movieApi;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void discoverMovies_200OkResponse_InvokesCorrectApiCalls() throws Exception {
        //Arrange
        when(movieApi.discoverMovies(1)).thenReturn(getMovieResponse());

        //Action
        TestSubscriber<MovieResponse<Movie>> subscriber = new TestSubscriber<>();
        movieApi.discoverMovies(1).subscribe(subscriber);

        //Assert
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        List<MovieResponse<Movie>> onNextEvents = subscriber.getOnNextEvents();
        MovieResponse<Movie> movieResponse = onNextEvents.get(0);
        Assert.assertEquals(MOVIE1_TITLE, movieResponse.getResults().get(0).getTitle());
        Assert.assertEquals(MOVIE2_TITLE, movieResponse.getResults().get(1).getTitle());
        verify(movieApi).discoverMovies(1);
    }

    private Observable<MovieResponse<Movie>> getMovieResponse() {
        MovieResponse<Movie> movieResponse = new MovieResponse<>(1,getDummyMovieList(),1,2);
        return Observable.just(movieResponse);
    }

    @Test
    public void dicoverMovies_HttpError_TerminatedWithError() {
        //Arrange
        when(movieApi.discoverMovies(1)).thenReturn(get403ForbiddenError());

        //Action
        TestSubscriber<MovieResponse<Movie>> subscriber = new TestSubscriber<>();
        movieApi.discoverMovies(1).subscribe(subscriber);

        //Assert
        subscriber.awaitTerminalEvent();
        subscriber.assertError(HttpException.class);


        verify(movieApi).discoverMovies(1);

    }


    private Observable<MovieResponse<Movie>> get403ForbiddenError() {
        return Observable.error(new HttpException(
                Response.error(403, ResponseBody.create(MediaType.parse("application/json"), "Forbidden"))));
    }
}