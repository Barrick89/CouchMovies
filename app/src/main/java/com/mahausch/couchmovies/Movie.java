package com.mahausch.couchmovies;

public class Movie {

    private String mImage;
    private String mTitle;
    private String mDate;
    private String mPlot;
    private double mRating;

    public Movie (String image, String title, String date, String plot, double rating) {
        mImage = image;
        mTitle = title;
        mDate = date;
        mPlot = plot;
        mRating = rating;
    }

    public String getImage () {
        return mImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getPlot() {
        return mPlot;
    }

    public double getRating() {
        return mRating;
    }
}
