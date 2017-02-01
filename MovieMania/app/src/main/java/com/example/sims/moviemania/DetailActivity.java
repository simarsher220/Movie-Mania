package com.example.sims.moviemania;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sims.moviemania.Favourites.FavouritesContract;
import com.example.sims.moviemania.Favourites.FavouritesHelper;
import com.example.sims.moviemania.Movie.MovieItem;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import mehdi.sakout.fancybuttons.FancyButton;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView poster;
    private TextView title, releaseDate, overview;
    private RatingBar movieRating;
    private float rating;
    private FancyButton trailers, reviews;
    private int id;
    private LikeButton favourites;
    private MovieItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        poster = (ImageView) findViewById(R.id.movie_big_poster);
        title = (TextView) findViewById(R.id.movie_full_title);
        releaseDate = (TextView) findViewById(R.id.release_date);
        overview = (TextView) findViewById(R.id.movie_overview);
        movieRating = (RatingBar) findViewById(R.id.movie_rate);
        trailers = (FancyButton) findViewById(R.id.btn_trailers);
        reviews = (FancyButton) findViewById(R.id.btn_reviews);
        favourites = (LikeButton) findViewById(R.id.add_favourites);

        Intent i = getIntent();
        if (i.resolveActivity(getPackageManager()) != null){
            item = (MovieItem) i.getSerializableExtra(Intent.EXTRA_TEXT);
            title.setText(item.getOriginalTitle());
            releaseDate.setText(item.getReleaseDate());
            overview.setText(item.getOverview());
            ImageLoader.getInstance().displayImage(item.getPosterPath(), poster);
            rating = (float) 0.0;
            rating = (float) item.getVoteAverage();
            movieRating.setRating((float) item.getVoteAverage()/2);
            id = item.getMovieId();
            movieRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    movieRating.setRating(rating);
                }
            });

            ContentResolver resolver = getContentResolver();
            Cursor cr = resolver.query(FavouritesContract.FavouritesEntry.CONTENT_URI.buildUpon().
                    appendPath(item.getMovieId()+"")
                    .build(),
                    null, null, null, null);
            if (cr.getCount() == 0){
                favourites.setLiked(false);
            }
            else {
                favourites.setLiked(true);
            }
        }

        trailers.setOnClickListener(this);
        reviews.setOnClickListener(this);
        favourites.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ContentValues cv = new ContentValues();
                cv.put(FavouritesContract.FavouritesEntry.TITLE, item.getOriginalTitle());
                cv.put(FavouritesContract.FavouritesEntry.OVERVIEW, item.getOverview());
                cv.put(FavouritesContract.FavouritesEntry.POPULARITY, item.getPopularity());
                cv.put(FavouritesContract.FavouritesEntry.POSTER_PATH, item.getPosterPath());
                cv.put(FavouritesContract.FavouritesEntry.RELEASE_DATE, item.getReleaseDate());
                cv.put(FavouritesContract.FavouritesEntry.RATING, item.getVoteAverage());
                cv.put(FavouritesContract.FavouritesEntry.MOVIE_ID, item.getMovieId());
                Uri uri = getContentResolver().insert(FavouritesContract.FavouritesEntry.CONTENT_URI, cv);
                if (uri != null) {
                    Toast.makeText(DetailActivity.this, R.string.add_favourites, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                getContentResolver().delete(FavouritesContract.FavouritesEntry.CONTENT_URI.buildUpon().
                                appendPath(item.getMovieId()+"")
                                .build()
                        , null, null);
                Toast.makeText(DetailActivity.this, R.string.delete_favourites, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(DetailActivity.this, TrailerAndReviewsActivity.class);
        i.putExtra("MOVIE_ID", id);
        if (view.getId() == R.id.btn_trailers) {
            i.putExtra(Intent.EXTRA_TEXT, "trailers");
            startActivity(i);
        }
        else if(view.getId() == R.id.btn_reviews) {
            i.putExtra(Intent.EXTRA_TEXT, "reviews");
            startActivity(i);
        }
    }
}
