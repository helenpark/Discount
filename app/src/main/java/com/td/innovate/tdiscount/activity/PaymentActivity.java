package com.td.innovate.tdiscount.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.savingstracker.Transaction;
import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.model.CreditCard;
import com.td.innovate.tdiscount.model.Profile;
import com.td.mobile.controllers.LoginController;

import com.td.mobile.managers.DataManager;
import com.td.mobile.model.Account;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
public class PaymentActivity extends AppCompatActivity {

    public static ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

    private Context context;
    private Profile profile;


    private double positionBeforePurchase = 0.0;
    private double positionAfterPurchase = 0.0;
    private double itemCost = 0.0;
    private boolean isAffordable = false;

    private SweetAlertDialog pDialog;
    private int i = -1;
    private RelativeLayout relativeLayout;

    @Override
    public void onBackPressed() {
        com.td.innovate.tdiscount.activity.MainActivity.isComingBackFromPayments = true;
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("PAYMENTS ACTIVITY", "ON PAUSE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("PAYMENTS ACTIVITY", "ON RESUME");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Log.d("ASYNC TASK --------", "CHECK 0");
        profile = new Profile(getApplicationContext());

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        relativeLayout.setVisibility(View.INVISIBLE);

        new AffordabilityCalculator().execute();
        Log.d("ASYNC TASK --------", "CHECK 0.5");

        getSupportActionBar().setTitle("Affordability");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        context = this;

        Button moreInfoButton = (Button) findViewById(R.id.moreInfoButton);
        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moreInfoIntent = new Intent(context, MorePaymentInfoActivity.class);
                moreInfoIntent.putExtra("itemCost", itemCost);
                startActivity(moreInfoIntent);
            }
        });

        Button paymentNotificationButton = (Button) findViewById(R.id.paymentNotificationButton);
        paymentNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAffordable) {
                    Intent creditCardIntent = new Intent(context, CreditCardActivity.class);
                    startActivity(creditCardIntent);
                } else {
                }

            }
        });

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
//        pDialog.getProgressHelper().setSpinSpeed(2000);

        pDialog.show();

        new CountDownTimer(2000 * 70, 2000) {
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                if (pDialog == null) {
                    this.onFinish();
                    i = 7;
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

    private void readCSV() {
        Log.d("CHECK---------", "1.5");
        String line;
        ArrayList<Transaction> allTransaction = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(com.td.innovate.savingstracker.R.raw.demo_data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                String[] entry = cleanUpData(line);
                if (!entry[1].equals("PAYMENT - THANK YOU") && !entry[1].contains("TFR")) {
                    Calendar date = Calendar.getInstance();
                    try {
                        date.setTime(new SimpleDateFormat("MM/dd/yy", Locale.CANADA).parse(entry[0]));
                    } catch (ParseException e) {
                        Log.e("MAINACTIVITY", "Creating Transaction: Date retrieving error.");
                        continue;
                    }
                    Transaction newTransaction = new Transaction(date, entry[1], Double.parseDouble(entry[2]), Double.parseDouble(entry[3]), Transaction.ReoccurringType.NOT, Transaction.OccurredType.ARRIVED);
                    allTransaction.add(newTransaction);
                }
            }
            inputStream.close();
            reader.close();
        } catch (FileNotFoundException e) {
            Log.e("MAINACTIVITY", "File not found.");
        } catch (IOException e) {
            Log.e("MAINACTIVITY", "IO Exception.");
        }
        Calendar current = Calendar.getInstance();
        //TODO:
        transactionList = (ArrayList<Transaction>) allTransaction.clone();

        //current.set(2014, Calendar.NOVEMBER, 7);
        DataManipulation.initializeData(allTransaction, current);


    }


    private String[] cleanUpData(String entry) {
        String[] oneLine = new String[4];
        StringBuilder data = new StringBuilder();
        Boolean inQuote = false;
        int workingEntry = 0;
        for (int i = 0; i < entry.length(); i++) {
            if (entry.charAt(i) == '"') {
                inQuote = !inQuote;
            } else if (entry.charAt(i) == ',') {
                if (!inQuote && !data.toString().equals("")) {
                    oneLine[workingEntry] = data.toString();
                    data = new StringBuilder();
                    workingEntry++;
                } else if (!inQuote) {
                    oneLine[workingEntry] = "0";
                    workingEntry++;
                }
            } else {
                data.append(entry.charAt(i));
            }
        }
        return oneLine;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private class AffordabilityCalculator extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... params) {
            readCSV();
            Log.d("DO IN BACKGROUND", transactionList.get(0).toString());
            profile.setTransactions(transactionList);

            Log.d("ASYNC TASK --------", "CHECK 1");
            Log.d("ASYNC TASK --------", "CHECK 2");

            //TODO: build actual field for the overall position and add field to profile.

            itemCost = Double.valueOf(com.td.innovate.tdiscount.activity.MainActivity.mainProduct.getOffers().get(0).getPrice());

            //********************************

            // Recurring Transactions for today to the end of this month -> AMOUNT
            profile.setRecurringIncomeThisMonthAmount(DataManipulation.getThisMonthReoccurringIncomeUpdatedAmount());
            profile.setRecurringExpenseThisMonthAmount(DataManipulation.getThisMonthReoccurringExpenseUpdatedAmount());

            // Recurring Transactions for current month -> ARRAY LIST
            profile.setRecurringIncomeTransactionsThisMonth(DataManipulation.getThisMonthReoccurringIncomeUpdated());
            profile.setRecurringExpenseTransactionsThisMonth(DataManipulation.getThisMonthReoccurringExpenseUpdated());

            // Non-Recurring Transactions for current month -> AMOUNT
            profile.setUnexpectedIncomeThisMonthAmount(DataManipulation.getThisMonthUnexpectedIncomeTransactionsAmount());
            profile.setUnexpectedExpenseThisMonthAmount(DataManipulation.getThisMonthUnexpectedExpenseTransactionsAmount());

            // Non-Recurring Transactions for current month -> ARRAY LIST
            profile.setUnexpectedIncomeTransactionsThisMonth(DataManipulation.getThisMonthUnexpectedIncome());
            profile.setUnexpectedExpenseTransactionsThisMonth(DataManipulation.getThisMonthUnexpectedExpense());

            // an overall depiction of user's financial position prior to purchase
            profile.setBalanceComingIntoThisMonth(-100.0);
            profile.setBalanceBeforePurchase(
                    profile.getBalanceComingIntoThisMonth() +
                            profile.getUnexpectedIncomeThisMonthAmount() -
                            profile.getUnexpectedExpenseThisMonthAmount() +
                            profile.getRecurringIncomeThisMonthAmount() -
                            profile.getRecurringExpenseThisMonthAmount());

            Log.d("BalanceComingMonth", String.valueOf(profile.getBalanceComingIntoThisMonth()));

            Log.d("Unex-INCThisMonthAmnth", String.valueOf(DataManipulation.getThisMonthUnexpectedIncomeTransactionsAmount()));
            Log.d("Unex-INCThisMonth", String.valueOf(DataManipulation.getThisMonthUnexpectedIncome().get(0)));
            Log.d("Unex-INCThisMonth - REC", String.valueOf(DataManipulation.getThisMonthUnexpectedIncome().get(0).getReoccurring()));
            Log.d("Unex-EXPThisMonthAmnth", String.valueOf(DataManipulation.getThisMonthUnexpectedExpenseTransactionsAmount()));
            Log.d("Unex-EXPThisMonth", String.valueOf(DataManipulation.getThisMonthUnexpectedExpense().get(0)));
            Log.d("Unex-EXPThisMonth - REC", String.valueOf(DataManipulation.getThisMonthUnexpectedExpense().get(0).getReoccurring()));
            Log.d("Rec-INCThisMonthAmnt", String.valueOf(DataManipulation.getThisMonthReoccurringIncomeUpdatedAmount()));
            Log.d("Rec-INCThisMonth", String.valueOf(DataManipulation.getThisMonthReoccurringIncomeUpdated().get(0)));
            Log.d("Rec-INCThisMonth - SIZE", String.valueOf(DataManipulation.getThisMonthReoccurringIncomeUpdated().size()));

            Log.d("Rec-INCThisMonth - REC1", String.valueOf(DataManipulation.getThisMonthReoccurringIncomeUpdated().get(0).getReoccurring()));
//            Log.d("Rec-INCThisMonth - REC2", String.valueOf(DataManipulation.getThisMonthReoccurringIncomeUpdated().get(1).getReoccurring()));
//            Log.d("Rec-INCThisMonth - REc3", String.valueOf(DataManipulation.getThisMonthReoccurringIncomeUpdated().get(2).getReoccurring()));
//            Log.d("Rec-INCThisMonth - REC4", String.valueOf(DataManipulation.getThisMonthReoccurringIncomeUpdated().get(3).getReoccurring()));
//            Log.d("Rec-INCThisMonth - REC5", String.valueOf(DataManipulation.getThisMonthReoccurringIncomeUpdated().get(4).getReoccurring()));
            Log.d("Rec-EXPThisMonthAmnt", String.valueOf(DataManipulation.getThisMonthReoccurringExpenseUpdatedAmount()));
            Log.d("Rec-EXPThisMonth", String.valueOf(DataManipulation.getThisMonthReoccurringExpenseUpdated().get(0)));
            Log.d("Rec-EXPThisMonth - REC", String.valueOf(DataManipulation.getThisMonthReoccurringExpenseUpdated().get(0).getReoccurring()));

            Log.d("Balance before purchase", String.valueOf(profile.getBalanceBeforePurchase()));

            positionAfterPurchase = profile.getBalanceBeforePurchase() - itemCost;

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (positionAfterPurchase >= 0) {
                //affordable
                isAffordable = true;
                Log.d("ASYNC TASK --------", "YAYAY you can afforfd it = "+ String.valueOf(positionAfterPurchase));
                Log.d("ASYNC TASK --------", "AFFORDABLE");

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageResource(R.drawable.green_check);

                TextView textView = (TextView) findViewById(R.id.payment_affordable_title);
                textView.setText("Cha-ching...\nYou can afford this.");

                textView = (TextView) findViewById(R.id.paymentWarning);
                textView.setText("Based on your balance and upcoming payments, we suggest you will be able to afford this.");

                Button dialogButton = (Button) findViewById(R.id.paymentNotificationButton);
                dialogButton.setText("Show Best Payment Option");
            } else {
                //unaffordable
                isAffordable = false;
                Log.d("ASYNC TASK --------", "UNAFFORDABLE");
                DecimalFormat df = new DecimalFormat("#.##");

                TextView textView = (TextView) findViewById(R.id.payment_affordable_title);
                textView.setText("Uh-oh...\nYou can't afford this.");

                textView = (TextView) findViewById(R.id.paymentWarning);
                textView.setText("This purchase of $ " + MainActivity.mainProduct.getOffers().get(0).getPrice() + " will put you over your credit card limit. Your current available credit is $"+ df.format(positionAfterPurchase) + ".");

                Button dialogButton = (Button) findViewById(R.id.paymentNotificationButton);
                dialogButton.setText("Notify Me When I Can");
            }

            pDialog.hide();
            relativeLayout.setVisibility(View.VISIBLE);

            Log.d("PAYMENT Activity", " My Cash flow was: " + DataManipulation.getXMonthAgoCashFlow(2));
            Log.d("PAYMENT Activity", " My Cash vendor was: " + DataManipulation.allTransactions.get(0).getVendor());
        }
    }
}