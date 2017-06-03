package com.example.android.popularmovies.utilities;


import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Class to create urls and communicate with themoviedb.org servers.
 */

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    private static final String REQ_SCHEME_HTTPS = "https";
    private static final String REQ_SCHEME_HTTP = "http";

    private static final String REQ_AUTHORITY = "api.themoviedb.org";
    private static final String PATH_GETMOVIES = "/3/movie";

    private static final String PARAM_APIKEY = "api_key";


    private static final String IMGPATH_AUTHORITY = "image.tmdb.org";
    private static final String IMGPATH_SIZE = "w185";

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


}
