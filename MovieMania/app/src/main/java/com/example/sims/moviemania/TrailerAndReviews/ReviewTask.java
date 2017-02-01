package com.example.sims.moviemania.TrailerAndReviews;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sims.moviemania.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sims on 28/12/16.
 */

public class ReviewTask extends AsyncTask<URL, Void, List<ReviewItem>> {

    Context context;
    ListView reviewListView;
    Activity activity;

    public ReviewTask(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected List<ReviewItem> doInBackground(URL[] objects) {
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
            List<ReviewItem> reviewItemList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                reviewItemList.add(new ReviewItem(jsonObject.getString("author"), jsonObject.getString("content")));
            }
            return reviewItemList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<ReviewItem> o) {
        super.onPostExecute(o);
        reviewListView = (ListView) activity.findViewById(R.id.movie_trailers_reviews);
        ReviewAdapter adapter = new ReviewAdapter(context, R.layout.movie_review_item, o);
        reviewListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public class ReviewAdapter extends ArrayAdapter{

        Context context;
        int resource;
        List<ReviewItem> reviewItems;
        public ReviewAdapter(Context context, int resource, List<ReviewItem> reviewItems) {
            super(context, resource, reviewItems);
            this.context = context;
            this.resource = resource;
            this.reviewItems = reviewItems;
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return reviewItems.size();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null)
                convertView = layoutInflater.inflate(resource, parent, false);
            TextView author = (TextView) convertView.findViewById(R.id.author);
            TextView content = (TextView) convertView.findViewById(R.id.content);
            author.setText(reviewItems.get(position).getAuthor());
            content.setText(reviewItems.get(position).getContent());
            return convertView;
        }
    }
}

