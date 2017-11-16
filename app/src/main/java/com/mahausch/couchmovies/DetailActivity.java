package com.mahausch.couchmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>> {

    private static final String TRAILER_URL_START = "http://api.themoviedb.org/3/movie/";
    private static final String TRAILER_URL_END = "/videos?api_key=";
    private static final String API_KEY = "";

    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final int TRAILER_LOADER = 100;

    private TextView mTitle;
    private TextView mDate;
    private ImageView mImage;
    private TextView mRating;
    private TextView mPlot;
    private TextView mTrailer1;
    private TextView mTrailer2;
    private TextView mTrailer3;
    private TrailerListener mListener;

    public ArrayList<String> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.title_textview);
        mDate = (TextView) findViewById(R.id.date_textview);
        mImage = (ImageView) findViewById(R.id.imageview);
        mRating = (TextView) findViewById(R.id.rating_textview);
        mPlot = (TextView) findViewById(R.id.plot_textview);
        mTrailer1 = (TextView) findViewById(R.id.trailer_1);
        mTrailer2 = (TextView) findViewById(R.id.trailer_2);
        mTrailer3 = (TextView) findViewById(R.id.trailer_3);
        mListener = new TrailerListener();

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        mTitle.setText(movie.getTitle());
        mDate.setText(movie.getDate());
        Picasso.with(mImage.getContext()).load(movie.getImage()).into(mImage);
        Double rating = movie.getRating();
        mRating.setText(rating.toString());
        mPlot.setText(movie.getPlot());

        if (rating < 4.0) {
            mRating.setBackgroundResource(R.color.bad);
        } else if (rating < 7.0) {
            mRating.setBackgroundResource(R.color.mediocre);
        } else {
            mRating.setBackgroundResource(R.color.good);
        }

        int movieId = movie.getMovieId();
        Bundle args = new Bundle();
        args.putInt("id", movieId);
        getLoaderManager().initLoader(TRAILER_LOADER, args, this).forceLoad();

    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args) {

        final int movieId = args.getInt("id");

        return new android.content.AsyncTaskLoader<ArrayList<String>>(this) {


            @Override
            public ArrayList<String> loadInBackground() {
                Uri uri;

                uri = Uri.parse(TRAILER_URL_START + movieId + TRAILER_URL_END + API_KEY);

                URL url = null;
                try {
                    url = new URL(uri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

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
                } catch (JSONException e){
                    e.printStackTrace();
                }
                return trailerList;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data) {
        mList = data;
        int size = mList.size();

        if(size == 1) {
            mTrailer1.setOnClickListener(mListener);
            mTrailer1.setTextColor(Color.BLACK);
        } else if (size == 2) {
            mTrailer1.setTextColor(Color.BLACK);
            mTrailer1.setOnClickListener(mListener);
            mTrailer2.setTextColor(Color.BLACK);
            mTrailer2.setOnClickListener(mListener);
        } else if (size >= 3) {
            mTrailer1.setTextColor(Color.BLACK);
            mTrailer1.setOnClickListener(mListener);
            mTrailer2.setTextColor(Color.BLACK);
            mTrailer2.setOnClickListener(mListener);
            mTrailer3.setTextColor(Color.BLACK);
            mTrailer3.setOnClickListener(mListener);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

    }

    public class TrailerListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id = v.getId();
            String trailerId = "";

            if (id == R.id.trailer_1){
                trailerId = mList.get(0);
            } else if (id == R.id.trailer_2){
                trailerId = mList.get(1);
            }else if (id == R.id.trailer_3) {
                trailerId = mList.get(2);
            } else {
                return;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(YOUTUBE_URL+trailerId));
            startActivity(intent);
        }
    }
}
