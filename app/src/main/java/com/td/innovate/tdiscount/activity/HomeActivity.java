package com.td.innovate.tdiscount.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.adapter.RecentItemsAdapter;
import com.td.innovate.tdiscount.model.CreditCard;
import com.td.innovate.tdiscount.model.Product;
import com.td.innovate.tdiscount.model.Profile;
import com.td.innovate.tdiscount.model.Transaction;
import com.td.innovate.tdiscount.service.AIService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class HomeActivity extends AppCompatActivity {


    private Context context;
    private SharedPreferences prefs;
    private static final String ACTION_AI = "com.td.innovate.tdiscount.Service.action.AI";

    private ImageButton scanBarcodeTV;
    private ImageButton takePictureTV;

    private View sheetView;

    private boolean isReady = false;

    private ArrayList<Product> pastResults;
    private ListView recentItemsListview;
    private RecentItemsAdapter adapter;

    private SlidingUpPanelLayout slidingLayout;

    private ImageView circle1;
    private ImageView circle2;

    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scanBarcodeTV = (ImageButton) findViewById(R.id.home_scan_barcode_tv);
        scanBarcodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeCapture(null);
            }
        });

        takePictureTV = (ImageButton) findViewById(R.id.home_take_picture_tv);
        takePictureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCapture(null);
            }
        });


        context = this;
        prefs = this.getSharedPreferences("com.td.innovate.tdiscount", Context.MODE_PRIVATE);
        loadDefaultData();
        if (!prefs.getBoolean("defaultDataSet", false)) {
            loadDefaultData();
        }

        startActionAI(this);
        Log.d("AI SERVICE STARTS NOW!", "--------------------");

        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        recentItemsListview = (ListView) findViewById(R.id.recentItemsListview);

        Profile profile = new Profile(this);
        pastResults = profile.getPastResults();

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        final ScaleAnimation growSmallAnim = new ScaleAnimation(1.0f, 1.09f, 1.0f, 1.09f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5F);
        final ScaleAnimation shrinkSmallAnim = new ScaleAnimation(1.09f, 1.0f, 1.09f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5F);

        final ScaleAnimation growBigAnim = new ScaleAnimation(1.0f, 1.15f, 1.0f, 1.15f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5F);
        final ScaleAnimation shrinkBigAnim = new ScaleAnimation(1.15f, 1.0f, 1.15f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5F);


        circle1 = (ImageView) findViewById(R.id.circle1);
        circle2 = (ImageView) findViewById(R.id.circle2);

        growSmallAnim.setDuration(1500);
        shrinkSmallAnim.setDuration(1500);

        growBigAnim.setDuration(1500);
        shrinkBigAnim.setDuration(1500);

        circle1.setAnimation(growBigAnim);
        circle2.setAnimation(growSmallAnim);
        growBigAnim.start();
        growSmallAnim.start();


        growBigAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circle1.setAnimation(shrinkBigAnim);
                circle2.setAnimation(shrinkSmallAnim);
                shrinkBigAnim.start();
                shrinkSmallAnim.start();

            }
        });
        shrinkBigAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circle1.setAnimation(growBigAnim);
                circle2.setAnimation(growSmallAnim);
                growBigAnim.start();
                growSmallAnim.start();

            }
        });
    }

    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void updateRecent(){

        Profile profile = new Profile(this);
        pastResults = profile.getPastResults();

        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);


        recentItemsListview = (ListView) findViewById(R.id.recentItemsListview);
        slidingLayout.setScrollableView(recentItemsListview);
        adapter = new RecentItemsAdapter(this, pastResults);
        recentItemsListview.setAdapter(adapter);


        slidingLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View view) {
                isReady = !isReady;
            }

            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        recentItemsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("position", String.valueOf(position));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("keywords", "null");

                Log.d("RECENT ITEMS", "BARCODE: " + pastResults.get(position).getBarcode());

                intent.putExtra("barcode", pastResults.get(position).getBarcode());
                startActivity(intent);
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Profile profile = new Profile(this);
        pastResults = profile.getPastResults();

        final DisplayMetrics dm = context.getResources().getDisplayMetrics();

        if (pastResults.size() > 0){
            updateRecent();
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) scanBarcodeTV.getLayoutParams();
//            params.topMargin =  convertDpToPx(190,dm);
//            ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) circle1.getLayoutParams();
//            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) circle1.getLayoutParams();
//            params1.topMargin = convertDpToPx(165,dm);
//            params2.topMargin = convertDpToPx(172,dm);
        } else {
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) scanBarcodeTV.getLayoutParams();
//            params.topMargin =  convertDpToPx(210,dm);
//            ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) circle1.getLayoutParams();
//            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) circle1.getLayoutParams();
//            params1.topMargin = convertDpToPx(195,dm);
//            params2.topMargin = convertDpToPx(202,dm);
        }

        circle1.requestLayout();
        circle2.requestLayout();
        scanBarcodeTV.requestLayout();

    }

    public void barcodeCapture(View view) {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivity(intent);
    }

    public void imageCapture(View view) {
        Intent intent = new Intent(this, ImageCaptureActivity.class);
        startActivity(intent);
    }

    public void getRecentItems(View view) {
        Intent intent = new Intent(this, RecentItemsActivity.class);
        startActivity(intent);
    }

    private void loadDefaultData() {
        try {
            InputStream file = context.getResources().getAssets().open("customerRecord.json");
            byte[] data = new byte[file.available()];
            file.read(data);
            file.close();
            String fileContentsString = new String(data);
            try {
                JSONObject fileContents = new JSONObject(fileContentsString);
                System.out.println("Successfully got JSON contents");

                Profile profile = new Profile(context);
                profile.setName(fileContents.getString("name"));
                profile.setAccountBalance(fileContents.getDouble("accountBalance"));

                JSONArray jsonCreditCards = fileContents.getJSONArray("credit_cards");
                ArrayList<CreditCard> creditCards = new ArrayList<>();

                for (int i = 0; i < jsonCreditCards.length(); i++) {
                    JSONObject creditcardObj = jsonCreditCards.getJSONObject(i);

                    Double creditcardLimit = creditcardObj.getDouble("credit_limit");
                    Double creditcardBalance = creditcardObj.getDouble("credit_balance");
                    String creditcardName = creditcardObj.getString("name");
                    Double creditcardAPR = creditcardObj.getDouble("annual_purchase_rate");
                    Double creditcardCashback = creditcardObj.getDouble("cashback");

                    Double creditcardrewards = creditcardObj.getDouble("rewards");
                    String creditcardrewardsCategory = creditcardObj.getString("rewards_category");



                    creditCards.add(new CreditCard(creditcardName,creditcardLimit
                                                    ,creditcardBalance,creditcardAPR,creditcardCashback,
                                                    creditcardrewards,creditcardrewardsCategory  ));
                }

                Log.d(" [HOME ACTIVITY] "," to string: " + creditCards.get(0).toString());

                profile.setCreditCards(creditCards);
                Log.d(" [HOME ACTIVITY] ", " PROFILE CREDIT = " + profile.getCreditCards().get(0).toString());


                prefs.edit().putBoolean("defaultDataSet", true).apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts this service to perform action AI with the given parameters. If
     * the service is already performing a task this action will be queued.
     */
    public static void startActionAI(Context context) {
        Log.d("test service","starting action ai");
        Intent intent = new Intent(context, AIService.class);
        intent.setAction(ACTION_AI);
        context.startService(intent);
    }
}
