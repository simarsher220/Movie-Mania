package com.example.sims.moviemania.Movie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sims.moviemania.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sims on 23/12/16.
 */

public class MovieTask extends AsyncTask<URL, Void, List<MovieItem>> {

    private Context context;
    private Activity activity;
    ProgressDialog dialog;
    public MovieTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected List<MovieItem> doInBackground(URL[] objects) {
        URL url = objects[0];
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder("");
            String s = "";

            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }

            JSONObject object = new JSONObject(stringBuilder.toString());
            JSONArray array = object.getJSONArray("results");
            List<MovieItem> movieItemList = new ArrayList<MovieItem>();
            for(int i = 0;i < array.length();i++){

                JSONObject jsonObject = array.getJSONObject(i);
                String original_title = jsonObject.getString("original_title");
                String poster_path = "http://image.tmdb.org/t/p/original//" + jsonObject.getString("poster_path");
                String overview = jsonObject.getString("overview");
                double vote_average = jsonObject.getDouble("vote_average");
                String release_date = jsonObject.getString("release_date");
                int id = jsonObject.getInt("id");
                double popularity = jsonObject.getDouble("popularity");
                movieItemList.add(new MovieItem(original_title, poster_path, overview, vote_average, release_date, id, popularity));
            }

            return movieItemList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<MovieItem> o) {

        RecyclerView movieList;
        movieList = (RecyclerView) activity.findViewById(R.id.rv_movie_list);

        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        movieList.setLayoutManager(layoutManager);
        movieList.setHasFixedSize(false);
        if (o != null) {
            dialog.dismiss();
            MovieAdapter adapter = new MovieAdapter(o, (MovieAdapter.MovieItemClickListener) context);
            movieList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else {
            dialog.dismiss();
            movieList.setVisibility(View.INVISIBLE);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage("Failed To Load Results");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activity.finish();
                }
            });
            alertDialog.show();
        }

    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...Please Wait");
        dialog.show();
        super.onPreExecute();
    }

}
