package com.td.innovate.tdiscount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.td.innovate.tdiscount.R;
import java.util.ArrayList;
import com.td.innovate.savingstracker.Transaction;

/**
 * Created by mmmoussa on 2015-11-16.
 */
public class SettingsTransactionAdapter extends ArrayAdapter<Transaction> {
    private final Context context;
    private final ArrayList<Transaction> transactions;

    public SettingsTransactionAdapter(Context context, ArrayList<Transaction> transactions) {
        super(context, -1, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.settings_transaction_list_item, parent, false);
        TextView transactionName = (TextView) rowView.findViewById(R.id.transactionName);
        TextView transactionDate = (TextView) rowView.findViewById(R.id.transactionDate);
        EditText transactionAmount = (EditText) rowView.findViewById(R.id.transactionAmount);

        Transaction transaction = transactions.get(position);
        transactionName.setText(transaction.getVendor());
        transactionDate.setText(transaction.getDate().toString());
        if (transaction.getCredit()!=0.0) {
            transactionAmount.setText(transaction.getCredit().toString());
        } if (transaction.getDebit()!=0.0) {
            transactionAmount.setText(transaction.getDebit().toString());
        }
        return rowView;
    }

}

