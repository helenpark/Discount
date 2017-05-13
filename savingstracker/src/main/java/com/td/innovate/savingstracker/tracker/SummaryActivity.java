package com.td.innovate.savingstracker.tracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.savingstracker.MainActivity;
import com.td.innovate.savingstracker.R;

import java.util.Calendar;
import java.util.Locale;

public class SummaryActivity extends FragmentActivity implements CashFlowTrackerDailyFragment.OnFragmentInteractionListener,
        CashFlowTrackerMonthlyFragment.OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks {
    private ViewPager fragmentViewPager;
    private Button dailyButton;
    private Button monthlyButton;
    private View dailyButtonStripe;
    private View monthlyButtonStripe;
    private final int DarkGrey = Color.rgb(54, 54, 54);
    private final int TAB_UNFOCUSED = Color.rgb(141, 141, 141);
    public static GoogleApiClient googleClient;
    private static Intent listenerServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        fragmentViewPager = (ViewPager) findViewById(R.id.tracker);
        dailyButton = (Button) findViewById(R.id.dailyButton);
        monthlyButton = (Button) findViewById(R.id.monthlyButton);
        dailyButtonStripe = findViewById(R.id.dailyButtonStripe);
        monthlyButtonStripe = findViewById(R.id.monthlyButtonStripe);
        initializeViewPager();
        toMonthly(monthlyButton);
        initializeApi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        MenuItem datePicker = menu.findItem(R.id.date_picker);
        datePicker.setTitle(
                String.format(Locale.CANADA, "%tb", DataManipulation.getNow()) + " " +
                        String.format(Locale.CANADA, "%te", DataManipulation.getNow()) + ", " +
                        String.format(Locale.CANADA, "%tY", DataManipulation.getNow()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings){
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        }else if(item.getItemId() == R.id.date_picker){
            Calendar now = DataManipulation.getNow();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                    DataManipulation.setNow(selectedYear, selectedMonth, selectedDay);
                    recreate();
                }
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setTitle("Select Date");
            datePickerDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (!mResolvingError) {  // more about this later

//        }
        Log.e("onStart", "invoked");
//        if (listenerServiceIntent == null) {
        listenerServiceIntent = new Intent(SummaryActivity.this, ListenerService.class);
        SummaryActivity.this.startService(listenerServiceIntent);

        googleClient.connect();
//        }
    }

    @Override
    protected void onStop() {
        googleClient.disconnect();
        SummaryActivity.this.stopService(listenerServiceIntent);
        super.onStop();
    }

    private void initializeApi() {
        googleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Wearable.API)

                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.e("OOPS", "Wearable connection failed.");
                    }
                })
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("OOPS", "connected to Google Play Services on Wear!");
//        sendStartMessage();
        new SendMessageOnOtherThread("/start", null).start();
//        Wearable.MessageApi.addListener(googleClient, this).setResultCallback(resultCallback);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    static class SendMessageOnOtherThread extends Thread {

        String path;
        String message;

        // Constructor to send a message to the data layer
        SendMessageOnOtherThread(String p, String msg) {
            path = p;
            message = msg;
        }

        public void run() {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleClient).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleClient, node.getId(), path, null).await();
                if (result.getStatus().isSuccess()) {
                    Log.v("Phone", "Message: {" + message + "} sent to: " + node.getDisplayName());
                }
                else {
                    // Log an error
                    Log.v("myTag", "ERROR: failed to send Message");
                }
            }
        }

    }

    private void initializeViewPager() {
        dailyButton.setTextColor(Color.WHITE);
        dailyButtonStripe.setBackgroundColor(Color.WHITE);
        monthlyButton.setTextColor(TAB_UNFOCUSED);
        monthlyButtonStripe.setBackgroundColor(DarkGrey);

        DailyMonthlyTrackerViewPagerAdapter mAdapter = new DailyMonthlyTrackerViewPagerAdapter(this.getSupportFragmentManager());
        fragmentViewPager.setAdapter(mAdapter);

        //on swiping the viewpager make respective tab selected
        fragmentViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    dailyButton.setTextColor(Color.WHITE);
                    dailyButtonStripe.setBackgroundColor(TAB_UNFOCUSED);
                    monthlyButton.setTextColor(TAB_UNFOCUSED);
                    monthlyButtonStripe.setBackgroundColor(DarkGrey);
                }
                if (position == 1) {
                    dailyButton.setTextColor(TAB_UNFOCUSED);
                    dailyButtonStripe.setBackgroundColor(DarkGrey);
                    monthlyButton.setTextColor(Color.WHITE);
                    monthlyButtonStripe.setBackgroundColor(TAB_UNFOCUSED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    public void toDaily(View view) {
        fragmentViewPager.setCurrentItem(0, true);
    }

    public void toMonthly(View view) {
        fragmentViewPager.setCurrentItem(1, true);
    }
}