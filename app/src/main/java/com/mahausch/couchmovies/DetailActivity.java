package com.mahausch.couchmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mDate;
    private ImageView mImage;
    private TextView mRating;
    private TextView mPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.title_textview);
        mDate = (TextView) findViewById(R.id.date_textview);
        mImage = (ImageView) findViewById(R.id.imageview);
        mRating = (TextView) findViewById(R.id.rating_textview);
        mPlot = (TextView) findViewById(R.id.plot_textview);

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

    }
}
