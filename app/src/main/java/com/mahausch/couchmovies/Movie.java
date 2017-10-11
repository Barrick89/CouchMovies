package com.mahausch.couchmovies;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.format;

public class Movie {

    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private String mImage;
    private String mTitle;
    private String mDate;
    private String mPlot;
    private double mRating;

    public Movie (String image, String title, String date, String plot, double rating) {
        mImage = IMAGE_BASE_URL + image;
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
}
