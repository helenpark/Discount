package com.td.innovate.tdiscount.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.model.Offer;

import java.util.ArrayList;

/**
 * Created by joshuahill on 2015-11-10.
 */
public class StoreAdapter extends ArrayAdapter<Offer> {

    ArrayList<Offer> offers;

    public StoreAdapter(Context context, ArrayList<Offer> offers) {
        super(context, 0, offers);
        this.offers = offers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Offer offer = getItem(position);
        Log.d("StoreAdapter", "Offers " + position + " :" + offer.toString());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.store_list_view, parent, false);
        }

        // Lookup view for data population
        TextView tvSName = (TextView) convertView.findViewById(R.id.listview_offers_storename);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.listview_offers_store_price);
        //ImageView storeImage = (ImageView) convertView.findViewById(R.id.listview_offers_image);

        // Populate the data into the template view using the data object
        tvSName.setText(offer.getShop_name());
        tvPrice.setText("$ "+offer.getPrice() + " " + offer.getCurrency());


        // Return the completed view to render on screen
        return convertView;
    }
}
