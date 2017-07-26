package com.example.android.popularmovies.utilities;


import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Class to create urls and communicate with themoviedb.org server.
 */

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    private static final String REQ_SCHEME_HTTPS = "https";
    private static final String REQ_SCHEME_HTTP = "http";

    private static final String REQ_AUTHORITY = "api.themoviedb.org";
    private static final String PATH_GETMOVIES = "/3/movie";

    private static final String PARAM_APIKEY = "api_key";


    private static final String IMGPATH_AUTHORITY = "image.tmdb.org";
    private static final String IMGPATH_SIZE = "w300";

    public static URL buildURL(String sortpath) {

        String apikey = BuildConfig.MOVIEDB_APIKEY;

        Uri uri = new Uri.Builder().scheme(REQ_SCHEME_HTTPS)
                .authority(REQ_AUTHORITY).appendEncodedPath(PATH_GETMOVIES)

                .appendPath(sortpath)
                .appendQueryParameter(PARAM_APIKEY, apikey)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            Log.i(TAG, "url - " + url.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildURLForReview(int movie_id) {
        String apikey = BuildConfig.MOVIEDB_APIKEY;

        Uri uri = new Uri.Builder().scheme(REQ_SCHEME_HTTPS)
                .authority(REQ_AUTHORITY).appendEncodedPath(PATH_GETMOVIES)

                .appendPath(String.valueOf(movie_id)).appendPath("reviews").appendQueryParameter(PARAM_APIKEY, apikey)

                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            Log.i(TAG, "url - " + url.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildURLForTrailer(int movie_id) {
        String apikey = BuildConfig.MOVIEDB_APIKEY;

        Uri uri = new Uri.Builder().scheme(REQ_SCHEME_HTTPS)
                .authority(REQ_AUTHORITY).appendEncodedPath(PATH_GETMOVIES)

                .appendPath(String.valueOf(movie_id)).appendPath("videos").appendQueryParameter(PARAM_APIKEY, apikey)

                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            Log.i(TAG, "url - " + url.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static URL buildPosterImageURL(String posterPath) {
        Uri uri = new Uri.Builder().scheme(REQ_SCHEME_HTTP).authority(IMGPATH_AUTHORITY)
                .appendEncodedPath("t/p").appendPath(IMGPATH_SIZE).appendEncodedPath(posterPath).build();

        URL url = null;

        try {
            url = new URL(uri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static Uri getYoutubeWatchUri(String key) {
        Uri watchUri = Uri.parse("https://www.youtube.com/watch")
                .buildUpon()
                .appendQueryParameter("v", key)
                .build();
        return watchUri;
    }

    public static String reqHttpForJSONData(URL reqUrl) {

        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request req = new Request.Builder().url(reqUrl).build();
        try {
            Response response = client.newCall(req).execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            return "Please try again later";
        }

    }
}

//https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=<<api_key>>&language=en-US&page=1
//https://api.themoviedb.org/3/movie/{movie_id}/reviews?api_key=<<api_key>>&language=en-US&page=1
//https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1