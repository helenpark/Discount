package com.td.innovate.savingstracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.td.innovate.savingstracker.onboard.OnBoardActivity;
import com.td.mobile.controllers.AccountsSummaryController;
import com.td.mobile.controllers.BaseController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class    MainActivity extends BaseController {
    private static final String CLASS_NAME = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchEULA(View view) {

        AlertDialog.Builder EULALogin = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View contentView = inflater.inflate(R.layout.eula_modal, null);
        EULALogin.setView(contentView);

        EULALogin.setNegativeButton("Cancel", null);
        EULALogin.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchActivity(contentView);
            }
        });

        AlertDialog dialog = EULALogin.create();
        dialog.show();
    }

    public void launchActivity(View view) {
        final CharSequence[] listOfOptions = {"Your TD Credentials", "Demo Data"};

        AlertDialog.Builder CSVLogin = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        CSVLogin.setItems(listOfOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        loginTD();
                        break;
                    case 1:
                        readCSV();
                        break;
                }
            }
        });
        CSVLogin.setTitle("Login Using...");
        AlertDialog dialog = CSVLogin.create();
        dialog.show();
    }

    private void loginTD() {
        // Restore activity state from shared preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("isSignedIn", false)) {
            Intent restoreIntent = new Intent(this, MainActivity.class);
            startActivity(restoreIntent);
        }
        verifyUserLogin(AccountsSummaryController.class);
    }

    private void readCSV() {
        String line;
        ArrayList<Transaction> allTransaction = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.demo_data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                String[] entry = cleanUpData(line);
                if (!entry[1].equals("PAYMENT - THANK YOU") && !entry[1].contains("TFR")) {
                    Calendar date = Calendar.getInstance();
                    try {
                        date.setTime(new SimpleDateFormat("MM/dd/yy", Locale.CANADA).parse(entry[0]));
                    } catch (ParseException e) {
                        Log.e(CLASS_NAME, "Creating Transaction: Date retrieving error.");
                        continue;
                    }
                    Transaction newTransaction = new Transaction(date, entry[1], Double.parseDouble(entry[2]), Double.parseDouble(entry[3]), Transaction.ReoccurringType.NOT, Transaction.OccurredType.ARRIVED);
                    allTransaction.add(newTransaction);
                }
            }
            inputStream.close();
            reader.close();
        } catch (FileNotFoundException e) {
            Log.e(CLASS_NAME, "File not found.");
        } catch (IOException e) {
            Log.e(CLASS_NAME, "IO Exception.");
        }
        Calendar current = Calendar.getInstance();
        DataManipulation.initializeData(allTransaction, current);
        Intent onBoard = new Intent(this, OnBoardActivity.class);
        onBoard.putExtra("To OnBoard", "From Login");
        startActivity(onBoard);
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
}