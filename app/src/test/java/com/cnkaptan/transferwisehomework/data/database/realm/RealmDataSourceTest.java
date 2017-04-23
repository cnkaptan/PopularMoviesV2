package com.cnkaptan.transferwisehomework.data.database.realm;

import com.cnkaptan.transferwisehomework.BuildConfig;
import com.cnkaptan.transferwisehomework.MovieApplication;
import com.cnkaptan.transferwisehomework.model.Movie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.log.RealmLog;

import static com.cnkaptan.transferwisehomework.util.TestUtilities.MOVIE1_TITLE;
import static com.cnkaptan.transferwisehomework.util.TestUtilities.MOVIE2_TITLE;
import static com.cnkaptan.transferwisehomework.util.TestUtilities.getDummyMovieList;
import static com.cnkaptan.transferwisehomework.util.TestUtilities.movie1FullDetails;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by cnkaptan on 23/04/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25, application = MovieApplication.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest({Realm.class,RealmLog.class,RealmResults.class,RealmQuery.class})
public class RealmDataSourceTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();
    Realm mockRealm;
    private RealmDataSource realmDataSource;

    @Before
    public void setUp() throws Exception {
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);
        mockStatic(RealmResults.class);
        mockStatic(RealmQuery.class);
        Realm mockRealm = PowerMockito.mock(Realm.class);

        when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        this.mockRealm = mockRealm;
        realmDataSource = new RealmDataSource();

        RealmQuery<Movie> mockMovieListQuery = mockRealmQuery();
        when(mockRealm.where(Movie.class)).thenReturn(mockMovieListQuery);
    }

    @Test
    public void shouldBeAbleToGetDefaultInstance() {
        Assert.assertThat(Realm.getDefaultInstance(), is(mockRealm));
    }

    @Test
    public void shouldBeAbleToMockRealmMethods() {
        when(mockRealm.isAutoRefresh()).thenReturn(true);
        Assert.assertThat(mockRealm.isAutoRefresh(), is(true));

        when(mockRealm.isAutoRefresh()).thenReturn(false);
        Assert.assertThat(mockRealm.isAutoRefresh(), is(false));
    }

    @Test
    public void shouldBeAbleToCreateARealmObject() {
        Movie movie = movie1FullDetails();
        when(mockRealm.createObject(Movie.class)).thenReturn(movie);

        Movie output = mockRealm.createObject(Movie.class);

        Assert.assertThat(output, is(movie));
    }

    @Test
    public void saveMovieWithValidValues() throws Exception {
        // Arrange
        doCallRealMethod().when(mockRealm).executeTransaction(Mockito.any(Realm.Transaction.class));
        Movie movie = mock(Movie.class);
        when(mockRealm.createObject(Movie.class,movie.getId())).thenReturn(movie);


        // Action
        realmDataSource.saveMovie(movie);

        //Assert
        verify(mockRealm, times(1)).createObject(Movie.class, movie.getId());
        verify(movie, times(1)).setMovieId(Mockito.anyLong());
        verify(movie, times(1)).setTitle(Mockito.anyString());
        verify(movie, times(1)).setOriginalTitle(Mockito.anyString());
        verify(movie, times(1)).setOverview(Mockito.anyString());
        verify(movie, times(1)).setReleaseDate(Mockito.anyString());
        verify(movie, times(1)).setPosterPath(Mockito.anyString());
        verify(movie, times(1)).setPopularity(Mockito.anyDouble());
        verify(movie, times(1)).setAverageVote(Mockito.anyDouble());
        verify(movie, times(1)).setVoteCount(Mockito.anyLong());
        verify(movie, times(1)).setBackdropPath(Mockito.anyString());
        verify(movie, times(1)).setBackdropPath(Mockito.anyString());
        verify(mockRealm, times(1)).insertOrUpdate(movie);
        verify(mockRealm, times(1)).close();

    }

    @Test
    public void testgettingAllMovies() throws Exception {
        List<Movie> dummyMovieList = getDummyMovieList();
        RealmResults<Movie> mockMovieListResult = mockRealmResults();
        when(mockRealm.where(Movie.class).findAll()).thenReturn(mockMovieListResult);
        when(mockRealm.copyFromRealm(mockMovieListResult)).thenReturn(dummyMovieList);

        List<Movie> listObservable = realmDataSource.getAllMovies();

        Assert.assertEquals(MOVIE1_TITLE, listObservable.get(0).getTitle());
        Assert.assertEquals(MOVIE2_TITLE, listObservable.get(1).getTitle());
    }

    @Test
    public void testGettingCurrentPage() throws Exception {
        List<Movie> dummyMovieList = getDummyMovieList();
        RealmResults<Movie> mockMovieListResult = mockRealmResults();
        when(mockRealm.where(Movie.class).findAll()).thenReturn(mockMovieListResult);
        when(mockRealm.copyFromRealm(mockMovieListResult)).thenReturn(dummyMovieList);

        int currentPage = realmDataSource.getCurrentPage();

        Assert.assertEquals(1,currentPage);

    }

    @Test
    public void testGettingCurrentPageWithNullMovieList() throws Exception {
        RealmResults<Movie> mockMovieListResult = mockRealmResults();
        when(mockRealm.where(Movie.class).findAll()).thenReturn(mockMovieListResult);
        when(mockRealm.copyFromRealm(mockMovieListResult)).thenReturn(null);

        int currentPage = realmDataSource.getCurrentPage();

        Assert.assertEquals(1,currentPage);

    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmResults<T> mockRealmResults() {
        return mock(RealmResults.class);
    }

    private <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
        return mock(RealmQuery.class);
    }
}