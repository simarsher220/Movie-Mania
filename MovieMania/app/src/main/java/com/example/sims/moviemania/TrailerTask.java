package com.example.sims.moviemania;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sims on 28/12/16.
 */

public class TrailerTask extends AsyncTask<URL, Void, List<TrailerItem>> {

    Context context;
    ListView trailerListView;
    Activity activity;

    public TrailerTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected List<TrailerItem> doInBackground(URL[] objects) {
        URL url = objects[0];
        List<TrailerItem> trailerItemList = new ArrayList<>();
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
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                trailerItemList.add(new TrailerItem("Trailer"+(i+1), jsonObject.getString("key")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trailerItemList;
    }

    @Override
    protected void onPostExecute(List<TrailerItem> o) {
        super.onPostExecute(o);
        trailerListView = (ListView) activity.findViewById(R.id.movie_trailers_reviews);
        TrailerAdapter adapter = new TrailerAdapter(context, R.layout.movie_trailer_item, o);
        trailerListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TrailerItem item = (TrailerItem) trailerListView.getItemAtPosition(i);

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + item.getTrailerUrl()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + item.getTrailerUrl()));
                try {
                    activity.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    activity.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public class TrailerAdapter extends ArrayAdapter {

        List<TrailerItem> trailerItems;
        Context context;
        int resource;
        public TrailerAdapter(Context context, int resource, List<TrailerItem> trailerItems) {
            super(context, resource);
            this.context = context;
            this.resource = resource;
            this.trailerItems = trailerItems;
        }

        @Override
        public int getCount() {
            return trailerItems.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return trailerItems.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null)
                convertView = layoutInflater.inflate(resource, parent, false);
            TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_text);
            trailerName.setText(trailerItems.get(position).getTrailerName());
            return convertView;
        }

    }
}
