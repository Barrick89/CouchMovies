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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahausch.couchmovies.utilities.NetworkUtils;
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

public class DetailActivity extends AppCompatActivity{

    private static final int TRAILER_LOADER = 100;
    private static final int REVIEW_LOADER = 200;
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

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
    RecyclerView mRecyclerView;
    ReviewAdapter mAdapter;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.reviewRecycler);
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

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ReviewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        int movieId = movie.getMovieId();
        Bundle args = new Bundle();
        args.putInt("id", movieId);
        getLoaderManager().initLoader(TRAILER_LOADER, args, mLoaderCallbacksTrailer).forceLoad();
        getLoaderManager().initLoader(REVIEW_LOADER, args, mLoaderCallbacksReview).forceLoad();

    }

    private LoaderManager.LoaderCallbacks<ArrayList<String>> mLoaderCallbacksTrailer = new LoaderManager.LoaderCallbacks<ArrayList<String>>() {
        @Override
        public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args) {

            final int movieId = args.getInt("id");

            return new android.content.AsyncTaskLoader<ArrayList<String>>(getBaseContext()) {


                @Override
                public ArrayList<String> loadInBackground() {

                    URL url = NetworkUtils.buildDetailUrl(NetworkUtils.TRAILER_URL_END, movieId);
                    String jsonData = NetworkUtils.getResponseFromDetailHttpUrl(url);

                    return NetworkUtils.getTrailerDataFromJson(jsonData);
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
    };


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

    private LoaderManager.LoaderCallbacks<ArrayList<Review>> mLoaderCallbacksReview = new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {

        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {

            final int movieId = args.getInt("id");

            return new android.content.AsyncTaskLoader<ArrayList<Review>>(getBaseContext()) {


                @Override
                public ArrayList<Review> loadInBackground() {

                    URL url = NetworkUtils.buildDetailUrl(NetworkUtils.REVIEW_URL_END, movieId);
                    String jsonData = NetworkUtils.getResponseFromDetailHttpUrl(url);

                    return NetworkUtils.getReviewDataFromJson(jsonData);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
            if (data != null){
                mAdapter.setReviewData(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {

        }
    };
}
