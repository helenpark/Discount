package com.td.innovate.tdiscount.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.td.innovate.tdiscount.model.CreditCard;
import com.td.innovate.tdiscount.model.Profile;
import com.td.innovate.savingstracker.Transaction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class AIService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_AI = "com.td.innovate.tdiscount.Service.action.AI";

    public static Profile profile;

    public AIService() {
        super("AIService");
        Log.d("AI service", " test contructor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_AI.equals(action)) {
                Log.d("AI service"," handling intent");
//                handleActionAI();
            }
        }
    }

    /**
     * Handle action AI in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAI() {
        // create a handler to post messages to the main thread
//        Handler mHandler = new Handler(getMainLooper());
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Get a profile instance in order to interact with shared preferences
        profile = new Profile(getApplicationContext());

        // Get transaction history from shared preferences
        ArrayList<Transaction> transactions = profile.getTransactions();

        // Get today's date and store in convenient variables
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayMonthDay = today.get(Calendar.DAY_OF_MONTH);
        // Get the first day of this month and the first day of this month one year ago to be used in calculations
        Calendar beginningOfThisMonth = Calendar.getInstance();
        beginningOfThisMonth.set(todayYear, todayMonth, 1);
        Calendar beginningOfThisMonthAYearAgo = Calendar.getInstance();
        beginningOfThisMonthAYearAgo.set(todayYear - 1, todayMonth, 1);

        // Initialize variables to be used in calculations
        Double currentMonthSpending = 0.0; // Variable for this month's spending
        Double[] monthExpensesAfterDate = new Double[12]; // Variable for storing each of the 12 past months' expenditure from today's day-of-month until the end of the month
        Arrays.fill(monthExpensesAfterDate, 0.0);
        List<Double> monthExpensesAfterDateList = Arrays.asList(monthExpensesAfterDate);
        Map<String, Integer> monthlyTransactionReptition = new HashMap<String, Integer>();
        int repetitiveThreshold = 7;
        ArrayList<Transaction> bills = new ArrayList<>();
        int numOfBills = 0;

        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            Calendar transactionDate = new GregorianCalendar();
            transactionDate = (Calendar)transaction.getDate().clone();
            //transactionMonth: numerical value of month
            int transactionMonth = transactionDate.get(Calendar.MONTH);

            // A1 #1:
            // If the transaction happened this month, add it to the current month's spending
            if (transactionDate.get(Calendar.MONTH) == todayMonth) {
                if (transaction.getDebit()!=0.0) {
                    currentMonthSpending += transaction.getDebit();
                } else {
                    currentMonthSpending += transaction.getCredit();
                }
            }

            // AI #2:
            // If the transaction happened within one year before the beginning of this month and its day-of-month is after today's day-of-month, add it to the correct months' expenses list
            // Known issue is that transactions still likely to happen later today based on the past are ignored because today's day-of-month is not included in analysis of past transactions
            if (transactionDate.compareTo(beginningOfThisMonthAYearAgo) > 0 && transactionDate.compareTo(beginningOfThisMonth) < 0) {
                if (transactionDate.get(Calendar.DAY_OF_MONTH) > todayMonthDay) {
                    int tempListIndex = transactionDate.get(Calendar.MONTH);
                    //TODO: TAKE INTO ACCOUNT THE CREDIT OPTION
                    monthExpensesAfterDateList.set(tempListIndex, monthExpensesAfterDateList.get(tempListIndex) + transaction.getDebit());
                }

            }

            // Save current month's spending
            profile.setCurrentMonthSpending(currentMonthSpending);

            Double totalHistoricTransactionCostUntilEndOfMonth = 0.0;
            int numOfHistoricMonthsToConsider = 0;


            // Get sum of relevant expenses for calculation of average spending until end of month
            // Known issue with this method: If you simply don't have any transactions for a month it gets omitted from the average spending until the end of the month calculation
            for (int k = 0; k < 12; k++) {
                Double monthBeingCheckedTotal = monthExpensesAfterDateList.get(k);
                if (monthBeingCheckedTotal != 0.0) {
                    totalHistoricTransactionCostUntilEndOfMonth += monthBeingCheckedTotal;
                    numOfHistoricMonthsToConsider += 1;
                }
            }

            // Do calculation of average for average spending until end of month
            Double averageSpendingUntilEndOfMonth = totalHistoricTransactionCostUntilEndOfMonth / numOfHistoricMonthsToConsider;

            // Save average spending until end of month
            profile.setExpectedExpenditureByEndOfMonth(averageSpendingUntilEndOfMonth);
            //Log.d("averageSpendingUn...", String.valueOf(averageSpendingUntilEndOfMonth));
        }
        //AI #3:
        //Examine reoccuring transactions and predict upcoming bills/expenditures
        int repetition = 0;
        int billIndex = 0;
        int monthGap = 1;
        //Denotes at what point a transaction is considered repetitive or not
        int repThreshold = 2;
        //Examine curr transactions in the past month, and compare for up to 4 months prior.
        //     -> If there are 4 re-occuring similar transactions
        for (int curr = 0; curr < transactions.size(); curr++) {
            Transaction currTrans = transactions.get(curr);
            Calendar transactionDate = new GregorianCalendar();
            transactionDate = (Calendar)currTrans.getDate().clone();
            int m1 = transactionDate.get(Calendar.MONTH);
            monthGap = 1;
            //check transactions starting prior month
            if (m1==(todayMonth-1)) {

                for (int ind = curr+1; ind < transactions.size(); ind++) {
                    Transaction inspectTrans = transactions.get(ind);
                    Calendar inspectDate = new GregorianCalendar();
                    inspectDate = (Calendar)inspectTrans.getDate().clone();
                    int m2 = inspectDate.get(Calendar.MONTH);
                    if ((m1-m2)==monthGap+1) {
                        break;
                    }
                    if ((m1-m2) == monthGap) {
                        //if the inspectTrans is not similar to currentTrans, move on to inspect next transaction
                        if (!(currTrans.getCredit().equals(inspectTrans.getCredit()))
                                || !(currTrans.getVendor().equals(inspectTrans.getVendor()))
                                || !(currTrans.getDebit().equals(inspectTrans.getDebit()))) {
                        } else {
                            repetition++;
                            monthGap++;
                            //if the transaction has repeated 4 months in a row consider it a recurring transaction
                            if (repetition >= repThreshold) {
                                bills.add(billIndex, currTrans);
                                monthGap++;
                                repetition = 0;
                                break;
                            }
                        }
                    }
                }
            }
        }
        // Test: for predicitng upcoming bills
        for (int n = 0; n < numOfBills; n++) {
            Log.d("[Bill]", String.valueOf(bills.get(n)));
        }
        //save results to the profile instance
        profile.setUpcomingBills(bills);
        Log.d("profile", String.valueOf(profile.getUpcomingBills()));
        // Save average spending until end of month

        double totalBills = 0.0;
        for (int i=0; i<profile.getUpcomingBills().size(); i++) {
            if (profile.getUpcomingBills().get(i).getDebit()!=0) {
                totalBills = totalBills + profile.getUpcomingBills().get(i).getDebit();
            } else {
                //TODO: TAKE INTO ACCOUNT THE CREDIT OPTION
                Log.d("[AI] TOTAL BILLS: ", "credit...");

            }        }
        profile.setExpectedSpendings(totalBills+profile.getExpectedExpenditureByEndOfMonth());
        Log.d("EXPECTED SPENDINGS ", String.valueOf(profile.getExpectedSpendings()));

    }



}


