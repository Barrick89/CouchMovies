package com.mahausch.couchmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mahausch.couchmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;
    ProgressBar mProgress;
    SharedPreferences mSharedPref;
    String mPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mPreference = mSharedPref.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        mProgress = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_grid);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        startTask();

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                startTask();
                swipe.setRefreshing(false);
            }
        });

    }

    @Override
    public void onClick(Movie movieData) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("title", movieData.getTitle());
        intent.putExtra("image", movieData.getImage());
        intent.putExtra("date", movieData.getDate());
        intent.putExtra("rating", movieData.getRating());
        intent.putExtra("plot", movieData.getPlot());
        startActivity(intent);

    }

    private class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {

            URL movieResponseURL = NetworkUtils.buildUrl(strings[0]);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieResponseURL);

                return NetworkUtils.getMovieDataFromJson(jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mProgress.setVisibility(View.INVISIBLE);
            if (movies != null) {
                mAdapter.setMovieData(movies);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        if (!mPreference.equals(orderBy)) {
            mPreference = orderBy;
            startTask();
        }
    }

    public void startTask() {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mProgress.setVisibility(View.VISIBLE);
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

            String orderBy = sharedPrefs.getString(
                    getString(R.string.settings_order_by_key),
                    getString(R.string.settings_order_by_default)
            );

            new FetchMoviesTask().execute(orderBy);
        } else {
            mProgress.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
