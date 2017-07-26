package com.example.android.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.MovieDetailsUtil;
import com.example.android.popularmovies.utilities.NetworkUtils;


import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Initial activity to handle list of Movies received from themoviedb.org.
 * Most Popular and Top Rated movies are fetched from api over http request
 */

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    private static final String TAG = "MainActivity";

    private RecyclerView mMoviesRV;
    private MoviesAdapter mMovieRVAdapter;
    private SharedPreferences mSharedPref;
    private TextView mErrorMsgView;
    private ProgressBar mProgressBar;

    private static final int LOADER_POPULAR = 40;
    private static final int LOADER_RATING = 41;
    private static final int LOADER_FAVOURITE = 42;


    private SwipeRefreshLayout mySwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.i(TAG, "onCreate Starts");
        mSharedPref = this.getSharedPreferences(getString(R.string.moviesharedpreffile), Context.MODE_PRIVATE);

        mMovieRVAdapter = new MoviesAdapter(this);


        mMoviesRV = (RecyclerView) findViewById(R.id.rv_movies);
        mErrorMsgView = (TextView) findViewById(R.id.tv_errormessage);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_inprogress);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        mMoviesRV.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.moviegrid_columns)));
        mMoviesRV.setHasFixedSize(true);
        mMoviesRV.setAdapter(mMovieRVAdapter);


        Log.i(TAG, "onCreate Ends");
        //Swipe upside down to refresh the data based on the current sort type
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh");

                String sortType = mSharedPref.getString(getString(R.string.sortbysharedpref), null);
                if (isInternetActive() | sortType.equals(getString(R.string.showfavmovies))) {
                    restartSortLoader(sortType);
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.nointerneterror), Toast.LENGTH_LONG).show();
                    initializeSortLoader(sortType);
                }

                mySwipeRefreshLayout.setRefreshing(false);


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        removeErrorView();
        Log.i(TAG, "onStart starts");
        String sortType = mSharedPref.getString(getString(R.string.sortbysharedpref), null);
        if (sortType == null) {
            sortType = getString(R.string.sortbypopular);

        }

        Log.i(TAG, "sorttype is " + sortType);
        Log.i(TAG, "Internet connectivity status onStart " + isInternetActive());


        initializeSortLoader(sortType);


    }

    private void initializeSortLoader(String sorttype) {

        final String sortByPopular = "popular";
        final String sortByRating = "top_rated";
        final String sortByFav = "favourite_movies";
        mSharedPref.edit().putString(getString(R.string.sortbysharedpref), sorttype).commit();


        //    if (isInternetActive() || sorttype.equals(sortByFav)) {
        switch (sorttype) {


            case sortByPopular:
                getSupportLoaderManager().initLoader(LOADER_POPULAR, null, this);
                break;
            case sortByRating:
                getSupportLoaderManager().initLoader(LOADER_RATING, null, this);
                break;
            case sortByFav:
                if (mSharedPref.getString("reloadfav", "").equals("true")) {
                    Log.i(TAG, "restartting from initSortLoader");
                    restartSortLoader(mSharedPref.getString(getString(R.string.sortbysharedpref), null));
                    mSharedPref.edit().remove("reloadfav").commit();
                } else {
                    getSupportLoaderManager().initLoader(LOADER_FAVOURITE, null, this);
                }

                break;

        }

    }

    private void restartSortLoader(String sorttype) {
        final String sortByPopular = "popular";
        final String sortByRating = "top_rated";
        final String sortByFav = "favourite_movies";
        mSharedPref.edit().putString(getString(R.string.sortbysharedpref), sorttype).commit();

        Log.i(TAG, "Internet connectivity status restartloader" + isInternetActive());
        if (isInternetActive() || sorttype.equals(sortByFav)) {


            switch (sorttype) {


                case sortByPopular:
                    getSupportLoaderManager().restartLoader(LOADER_POPULAR, null, this);
                    break;
                case sortByRating:
                    getSupportLoaderManager().restartLoader(LOADER_RATING, null, this);
                    break;
                case sortByFav:
                    getSupportLoaderManager().restartLoader(LOADER_FAVOURITE, null, this);
                    break;

            }
        } else {
            showErrorMsg(getString(R.string.nointerneterror));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sortoptions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sortpopular:
                setTitle("Popular Movies");
                initializeSortLoader(getString(R.string.sortbypopular));
                break;
            case R.id.action_sortratings:
                setTitle("Top Rated Movies");
                initializeSortLoader(getString(R.string.sortbyrating));
                break;
            case R.id.action_favmovie:
                setTitle("Favourite Movies");
                initializeSortLoader(getString(R.string.showfavmovies));

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClick(Movie currentMovie) {
        Log.i("MainActivity", "movieId " + currentMovie.getEnglishTitle());
        Intent i = new Intent(MainActivity.this, DetailActivity.class);
        i.putExtra(getString(R.string.intentextra_movie), currentMovie);
        startActivity(i);


    }


    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {
            ArrayList<Movie> loaderArrayList;

            @Override
            protected void onStartLoading() {
                // super.onStartLoading();
                Log.i(TAG, "onStartLoading starts");

                if (loaderArrayList != null) {


                    deliverResult(loaderArrayList);
                } else {
                    Log.i(TAG, "onStart Loading calling forceLoad");

                    forceLoad();
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mMoviesRV.setVisibility(View.INVISIBLE);
                mErrorMsgView.setVisibility(View.INVISIBLE);

            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                Log.i(TAG, "loadinBackground starts");


                if (getId() == LOADER_FAVOURITE) {
                    Log.i(TAG, "loadinBackground for Favourite");
                    Cursor data = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, "_id");
                    data.setNotificationUri(getContentResolver(), Uri.parse(MovieContract.MovieEntry.CONTENT_URI + "/#"));


                    return MovieDetailsUtil.getMovieArrayListFromCursor(data);


                } else if (getId() == LOADER_POPULAR) {
                    if (isInternetActive()) {


                        Log.i(TAG, "loadinBackground for Popular");
                        URL movieListURL = NetworkUtils.buildURL(getString(R.string.sortbypopular));

                        OkHttpClient client = new OkHttpClient.Builder()
                                //    .addNetworkInterceptor(new StethoInterceptor())
                                .build();

                        Request req = new Request.Builder().url(movieListURL).build();

                        try {

                            Response response = client.newCall(req).execute();
                            return MovieDetailsUtil.getMovieArrayListFromJSON(response.body().string());


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        return null;
                    }
                } else if (getId() == LOADER_RATING) {
                    if (isInternetActive()) {


                        Log.i(TAG, "loadinBackground for Rating");
                        URL movieListURL = NetworkUtils.buildURL(getString(R.string.sortbyrating));

                        OkHttpClient client = new OkHttpClient.Builder()
                                //    .addNetworkInterceptor(new StethoInterceptor())
                                .build();

                        Request req = new Request.Builder().url(movieListURL).build();

                        try {

                            Response response = client.newCall(req).execute();
                            return MovieDetailsUtil.getMovieArrayListFromJSON(response.body().string());


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    return null;
                }
                return null;


            }

            @Override
            public void deliverResult(ArrayList<Movie> data) {


                loaderArrayList = data;
                //Log.i(TAG, "deliveResults" + data.get(2).getEnglishTitle());

                super.deliverResult(data);

                Log.i(TAG, "deliverresults post super call");
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {

        Log.i(TAG, "onLoadFinished" + loader.getId());
        if (data == null || data.size() <= 0) {
            if (loader.getId() == LOADER_FAVOURITE) {
                showErrorMsg(getString(R.string.nofavouriteerror));
            } else {
                if(isInternetActive()) {
                    showErrorMsg(getString(R.string.nodataerror));
                }else{
                    showErrorMsg(getString(R.string.nointerneterror));
                }
            }

        } else {

            mMovieRVAdapter.setMoviesList(data);
            removeErrorView();
        }


        //
    }

    private void showErrorMsg(String msg) {
        mErrorMsgView.setText(msg);
        mErrorMsgView.setVisibility(View.VISIBLE);
        mMoviesRV.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void removeErrorView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMoviesRV.setVisibility(View.VISIBLE);
        mErrorMsgView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    private boolean isInternetActive() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume starts" + mSharedPref.getString("reloadfav", null));
        if (mSharedPref.getString(getString(R.string.sortbysharedpref), null).equals(getString(R.string.showfavmovies))) {
            Log.i(TAG, "onResume sorttype is fav");
            if (mSharedPref.getString("reloadfav", "").equals("true")) {
                Log.i(TAG, "restartting");
                restartSortLoader(mSharedPref.getString(getString(R.string.sortbysharedpref), null));
                mSharedPref.edit().remove("reloadfav").commit();
            }

        }

    }
}
