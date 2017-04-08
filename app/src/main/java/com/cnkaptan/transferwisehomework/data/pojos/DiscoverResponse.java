package com.cnkaptan.transferwisehomework.data.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cnkaptan on 08/04/2017.
 */

public class DiscoverResponse<T> {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<T> results;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private long totalResults;

    public DiscoverResponse(int page, List<T> results, int totalPages, long totalResults) {
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public int getPage() {
        return page;
    }

    public List<T> getResults() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }
}
