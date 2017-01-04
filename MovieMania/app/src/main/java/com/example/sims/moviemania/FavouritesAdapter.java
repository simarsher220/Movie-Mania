package com.example.sims.moviemania;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by sims on 29/12/16.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    List<MovieItem> items;
    Context context;
    public FavouritesAdapter(List<MovieItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.favourite_item, parent, false);
        FavouritesViewHolder holder = new FavouritesViewHolder(view, (FavouritesActivity) context);
        return holder;
    }

    @Override
    public void onBindViewHolder(FavouritesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView poster;
        FavouritesActivity activity;
        public FavouritesViewHolder(View itemView, FavouritesActivity activity) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.movie_poster_favourite);
            this.activity = activity;
            poster.setOnLongClickListener(activity);
            poster.setOnClickListener(this);
        }

        public void bind(int position){
            ImageLoader.getInstance().displayImage(items.get(position).getPosterPath(), poster);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            activity.createSelection((ImageView)view, items.get(position));
        }
    }
}
