package com.mahausch.couchmovies.utilities;


import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    private static final String POPULAR_MOVIES_URL = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String TOP_RATED_MOVIES_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=";

    private static final String API_KEY = "";


    public static URL buildUrl(Integer searchCategory) {

        Uri uri;

        if (searchCategory == 1) {
            uri = Uri.parse(POPULAR_MOVIES_URL + API_KEY);
        } else {
            uri = Uri.parse(TOP_RATED_MOVIES_URL + API_KEY);
        }

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }
}
