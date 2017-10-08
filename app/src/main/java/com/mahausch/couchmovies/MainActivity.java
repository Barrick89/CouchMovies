package com.mahausch.couchmovies;

import android.net.Network;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.mahausch.couchmovies.utilities.NetworkUtils;

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

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, R.id.movie_grid);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(Movie movieData) {

    }

    public class FetchMoviesTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(Integer... integers) {

            URL movieResponseURL = NetworkUtils.buildUrl(integers[0]);

            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieResponseURL);

            return null;
        }
    }
}
