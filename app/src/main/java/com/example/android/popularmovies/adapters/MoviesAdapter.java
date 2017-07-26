package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;

/**
 * Adapter to handle the recycler view with movie data
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private static final String TAG = "MoviesAdapter";
    private ArrayList<Movie> mMoviesList;

    private MovieAdapterOnClickHandler mClickHandler;

    public MoviesAdapter(MovieAdapterOnClickHandler mHandler) {
        mClickHandler = mHandler;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int LayoutIdForGridItem = R.layout.moviegrid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(LayoutIdForGridItem, parent, shouldAttachToParentImmediately);

        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {

        Movie holderMovie = mMoviesList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(holderMovie.getPosterUrl())
                .placeholder(R.drawable.ic_movie_filter_black_200dp)
                .into(holder.mMovieImageIV);
    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null) {
            return 0;
        } else {
            return mMoviesList.size();
        }

    }

    public interface MovieAdapterOnClickHandler {
        void onMovieClick(Movie currentMovie);
    }

    public void setMoviesList(ArrayList<Movie> movielist) {

        Log.i(TAG,"setMoviesList starts"+movielist.size());
        mMoviesList = movielist;
        notifyDataSetChanged();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMovieImageIV;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            mMovieImageIV = (ImageView) itemView.findViewById(R.id.ivmovieimage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"Movie Id "+mMoviesList.get(getAdapterPosition()).getMovieId());
                    mClickHandler.onMovieClick(mMoviesList.get(getAdapterPosition()));
                }
            });
        }
    }
}
