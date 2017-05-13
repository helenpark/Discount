package com.td.innovate.savingstracker.tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.td.innovate.savingstracker.MainActivity;
import com.td.innovate.savingstracker.R;
import com.td.innovate.savingstracker.TransactionsAdapter;
import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.savingstracker.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class IncomeExpenseListActivity extends Activity {
    private ArrayList<Transaction> finalList = new ArrayList<Transaction>();
    private ListView listView;
    private double totalAmount;
    private String negative = "";
    int defaultColor;
    String time;
    private static final Typeface DEFAULT_FONT = Typeface.create("sans-serif-light", Typeface.NORMAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expense_list);
        listView = (ListView) findViewById(R.id.list);
        int data = getIntent().getIntExtra("Income or Expense", -1);
        if (data == 0) {
            pendingAndCompleted(DataManipulation.getThisMonthReoccurringExpenseUpdated(), DataManipulation.getThisMonthOtherDebit());
            totalAmount = DataManipulation.getThisMonthReoccurringExpenseUpdatedAmount() + DataManipulation.getThisMonthOtherDebitAmount();
            ((TextView) findViewById(R.id.incomeOrExpense)).setText("EXPENSES");
            defaultColor = Color.rgb(202, 72, 77);
            negative = "-";
            time = String.format(Locale.CANADA, "%tB", DataManipulation.getNow());
            populateLayout();
        } else if (data == 1) {
            pendingAndCompleted(DataManipulation.getThisMonthReoccurringIncomeUpdated(), DataManipulation.getThisMonthOtherCredit());
            totalAmount = DataManipulation.getThisMonthReoccurringIncomeUpdatedAmount() + DataManipulation.getThisMonthOtherCreditAmount();
            ((TextView) findViewById(R.id.incomeOrExpense)).setText("INCOME");
            defaultColor = Color.rgb(83, 184, 76);
            time = String.format(Locale.CANADA, "%tB", DataManipulation.getNow());
            populateLayout();
        } else if (data == 2) {
            finalList = DataManipulation.getTodayTransaction();
            totalAmount = - DataManipulation.getTodayTransactionAmount();
            ((TextView) findViewById(R.id.incomeOrExpense)).setText("EXPENSES");
            negative = "-";
            defaultColor = Color.rgb(202, 72, 77);
            time = String.format(Locale.CANADA, "%tb", DataManipulation.getNow()) + " " +
                    String.format(Locale.CANADA, "%te", DataManipulation.getNow()) + ", " +
                    String.format(Locale.CANADA, "%tY", DataManipulation.getNow());
            populateLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_income_expense_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if( item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if(item.getItemId() == R.id.action_settings){
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void pendingAndCompleted(ArrayList<Transaction> reoccurring, HashMap<String, ArrayList<Transaction>> other) {
        ArrayList<Transaction> pending = new ArrayList<Transaction>();
        ArrayList<Transaction> completed = new ArrayList<Transaction>();

        for (Transaction aReoccurring : reoccurring) {
            if (aReoccurring.getOccurred() == Transaction.OccurredType.ARRIVED) {
                completed.add(aReoccurring);
            } else {
                pending.add((aReoccurring));
            }
        }
        for (String vendorName : other.keySet()) {
            completed.addAll(other.get(vendorName));
        }
        if (!pending.isEmpty()) {
            Collections.sort(pending);
            finalList.add(new Transaction(Calendar.getInstance(), "PENDING", 1.0, 1.0, Transaction.ReoccurringType.NOT, Transaction.OccurredType.ARRIVED));
            finalList.addAll(pending);
        }
        if (!completed.isEmpty()) {
            Collections.sort(completed);
            finalList.add(new Transaction(Calendar.getInstance(), "COMPLETED", 2.0, 2.0, Transaction.ReoccurringType.NOT, Transaction.OccurredType.ARRIVED));
            finalList.addAll(completed);
        }
    }

    private void populateLayout() {
        ((TextView) findViewById(R.id.currentMonth)).setText(time);
        ((TextView) findViewById(R.id.amount)).setText(negative + "$" + String.format("%.2f", totalAmount));
        ((TextView) findViewById(R.id.amount)).setTextColor(defaultColor);
        ((TextView) findViewById(R.id.amount)).setTypeface(DEFAULT_FONT);
        ((TextView) findViewById(R.id.incomeOrExpense)).setTextColor(defaultColor);
        findViewById(R.id.divisionLine).setBackgroundColor(defaultColor);
        TransactionsAdapter pendingAdapter = new TransactionsAdapter(this, finalList);
        listView.setAdapter(pendingAdapter);
    }
}