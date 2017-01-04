package com.example.sims.moviemania;

import java.net.URL;

/**
 * Created by sims on 28/12/16.
 */

public class TrailerItem {

    private  String trailerName;
    private String trailerUrl;

    public TrailerItem(String trailerName, String trailerUrl) {
        this.trailerName = trailerName;
        this.trailerUrl = trailerUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public String getTrailerName() {
        return trailerName;
    }
}
