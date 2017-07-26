package com.example.android.popularmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by nikhil on 11/7/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().
                appendPath(PATH_MOVIE).
                build();

        public static final String TABLE_NAME = "movies";


        public static final String COLUMN_MOVIEID = "movieid";
        public static final String COLUMN_ORIGINALTITLE = "originaltitle";
        public static final String COLUMN_ENGLISHTITLE = "englishtitle";
        public static final String COLUMN_RELEASEDATE = "releasedate";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTERURL = "posterurl";
        public static final String COLUMN_USERRATING = "userrating";
        public static final String COLUMN_BACKDROPURL="backdropurl";

        public static Uri getUriForMovieId(int movieId){
            Log.i("Contract","Movie id "+movieId);
            return ContentUris.withAppendedId(MovieEntry.CONTENT_URI,movieId);
        }

    }


}
