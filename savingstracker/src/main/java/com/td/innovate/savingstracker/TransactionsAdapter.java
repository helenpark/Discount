package com.td.innovate.savingstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.innovate.savingstracker.R;
import com.td.innovate.savingstracker.Transaction;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by rayhou on 14-11-03.
 *
 * This adapter is used to display the list of transactions on the fragment with customized layout
 */
public class TransactionsAdapter extends ArrayAdapter<Transaction> {
    Context context;
    ArrayList<Transaction> values;

    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
        super(context, R.layout.listview_custom_transaction, transactions);
        this.context = context;
        this.values = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_custom_transaction, parent, false);
        }
        LinearLayout title = (LinearLayout) view.findViewById(R.id.title);
        TextView titleName = (TextView) view.findViewById(R.id.titleName);
        LinearLayout tab = (LinearLayout) view.findViewById(R.id.tab);
        TextView vendor = (TextView) view.findViewById(R.id.vendorLabel);
        TextView amount = (TextView) view.findViewById(R.id.amountLabel);
        TextView date = (TextView) view.findViewById(R.id.date);

        Transaction transaction = values.get(position);
        if (transaction.getDebit() != 0.0 && transaction.getCredit() != 0.0) {
            title.setVisibility(View.VISIBLE);
            tab.setVisibility(View.GONE);
            titleName.setText(transaction.getVendor());
        } else {
            title.setVisibility(View.GONE);
            tab.setVisibility(View.VISIBLE);
            date.setText(String.format(Locale.CANADA, "%tb", transaction.getDate()) + " " +
                    String.format(Locale.CANADA, "%te", transaction.getDate()));
            vendor.setText(transaction.getVendor());
            if (transaction.getCredit() != 0) {
                amount.setText("$" + String.format("%.2f", transaction.getCredit()));
            } else {
                amount.setText("-$" + String.format("%.2f", transaction.getDebit()));
            }
        }
        return view;
    }
}