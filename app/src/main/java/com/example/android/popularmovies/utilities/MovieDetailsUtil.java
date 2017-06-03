package com.example.android.popularmovies.utilities;

import android.util.Log;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class used to convert movies json data to movie object .
 */

public class MovieDetailsUtil {
    private static final String TAG = "MovieDetailsUtil";

    private static final String MD_ORIGTITLE = "original_title";
    private static final String MD_POSTERPATH = "poster_path";
    private static final String MD_OVERVIEW = "overview";
    private static final String MD_RATING = "vote_average";
    private static final String MD_RELEASEDATE = "release_date";

    private static final String MD_TITLE = "title";

    public static ArrayList<Movie> getMovieArrayListFromJSON(String jsonString) throws JSONException {

        ArrayList<Movie> movieArrayList = new ArrayList<Movie>();

        JSONObject movieJSONData = new JSONObject(jsonString);

        JSONArray resultsArray = movieJSONData.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject currentResult = resultsArray.getJSONObject(i);

            Movie currentMovie = new Movie();

            String picUrl = NetworkUtils.buildPosterImageURL(currentResult.getString(MD_POSTERPATH)).toString();

            //Copy json values to movie object
            currentMovie.setOriginalTitle(currentResult.getString(MD_ORIGTITLE));
            currentMovie.setPosterUrl(picUrl);
            currentMovie.setOverview(currentResult.getString(MD_OVERVIEW));
            currentMovie.setReleaseDate(currentResult.getString(MD_RELEASEDATE));
            currentMovie.setUserRating(currentResult.getDouble(MD_RATING));
            currentMovie.setEnglishTitle(currentResult.getString(MD_TITLE));

            movieArrayList.add(currentMovie);

        }

        Log.i(TAG, "Movie List size :" + movieArrayList.size());
        return movieArrayList;
    }

}
