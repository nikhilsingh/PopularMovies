package com.example.android.popularmovies.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import static com.example.android.popularmovies.utilities.MovieDetailsUtil.getTrailerArrayListFromJSON;

/**
 * Created by nikhil on 21/7/17.
 */

public class TrailerAsyncLoader extends AsyncTaskLoader<ArrayList<Trailer>> {
    private static final String TAG = "TrailerAsyncLoader";
    private int movieid;
    private ArrayList<Trailer> currentTrailerList;
    public TrailerAsyncLoader(Context context,int movieid) {
        super(context);

        this.movieid=movieid;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.i(TAG, "on Startloading");

        if(currentTrailerList!=null){
            deliverResult(currentTrailerList);
        }else{
            currentTrailerList=new ArrayList<Trailer>();
            forceLoad();
        }

    }

    @Override
    public ArrayList<Trailer> loadInBackground() {
        Log.i(TAG, "Load in background starts");

        URL movieListURL = NetworkUtils.buildURLForTrailer(movieid);
        String responseString = NetworkUtils.reqHttpForJSONData(movieListURL);
        try {
            return getTrailerArrayListFromJSON(responseString, movieid);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return null;
    }



    @Override
    public void deliverResult(ArrayList<Trailer> data) {
        currentTrailerList=data;
        super.deliverResult(currentTrailerList);
    }
}
