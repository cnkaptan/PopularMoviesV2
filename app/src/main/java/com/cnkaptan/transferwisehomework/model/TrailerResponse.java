package com.cnkaptan.transferwisehomework.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cnkaptan on 08/04/2017.
 */

public class TrailerResponse {
    @SerializedName("id")
    private long movieId;

    @SerializedName("results")
    private ArrayList<Trailer> results;

    public TrailerResponse(long movieId, ArrayList<Trailer> results) {
        this.movieId = movieId;
        this.results = results;
    }

    public long getMovieId() {
        return movieId;
    }

    public ArrayList<Trailer> getResults() {
        return results;
    }
}
