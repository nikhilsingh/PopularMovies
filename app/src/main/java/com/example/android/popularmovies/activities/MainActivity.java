package com.example.android.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MoviesAdapter;
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

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler {
    private static final String TAG = "MainActivity";

    private RecyclerView mMoviesRV;
    private MoviesAdapter mMovieRVAdapter;
    private SharedPreferences mSharedPref;
    private TextView mErrorMsgView;
    private ProgressBar mProgressBar;


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


        mMoviesRV.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.moviegrid_columns)));
        mMoviesRV.setHasFixedSize(true);
        mMoviesRV.setAdapter(mMovieRVAdapter);


        String sortType = mSharedPref.getString(getString(R.string.sortbysharedpref), null);
        if (sortType == null) {
            sortType = getString(R.string.sortbypopular);
        }

        loadMovieData(sortType);

        Log.i(TAG, "onCreate Ends");
    }

    private void loadMovieData(String sorttype) {
        mSharedPref.edit().putString(getString(R.string.sortbysharedpref), sorttype).commit();
        new FetchMoviesTask().execute(sorttype);

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

                loadMovieData(getString(R.string.sortbypopular));
                break;
            case R.id.action_sortratings:

                loadMovieData(getString(R.string.sortbyrating));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClick(Movie currentMovie) {
        Log.i("MainActivity", "Title is " + currentMovie.getOriginalTitle());

        Intent i = new Intent(MainActivity.this, DetailActivity.class);
        i.putExtra(getString(R.string.intentextra_movie), currentMovie);
        startActivity(i);


    }

    private class FetchMoviesTask extends AsyncTask<String, String, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }
            String sorttype = params[0];
            URL movieListURL = NetworkUtils.buildURL(sorttype);

            OkHttpClient client = new OkHttpClient();
            Request req = new Request.Builder().url(movieListURL).build();

            try {
                Response response = client.newCall(req).execute();
                return MovieDetailsUtil.getMovieArrayListFromJSON(response.body().string());


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mMoviesRV.setVisibility(View.INVISIBLE);
            mErrorMsgView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            if (movies == null) {
                mErrorMsgView.setVisibility(View.VISIBLE);
                mMoviesRV.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
                mMoviesRV.setVisibility(View.VISIBLE);
                mErrorMsgView.setVisibility(View.INVISIBLE);
            }

            mMovieRVAdapter.setMoviesList(movies);

        }
    }


}
