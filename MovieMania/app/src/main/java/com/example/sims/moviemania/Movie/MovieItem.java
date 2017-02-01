package com.example.sims.moviemania.Movie;

import java.io.Serializable;

/**
 * Created by sims on 24/12/16.
 */

public class MovieItem implements Serializable{

    private String originalTitle;
    private String posterPath;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private int movieId;
    private double popularity;

    public MovieItem(String originalTitle, String posterPath, String overview, double voteAverage, String releaseDate, int movieId, double popularity) {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
        this.popularity = popularity;

    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getMovieId() {
        return movieId;
    }

    public double getPopularity() {
        return popularity;
    }
}
