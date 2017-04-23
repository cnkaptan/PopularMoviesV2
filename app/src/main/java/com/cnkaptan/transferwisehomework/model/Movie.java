package com.cnkaptan.transferwisehomework.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by cnkaptan on 08/04/2017.
 */

public class Movie extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("realm_id")
    private String id;

    @SerializedName("id")
    private long movieId;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("title")
    private String title;

    @SerializedName("vote_average")
    private double averageVote;

    @SerializedName("vote_count")
    private long voteCount;

    @SerializedName("backdrop_path")
    private String backdropPath;

    public Movie(long movieId, String title, String overview, double popularity, double averageVote) {
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.popularity = popularity;
        this.averageVote = averageVote;
    }

    public Movie(long movieId, String originalTitle, String overview, String releaseDate, String posterPath,
                 double popularity, String title, double averageVote, long voteCount, String backdropPath) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.title = title;
        this.averageVote = averageVote;
        this.voteCount = voteCount;
        this.backdropPath = backdropPath;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(double averageVote) {
        this.averageVote = averageVote;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
    @Override
    public String toString() {
        return "[" + this.movieId + ", " + this.title + "]";
    }
    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (movieId != movie.movieId) return false;
        if (Double.compare(movie.popularity, popularity) != 0) return false;
        if (Double.compare(movie.averageVote, averageVote) != 0) return false;
        if (voteCount != movie.voteCount) return false;
        if (originalTitle != null ? !originalTitle.equals(movie.originalTitle) : movie.originalTitle != null)
            return false;
        if (overview != null ? !overview.equals(movie.overview) : movie.overview != null) return false;
        if (releaseDate != null ? !releaseDate.equals(movie.releaseDate) : movie.releaseDate != null) return false;
        if (posterPath != null ? !posterPath.equals(movie.posterPath) : movie.posterPath != null) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        return backdropPath != null ? backdropPath.equals(movie.backdropPath) : movie.backdropPath == null;

    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result;
        long temp;
        result = (int) (movieId ^ (movieId >>> 32));
        result = 31 * result + (originalTitle != null ? originalTitle.hashCode() : 0);
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (posterPath != null ? posterPath.hashCode() : 0);
        temp = Double.doubleToLongBits(popularity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        temp = Double.doubleToLongBits(averageVote);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (voteCount ^ (voteCount >>> 32));
        result = 31 * result + (backdropPath != null ? backdropPath.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.movieId);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeDouble(this.popularity);
        dest.writeString(this.title);
        dest.writeDouble(this.averageVote);
        dest.writeLong(this.voteCount);
        dest.writeString(this.backdropPath);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.movieId = in.readLong();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.popularity = in.readDouble();
        this.title = in.readString();
        this.averageVote = in.readDouble();
        this.voteCount = in.readLong();
        this.backdropPath = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };



}
