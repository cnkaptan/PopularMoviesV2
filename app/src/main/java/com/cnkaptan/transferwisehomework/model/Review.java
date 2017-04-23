package com.cnkaptan.transferwisehomework.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cnkaptan on 08/04/2017.
 */

public class Review implements Parcelable {
    @SerializedName("id")
    private String reviewId;

    @SerializedName("author")
    private String author;

    @SerializedName("url")
    private String reviewUrl;

    @SerializedName("content")
    private String content;

    public Review(String author, String reviewUrl) {
        this.author = author;
        this.reviewUrl = reviewUrl;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (reviewId != null ? !reviewId.equals(review.reviewId) : review.reviewId != null) return false;
        if (author != null ? !author.equals(review.author) : review.author != null) return false;
        if (reviewUrl != null ? !reviewUrl.equals(review.reviewUrl) : review.reviewUrl != null) return false;
        return content != null ? content.equals(review.content) : review.content == null;

    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = reviewId != null ? reviewId.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (reviewUrl != null ? reviewUrl.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reviewId);
        dest.writeString(this.author);
        dest.writeString(this.reviewUrl);
        dest.writeString(this.content);
    }

    public Review() {
    }

    protected Review(Parcel in) {
        this.reviewId = in.readString();
        this.author = in.readString();
        this.reviewUrl = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
