package com.mahausch.couchmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Movie implements Parcelable {

    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342/";

    private String mImage;
    private String mTitle;
    private int mMovieId;
    private String mDate;
    private String mPlot;
    private double mRating;

    public Movie(String image, String title, int movieId, String date, String plot, double rating) {
        mImage = image;
        mTitle = title;
        mMovieId = movieId;
        mDate = date;
        mPlot = plot;
        mRating = rating;
    }

    public Movie (Parcel in){
        readFromParcel(in);
    }

    public String getImage() {
        String image = IMAGE_BASE_URL + mImage;
        return image;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getMovieId(){
        return mMovieId;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    public String getPlot() {
        return mPlot;
    }

    public double getRating() {
        return mRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mImage);
        parcel.writeString(mTitle);
        parcel.writeString(mDate);
        parcel.writeString(mPlot);
        parcel.writeDouble(mRating);
    }

    public void readFromParcel (Parcel in){
        mImage = in.readString();
        mTitle = in.readString();
        mDate = in.readString();
        mPlot = in.readString();
        mRating = in.readDouble();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    }; {

    }
}
