package com.td.innovate.tdiscount.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.td.innovate.tdiscount.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.td.innovate.savingstracker.Transaction;

/**
 * Created by mmmoussa on 2015-11-10.
 */
public class Profile {
    private SharedPreferences prefs;
    private String name = "";
    private Double accountBalance = 0.0;
    private ArrayList<com.td.innovate.tdiscount.model.Transaction> transactions = new ArrayList<>();
    private ArrayList<Product> pastResults = new ArrayList<>();
    private ArrayList<CreditCard> creditCards = new ArrayList<>();

    private ArrayList<CreditCard> allTDHardcodedCreditCards = new ArrayList<>();

    private Context context;

    // Done by AI
    // an overall depiction of user's financial position prior to purchase
    private Double balanceBeforePurchase;
    private Double balanceComingIntoThisMonth;

    // Recurring Transactions for current month
    private Double recurringIncomeThisMonthAmount;
    private Double recurringExpenseThisMonthAmount;

    private ArrayList<Transaction> recurringIncomeTransactionsThisMonth;
    private ArrayList<Transaction> recurringExpenseTransactionsThisMonth;

    // Recurring Transactions for today to the end of this month
    private ArrayList<Transaction> upcomingRecurringIncomeTransactionsThisMonth;
    private ArrayList<Transaction> upcomingRecurringExpenseTransactionsThisMonth;

    // Non-Recurring Transactions for current month
    private Double unexpectedIncomeThisMonthAmount;
    private Double unexpectedExpenseThisMonthAmount;

    private ArrayList<Transaction> unexpectedIncomeTransactionsThisMonth;
    private ArrayList<Transaction> unexpectedExpenseTransactionsThisMonth;

    //GET / SET: AI FIELDS
    public void setAllTDHardcodedCreditCards(ArrayList<CreditCard> allTDHardcodedCreditCards) {
        Gson gson = new Gson();
        prefs.edit().putString("transactions", gson.toJson(transactions)).apply();    }

    public Double getBalanceComingIntoThisMonth() {
        return Double.longBitsToDouble(prefs.getLong("balanceComingIntoThisMonth", Double.doubleToLongBits(0.0)));
    }

    public void setBalanceComingIntoThisMonth(Double balanceComingIntoThisMonth) {
        prefs.edit().putLong("balanceComingIntoThisMonth", Double.doubleToRawLongBits(balanceComingIntoThisMonth)).apply();
    }

    public Double getRecurringIncomeThisMonthAmount() {
        return Double.longBitsToDouble(prefs.getLong("recurringIncomeThisMonthAmount", Double.doubleToLongBits(0.0)));
    }

    public void setRecurringIncomeThisMonthAmount(Double recurringIncomeThisMonthAmount) {
        prefs.edit().putLong("recurringIncomeThisMonthAmount", Double.doubleToRawLongBits(recurringIncomeThisMonthAmount)).apply();
    }

    public Double getRecurringExpenseThisMonthAmount() {
        return Double.longBitsToDouble(prefs.getLong("recurringExpenseThisMonthAmount", Double.doubleToLongBits(0.0)));
    }

    public void setRecurringExpenseThisMonthAmount(Double recurringExpenseThisMonthAmount) {
        prefs.edit().putLong("recurringExpenseThisMonthAmount", Double.doubleToRawLongBits(recurringExpenseThisMonthAmount)).apply();
    }

