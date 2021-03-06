package com.mahausch.couchmovies.utilities;

import android.net.Uri;

import com.mahausch.couchmovies.Movie;
import com.mahausch.couchmovies.Review;

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

public class NetworkUtils {

    private static final String POPULAR_MOVIES_URL = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String TOP_RATED_MOVIES_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static final String PAGE_URL_APPENDIX = "&page=";

    private static final String URL_START = "http://api.themoviedb.org/3/movie/";
    public static final String TRAILER_URL_END = "/videos?api_key=";
    public static final String REVIEW_URL_END = "/reviews?api_key=";

    private static final String API_KEY = "";


    public static ArrayList<URL> buildUrl(String searchCategory) {

        Uri uri;
        ArrayList<URL> urlList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {

            if (searchCategory.equals("popular")) {
                uri = Uri.parse(POPULAR_MOVIES_URL + API_KEY + PAGE_URL_APPENDIX + i);
            } else {
                uri = Uri.parse(TOP_RATED_MOVIES_URL + API_KEY + PAGE_URL_APPENDIX + i);
            }

            URL url = null;
            try {
                url = new URL(uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            urlList.add(url);
        }
        return urlList;

    }

    public static ArrayList<String> getResponseFromHttpUrl(ArrayList<URL> url) throws IOException {

        ArrayList<String> jsonList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            HttpURLConnection urlConnection = (HttpURLConnection) url.get(i).openConnection();

            try {
                InputStream input = urlConnection.getInputStream();

                Scanner scanner = new Scanner(input);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    jsonList.add(scanner.next());
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }
        return jsonList;
    }

    public static ArrayList getMovieDataFromJson(ArrayList<String> jsonData) throws JSONException {

        ArrayList movieList = new ArrayList();

        for (int i = 0; i < 5; i++) {

            if (jsonData != null) {
                JSONObject jsonObject = new JSONObject(jsonData.get(i));

                JSONArray results = jsonObject.getJSONArray("results");

                String image;
                String title;
                int movieId;
                String date;
                String plot;
                double rating;

                for (int x = 0; x < results.length(); x++) {
                    JSONObject movieObject = results.getJSONObject(x);
                    image = movieObject.getString("poster_path");
                    title = movieObject.getString("title");
                    movieId = movieObject.getInt("id");
                    date = movieObject.getString("release_date");
                    plot = movieObject.getString("overview");
                    rating = movieObject.getDouble("vote_average");

                    Movie movie = new Movie(image, title, movieId, date, plot, rating);

                    movieList.add(movie);
                }
            }
        }
        return movieList;
    }

    public static URL buildDetailUrl(String searchCategory, int movieId) {

        Uri uri;

        uri = Uri.parse(URL_START + movieId + searchCategory + API_KEY);

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromDetailHttpUrl(URL url) {
        HttpURLConnection urlConnection = null;
        String jsonData = "";

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream input = urlConnection.getInputStream();

            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                jsonData = scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return jsonData;
    }

    public static ArrayList<String> getTrailerDataFromJson(String jsonData) {

        ArrayList<String> trailerList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.getJSONArray("results");

            String trailer;

            for (int x = 0; x < results.length(); x++) {
                JSONObject movieObject = results.getJSONObject(x);
                trailer = movieObject.getString("key");

                trailerList.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerList;
    }

    public static ArrayList<Review> getReviewDataFromJson(String jsonData) {

        ArrayList<Review> reviewList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.getJSONArray("results");

            String author, content;

            for (int x = 0; x < results.length(); x++) {
                JSONObject movieObject = results.getJSONObject(x);
                author = movieObject.getString("author");
                content = movieObject.getString("content");

                reviewList.add(new Review(author, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }
}
