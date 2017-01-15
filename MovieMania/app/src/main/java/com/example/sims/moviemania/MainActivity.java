package com.example.sims.moviemania;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener{

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String NOW_PLAYING = "now_playing";
    private static final String POPULARITY = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String API_PARAM = "api_key";
    //TODO(1) enter the API_KEY in the API_VALUE
    private static final String API_VALUE = "9e4561906e8ceeba1f6f963b2beee6dc";
    private static final String LANG_PARAM = "language";
    private static final String LANG_VALUE = "en-US";
    private static final String PAGE_PARAM = "page";
    private static final String PAGE_VALUE = "1";
    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri = buildNowPlayingyUri();
        url = buildUrl(uri);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
                .cacheOnDisk(true)
        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .build();
        ImageLoader.getInstance().init(config);

        new MovieTask(this).execute(url);
    }

    @Override
    public void onMovieItemClick(MovieItem item) {

        Intent i = new Intent(MainActivity.this, DetailActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, item);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_movies_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Uri uri;
        switch (item.getItemId()){
            case R.id.popularity:
                uri = buildPopularityUri();
                url = buildUrl(uri);
                new MovieTask(this).execute(url);
                break;
            case R.id.top_rated:
                uri = buildTopRatedUri();
                url = buildUrl(uri);
                new MovieTask(this).execute(url);
                break;
            case R.id.favourites_list:
                startActivity(new Intent(this, FavouritesActivity.class));
        }
        return true;
    }

    public Uri buildTopRatedUri(){
        return Uri.parse(BASE_URL+TOP_RATED).buildUpon()
                .appendQueryParameter(API_PARAM, API_VALUE)
                .appendQueryParameter(LANG_PARAM, LANG_VALUE)
                .appendQueryParameter(PAGE_PARAM, PAGE_VALUE).build();
    }

    public Uri buildPopularityUri(){
        return Uri.parse(BASE_URL+POPULARITY).buildUpon()
                .appendQueryParameter(API_PARAM, API_VALUE)
                .appendQueryParameter(LANG_PARAM, LANG_VALUE)
                .appendQueryParameter(PAGE_PARAM, PAGE_VALUE).build();
    }

    public Uri buildNowPlayingyUri(){
        return Uri.parse(BASE_URL+NOW_PLAYING).buildUpon()
                .appendQueryParameter(API_PARAM, API_VALUE)
                .appendQueryParameter(LANG_PARAM, LANG_VALUE)
                .appendQueryParameter(PAGE_PARAM, PAGE_VALUE).build();
    }
    public URL buildUrl(Uri uri){
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
