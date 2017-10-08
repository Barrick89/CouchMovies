package com.mahausch.couchmovies;

public class Movie {

    private String mImage;
    private String mTitle;
    private String mDate;
    private String mPlot;
    private int mRating;

    public Movie (String image, String title, String date, String plot, int rating) {
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

    public int getRating() {
        return mRating;
    }
}
