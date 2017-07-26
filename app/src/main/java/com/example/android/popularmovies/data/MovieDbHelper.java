package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by nikhil on 11/7/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "MovieDBHelper";
    private static final String DATABASE_NAME = "moviesDB.db";
    private static final int VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " ( " +
                MovieEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIEID       + " INTEGER NOT NULL ," +
                MovieEntry.COLUMN_ENGLISHTITLE  + " TEXT ," +
                MovieEntry.COLUMN_ORIGINALTITLE + " TEXT ," +
                MovieEntry.COLUMN_OVERVIEW      + " TEXT ," +
                MovieEntry.COLUMN_POSTERURL     + " TEXT ," +
                MovieEntry.COLUMN_RELEASEDATE   + " TEXT ," +
                MovieEntry.COLUMN_USERRATING    + " REAL, " +
                MovieEntry.COLUMN_BACKDROPURL   + " TEXT,"+

                " UNIQUE ("+MovieEntry.COLUMN_MOVIEID+") ON CONFLICT REPLACE);";

        Log.i(TAG,CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
