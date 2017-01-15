package com.example.sims.moviemania;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.net.MalformedURLException;
import java.net.URL;

public class TrailerAndReviews extends AppCompatActivity {

    private static final String BASIC_URL1 = "https://api.themoviedb.org/3/movie/";
    private static final String BASIC_URL2 = "/videos";
    private static final String BASIC_URL3 = "/reviews";
    private static final String API_PARAM = "api_key";
    //TODO(2) enter the API_KEY in the API_VALUE
    private static final String API_VALUE = "9e4561906e8ceeba1f6f963b2beee6dc";
    private static final String LANG_PARAM = "language";
    private static final String LANG_VALUE = "en-US";
    private static final String PAGE_PARAM = "page";
    private static final String PAGE_VALUE = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_and_reviews);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        if (i.resolveActivity(getPackageManager()) != null){
            String action = i.getStringExtra(Intent.EXTRA_TEXT);
            Bundle b = i.getExtras();
            if (action.equals("trailers")){
                new TrailerTask(this).execute(trailerUrlBuild(b.getInt("MOVIE_ID")));
            }
            else if (action.equals("reviews")){
                new ReviewTask(this).execute(reviewUrlBuild(b.getInt("MOVIE_ID")));
            }
        }
    }

    public URL trailerUrlBuild(int id){
        Uri uri = Uri.parse(BASIC_URL1+id+BASIC_URL2).buildUpon()
                .appendQueryParameter(API_PARAM, API_VALUE)
                .appendQueryParameter(LANG_PARAM, LANG_VALUE).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  url;
    }

    public URL reviewUrlBuild(int id){
        Uri uri = Uri.parse(BASIC_URL1+id+BASIC_URL3).buildUpon()
                .appendQueryParameter(API_PARAM, API_VALUE)
                .appendQueryParameter(LANG_PARAM, LANG_VALUE)
                .appendQueryParameter(PAGE_PARAM, PAGE_VALUE).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  url;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
