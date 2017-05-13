package com.td.innovate.tdiscount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by helenpark on 2015-11-16.
 */
public abstract class CustomArrayAdapter<T> extends ArrayAdapter<T> {

    // The layout passed in the constructor
    private int resource;
    // Constructor: make sure you call super(context resource, values) in your constructor
    public CustomArrayAdapter(Context context, int resource, ArrayList<T> values) {
        super(context, resource, values);
        this.resource = resource;
    }
    // getView recycles views, uses getViewHolder and fillViewHolder to display data in view
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // Conditionally inflate view
        Object viewHolder;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(resource, parent, false);
            // Setup View Holder
            viewHolder = getViewHolder(rowView);
            // Set tag for recycling
            rowView.setTag(viewHolder);
        } else {
            viewHolder = rowView.getTag();
        }
        // Get data
        T data = getItem(position);
        // Populate View Holder
        fillViewHolder(viewHolder, data);
        // Return view (required by super class)
        return rowView;
    }
    /**
     * Get an object which contains variables that reference an item's views
     *
     * @param rowView The inflated view. Find views by doing rowView.findViewById(...)
     * @return The object with view references
     */
    public abstract Object getViewHolder(View rowView);
    /**
     * Fill a Viewholder with data
     *
     * @param viewHolder Object configured in getViewHolder
     * @param data the data to add to the display
     */
    public abstract void fillViewHolder(Object viewHolder, T data);
}

