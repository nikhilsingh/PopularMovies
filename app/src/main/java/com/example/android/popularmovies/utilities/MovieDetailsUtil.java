package com.example.android.popularmovies.utilities;

import android.database.Cursor;
import android.util.Log;


import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class used to handle cursor data and json data .
 */

public class MovieDetailsUtil {
    private static final String TAG = "MovieDetailsUtil";


    private static final String MD_MOVIEID = "id";
    private static final String MD_ORIGTITLE = "original_title";
    private static final String MD_POSTERPATH = "poster_path";
    private static final String MD_OVERVIEW = "overview";
    private static final String MD_RATING = "vote_average";
    private static final String MD_RELEASEDATE = "release_date";

    private static final String MD_TITLE = "title";
    private static final String MD_BACKDROPPATH = "backdrop_path";


    private static final String MR_AUTHOR = "author";
    private static final String MR_CONTENT = "content";
    private static final String MR_REVIEWID = "id";


    private static final String MT_TRAILERNAME = "name";
    private static final String MT_TRAILERKEY = "key";

    public static ArrayList<Movie> getMovieArrayListFromJSON(String jsonString) throws JSONException {

        ArrayList<Movie> movieArrayList = new ArrayList<Movie>();

        JSONObject movieJSONData = new JSONObject(jsonString);

        JSONArray resultsArray = movieJSONData.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject currentResult = resultsArray.getJSONObject(i);
            Movie currentMovie = new Movie();
            String picUrl = NetworkUtils.buildPosterImageURL(currentResult.getString(MD_POSTERPATH)).toString();
            String backDropurl = NetworkUtils.buildPosterImageURL(currentResult.getString(MD_BACKDROPPATH)).toString();
            //Copy json values to movie object
            currentMovie.setOriginalTitle(currentResult.getString(MD_ORIGTITLE));
            currentMovie.setPosterUrl(picUrl);
            currentMovie.setOverview(currentResult.getString(MD_OVERVIEW));
            currentMovie.setReleaseDate(currentResult.getString(MD_RELEASEDATE));
            currentMovie.setUserRating(currentResult.getDouble(MD_RATING));
            currentMovie.setEnglishTitle(currentResult.getString(MD_TITLE));
            currentMovie.setMovieId(currentResult.getInt(MD_MOVIEID));
            currentMovie.setBackdroppath(backDropurl);
                  movieArrayList.add(currentMovie);

        }

        Log.i(TAG, "Movie List size :" + movieArrayList.size());
        return movieArrayList;
    }

    public static ArrayList<Review> getReviewArrayListFromJSON(String jsonString, int movieId) throws JSONException {
        ArrayList<Review> reviewArrayList = new ArrayList<>();

        JSONObject reviewJSONData = new JSONObject(jsonString);

        JSONArray reviewArray = reviewJSONData.getJSONArray("results");
        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject currentResult = reviewArray.getJSONObject(i);

            Review currentReview = new Review();

            currentReview.setMovieId(movieId);
            currentReview.setAuthor(currentResult.getString(MR_AUTHOR));
            currentReview.setContent(currentResult.getString(MR_CONTENT));
            currentReview.setReviewId(currentResult.getString(MR_REVIEWID));
            reviewArrayList.add(currentReview);
            Log.i(TAG, currentReview.getAuthor());
        }

        return reviewArrayList;


    }

    public static ArrayList<Trailer> getTrailerArrayListFromJSON(String jsonString, int movieId) throws JSONException {
        ArrayList<Trailer> trailerArrayList = new ArrayList<>();


        JSONObject trailersJSONData = new JSONObject(jsonString);
        JSONArray trailerArray = trailersJSONData.getJSONArray("results");
        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject currentResult = trailerArray.getJSONObject(i);
            Trailer currentTrailer = new Trailer();

            currentTrailer.setMovieId(movieId);
            currentTrailer.setName(currentResult.getString(MT_TRAILERNAME));
            currentTrailer.setTrailerKey(currentResult.getString(MT_TRAILERKEY));
            trailerArrayList.add(currentTrailer);

        }


        return trailerArrayList;
    }

    public static ArrayList<Movie> getMovieArrayListFromCursor(Cursor data) {
        ArrayList<Movie> movieArrayList = new ArrayList<Movie>();

        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                Movie currentMovie = new Movie();
                currentMovie.setOriginalTitle(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINALTITLE)));
                currentMovie.setPosterUrl(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTERURL)));
                currentMovie.setOverview(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                currentMovie.setReleaseDate(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASEDATE)));
                currentMovie.setUserRating(data.getDouble(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_USERRATING)));
                currentMovie.setEnglishTitle(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_ENGLISHTITLE)));
                currentMovie.setMovieId(data.getInt(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEID)));
                currentMovie.setBackdroppath(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROPURL)));


                currentMovie.setFav(true);

                Log.i(TAG, "From DB Movie" + currentMovie.getEnglishTitle());
                movieArrayList.add(currentMovie);

            } while (data.moveToNext());


        }
        data.close();


        return movieArrayList;
    }


}
