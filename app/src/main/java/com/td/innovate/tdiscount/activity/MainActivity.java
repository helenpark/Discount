package com.td.innovate.tdiscount.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.savingstracker.HoloCircularProgressBar;

import com.td.innovate.savingstracker.Transaction;
import com.td.innovate.savingstracker.onboard.OnBoardActivity;
import com.td.innovate.tdiscount.R;
import com.koushikdutta.ion.Ion;
import com.td.innovate.tdiscount.adapter.ViewPagerAdapter;
import com.td.innovate.tdiscount.fragment.ReviewFragment;
import com.td.innovate.tdiscount.fragment.StoreFragment;
import com.td.innovate.tdiscount.model.Product;
import com.td.innovate.tdiscount.model.Profile;
import com.td.innovate.tdiscount.service.AIService;
import com.td.innovate.tdiscount.tools.JSONReader;
import com.td.innovate.tdiscount.tools.ScrollableFragmentListener;
import com.td.innovate.tdiscount.tools.ScrollableListener;
import com.td.innovate.tdiscount.tools.ShakeDetector;
import com.td.innovate.tdiscount.tools.UpdateReviewsInfoCallBack;
import com.td.innovate.tdiscount.tools.UpdateStoreInfoCallBack;
import com.td.innovate.tdiscount.tools.ViewPagerHeaderHelper;
import com.td.innovate.tdiscount.widgets.SlidingTabLayout;
import com.td.innovate.tdiscount.widgets.TouchCallbackLayout;
import com.td.mobile.controllers.AccountsSummaryController;
import com.td.mobile.controllers.LoginController;
import com.td.mobile.nextgen.restful.Session;
import com.td.mobile.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends ActionBarActivity
        implements TouchCallbackLayout.TouchEventListener, ScrollableFragmentListener,
        ViewPagerHeaderHelper.OnViewPagerTouchListener {

    public static boolean isComingBackFromPayments = false;


    private static final long DEFAULT_DURATION = 300L;
    private static final float DEFAULT_DAMPING = 1.5f;
    private static final String TAG = "MainActivity";


    //--------Price API--------//
    private static final String PRICEAPI_URL = "https://api.priceapi.com/products/single?";
    private static final String TOKEN = "QYJSWUMEBRHLHASFAHQUKFDXQDSXIGIEQCJNNUGKTSJDENJOZYYMHHJCYIITOTKP";
    private static final String COUNRTY = "us";
    private static final String SOURCE = "google-shopping";
    private static final String CURRENTNESS = "daily_updated";
    private static final String COMPLETENESS = "one_page";
    private static String KEY = "keyword";
    private String value;

    public static Product mainProduct;
    JSONObject json = null;

    private String keywords;
    private String barcode;

    public static boolean hasFoundResults = false;

    private Button buyButton;

    //--------Price API--------//


    //-------Shake--------//
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    //-------Shake--------//

    //Callbacks for the fragment
    public static UpdateStoreInfoCallBack updateStoreInfoCallBack;
    public static UpdateReviewsInfoCallBack updateReviewsInfoCallBack;


    private SparseArrayCompat<ScrollableListener> mScrollableListenerArrays =
            new SparseArrayCompat<>();
    private ViewPager mViewPager;
    private View mHeaderLayoutView;
    private ViewPagerHeaderHelper mViewPagerHeaderHelper;

    private int mTouchSlop;
    private int mTabHeight;
    private int mHeaderHeight;

    private boolean shouldGoToPayment = false;
    private boolean isComingBackFromLogin = false;


    private Interpolator mInterpolator = new DecelerateInterpolator();

    private Context context;


    private TextView productName;
    private TextView productMaker;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tabs_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.viewpager_header_height);

        mViewPagerHeaderHelper = new ViewPagerHeaderHelper(this, this);

        TouchCallbackLayout touchCallbackLayout = (TouchCallbackLayout) findViewById(R.id.layout);
        touchCallbackLayout.setTouchEventListener(this);

        mHeaderLayoutView = findViewById(R.id.header);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        slidingTabLayout.setDistributeEvenly(true);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        slidingTabLayout.setViewPager(mViewPager);

        ViewCompat.setTranslationY(mViewPager, mHeaderHeight);

        final ImageView iv = (ImageView) findViewById(R.id.iview);

        Ion.getDefault(getApplicationContext()).configure().setLogging("MyLogs", Log.DEBUG);

