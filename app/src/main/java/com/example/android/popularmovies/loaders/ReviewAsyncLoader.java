package com.example.android.popularmovies.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.android.popularmovies.utilities.MovieDetailsUtil.getReviewArrayListFromJSON;

/**
 * Created by nikhil on 21/7/17.
 */

public class ReviewAsyncLoader extends AsyncTaskLoader<ArrayList<Review>> {
    private static final String TAG = "ReviewAsyncLoader";
    private int movieid;
    ArrayList<Review> currentReviewList = null;

    public ReviewAsyncLoader(Context context, int movieid) {
        super(context);
        this.movieid = movieid;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        Log.i(TAG, "on Startloading");
        if (currentReviewList != null) {
            Log.i(TAG, "revielist is not null.. delivering it ");
            deliverResult(currentReviewList);
        } else {
            currentReviewList = new ArrayList<Review>();
            forceLoad();
        }
    }


    @Override
    public ArrayList<Review> loadInBackground() {
        Log.i(TAG, "Loiad in background starts");

        URL movieListURL = NetworkUtils.buildURLForReview(movieid);
        String responseString = NetworkUtils.reqHttpForJSONData(movieListURL);
        try {
            return getReviewArrayListFromJSON(responseString, movieid);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return null;

    }

    @Override
    public void deliverResult(ArrayList<Review> data) {
        if (data != null) {
            currentReviewList = data;
        }
        super.deliverResult(data);
    }
}

