package com.mahausch.couchmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mahausch.couchmovies.data.MovieContract;
import com.mahausch.couchmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    RecyclerView mRecyclerView;
    GridLayoutManager mManager;
    MovieAdapter mAdapter;
    ProgressBar mProgress;
    SharedPreferences mSharedPref;
    String mPreference;
    static Parcelable mListState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_grid);

        mManager = new GridLayoutManager(this, numberOfColumns(), GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);

        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mPreference = mSharedPref.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        startTaskLoader();

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                startTaskLoader();
                swipe.setRefreshing(false);
            }
        });

    }

    //Get the number of columns dependent on the screen width
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthDivider = 300;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public void onClick(Movie movieData) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie", movieData);
        startActivity(intent);
    }


    private LoaderManager.LoaderCallbacks<ArrayList<Movie>> mLoaderCallbacksInternet = new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {

        @Override
        public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {

            return new AsyncTaskLoader<ArrayList<Movie>>(getBaseContext()) {

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();


                    mProgress.setVisibility(View.VISIBLE);
                    forceLoad();
                }

                @Override
                public ArrayList<Movie> loadInBackground() {

                    ArrayList<URL> movieResponseURLs = NetworkUtils.buildUrl(mPreference);

                    try {
                        ArrayList<String> jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieResponseURLs);

                        return NetworkUtils.getMovieDataFromJson(jsonMovieResponse);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
            mProgress.setVisibility(View.INVISIBLE);
            if (data != null) {
                mAdapter.setMovieData(data);
                if (mListState != null) {
                    mManager.onRestoreInstanceState(mListState);
                }
            } else {
                Toast toast = Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

        }
    };


    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacksFavorites = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            return new AsyncTaskLoader<Cursor>(getBaseContext()) {


                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    mProgress.setVisibility(View.VISIBLE);
                    forceLoad();
                }

                @Override
                public Cursor loadInBackground() {

                    Uri currentUri = MovieContract.MovieEntry.CONTENT_URI;

                    return getContentResolver().query(currentUri, null, null, null, null);
                }

            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data.getCount() != 0) {

                ArrayList<Movie> list = new ArrayList<>();
                data.moveToFirst();

                int movieIdIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                int titleIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                int dateIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE);
                int ratingIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
                int imageIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE);
                int contentIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_CONTENT);

                do {
                    int movieId = data.getInt(movieIdIndex);
                    String title = data.getString(titleIndex);
                    String date = data.getString(dateIndex);
                    double rating = data.getDouble(ratingIndex);
                    String image = data.getString(imageIndex);
                    String content = data.getString(contentIndex);

                    Movie movie = new Movie(image, title, movieId, date, content, rating);
                    list.add(movie);
                } while (data.moveToNext());

                mProgress.setVisibility(View.INVISIBLE);

                mAdapter.setMovieData(list);
                if (mListState != null) {
                    mManager.onRestoreInstanceState(mListState);
                }
            } else {
                Toast toast = Toast.makeText(MainActivity.this, R.string.no_favorites, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

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

        if (mListState != null) {
            mManager.onRestoreInstanceState(mListState);
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        if (!mPreference.equals(orderBy)) {
            mPreference = orderBy;
            startTaskLoader();
        }
    }

    /*If preferences are set on "favorites", the favorites loadermanager gets called,
        otherwise if there is an internet connection the internet loadermanager gets called
     */
    public void startTaskLoader() {

        if (mPreference.equals("favorites")) {
            getSupportLoaderManager().restartLoader(1, null, mLoaderCallbacksFavorites);
        } else {

            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                mProgress.setVisibility(View.VISIBLE);

                getSupportLoaderManager().restartLoader(1, null, mLoaderCallbacksInternet);

            } else {
                mProgress.setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mListState = mManager.onSaveInstanceState();
        outState.putParcelable("listState", mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mListState = savedInstanceState.getParcelable("listState");
    }

}
