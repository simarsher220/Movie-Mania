package com.example.sims.moviemania.Favourites;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sims on 1/2/17.
 */

public class FavouritesContract {

    public static final String AUTHORITY = "com.example.sims.moviemania";
    public static final String PATH = "Favourites";

    public static class FavouritesEntry{

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY)
                .buildUpon().appendPath(PATH).build();

        public static final int DB_VERSION = 1;
        public static final String DB_NAME = "Favourite Movies";
        public static final String TABLE_NAME = "Favourites";
        public static final String TITLE = "title";
        public static final String POSTER_PATH = "image";
        public static final String OVERVIEW = "overview";
        public static final String RATING = "rating";
        public static final String RELEASE_DATE = "release_date";
        public static final String MOVIE_ID = "movie_id";
        public static final String POPULARITY = "popularity";

    }
}
