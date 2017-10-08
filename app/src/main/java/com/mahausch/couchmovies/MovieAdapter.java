package com.mahausch.couchmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieHolder>{

    private String [] mImageData;


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
        String image = mImageData[position];
        Picasso.with(holder.mImageView.getContext()).load(image).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (mImageData != null) {
            return mImageData.length;
        } else {
            return 0;
        }
    }

    public void setImageData (String [] imageData) {
        mImageData = imageData;
        notifyDataSetChanged();
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        public final ImageView mImageView;

        public MovieHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