//        Ion.with(iv)
//                .load("https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcQLz-5tv-ijbPphIwQbg98tw6EJkafz1b9Gctew87ZYq7nXSB5EwplHQ4RMHxKGJgNQ88G70clx&usqp=CAY");

        hasFoundResults = false;
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.keywords = extras.getString("keywords");
            this.barcode = extras.getString("barcode");
            new GetDataRequestTask().execute();
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(R.color.colorPrimary);
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        productName = (TextView) findViewById(R.id.productNameTextView);
        productMaker = (TextView) findViewById(R.id.productDescriptionTextView);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buyButton = (Button) findViewById(R.id.buyButton);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(!Session.getInstance().isUserLoggedIn()) {
                   isComingBackFromLogin = true;
                   loginTD();
               }else{
                   Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                   intent.putExtra("price", Double.parseDouble(mainProduct.getOffers().get(0).getPrice()));
                   startActivity(intent);
               }
            }
        });


//        iv.setImageResource(android.R.color.transparent);

    }


    @Override
    public void onResume() {
        super.onResume();

        Log.d("MAINACTIVITY", "ON RESUME");


        if(Session.getInstance().isUserLoggedIn() && shouldGoToPayment == true){
            shouldGoToPayment = false;
            Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
            intent.putExtra("price", Double.parseDouble(mainProduct.getOffers().get(0).getPrice()));
            startActivity(intent);
        }

        if(Session.getInstance().isUserLoggedIn() && shouldGoToPayment == false && isComingBackFromLogin == true){
            shouldGoToPayment =true;
            isComingBackFromLogin = false;
            Intent intent = new Intent(this, AccountsSummaryController.class);
            startActivity(intent);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

//
//    public void launchActivity(View view) {
//        final CharSequence[] listOfOptions = {"Your TD Credentials", "Demo Data"};
//
//        AlertDialog.Builder CSVLogin = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
//        CSVLogin.setItems(listOfOptions, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case 0:
//                        loginTD();
//                        break;
//                    case 1:
//                        readCSV();
//                        break;
//                }
//            }
//        });
//        CSVLogin.setTitle("Login Using...");
//        AlertDialog dialog = CSVLogin.create();
//        dialog.show();
//    }

    private void loginTD() {

        // Restore activity state from shared preferences
        //    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
/*
        if (preferences.getBoolean("isSignedIn", false)) {
            Intent restoreIntent = new Intent(this, com.td.innovate.savingstracker.MainActivity.class);
            startActivity(restoreIntent);
        }*/


        if (Session.getInstance().isUserLoggedIn()) {
            Intent intent = new Intent(context, AccountsSummaryController.class);
            startActivity(intent);
        } else {
            Intent i = new Intent(this, LoginController.class);
            i.putExtra("targetClass", AccountsSummaryController.class);
            startActivityForResult(i, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Log.d("MainActivity", "Came back from login, Result was okay: ");
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra("price", Double.parseDouble(mainProduct.getOffers().get(0).getPrice()));
                startActivity(intent);

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }


    private class GetDataRequestTask extends AsyncTask<String, Void, String> {
        ProgressDialog pd;
        SweetAlertDialog pDialog;
        int i = -1;


        @Override
        protected void onPreExecute() {
//            pd = new ProgressDialog(MainActivity.this);
//            pd.setMessage("Loading");
//            pd.show();
//            pd.setCanceledOnTouchOutside(false);

            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
//            pDialog.getProgressHelper().setSpinSpeed(2000);

            pDialog.show();

            new CountDownTimer(2000 * 70, 2000) {
                public void onTick(long millisUntilFinished) {
                    // you can change the progress bar color by ProgressHelper every 800 millis
                    i++;
                    if (pDialog == null) {
                        this.onFinish();
                    }
                    switch (i) {
                        case 0:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                            break;
                        case 1:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                            break;
                        case 2:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                        case 3:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                            break;
                        case 4:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                            break;
                        case 5:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                            break;
                        case 6:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                            i = -1;
                            break;
                    }
                }

                public void onFinish() {
                    i = -1;
                    if (pDialog != null) {
                        this.start();
                    }
                }
            }.start();

        }

        @Override
        protected String doInBackground(String... params) {

            if (keywords.equals("null")) {
                KEY = "gtin";
                value = barcode;
            } else {
                KEY = "keyword";
                value = keywords;
            }
            Log.d("Values", "Key : " + KEY + " |  value " + value);

            try {

                json = JSONReader.readJsonFromUrl(PRICEAPI_URL + "token=" + TOKEN
                        + "&country=" + COUNRTY + "&source=" + SOURCE + "&currentness="
                        + CURRENTNESS + "&completeness=" + COMPLETENESS + "&key="
                        + KEY + "&value=" + URLEncoder.encode(value, "UTF-8"));

                System.out.println(json.toString());

                hasFoundResults = true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "testing:1 ");

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();

            }
            if (pDialog != null) {
//                pDialog.setTitleText("Item Found!");
//                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                pDialog.showCancelButton(false);
//                pDialog.setConfirmText("");
//                new CountDownTimer(1000, 1000) {
//                    public void onTick(long millisUntilFinished) {
//                    }
//                public void onFinish() {
//                        pDialog.dismiss();
//                        pDialog = null;
//                }
//                }.start();
                pDialog.dismissWithAnimation();
            }
            hasFoundResults = true;

            if (json != null) {
                try {
                    mainProduct = new Product(json.getJSONArray("products").get(0).toString());
                    Profile profile = new Profile(context);

                    if (!mainProduct.couldNotFindProduct()) {
                        String productName = mainProduct.getName();
                        ArrayList<Product> pastResults = profile.getPastResults();
                        System.out.println(pastResults);
                        boolean alreadyInPastResults = false;
                        for (int i = 0; i < pastResults.size(); i++) {
                            if (productName.equals(pastResults.get(i).getName())) {
                                alreadyInPastResults = true;
                            }
                        }
                        if (!alreadyInPastResults) {
                            pastResults.add(mainProduct);
                            profile.setPastResults(pastResults);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!mainProduct.couldNotFindProduct()) {
                    Log.d(TAG, "testing: " + mainProduct.toString());


                    updateStoreInfoCallBack.updateFragmentsCall();
                    updateReviewsInfoCallBack.updateFragmentsCall();
                    productName.setText(mainProduct.getName());

                    // TODO decide how we want to parse description
                    String[] words = mainProduct.getDescription().split(Pattern.quote("."));
                    Log.i("words", words[0]);


                    if (mainProduct.getBrand_name() != "null") {

                        productMaker.setText("By " + mainProduct.getBrand_name());
                    } else {

                    }
                    ImageView iv = (ImageView) findViewById(R.id.iview);
                    iv.setImageResource(android.R.color.transparent);
                    Ion.with(iv)
                            .load(mainProduct.getImage_url());

                    Log.i("key", mainProduct.getValue());
                } else {
                    Toast.makeText(context, "No Info Found", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, NoCloseMatchesActivity.class);
                    intent.putExtra("keywords", keywords);
                    intent.putExtra("barcode", barcode);
                    startActivity(intent);
                    finish();

                }
            } else {
                Log.e("Product request", "No JSON retrieved");
                Intent intent = new Intent(context, NoCloseMatchesActivity.class);
                intent.putExtra("keywords", keywords);
                intent.putExtra("barcode", barcode);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    @Override
    public boolean onLayoutInterceptTouchEvent(MotionEvent event) {

        return mViewPagerHeaderHelper.onLayoutInterceptTouchEvent(event,
                mTabHeight + mHeaderHeight);
    }

    @Override
    public boolean onLayoutTouchEvent(MotionEvent event) {
        return mViewPagerHeaderHelper.onLayoutTouchEvent(event);
    }

    @Override
    public boolean isViewBeingDragged(MotionEvent event) {

        return mScrollableListenerArrays.valueAt(mViewPager.getCurrentItem())
                .isViewBeingDragged(event);
//            return true;
    }

    @Override
    public void onMoveStarted(float y) {

    }

    @Override
    public void onMove(float y, float yDx) {
        float headerTranslationY = ViewCompat.getTranslationY(mHeaderLayoutView) + yDx;
        if (headerTranslationY >= 0) { // pull end
            headerExpand(0L);

            if (countPullEnd >= 1) {
                if (countPullEnd == 1) {
                    downtime = SystemClock.uptimeMillis();
                    simulateTouchEvent(mViewPager, downtime, SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 250, y + mHeaderHeight);
                }
                simulateTouchEvent(mViewPager, downtime, SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, 250, y + mHeaderHeight);
            }
            countPullEnd++;

        } else if (headerTranslationY <= -mHeaderHeight) { // push end
            headerFold(0L);

            if (countPushEnd >= 1) {
                if (countPushEnd == 1) {
                    downtime = SystemClock.uptimeMillis();
                    simulateTouchEvent(mViewPager, downtime, SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 250, y + mHeaderHeight);
                }
                simulateTouchEvent(mViewPager, downtime, SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, 250, y + mHeaderHeight);
            }
            countPushEnd++;

        } else {

            ViewCompat.animate(mHeaderLayoutView)
                    .translationY(headerTranslationY)
                    .setDuration(0)
                    .start();
            ViewCompat.animate(mViewPager)
                    .translationY(headerTranslationY + mHeaderHeight)
                    .setDuration(0)
                    .start();
        }
    }

    long downtime = -1;
    int countPushEnd = 0, countPullEnd = 0;

    private void simulateTouchEvent(View dispatcher, long downTime, long eventTime, int action, float x, float y) {
        MotionEvent motionEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), action, x, y, 0);
        try {
            dispatcher.dispatchTouchEvent(motionEvent);
        } catch (Throwable e) {
            Log.e(TAG, "simulateTouchEvent error: " + e.toString());
        } finally {
            motionEvent.recycle();
        }
    }

    @Override
    public void onMoveEnded(boolean isFling, float flingVelocityY) {

        countPushEnd = countPullEnd = 0;

        float headerY = ViewCompat.getTranslationY(mHeaderLayoutView); // 0到负数
        if (headerY == 0 || headerY == -mHeaderHeight) {
            return;
        }

        if (mViewPagerHeaderHelper.getInitialMotionY() - mViewPagerHeaderHelper.getLastMotionY()
                < -mTouchSlop) {  // pull > mTouchSlop = expand
            headerExpand(headerMoveDuration(true, headerY, isFling, flingVelocityY));
        } else if (mViewPagerHeaderHelper.getInitialMotionY()
                - mViewPagerHeaderHelper.getLastMotionY()
                > mTouchSlop) { // push > mTouchSlop = fold
            headerFold(headerMoveDuration(false, headerY, isFling, flingVelocityY));
        } else {
            if (headerY > -mHeaderHeight / 2f) {  // headerY > header/2 = expand
                headerExpand(headerMoveDuration(true, headerY, isFling, flingVelocityY));
            } else { // headerY < header/2= fold
                headerFold(headerMoveDuration(false, headerY, isFling, flingVelocityY));
            }
        }
    }

    private long headerMoveDuration(boolean isExpand, float currentHeaderY, boolean isFling,
                                    float velocityY) {

        long defaultDuration = DEFAULT_DURATION;

        if (isFling) {

            float distance = isExpand ? Math.abs(mHeaderHeight) - Math.abs(currentHeaderY)
                    : Math.abs(currentHeaderY);
            velocityY = Math.abs(velocityY) / 1000;

            defaultDuration = (long) (distance / velocityY * DEFAULT_DAMPING);

            defaultDuration =
                    defaultDuration > DEFAULT_DURATION ? DEFAULT_DURATION : defaultDuration;
        }

        return defaultDuration;
    }

    private void headerFold(long duration) {
        ViewCompat.animate(mHeaderLayoutView)
                .translationY(-mHeaderHeight)
                .setDuration(duration)
                .setInterpolator(mInterpolator)
                .start();

        ViewCompat.animate(mViewPager).translationY(0).
                setDuration(duration).setInterpolator(mInterpolator).start();

        mViewPagerHeaderHelper.setHeaderExpand(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        android.support.v7.app.ActionBar app = this.getSupportActionBar();
//        app.setBackgroundDrawable(new ColorDrawable());
        setTitle(mainProduct.getName());
    }

    private void headerExpand(long duration) {
        ViewCompat.animate(mHeaderLayoutView)
                .translationY(0)
                .setDuration(duration)
                .setInterpolator(mInterpolator)
                .start();

        ViewCompat.animate(mViewPager)
                .translationY(mHeaderHeight)
                .setDuration(duration)
                .setInterpolator(mInterpolator)
                .start();
        mViewPagerHeaderHelper.setHeaderExpand(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof StoreFragment)
            updateStoreInfoCallBack = (StoreFragment) fragment;
        Log.d(TAG, "ATTACHED STORE FRAGMENT: " + (updateStoreInfoCallBack == null));

        if (fragment instanceof ReviewFragment)
            updateReviewsInfoCallBack = (ReviewFragment) fragment;
        Log.d(TAG, "ATTACHED REVIEW FRAGMENT: " + (updateReviewsInfoCallBack == null));
    }


    @Override
    public void onFragmentAttached(ScrollableListener listener, int position) {
//        Log.i("yayattach", String.valueOf(position));
        mScrollableListenerArrays.put(position, listener);
    }

    @Override
    public void onFragmentDetached(ScrollableListener listener, int position) {
//        Log.i("yaydetach", String.valueOf(position));
        mScrollableListenerArrays.remove(position);
    }

    public void moveHeader() {
        headerFold(0L);
    }

}
