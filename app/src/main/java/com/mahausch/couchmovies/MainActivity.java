package com.mahausch.couchmovies;

import android.net.Network;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.mahausch.couchmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_grid);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        new FetchMoviesTask().execute(1);
    }

    @Override
    public void onClick(Movie movieData) {

    }

    public class FetchMoviesTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(Integer... integers) {

            URL movieResponseURL = NetworkUtils.buildUrl(integers[0]);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieResponseURL);

                ArrayList<Movie> movieList = NetworkUtils.getMovieDataFromJson(jsonMovieResponse);

                return movieList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            if (movies != null) {
                mAdapter.setMovieData(movies);
            }
        }
    }
}
