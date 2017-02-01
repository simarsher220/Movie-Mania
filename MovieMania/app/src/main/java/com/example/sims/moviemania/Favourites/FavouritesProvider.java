package com.example.sims.moviemania.Favourites;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.sims.moviemania.R;

/**
 * Created by sims on 1/2/17.
 */

public class FavouritesProvider extends ContentProvider {

    private FavouritesHelper helper;

    public static final int FAVOURITES = 100;
    public static final int FAVOURITES_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavouritesContract.AUTHORITY, FavouritesContract.PATH, FAVOURITES);
        matcher.addURI(FavouritesContract.AUTHORITY, FavouritesContract.PATH + "/#", FAVOURITES_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        helper = new FavouritesHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db;
        db = helper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor cursor;
        switch (match){
            case FAVOURITES:
                cursor = db.query(FavouritesContract
                        .FavouritesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FAVOURITES_WITH_ID:
                String movieId = uri.getLastPathSegment();
                selection = FavouritesContract.FavouritesEntry.MOVIE_ID + " = ?";
                selectionArgs = new String[]{movieId};
                cursor = db.query(FavouritesContract
                        .FavouritesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException(getContext().getResources().getString(R.string.uri_error)+uri);
        }

        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = helper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match){
            case FAVOURITES:
                long id = db.insert(FavouritesContract.FavouritesEntry.TABLE_NAME, null, values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(uri, id);
                }
                break;

            default:
                throw new android.database.SQLException(getContext().getResources().getString(R.string.insert_items_error));
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int rows;
        switch (match){
            case FAVOURITES_WITH_ID:
                String movieId = uri.getLastPathSegment();
                selection = FavouritesContract.FavouritesEntry.MOVIE_ID + " = ?";
                selectionArgs = new String[]{movieId};
                rows = db.delete(FavouritesContract.FavouritesEntry.TABLE_NAME, selection, selectionArgs);
                if (rows < 0)
                    throw new android.database.SQLException(getContext().getResources().getString(R.string.delete_items_error));
                break;

            default:
                throw new UnsupportedOperationException(getContext().getResources().getString(R.string.uri_error)+uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