    public ArrayList<Transaction> getRecurringIncomeTransactionsThisMonth() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("recurringIncomeTransactionsThisMonth", null);
        if (jsonString == null) {
            return new ArrayList<Transaction>();
        }
        Type type = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        return gson.fromJson(jsonString, type);    }

    public void setRecurringIncomeTransactionsThisMonth(ArrayList<Transaction> recurringIncomeTransactionsThisMonth) {
        Gson gson = new Gson();
        prefs.edit().putString("recurringIncomeTransactionsThisMonth", gson.toJson(recurringIncomeTransactionsThisMonth)).apply();    }

    public ArrayList<Transaction> getrecurringExpenseTransactionsThisMonth() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("recurringExpenseTransactionsThisMonth", null);
        if (jsonString == null) {
            return new ArrayList<Transaction>();
        }
        Type type = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        return gson.fromJson(jsonString, type);    }

    public void setRecurringExpenseTransactionsThisMonth(ArrayList<Transaction> recurringExpenseTransactionsThisMonth) {
        Gson gson = new Gson();
        prefs.edit().putString("recurringExpenseTransactionsThisMonth", gson.toJson(recurringExpenseTransactionsThisMonth)).apply();    }

    public ArrayList<Transaction> getUpcomingRecurringIncomeTransactionsThisMonth() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("upcomingRecurringIncomeTransactionsThisMonth", null);
        if (jsonString == null) {
            return new ArrayList<Transaction>();
        }
        Type type = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        return gson.fromJson(jsonString, type);    }

    public void setUpcomingRecurringIncomeTransactionsThisMonth(ArrayList<Transaction> upcomingRecurringIncomeTransactionsThisMonth) {
        Gson gson = new Gson();
        prefs.edit().putString("upcomingRecurringIncomeTransactionsThisMonth", gson.toJson(upcomingRecurringIncomeTransactionsThisMonth)).apply();    }

    public ArrayList<Transaction> getUpcomingRecurringExpenseTransactionsThisMonth() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("upcomingRecurringExpenseTransactionsThisMonth", null);
        if (jsonString == null) {
            return new ArrayList<Transaction>();
        }
        Type type = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        return gson.fromJson(jsonString, type);    }

    public void setUpcomingRecurringExpenseTransactionsThisMonth(ArrayList<Transaction> upcomingRecurringExpenseTransactionsThisMonth) {
        Gson gson = new Gson();
        prefs.edit().putString("upcomingRecurringExpenseTransactionsThisMonth", gson.toJson(upcomingRecurringExpenseTransactionsThisMonth)).apply();    }

    public Double getUnexpectedIncomeThisMonthAmount() {
        return Double.longBitsToDouble(prefs.getLong("unexpectedIncomeThisMonthAmount", Double.doubleToLongBits(0.0)));
    }

    public void setUnexpectedIncomeThisMonthAmount(Double unexpectedIncomeThisMonthAmount) {
        prefs.edit().putLong("unexpectedIncomeThisMonthAmount", Double.doubleToRawLongBits(unexpectedIncomeThisMonthAmount)).apply();
    }

    public Double getUnexpectedExpenseThisMonthAmount() {
        return Double.longBitsToDouble(prefs.getLong("unexpectedExpenseThisMonthAmount", Double.doubleToLongBits(0.0)));
    }

    public void setUnexpectedExpenseThisMonthAmount(Double unexpectedExpenseThisMonthAmount) {
        prefs.edit().putLong("unexpectedExpenseThisMonthAmount", Double.doubleToRawLongBits(unexpectedExpenseThisMonthAmount)).apply();
    }

    public ArrayList<Transaction> getUnexpectedIncomeTransactionsThisMonth() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("unexpectedIncomeTransactionsThisMonth", null);
        if (jsonString == null) {
            return new ArrayList<Transaction>();
        }
        Type type = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        return gson.fromJson(jsonString, type);    }

    public void setUnexpectedIncomeTransactionsThisMonth(ArrayList<Transaction> unexpectedIncomeTransactionsThisMonth) {
        Gson gson = new Gson();
        prefs.edit().putString("unexpectedIncomeTransactionsThisMonth", gson.toJson(unexpectedIncomeTransactionsThisMonth)).apply();    }

    public ArrayList<Transaction> getUnexpectedExpenseTransactionsThisMonth() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("unexpectedExpenseTransactionsThisMonth", null);
        if (jsonString == null) {
            return new ArrayList<Transaction>();
        }
        Type type = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        return gson.fromJson(jsonString, type);
    }

    public void setUnexpectedExpenseTransactionsThisMonth(ArrayList<Transaction> unexpectedExpenseTransactionsThisMonth) {
        Gson gson = new Gson();
        prefs.edit().putString("unexpectedExpenseTransactionsThisMonth", gson.toJson(unexpectedExpenseTransactionsThisMonth)).apply();    }

    public Profile(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences("com.td.innovate.tdiscount", Context.MODE_PRIVATE);
        readHardCodedCreditCardInfo();
    }

    //GET: Given fields
    public String getName() {
        return prefs.getString("name", "No name found");
    }

    public Double getAccountBalance() {
        return Double.longBitsToDouble(prefs.getLong("accountBalance", Double.doubleToLongBits(0.0)));
    }

    public Double getCreditBalance() {
        return Double.longBitsToDouble(prefs.getLong("creditBalance", Double.doubleToLongBits(0.0)));
    }

    public Double getCreditLimit() {
        return Double.longBitsToDouble(prefs.getLong("creditLimit", Double.doubleToLongBits(0.0)));
    }

    public ArrayList<Transaction> getTransactions() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("transactions", null);
        if (jsonString == null) {
            return new ArrayList<Transaction>();
        }
        Type type = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        return gson.fromJson(jsonString, type);
    }

    public ArrayList<CreditCard> getCreditCards() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("creditcards", null);
        Type type = new TypeToken<ArrayList<CreditCard>>() {
        }.getType();
        return gson.fromJson(jsonString, type);
    }

    public ArrayList<Product> getPastResults() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("pastResults", null);
        if (jsonString == null) {
            return new ArrayList<Product>();
        } else {
            Type type = new TypeToken<ArrayList<Product>>() {
            }.getType();
            return gson.fromJson(jsonString, type);
        }
    }

    //GET: Calculated AI fields
    public Double getCurrentMonthSpending() {
        return Double.longBitsToDouble(prefs.getLong("currentMonthSpending", Double.doubleToLongBits(0.0)));
    }

    public Double getExpectedExpenditureByEndOfMonth() {
        return Double.longBitsToDouble(prefs.getLong("expectedExpenditureByEndOfMonth", Double.doubleToLongBits(0.0)));
    }

    public ArrayList<Transaction> getUpcomingBills() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("upcomingBills", null);
        if (jsonString == null) {
            return new ArrayList<Transaction>();
        } else {
            Type type = new TypeToken<ArrayList<Transaction>>() {
            }.getType();
            return gson.fromJson(jsonString, type);
        }
    }

    public Double getExpectedSpendings() {
        return Double.longBitsToDouble(prefs.getLong("expectedSpendings", Double.doubleToLongBits(0.0)));
    }

    public Double getBalanceBeforePurchase() {
        return Double.longBitsToDouble(prefs.getLong("balanceBeforePurchase", Double.doubleToLongBits(0.0)));
    }

    //SET: Given fields
    public void setName(String name) {
        prefs.edit().putString("name", name).apply();
    }

    public void setAccountBalance(Double accountBalance) {
        prefs.edit().putLong("accountBalance", Double.doubleToRawLongBits(accountBalance)).apply();
    }

    public void setCreditBalance(Double creditBalance) {
        prefs.edit().putLong("creditBalance", Double.doubleToRawLongBits(creditBalance)).apply();
    }

    public void setCreditLimit(Double creditLimit) {
        prefs.edit().putLong("creditLimit", Double.doubleToRawLongBits(creditLimit)).apply();
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        Gson gson = new Gson();
        prefs.edit().putString("transactions", gson.toJson(transactions)).apply();
    }

    public void setCreditCards(ArrayList<CreditCard> creditCards) {
        Gson gson = new Gson();
        prefs.edit().putString("creditcards", gson.toJson(creditCards)).apply();
    }

    public void setPastResults(ArrayList<Product> pastResults) {
        Gson gson = new Gson();
        prefs.edit().putString("pastResults", gson.toJson(pastResults)).apply();
    }

    //SET: Calculated AI fields
    public void setCurrentMonthSpending(Double currentMonthSpending) {
        prefs.edit().putLong("currentMonthSpending", Double.doubleToRawLongBits(currentMonthSpending)).apply();
    }

    public void setExpectedExpenditureByEndOfMonth(Double expectedExpenditureByEndOfMonth) {
        prefs.edit().putLong("expectedExpenditureByEndOfMonth", Double.doubleToRawLongBits(expectedExpenditureByEndOfMonth)).apply();
    }

    public void setUpcomingBills(ArrayList<Transaction> upcomingBills) {
        Gson gson = new Gson();
        prefs.edit().putString("upcomingBills", gson.toJson(upcomingBills)).apply();
    }

    public void setExpectedSpendings(Double expectedSpendings) {
        prefs.edit().putLong("expectedSpendings", Double.doubleToRawLongBits(expectedSpendings)).apply();
    }

    private void readHardCodedCreditCardInfo() {
        String line;
        InputStream inputStream = context.getResources().openRawResource(R.raw.all_td_credit_card_info);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Log.d("PROFILE","Starting loop");
        try {
            while ( (line = reader.readLine()) != null){
                Log.d("PROFILE","Line: "+line);
                String[] tokens = line.split(",");
                Log.d("PROFILE","tokens[0]: "+tokens[0]
                                + "tokens[1]: "+tokens[1]
                                + "tokens[2]: "+tokens[2]);

                allTDHardcodedCreditCards.add(new CreditCard(tokens[0], 0.0, 0.0, Double.parseDouble(tokens[1]),
                        Double.parseDouble(tokens[2]), null, null));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("PROFILE", "allTDHardcodedCreditCards.size: " + allTDHardcodedCreditCards.size());

    }

    public ArrayList<CreditCard> getAllTDHardcodedCreditCards() {
        return allTDHardcodedCreditCards;

    }

    public void setBalanceBeforePurchase(Double expectedSpendings) {
        prefs.edit().putLong("balanceBeforePurchase", Double.doubleToRawLongBits(expectedSpendings)).apply();
    }
}
