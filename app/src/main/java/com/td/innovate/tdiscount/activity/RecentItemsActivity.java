package com.td.innovate.tdiscount.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.adapter.RecentItemsAdapter;
import com.td.innovate.tdiscount.model.Product;
import com.td.innovate.tdiscount.model.Profile;

import java.util.ArrayList;

public class RecentItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recent_items);


        Profile profile = new Profile(this);
        final ArrayList<Product> pastResults = profile.getPastResults();

        ListView recentItemsListview = (ListView) findViewById(R.id.recentItemsListview);
        RecentItemsAdapter adapter = new RecentItemsAdapter(this, pastResults);
        recentItemsListview.setAdapter(adapter);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Recently Viewed Items");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recentItemsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("position", String.valueOf(position));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("keywords", "null");

                Log.d("RECENT ITEMS","BARCODE: "+pastResults.get(position).getBarcode());

                intent.putExtra("barcode", pastResults.get(position).getBarcode());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}

