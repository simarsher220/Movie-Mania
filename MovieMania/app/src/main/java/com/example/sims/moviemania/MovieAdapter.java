package com.example.sims.moviemania;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sims on 24/12/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieItemHolder> {

    List<MovieItem> movieItemList;
    MovieItemClickListener listener;

    public interface MovieItemClickListener{
        public void onMovieItemClick(MovieItem item);
    }

    public MovieAdapter(List<MovieItem> movieItemList, MovieItemClickListener listener) {
        this.movieItemList = movieItemList;
        this.listener = listener;
    }

    @Override
    public MovieItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        MovieItemHolder movieItemHolder = new MovieItemHolder(view);
        return movieItemHolder;
    }

    @Override
    public void onBindViewHolder(MovieItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movieItemList.size();
    }

    public class MovieItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView moviePoster;
        public MovieItemHolder(View itemView) {
            super(itemView);

            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        public void bind(int position){
            ImageLoader.getInstance().displayImage(movieItemList.get(position).getPosterPath(), moviePoster);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onMovieItemClick(movieItemList.get(position));
        }
    }
}
