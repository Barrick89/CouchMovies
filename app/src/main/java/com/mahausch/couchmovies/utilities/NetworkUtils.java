package com.mahausch.couchmovies.utilities;


import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import com.mahausch.couchmovies.Movie;

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

    public static String getResponseFromHttpUrl (URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        if (urlConnection.getResponseCode() == 200) {
            try {
                InputStream input = urlConnection.getInputStream();

                Scanner scanner = new Scanner(input);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }else {
            return null;
        }
    }

    public static ArrayList getMovieDataFromJson (String jsonData) throws JSONException {

        ArrayList movieList = null;

        if (jsonData != null) {
            JSONObject jsonObject = new JSONObject(jsonData);

            JSONArray results = jsonObject.getJSONArray("results");

            String image;
            String title;
            String date;
            String plot;
            double rating;

            for (int i = 0; i < results.length(); i++ ){
                JSONObject movieObject = results.getJSONObject(i);
                image = movieObject.getString("poster_path");
                title = movieObject.getString("title");
                date = movieObject.getString("release_date");
                plot = movieObject.getString("overview");
                rating = movieObject.getDouble("vote_average");

                Movie movie = new Movie (image, title, date, plot, rating);

                movieList.add(movie);
            }
        }
        return movieList;
    }
}
