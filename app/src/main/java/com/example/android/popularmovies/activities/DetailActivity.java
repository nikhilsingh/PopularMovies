package com.example.android.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG="DetailActivity";

    private TextView mTitleTV,mReleaseDateTV,mOverviewTV,mRatingTV;
    private ImageView mPosterIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.i(TAG,"On Create Starting");
        mTitleTV = (TextView) findViewById(R.id.details_tv_title);
        mReleaseDateTV=(TextView) findViewById(R.id.details_tv_releasedate) ;
        mOverviewTV=(TextView) findViewById(R.id.detail_tv_overview);
        mRatingTV=(TextView) findViewById(R.id.details_tv_rating);
        mPosterIV=(ImageView) findViewById(R.id.details_iv_poster);

        Movie currentMovie =getIntent().getParcelableExtra(getString(R.string.intentextra_movie));

        mTitleTV.setText(currentMovie.getEnglishTitle());
        mReleaseDateTV.setText("Released On :"+currentMovie.getReleaseDate());
        mOverviewTV.setText(currentMovie.getOverview());
        mRatingTV.setText(currentMovie.getUserRating()+"/10");

        Glide.with(this).load(currentMovie.getPosterUrl()).into(mPosterIV);

        Log.i(TAG,"On Create Ends");
    }
}
