package com.td.innovate.tdiscount.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.model.Product;

import java.util.ArrayList;

/**
 * Created by mmmoussa on 2015-11-16.
 */
public class RecentItemsAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final ArrayList<Product> items;

    public RecentItemsAdapter(Context context, ArrayList<Product> items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.recent_item_list_item, parent, false);
        TextView productName = (TextView) rowView.findViewById(R.id.productName);
        TextView productDescription = (TextView) rowView.findViewById(R.id.productDescription);
        ImageView productImage = (ImageView) rowView.findViewById(R.id.productImage);


//        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
//        productName.setTypeface(boldTypeface);

        Product product = items.get(position);
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        Ion.with(productImage).load(product.getImage_url());

        return rowView;
    }
}
