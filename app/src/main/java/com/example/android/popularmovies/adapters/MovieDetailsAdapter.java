package com.example.android.popularmovies.adapters;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.bumptech.glide.Glide;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.NetworkUtils;
import java.util.ArrayList;

/**
 * Adapter to handle DetailActivity Recyclerview
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MovieDetailsAdapter";
    private final int MOVIE = 0, REVIEW = 1, TRAILER = 2, HEADER = 3;
    private ArrayList<Object> mObject;
    private DetailAdapterOnClickHandler mClickHandler;

    public MovieDetailsAdapter(DetailAdapterOnClickHandler handler, ArrayList<Object> mObject) {
        this.mObject = mObject;
        this.mClickHandler = handler;
    }

    public interface DetailAdapterOnClickHandler {
        void onFavChecked(Movie movie);

        void onFavUnchecked(Movie movie);


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MOVIE:
                viewHolder = new ViewHolderMovie(inflater.inflate(R.layout.movie_item, parent, false));
                break;
            case REVIEW:
                viewHolder = new ViewHolderReview(inflater.inflate(R.layout.review_item, parent, false));
                break;
            case TRAILER:
                viewHolder = new ViewHolderTrailer(inflater.inflate(R.layout.trailers_item, parent, false));
                break;
            case HEADER:
                viewHolder = new ViewHolderHeader(inflater.inflate(R.layout.header_item, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case MOVIE:
                setupMovieViewHolder((ViewHolderMovie) holder, position);
                break;
            case TRAILER:
                setupTrailerViewHolder((ViewHolderTrailer) holder, position);
                break;
            case REVIEW:
                setupReviewViewHolder((ViewHolderReview) holder, position);
                break;
            case HEADER:
                setupHeaderViewHolder((ViewHolderHeader) holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mObject.get(position) instanceof Movie) {
            return MOVIE;
        } else if (mObject.get(position) instanceof Review) {
            return REVIEW;
        } else if (mObject.get(position) instanceof Trailer) {
            return TRAILER;
        } else if (mObject.get(position) instanceof String) {
            return HEADER;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mObject.size();
    }

    public class ViewHolderMovie extends RecyclerView.ViewHolder {
        ImageView mPosterIV;
        ToggleButton mFavButton;
        TextView mTitleTV, mReleaseDateTV, mRatingTV, mOverviewTV;

        public ViewHolderMovie(final View itemView) {
            super(itemView);
            mPosterIV = (ImageView) itemView.findViewById(R.id.iv_details_poster);
            mFavButton = (ToggleButton) itemView.findViewById(R.id.iv_details_fav);
            mTitleTV = (TextView) itemView.findViewById(R.id.tv_details_title);
            mReleaseDateTV = (TextView) itemView.findViewById(R.id.tv_details_releasedate);
            mRatingTV = (TextView) itemView.findViewById(R.id.tv_details_rating);
            mOverviewTV = (TextView) itemView.findViewById(R.id.tv_details_overview);
        }

        public TextView getOverviewTV() {
            return mOverviewTV;
        }

        public ImageView getPosterIV() {
            return mPosterIV;
        }

        public ToggleButton getFavButton() {
            return mFavButton;
        }

        public TextView getTitleTV() {
            return mTitleTV;
        }

        public TextView getReleaseDateTV() {
            return mReleaseDateTV;
        }

        public TextView getRatingTV() {
            return mRatingTV;
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView mHeaderTV;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            mHeaderTV = (TextView) itemView.findViewById(R.id.tv_header);
        }

        public TextView getmHeaderTV() {
            return mHeaderTV;
        }
    }

    public class ViewHolderReview extends RecyclerView.ViewHolder {

        TextView mAuthorTV, mContentTV;

        public ViewHolderReview(View itemView) {
            super(itemView);
            mAuthorTV = (TextView) itemView.findViewById(R.id.tv_reviewauthor);
            mContentTV = (TextView) itemView.findViewById(R.id.tv_reviewcontent);
        }

        public TextView getAuthorTV() {
            return mAuthorTV;
        }

        public TextView getContentTV() {
            return mContentTV;
        }
    }


    public class ViewHolderTrailer extends RecyclerView.ViewHolder {
        TextView mTrailerNameTV;
        Button mButton;

        public ViewHolderTrailer(View itemView) {
            super(itemView);
            mTrailerNameTV = (TextView) itemView.findViewById(R.id.tv_trailerDisplayName);
            mButton = (Button) itemView.findViewById(R.id.btn_playtrailer);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "on click play trailer");
                    Intent a = new Intent(Intent.ACTION_VIEW, NetworkUtils.getYoutubeWatchUri(((Trailer) mObject.get(getAdapterPosition())).getTrailerKey()));
                    v.getContext().startActivity(a);
                }
            });

        }

        public TextView getTrailerNameTV() {
            return mTrailerNameTV;
        }


    }


    private void setupMovieViewHolder(final ViewHolderMovie movieHolder, int position) {
        Log.i(TAG, "Setting Movie Details in Recycler");
        Movie currentMovie = (Movie) mObject.get(position);
        if (currentMovie != null) {

            Glide.with(movieHolder.itemView.getContext())
                    .load(currentMovie.getBackdroppath())
                    .placeholder(R.drawable.ic_movie_filter_black_200dp)
                    .into(movieHolder.getPosterIV());


            movieHolder.getFavButton().setChecked(currentMovie.isFav());
            final ToggleButton currentFav = movieHolder.getFavButton();
            if (currentMovie.isFav()) {
                currentFav.setBackgroundDrawable(ContextCompat.getDrawable(currentFav.getContext(), R.drawable.ic_favorite_red_24dp));
            }

            movieHolder.getTitleTV().setText(currentMovie.getEnglishTitle());
            movieHolder.getRatingTV().setText(currentMovie.getUserRating() + "/10");
            movieHolder.getReleaseDateTV().setText(currentMovie.getReleaseDate());
            movieHolder.getOverviewTV().setText(currentMovie.getOverview());

            movieHolder.getFavButton().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.i(TAG, "onCheckedChanged Called" + isChecked);
                    Movie movieItem = null;
                    if (mObject.get(movieHolder.getAdapterPosition()) instanceof Movie) {
                        movieItem = (Movie) mObject.get(movieHolder.getAdapterPosition());
                    }
                    if (movieItem != null) {

                        if (isChecked) {
                            currentFav.setBackgroundDrawable(ContextCompat.getDrawable(currentFav.getContext(), R.drawable.ic_favorite_red_24dp));
                            mClickHandler.onFavChecked(movieItem);

                        } else {
                            currentFav.setBackgroundDrawable(ContextCompat.getDrawable(currentFav.getContext(), R.drawable.ic_favorite_grey_24dp));
                            mClickHandler.onFavUnchecked(movieItem);
                        }
                    }
                }
            });
        }

    }

    private void setupReviewViewHolder(ViewHolderReview reviewHolder, int position) {
        Log.i(TAG, "Setting Review Details in Recycler");
        Review currentReview = (Review) mObject.get(position);
        if (currentReview != null) {
            reviewHolder.getAuthorTV().setText(currentReview.getAuthor());
            reviewHolder.getContentTV().setText(currentReview.getContent());
        }
    }

    private void setupTrailerViewHolder(ViewHolderTrailer trailerHolder, int position) {
        Log.i(TAG, "Setting Trailer Details in Recycler");
        Trailer currentTrailer = (Trailer) mObject.get(position);
        if (currentTrailer != null) {
            trailerHolder.getTrailerNameTV().setText(currentTrailer.getName());
        }
    }

    private void setupHeaderViewHolder(ViewHolderHeader headerHolder, int position) {

        String headerString = (String) mObject.get(position);
        if (headerString != null) {
            headerHolder.getmHeaderTV().setText(headerString);
        }
    }

}
