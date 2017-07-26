package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by nikhil on 11/7/17.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIE_WITHID = 101;
    private MovieDbHelper mMovieDBHelper;

    UriMatcher mUriMatcher = buildUriMatcher();

    private UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITHID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mMovieDBHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (mUriMatcher.match(uri)) {
            case CODE_MOVIES:
                cursor = mMovieDBHelper.getReadableDatabase().
                        query(MovieContract.MovieEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);

                break;
            case CODE_MOVIE_WITHID:
                    String movieId=uri.getPathSegments().get(1);
                    String mSelection=MovieContract.MovieEntry.COLUMN_MOVIEID+"=?";
                    String [] mSelectionArgs = new String[]{movieId};

                cursor = mMovieDBHelper.getReadableDatabase().
                        query(MovieContract.MovieEntry.TABLE_NAME,
                                projection,
                                mSelection,
                                mSelectionArgs,
                                null,
                                null,
                                sortOrder
                                );

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        //TODO: Uncomment following if not done. after resolver code.
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db= mMovieDBHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (mUriMatcher.match(uri)){
            case CODE_MOVIES:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                returnUri= ContentUris.withAppendedId(uri,values.getAsInteger(MovieContract.MovieEntry.COLUMN_MOVIEID));

                break;
            default:new UnsupportedOperationException("Unknown URI"+uri);
        }
        
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i("Provider","delete starts");
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int deletedRecords=0;
        switch (mUriMatcher.match(uri)){
            case CODE_MOVIE_WITHID:
                String id = uri.getPathSegments().get(1);
                deletedRecords=db.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_MOVIEID+"=?",new String[]{id});
                break;
            default:new UnsupportedOperationException("Unknown Uri"+uri);
        }
        Log.i("Provider","uri is "+uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRecords;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
