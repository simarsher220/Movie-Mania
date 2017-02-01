package com.example.sims.moviemania;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sims.moviemania.Favourites.FavouriteTask;
import com.example.sims.moviemania.Favourites.FavouritesContract;
import com.example.sims.moviemania.Favourites.FavouritesHelper;
import com.example.sims.moviemania.Movie.MovieItem;
import com.example.sims.moviemania.R;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity implements View.OnLongClickListener{

    Toolbar toolbar;
    TextView itemsSelected;
    TextView appName;
    List<MovieItem> selectionList;
    int counter;
    boolean isActionMode = false;
    FavouriteTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        toolbar = (Toolbar) findViewById(R.id.fav_selection_toolbar);
        setSupportActionBar(toolbar);
        task = new FavouriteTask(this, 0);
        task.execute();
        itemsSelected = (TextView) findViewById(R.id.items_selected);
        itemsSelected.setVisibility(View.INVISIBLE);
        appName = (TextView) findViewById(R.id.app_name);
        selectionList = new ArrayList<>();
        counter = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav_movies_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.fav_popularity:
                task = new FavouriteTask(this, R.id.fav_popularity);
                task.execute();
                break;
            case R.id.fav_top_rated:
                task = new FavouriteTask(this, R.id.fav_top_rated);
                task.execute();
                break;
            case R.id.del_favourites:
                for (MovieItem movieItem : selectionList){
                    getContentResolver().delete(FavouritesContract.FavouritesEntry.CONTENT_URI.buildUpon().
                                    appendPath(movieItem.getMovieId()+"")
                                    .build()
                            , null, null);
                }
                clearActionMode();
                break;
            case android.R.id.home:
                clearActionMode();
        }
        return true;
    }

    public void clearActionMode(){
        isActionMode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.fav_movies_sort);
        appName.setVisibility(View.VISIBLE);
        itemsSelected.setVisibility(View.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counter = 0;
        selectionList.clear();
        task = new FavouriteTask(this, 0);
        task.execute();
    }

    @Override
    public boolean onLongClick(View view) {
        isActionMode = true;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.fav_option_menu);
        appName.setVisibility(View.INVISIBLE);
        itemsSelected.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    public void createSelection(ImageView view, MovieItem item){
        view.setClickable(true);
        if (view.isSelected()){
            selectionList.remove(item);
            counter--;
            itemsSelected.setText(counter+" "+getResources().getQuantityString(R.plurals.items_selected, counter));
            view.setSelected(false);
        }
        else{
            selectionList.add(item);
            counter++;
            itemsSelected.setText(counter+" "+getResources().getQuantityString(R.plurals.items_selected, counter));
            view.setSelected(true);
        }
    }

    @Override
    public void onBackPressed() {
        if(isActionMode){
            clearActionMode();
        }
        else {
            super.onBackPressed();
        }
    }
}
