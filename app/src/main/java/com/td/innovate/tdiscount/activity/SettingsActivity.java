package com.td.innovate.tdiscount.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.adapter.SettingsTransactionAdapter;
import com.td.innovate.tdiscount.model.Profile;

import com.td.innovate.savingstracker.Transaction;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editAccountBalance;
    private EditText editCreditBalance;
    private EditText editCreditLimit;
    private ListView transactionListView;
    private SettingsTransactionAdapter adapter;

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editName = (EditText) findViewById(R.id.editName);
        editAccountBalance = (EditText) findViewById(R.id.editAccountBalance);
        editCreditBalance = (EditText) findViewById(R.id.editCreditBalance);
        editCreditLimit = (EditText) findViewById(R.id.editCreditLimit);

        profile = new Profile(this);
        editName.setText(profile.getName());
        editAccountBalance.setText(profile.getAccountBalance().toString());
        editCreditBalance.setText(profile.getCreditBalance().toString());
        editCreditLimit.setText(profile.getCreditLimit().toString());

        ArrayList<com.td.innovate.savingstracker.Transaction> transactions = profile.getTransactions();
        transactionListView = (ListView) findViewById(R.id.listView);
        adapter = new SettingsTransactionAdapter(this, transactions);
        transactionListView.setAdapter(adapter);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        setTextChangeListeners();
    }

    private void setTextChangeListeners() {
        editName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                profile.setName(arg0.toString());
            }
        });

        editAccountBalance.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                profile.setAccountBalance(Double.parseDouble(arg0.toString()));
            }
        });

        editCreditBalance.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                profile.setCreditBalance(Double.parseDouble(arg0.toString()));
            }
        });

        editCreditLimit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                profile.setCreditLimit(Double.parseDouble(arg0.toString()));
            }
        });
    }
}