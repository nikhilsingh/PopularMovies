package com.example.android.popularmovies.activities;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MovieDetailsAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.loaders.ReviewAsyncLoader;
import com.example.android.popularmovies.loaders.TrailerAsyncLoader;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements MovieDetailsAdapter.DetailAdapterOnClickHandler {
    private static final String TAG = "DetailActivity";
    private String firstTrailerKey;
    private RecyclerView mMovieDetailsRV;
    private Movie currentMovie;
    private ArrayList<Object> mObject;
    private ArrayList<Review> mReviewList;
    private ArrayList<Trailer> mTrailerList;
    private MovieDetailsAdapter mMovieDetailAdapter;
    private LoaderManager.LoaderCallbacks reviewLoaderListener, trailerLoaderListener;
    private boolean reviewLoadFinished, trailerLoadFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.i(TAG, "onCreate starts");
        mObject = new ArrayList<>();

        mMovieDetailsRV = (RecyclerView) findViewById(R.id.rv_moviedetails);

        if (savedInstanceState != null) {
            Log.i(TAG, "Saved Instance state is not null");
            mReviewList = new ArrayList<Review>();
            mTrailerList = new ArrayList<Trailer>();
            currentMovie = new Movie();

            mReviewList = savedInstanceState.getParcelableArrayList("reviewlist");
            mTrailerList = savedInstanceState.getParcelableArrayList("trailerlist");
            currentMovie = savedInstanceState.getParcelable("moviedata");
        }

        if (currentMovie == null) {
            Log.i(TAG, "Current movie is null");
            currentMovie = getIntent().getParcelableExtra(getString(R.string.intentextra_movie));

            if (!currentMovie.isFav()) {
                Log.i(TAG, "Current Movie is not fav initially. Check from DB" + currentMovie.getMovieId());
                Cursor movieDBData = getMovieDataByProvider(currentMovie);
                if (movieDBData.getCount() > 0) {

                    currentMovie.setFav(true);
                }
            }
            if (currentMovie != null) {
                mObject.add(currentMovie);
            }
        } else {
            Log.i(TAG, "Current movie is not null");
            mObject.add(currentMovie);
        }
        if (mTrailerList == null) {
            Log.i(TAG, "TrailerList is null");
            setupTrailerLoaderCallback();
            getSupportLoaderManager().initLoader(23, null, trailerLoaderListener);
        } else {
            Log.i(TAG, "TrailerList is not null");
            if (mTrailerList.size() > 0) {
                Log.i(TAG, "TrailerList size > 0");
                firstTrailerKey = mTrailerList.get(0).getTrailerKey();
                mObject.add("Trailers");
                mObject.addAll(mTrailerList);
            }
        }
        if (mReviewList == null) {
            Log.i(TAG, "ReviewList is null");
            setupReviewLoaderCallback();
            getSupportLoaderManager().initLoader(22, null, reviewLoaderListener);
        } else {
            Log.i(TAG, "ReviewList is not null");
            if (mReviewList.size() > 0) {
                Log.i(TAG, "ReviewList size is > 0");
                mObject.add("Reviews");
                mObject.addAll(mReviewList);
            }
        }


        Log.i(TAG, "mObject size is " + mObject.size());


        mMovieDetailAdapter = new MovieDetailsAdapter(this, mObject);

        mMovieDetailsRV.setLayoutManager(new LinearLayoutManager(this));
        //mMovieDetailsRV.setHasFixedSize(true);
        mMovieDetailsRV.setAdapter(mMovieDetailAdapter);


    }


    private Cursor getMovieDataByProvider(Movie movie) {
        Log.i(TAG, "MovieId :getMovieData " + movie.getMovieId());
        return getContentResolver().query(MovieContract.MovieEntry.getUriForMovieId(movie.getMovieId()), null, null, null, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailsmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_trailer_share) {
            if (firstTrailerKey != null) {
                Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(NetworkUtils.getYoutubeWatchUri(firstTrailerKey).toString()).getIntent();
                startActivity(shareIntent);
            }else{
                Toast.makeText(this,"No Trailer Available to Share",Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupReviewLoaderCallback() {
        reviewLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {
            @Override
            public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
                return new ReviewAsyncLoader(DetailActivity.this, currentMovie.getMovieId());
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {

                //     if (data != null && data.size() > 0) {
                //  mReviewTV.setText(data.get(0).getContent());
                setReviewListView(data);
                // }
                Log.i(TAG, "Setting review key");
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Review>> loader) {

            }
        };


    }

    private void setupTrailerLoaderCallback() {
        trailerLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Trailer>>() {
            @Override
            public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
                return new TrailerAsyncLoader(DetailActivity.this, currentMovie.getMovieId());
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
                Log.i(TAG, "Setting trailer key");

                //    if (data != null && data.size() > 0) {

                setTrailersGridView(data); //mTrailerTV.setText(data.get(0).getTrailerKey());
                //     }
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

            }
        };

    }

    private void setReviewListView(ArrayList<Review> reviewList) {
        Log.i(TAG, "ReviewListView done");
        mReviewList = reviewList;
        reviewLoadFinished = true;
        Log.i(TAG, "setReviewListView review and trailer boolean" + reviewLoadFinished + trailerLoadFinished);


        if (trailerLoadFinished && reviewLoadFinished) {
            Log.i(TAG, "setReviewListView calling updateComplete");
            updateCompleteAdapterData();
        }


    }

    private void setTrailersGridView(ArrayList<Trailer> trailerList) {
        Log.i(TAG, "TrailerListView done");
        mTrailerList = trailerList;
        trailerLoadFinished = true;
        Log.i(TAG, "setTrailerView review and trailer boolean" + reviewLoadFinished + trailerLoadFinished);


        if (trailerLoadFinished && reviewLoadFinished) {
            Log.i(TAG, "setTrailerListView calling updateComplete");
            updateCompleteAdapterData();
        }
    }


    private void updateCompleteAdapterData() {


        if (mTrailerList != null && mTrailerList.size() > 0) {
            firstTrailerKey = mTrailerList.get(0).getTrailerKey();
            mObject.add("Trailers");
            mObject.addAll(mTrailerList);
        }
        if (mReviewList != null && mReviewList.size() > 0) {
            mObject.add("Reviews");
            mObject.addAll(mReviewList);
        }
        trailerLoadFinished = false;
        reviewLoadFinished = false;
        mMovieDetailAdapter.notifyDataSetChanged();
    }


    @Override
    public void onFavChecked(Movie movie) {
        addMovieByProvider(movie);
    }

    @Override
    public void onFavUnchecked(Movie movie) {
        deleteMovieByProvider(movie);
    }


    private Uri addMovieByProvider(Movie movie) {
        Log.i(TAG, "Adding Favourite movie to database" + movie.getEnglishTitle());
        currentMovie.setFav(true);
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIEID, movie.getMovieId());
        values.put(MovieContract.MovieEntry.COLUMN_ENGLISHTITLE, movie.getEnglishTitle());
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINALTITLE, movie.getOriginalTitle());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_POSTERURL, movie.getPosterUrl());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_USERRATING, movie.getUserRating());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROPURL, movie.getBackdroppath());

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
        if (uri != null) {
            Toast.makeText(getBaseContext(), "Movie Added to Favourites", Toast.LENGTH_LONG).show();
            SharedPreferences mSharedPref = this.getSharedPreferences(getString(R.string.moviesharedpreffile), Context.MODE_PRIVATE);
            mSharedPref.edit().putString("reloadfav", "true").commit();
        }
        Log.i(TAG, "Returned Insert Uri" + uri);
        return uri;
    }

    private boolean deleteMovieByProvider(Movie movie) {
        currentMovie.setFav(false);
        Log.i(TAG, "Removing Favourite movie from database" + movie.getEnglishTitle());
        Uri delUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getMovieId())).build();
        int delFavMovie = getContentResolver().delete(delUri, null, null);

        if (delFavMovie != 0) {
            Toast.makeText(getBaseContext(), "Movie Removed From Favourites", Toast.LENGTH_LONG).show();
            SharedPreferences mSharedPref = this.getSharedPreferences(getString(R.string.moviesharedpreffile), Context.MODE_PRIVATE);
            mSharedPref.edit().putString("reloadfav", "true").commit();
            return true;
        }
        return false;


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("reviewlist", mReviewList);
        outState.putParcelableArrayList("trailerlist", mTrailerList);
        outState.putParcelable("moviedata", currentMovie);

        super.onSaveInstanceState(outState);

    }

}