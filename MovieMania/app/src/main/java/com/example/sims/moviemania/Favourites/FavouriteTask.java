package com.example.sims.moviemania.Favourites;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sims.moviemania.Favourites.FavouritesAdapter;
import com.example.sims.moviemania.Favourites.FavouritesHelper;
import com.example.sims.moviemania.Movie.MovieItem;
import com.example.sims.moviemania.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sims on 3/1/17.
 */

public class FavouriteTask extends AsyncTask<Void, Void, List<MovieItem>>{

    Context ctx;
    Activity activity;
    private RecyclerView rvFavourites;
    private int sortParam;
    private FavouritesAdapter adapter;

    public FavouriteTask(Context ctx, int sortParam) {
        this.ctx = ctx;
        this.activity = (Activity) ctx;
        rvFavourites = (RecyclerView) activity.findViewById(R.id.rv_favourites);
        this.sortParam = sortParam;
    }

    @Override
    protected List<MovieItem> doInBackground(Void...objects) {
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(FavouritesContract.FavouritesEntry.CONTENT_URI, null, null, null, null);
        List<MovieItem> favouritePosters = new ArrayList<>();
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            do{
                favouritePosters.add(new MovieItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3),
                        cursor.getString(4), cursor.getInt(5), cursor.getDouble(6)));
            }while (cursor.moveToNext());
        }
        switch (sortParam){
            case R.id.fav_popularity:
                Collections.sort(favouritePosters, new Comparator<MovieItem>(){
                    public int compare(MovieItem item1, MovieItem item2) {
                        return Double.valueOf(item2.getPopularity()).compareTo(item1.getPopularity());
                    }
                });
                break;

            case R.id.fav_top_rated:
                Collections.sort(favouritePosters, new Comparator<MovieItem>(){
                    public int compare(MovieItem item1, MovieItem item2) {
                        return Double.valueOf(item2.getVoteAverage()).compareTo(item1.getVoteAverage());
                    }
                });
                break;

            case 0: break;
        }
        return favouritePosters;
    }

    @Override
    protected void onPostExecute(List<MovieItem> o) {
        GridLayoutManager layoutManager = new GridLayoutManager(ctx, 2);
        rvFavourites.setLayoutManager(layoutManager);
        rvFavourites.setHasFixedSize(false);
        adapter = new FavouritesAdapter(o, ctx);
        rvFavourites.setAdapter(adapter);
    }
}
