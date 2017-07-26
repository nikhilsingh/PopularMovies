package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String originalTitle, posterUrl, overview, releaseDate, englishTitle, backdroppath;
    private double userRating;
    private int movieId;
    private boolean isFav;

    public Movie() {
    }

    public String getBackdroppath() {
        return backdroppath;
    }

    public void setBackdroppath(String backdroppath) {
        this.backdroppath = backdroppath;
    }

    protected Movie(Parcel in) {
        originalTitle = in.readString();
        posterUrl = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        englishTitle = in.readString();
        backdroppath = in.readString();


        userRating = in.readDouble();
        movieId = in.readInt();
        isFav = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterUrl);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(englishTitle);
        dest.writeString(backdroppath);
        dest.writeDouble(userRating);
        dest.writeInt(movieId);


        dest.writeByte((byte) (isFav ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public boolean isFav() {


        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    /*
        public Movie(Parcel in) {
            originalTitle = in.readString();
            posterUrl = in.readString();
            overview = in.readString();
            releaseDate = in.readString();
            englishTitle = in.readString();
            userRating = in.readDouble();
            movieId=in.readInt();
            isFav=in.re
        }

        public static final Creator<Movie> CREATOR = new Creator<Movie>() {
            @Override
            public Movie createFromParcel(Parcel in) {
                return new Movie(in);
            }

            @Override
            public Movie[] newArray(int size) {
                return new Movie[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(originalTitle);
            dest.writeString(posterUrl);
            dest.writeString(overview);
            dest.writeString(releaseDate);
            dest.writeString(englishTitle);
            dest.writeDouble(userRating);
            dest.writeInt(movieId);
        }

    */
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getEnglishTitle() {
        return englishTitle;
    }

    public void setEnglishTitle(String englishTitle) {
        this.englishTitle = englishTitle;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }


}