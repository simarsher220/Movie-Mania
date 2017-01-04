package com.example.sims.moviemania;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sims on 2/1/17.
 */

public class FavouritesHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Favourite Movies";
    private static final String TABLE_NAME = "Favourites";
    private static final String TITLE = "title";
    private static final String POSTER_PATH = "image";
    private static final String OVERVIEW = "overview";
    private static final String RATING = "rating";
    private static final String RELEASE_DATE = "release_date";
    private static final String MOVIE_ID = "movie_id";
    private static final String POPULARITY = "popularity";
    private static final String CREATE_TABLE = "create table "+TABLE_NAME+"( "+TITLE+" text, "+POSTER_PATH+" text, "+OVERVIEW+" text, "
            +RATING+" real, "+RELEASE_DATE+" text, "+MOVIE_ID+" integer, "+POPULARITY+" real);";
    public FavouritesHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(SQLiteDatabase db, MovieItem item){
        ContentValues cv = new ContentValues();
        cv.put(TITLE, item.getOriginalTitle());
        cv.put(POSTER_PATH, item.getPosterPath());
        cv.put(OVERVIEW, item.getOverview());
        cv.put(RATING, item.getVoteAverage());
        cv.put(MOVIE_ID, item.getMovieId());
        cv.put(POPULARITY, item.getPopularity());
        cv.put(RELEASE_DATE, item.getReleaseDate());
        db.insert(TABLE_NAME, null, cv);
    }

    public void delete(SQLiteDatabase db, MovieItem item){
        String[] args = {item.getOriginalTitle()};
        db.delete(TABLE_NAME, TITLE+" LIKE ?", args);
    }

    public Cursor select(SQLiteDatabase db, MovieItem item){
        String[] columns = {TITLE};
        String[] args = {item.getOriginalTitle()};
        Cursor cursor = db.query(TABLE_NAME, columns, TITLE+" LIKE ?", args, null, null, null);
        return cursor;
    }

    public Cursor retrieveAll(SQLiteDatabase db){
        return db.rawQuery("select * from "+TABLE_NAME, null);
    }
}
