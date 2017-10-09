package com.mahausch.couchmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

        mTitle.setText(intent.getStringExtra("title"));
        mDate.setText(intent.getStringExtra("date"));
        Picasso.with(mImage.getContext()).load(intent.getStringExtra("image")).into(mImage);
        Double rating = intent.getDoubleExtra("rating", 0.0);
        mRating.setText(rating.toString());
        mPlot.setText(intent.getStringExtra("plot"));

    }
}
