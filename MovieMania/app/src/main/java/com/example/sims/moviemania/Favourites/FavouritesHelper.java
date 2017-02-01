package com.example.sims.moviemania.Favourites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sims.moviemania.Movie.MovieItem;

/**
 * Created by sims on 2/1/17.
 */

public class FavouritesHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE = "create table "+ FavouritesContract.FavouritesEntry.TABLE_NAME+"( "+
            FavouritesContract.FavouritesEntry.TITLE+" text, "+
            FavouritesContract.FavouritesEntry.POSTER_PATH+" text, "+
            FavouritesContract.FavouritesEntry.OVERVIEW+" text, " +
            FavouritesContract.FavouritesEntry.RATING+" real, "+
            FavouritesContract.FavouritesEntry.RELEASE_DATE+" text, "+
            FavouritesContract.FavouritesEntry.MOVIE_ID+" integer, "+
            FavouritesContract.FavouritesEntry.POPULARITY+" real);";

    public FavouritesHelper(Context context) {
        super(context, FavouritesContract.FavouritesEntry.DB_NAME, null, FavouritesContract.FavouritesEntry.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
