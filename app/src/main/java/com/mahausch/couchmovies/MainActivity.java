package com.mahausch.couchmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_grid);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, R.id.movie_grid);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
