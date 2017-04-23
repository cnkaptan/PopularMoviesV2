package com.cnkaptan.transferwisehomework.model;

import java.util.List;

/**
 * Created by cnkaptan on 16/04/2017.
 */

public class TrailerAndReviews {
    List<Trailer> trailers;
    List<Review> reviews;

    public TrailerAndReviews(List<Trailer> trailers, List<Review> reviews) {
        this.trailers = trailers;
        this.reviews = reviews;
    }

    public List<Trailer> getTrailers() {

        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
