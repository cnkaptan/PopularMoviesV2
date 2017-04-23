package com.cnkaptan.transferwisehomework.util;

import com.cnkaptan.transferwisehomework.model.Movie;
import com.cnkaptan.transferwisehomework.model.Review;
import com.cnkaptan.transferwisehomework.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnkaptan on 23/04/2017.
 */

public class TestUtilities {
    public static final String MOVIE1_TITLE = "AliBaba";
    public static final String MOVIE2_TITLE = "Meaning of Life";
    public static final int MOVIE1_ID = 10;
    public static final int MOVIE2_ID = 11;
    public static final long MOVIE_ID_FOR_DETAIL_PRESENTER = 1;
    public static final long MOVIE_ID_FOR_REVIEWS_AND_TRAILERS = 11;
    public static final String TRAILER_1_NAME = "Trailer1";
    public static final String REVIEW_AUTHOR = "Cihan";

    public static List<Movie> getDummyMovieList() {
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie1FullDetails());
        movieList.add(movie2FullDetails());
        return movieList;
    }

    public static Movie movie1FullDetails() {
        return new Movie(MOVIE1_ID, MOVIE1_TITLE,"Test Overview",3.21,9);
    }

    public static Movie movie2FullDetails() {
        return new Movie(MOVIE2_ID, MOVIE2_TITLE,"Test Overview",6.89,20);
    }



    public static List<Trailer> getDummyTrailerList(){
        List<Trailer> trailerList = new ArrayList<>();
        Trailer trailer = new Trailer(TRAILER_1_NAME);
        trailerList.add(trailer);
        return trailerList;
    }

    public static List<Review> getDummyReviewList(){
        List<Review> reviewList = new ArrayList<>();
        Review review = new Review(REVIEW_AUTHOR,"url1");
        reviewList.add(review);
        return reviewList;
    }
}
