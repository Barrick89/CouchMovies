package com.mahausch.couchmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieHolder>{

    private ArrayList<Movie> mMovieData;
    private final MovieAdapterOnClickHandler mOnClickHandler;

    public MovieAdapter (MovieAdapterOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }


    @Override
    public MovieAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.grid_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate (layout, parent, shouldAttachToParentImmediately);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieHolder holder, int position) {
        Movie movie = mMovieData.get(position);
        Picasso.with(holder.mImageView.getContext()).load(movie.getImage()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieData != null) {
            return mMovieData.size();
        } else {
            return 0;
        }
    }

    public void setMovieData (ArrayList<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        public void onClick (Movie movieData);
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;

        public MovieHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = mMovieData.get(position);
            mOnClickHandler.onClick(movie);
        }
    }
}
