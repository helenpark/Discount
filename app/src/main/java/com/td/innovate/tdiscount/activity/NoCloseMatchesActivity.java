package com.td.innovate.tdiscount.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.td.innovate.tdiscount.R;

public class NoCloseMatchesActivity extends AppCompatActivity {


    TextView tryAgainTV;
    TextView cancelTV;

    String keywords;
    String barcode;

    Context context;

    boolean retryWithBarcode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_close_matches_screen);
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("SCAN BARCODE");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.keywords = extras.getString("keywords");
            this.barcode = extras.getString("barcode");
        }

        if(keywords.equals("null")){
            retryWithBarcode = true;
        }else{
            retryWithBarcode = false;
        }



        tryAgainTV = (TextView) findViewById(R.id.try_again_tv);
        tryAgainTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(retryWithBarcode){
                    Intent intent = new Intent(context, BarcodeCaptureActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(context, ImageCaptureActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });


        cancelTV = (TextView) findViewById(R.id.cancel_tv);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_no_close_matches_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
