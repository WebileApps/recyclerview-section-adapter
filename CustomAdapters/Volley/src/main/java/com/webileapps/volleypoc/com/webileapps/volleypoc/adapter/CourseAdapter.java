package com.webileapps.volleypoc.com.webileapps.volleypoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webileapps.volleypoc.R;
import com.webileapps.volleypoc.com.webileapps.volleypoc.Fragments.WebViewActivityFragment;
import com.webileapps.volleypoc.com.webileapps.volleypoc.activities.WebViewActivity;
import com.webileapps.volleypoc.com.webileapps.volleypoc.model.Course;

import java.util.ArrayList;

/**
 * Created by venkatadinesh on 23/06/15.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private ArrayList<Course> mDataset;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView titleTextView,subTitleTextView;


        public ViewHolder(View v,TextView titleTextView,TextView subTitleTextView) {
            super(v);
            view = v;
            this.titleTextView = titleTextView;
            this.subTitleTextView = subTitleTextView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CourseAdapter(ArrayList<Course> myDataset) {

        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        context = parent.getContext();
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        TextView titleTextView = (TextView)v.findViewById(R.id.titleTextView);
        TextView subTitleTextView = (TextView)v.findViewById(R.id.subTitleTextView);
        ViewHolder vh = new ViewHolder(v,titleTextView,subTitleTextView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titleTextView.setText(mDataset.get(position).title);
        holder.subTitleTextView.setText(mDataset.get(position).subtitle);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(WebViewActivityFragment.URL,mDataset.get(position).homepage);
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
