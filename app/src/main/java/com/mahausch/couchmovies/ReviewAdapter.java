package com.mahausch.couchmovies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private ArrayList<Review> mReviewData;

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.review_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layout, parent, shouldAttachToParentImmediately);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = mReviewData.get(position);
        holder.mAuthor.setText(review.getAuthor());
        holder.mContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviewData != null) {
            return mReviewData.size();
        } else {
            return 0;
        }
    }

    public void setReviewData(ArrayList<Review> reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }


    class ReviewHolder extends RecyclerView.ViewHolder{

        final TextView mAuthor;
        final TextView mContent;

        public ReviewHolder(View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.author);
            mContent =  itemView.findViewById(R.id.content);
        }


    }


}
