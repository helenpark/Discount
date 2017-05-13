package com.td.innovate.savingstracker;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by David on 2014-10-21.
 * This is for all the Data Manipulation. Good Luck :)
 */
public class DataManipulation {
    private static final String CLASS_NAME = DataManipulation.class.getName();
    private static final double SMALL_TRANSACTION_THRESHOLD = 20;

    //Data used for calculations
    public static ArrayList<Transaction> allTransactions, currentMonthTransactions, reoccurring;
    private static HashMap<String, ArrayList<Transaction>> byVendorsPastMonths;
    //Retrievable Data outside the class
    private static Calendar now = Calendar.getInstance();
    private static HashMap<String, ArrayList<Transaction>> thisMonthOtherDebit, thisMonthOtherCredit;
    private static ArrayList<Transaction> thisMonthReoccurringIncomeUpdated, thisMonthReoccurringExpenseUpdated,
            thisMonthUnexpectedIncome, thisMonthUnexpectedExpense, todayTransaction;
    private static double PYFAmount = 0.0;
    private static double thisMonthUnexpectedIncomeTransactionsAmount, thisMonthUnexpectedExpenseTransactionsAmount = 0.0;


    private static boolean hasSortedbefore = false;

    private static double totalBalance =   0.0;

    //Public Functions for initializing and update data
    public static void initializeData(ArrayList<Transaction> transactions, Calendar current) {
        Log.d("DATA MANIP", "CHECK 1");
        DataManipulation.allTransactions = transactions;
        Log.d("DataManipulation","was initilized??");
        Log.d("DataManipulation", "2transactions null: " + (transactions == null));
        Log.d("DataManipulation","transactions first: "+ (transactions.get(0))   );

        Collections.sort(allTransactions);
        Log.d("LoginController", "got here 10");

        now = current;
        Log.d("LoginController","got here 11");

        updateData();
        Log.d("LoginController", "got here 12");

        updateTotalBalance();
    }

    public static void updateDateWithNewTransactions(ArrayList<Transaction> transactions) {
        Transaction lastOldTransaction = allTransactions.get(allTransactions.size() - 1);
        Collections.sort(transactions);
        if (lastOldTransaction.equals(transactions.get(transactions.size() - 1))) return;
        int index = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            if (transactions.get(i).equals(lastOldTransaction)) {
                index = i + 1;
                break;
            }
        }
        while (index < transactions.size()) {
            allTransactions.add(transactions.get(index));
            index++;
        }
        updateData();
    }

    //Setters
    public static void setNow(int year, int month, int day) {
        now.set(year, month, day);
        updateData();
    }

    public static void setPYFAmount(double PYFAmount) {
        DataManipulation.PYFAmount = PYFAmount;
    }

    //Getters with sum calculation
    public static Calendar getNow() {
        return now;
    }

    public static HashMap<String, ArrayList<Transaction>> getThisMonthOtherDebit() {
        return thisMonthOtherDebit;
    }

    public static double getThisMonthOtherDebitAmount() {
        double thisMonthOtherDebitAmount = 0.0;
        for (String vendorName : thisMonthOtherDebit.keySet()) {
            for (int i = 0; i < thisMonthOtherDebit.get(vendorName).size(); i++) {
                thisMonthOtherDebitAmount += thisMonthOtherDebit.get(vendorName).get(i).getDebit();
            }
        }
        return thisMonthOtherDebitAmount;
    }

    public static HashMap<String, ArrayList<Transaction>> getThisMonthOtherCredit() {
        return thisMonthOtherCredit;
    }

    public static double getThisMonthOtherCreditAmount() {
        double thisMonthOtherCreditAmount = 0.0;
        for (String vendorName : thisMonthOtherCredit.keySet()) {
            for (int i = 0; i < thisMonthOtherCredit.get(vendorName).size(); i++) {
                thisMonthOtherCreditAmount += thisMonthOtherCredit.get(vendorName).get(i).getCredit();
            }
        }
        return thisMonthOtherCreditAmount;
    }

    public static ArrayList<Transaction> getThisMonthReoccurringIncomeUpdated() {
        return thisMonthReoccurringIncomeUpdated;
    }

    public static double getThisMonthUnexpectedIncomeTransactionsAmount() {
        return thisMonthUnexpectedIncomeTransactionsAmount;
    }

    public static ArrayList<Transaction> getThisMonthUnexpectedIncome() {
        return thisMonthUnexpectedIncome;
    }

    public static ArrayList<Transaction> getThisMonthUnexpectedExpense() {
        return thisMonthUnexpectedExpense;
    }

    public static double getThisMonthUnexpectedExpenseTransactionsAmount() {
        return thisMonthUnexpectedExpenseTransactionsAmount;
    }

    public static double getThisMonthReoccurringIncomeUpdatedAmount() {
        double thisMonthReoccurringIncomeUpdatedAmount = 0.0;
        for (Transaction aThisMonthUpdatedReoccurringIncome : thisMonthReoccurringIncomeUpdated) {
            thisMonthReoccurringIncomeUpdatedAmount += aThisMonthUpdatedReoccurringIncome.getCredit();
        }
        return thisMonthReoccurringIncomeUpdatedAmount;
    }

    public static ArrayList<Transaction> getThisMonthReoccurringExpenseUpdated() {
        return thisMonthReoccurringExpenseUpdated;
    }

    public static double getThisMonthReoccurringExpenseUpdatedAmount() {
        double projectedExpensesAmountUpdated = 0.0;
        for (Transaction aThisMonthUpdatedReoccurringExpenses : thisMonthReoccurringExpenseUpdated) {
            projectedExpensesAmountUpdated += aThisMonthUpdatedReoccurringExpenses.getDebit();
        }
        return projectedExpensesAmountUpdated;
    }

    public static ArrayList<Transaction> getTodayTransaction() {
        return todayTransaction;
    }

    public static double getTodayTransactionAmount() {
        double todayTransactionAmount = 0.0;
        for (Transaction aTodayTransaction : todayTransaction) {
            todayTransactionAmount = todayTransactionAmount + aTodayTransaction.getCredit() - aTodayTransaction.getDebit();
        }
        return todayTransactionAmount;
    }

    public static double getXMonthAgoCashFlow(int x) {
        double cashFlow = 0.0;
        Calendar xMonthAgo = (Calendar) now.clone();
        xMonthAgo.add(Calendar.MONTH, -x);
        for (Transaction aTransaction : allTransactions) {
            if (aTransaction.getDate().get(Calendar.MONTH) == xMonthAgo.get(Calendar.MONTH)) {
                cashFlow = cashFlow + aTransaction.getCredit() - aTransaction.getDebit();
            }
        }
        return cashFlow;
    }

    public static double getPYFAmount() {
        return PYFAmount;
    }

    //Private Function for calculating data
    private static void updateData() {
        Log.d("LoginController", "got here 13");

        resetFunction();
        sortByVendors();

        Log.d("LoginController", "got here 14");

        identifyReoccurringTransaction();

        Log.d("LoginController", "got here 15");

        try {
            calculateCurrentMonthRecurringTransaction();
            Log.d("LoginController", "got here 16");

            upToToday();
            Log.d("LoginController", "got here 17");

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            Log.d("LoginController", "got here OH NO 18");
        }
        Log.d("LoginController", "got here 20");

    }

    private static void updateTotalBalance() {

        for(Transaction thisMonthTransaction : currentMonthTransactions){


            Log.d("DataMangerAccounts", "total amount before doing the transaction: " + totalBalance + "  hasSortedBefore: "+ hasSortedbefore);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            ;

            Log.d("DataMangerAccounts","This Month Transaction: " + thisMonthTransaction.getVendor()
                    +  " |debit" + thisMonthTransaction.getDebit()
                    +  " |credit" + thisMonthTransaction.getCredit()
                    +  " |Month:  " + dateFormat.format(thisMonthTransaction.getDate().getTime()));

            if(!hasSortedbefore) {
                if (thisMonthTransaction.getCredit() != null) {
                    Log.d("DataMangerAccounts", "transcations subtracting:" + thisMonthTransaction.getCredit().doubleValue());
                    subtractBalanceToTotalBalance(thisMonthTransaction.getCredit().doubleValue());
                }

                if (thisMonthTransaction.getDebit() != null) {
                    Log.d("DataMangerAccounts", "transcations adding:" + thisMonthTransaction.getDebit().doubleValue());
                    addBalanceToTotalBalance(thisMonthTransaction.getDebit().doubleValue());
                }
            }
        }

        hasSortedbefore = true;
        Log.d("DataMangerAccounts", "Total balance entring this month: "+totalBalance);


    }

    private static void resetFunction() {
        byVendorsPastMonths = new HashMap<>();
        currentMonthTransactions = new ArrayList<>();
        reoccurring = new ArrayList<>();
        thisMonthOtherDebit = new HashMap<>();
        thisMonthOtherCredit = new HashMap<>();
        thisMonthReoccurringIncomeUpdated = new ArrayList<>();
        thisMonthReoccurringExpenseUpdated = new ArrayList<>();
        thisMonthUnexpectedIncome = new ArrayList<>();
        thisMonthUnexpectedExpense = new ArrayList<>();
        thisMonthUnexpectedIncomeTransactionsAmount = 0.0;
        thisMonthUnexpectedExpenseTransactionsAmount = 0.0;
        todayTransaction = new ArrayList<>();
    }

    private static void sortByVendors() {
        int i = 0;
        Log.d("DATA MANIP", "this month trans" + allTransactions.get(0).toString());


        Log.d("DataManipulation","SORTING BY VENDORS NOW" );
        Log.d("DataManipulation","3allTransactions null: "+ (allTransactions ==null) );

        if(allTransactions != null) {
            while (i < allTransactions.size()) {
                if (now.after(allTransactions.get(i).getDate()) || now.equals(allTransactions.get(i).getDate())) {
                    if (allTransactions.get(i).getDate().get(Calendar.MONTH) == now.get(Calendar.MONTH)
                            && allTransactions.get(i).getDate().get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                        currentMonthTransactions.add(allTransactions.get(i));
                    } else {
                        String vendor = allTransactions.get(i).getVendor();
                        if (byVendorsPastMonths.containsKey(vendor)) {
                            byVendorsPastMonths.get(vendor).add(allTransactions.get(i));
                        } else {
                            ArrayList<Transaction> list = new ArrayList<>();
                            list.add(allTransactions.get(i));
                            byVendorsPastMonths.put(vendor, list);
                        }
                    }
                }
                i++;
            }
        }
        Log.d("DATA MANIP", "CHECK 2");
    }

    private static void identifyReoccurringTransaction() {
        Log.d("DATA MANIP", "CHECK 3");
        for (String vendorName : byVendorsPastMonths.keySet()) {
            ArrayList<Transaction> vendorTransaction = byVendorsPastMonths.get(vendorName);
            if (vendorTransaction.size() > 1) {
                double biMonthly = reoccurringBimonthly(vendorTransaction);
                if (biMonthly != 0) {
                    Transaction lastTransaction = vendorTransaction.get((vendorTransaction.size() - 1));
                    Transaction reoccurringTransaction;
                    if (biMonthly > 0) {
                        reoccurringTransaction = new Transaction(lastTransaction.getDate(), vendorName, 0.0, biMonthly, Transaction.ReoccurringType.BIMONTHLY, Transaction.OccurredType.ANTICIPATED);
                    } else {
                        reoccurringTransaction = new Transaction(lastTransaction.getDate(), vendorName, -biMonthly, 0.0, Transaction.ReoccurringType.BIMONTHLY, Transaction.OccurredType.ANTICIPATED);
                    }
                    reoccurring.add(reoccurringTransaction);
                } else {
                    double monthly = reoccurringMonthly(vendorTransaction);
                    if (monthly != 0) {
                        Transaction lastTransaction = vendorTransaction.get((vendorTransaction.size() - 1));
                        Transaction reoccurringTransaction;
                        if (monthly > 0) {
                            reoccurringTransaction = new Transaction(lastTransaction.getDate(), vendorName, 0.0, monthly, Transaction.ReoccurringType.MONTHLY, Transaction.OccurredType.ANTICIPATED);
                        } else {
                            reoccurringTransaction = new Transaction(lastTransaction.getDate(), vendorName, -monthly, 0.0, Transaction.ReoccurringType.MONTHLY, Transaction.OccurredType.ANTICIPATED);
                        }
                        reoccurring.add(reoccurringTransaction);
                    } else {
                        double biWeekly = reoccurringBiweekly(vendorTransaction);
                        if (biWeekly != 0) {
                            Transaction lastTransaction = vendorTransaction.get(vendorTransaction.size() - 1);
                            Transaction reoccurringTransaction;
                            if (biWeekly > 0) {
                                reoccurringTransaction = new Transaction(lastTransaction.getDate(), vendorName, 0.0, biWeekly, Transaction.ReoccurringType.BIWEEKLY, Transaction.OccurredType.ANTICIPATED);
                            } else {
                                reoccurringTransaction = new Transaction(lastTransaction.getDate(), vendorName, -biWeekly, 0.0, Transaction.ReoccurringType.BIWEEKLY, Transaction.OccurredType.ANTICIPATED);
                            }
                            reoccurring.add(reoccurringTransaction);
                        }
                    }
                }
            }
        }
        Log.d("DATA MANIP", "CHECK 4");

    }

    private static double reoccurringBiweekly(ArrayList<Transaction> vendorTransaction) {
        double averageTransactionAmount = vendorTransaction.get(vendorTransaction.size() - 1).getCredit() - vendorTransaction.get(vendorTransaction.size() - 1).getDebit();
        if (Math.abs(averageTransactionAmount) < SMALL_TRANSACTION_THRESHOLD) return 0;
        for (int i = 1; i < vendorTransaction.size(); i++) {
            Calendar firstDate = (Calendar) vendorTransaction.get(i - 1).getDate().clone();
            Calendar secondDate = vendorTransaction.get(i).getDate();
            firstDate.add(Calendar.WEEK_OF_YEAR, 2);
            if (firstDate.get(Calendar.WEEK_OF_YEAR) != secondDate.get(Calendar.WEEK_OF_YEAR))
                return 0;
            double thisTransactionAmount = vendorTransaction.get(i - 1).getCredit() - vendorTransaction.get(i - 1).getDebit();
            if (Math.abs(thisTransactionAmount) < SMALL_TRANSACTION_THRESHOLD) return 0;
            averageTransactionAmount = (averageTransactionAmount * i + thisTransactionAmount) / (i + 1);
        }
        return averageTransactionAmount;
    }

    private static double reoccurringMonthly(ArrayList<Transaction> vendorTransaction) {
        double averageTransactionAmount = vendorTransaction.get(vendorTransaction.size() - 1).getCredit() - vendorTransaction.get(vendorTransaction.size() - 1).getDebit();
        if (Math.abs(averageTransactionAmount) < SMALL_TRANSACTION_THRESHOLD) return 0;
        for (int i = 1; i < vendorTransaction.size(); i++) {
            Calendar firstDate = (Calendar) vendorTransaction.get(i - 1).getDate().clone();
            Calendar secondDate = vendorTransaction.get(i).getDate();
            firstDate.add(Calendar.MONTH, 1);
            if (firstDate.get(Calendar.MONTH) != secondDate.get(Calendar.MONTH))
                return 0;
            double thisTransactionAmount = vendorTransaction.get(i - 1).getCredit() - vendorTransaction.get(i - 1).getDebit();
            if (Math.abs(thisTransactionAmount) < SMALL_TRANSACTION_THRESHOLD) return 0;
            averageTransactionAmount = (averageTransactionAmount * i + thisTransactionAmount) / (i + 1);
        }
        return averageTransactionAmount;
    }

    private static double reoccurringBimonthly(ArrayList<Transaction> vendorTransaction) {
        double averageTransactionAmount = vendorTransaction.get(vendorTransaction.size() - 1).getCredit() - vendorTransaction.get(vendorTransaction.size() - 1).getDebit();
        if (Math.abs(averageTransactionAmount) < SMALL_TRANSACTION_THRESHOLD) return 0;
        for (int i = 1; i < vendorTransaction.size(); i++) {
            Calendar firstDate = (Calendar) vendorTransaction.get(i - 1).getDate().clone();
            Calendar secondDate = vendorTransaction.get(i).getDate();
            firstDate.add(Calendar.MONTH, 2);
            if (firstDate.get(Calendar.MONTH) != secondDate.get(Calendar.MONTH))
                return 0;
            double thisTransactionAmount = vendorTransaction.get(i - 1).getCredit() - vendorTransaction.get(i - 1).getDebit();
            if (Math.abs(thisTransactionAmount) < SMALL_TRANSACTION_THRESHOLD) return 0;
            averageTransactionAmount = (averageTransactionAmount * i + thisTransactionAmount) / (i + 1);
        }
        return averageTransactionAmount;
    }

    private static void calculateCurrentMonthRecurringTransaction() throws CloneNotSupportedException {
        Log.d("DATA MANIP", "CHECK 5");
        for (Transaction aReoccurring : reoccurring) {
            Transaction thisReoccurring = aReoccurring.clone();
            if (thisReoccurring.getCredit() != 0.0) {
                if (thisReoccurring.getReoccurring() == Transaction.ReoccurringType.BIWEEKLY) {
                    thisReoccurring.getDate().add(Calendar.WEEK_OF_YEAR, 2);
                    while (thisReoccurring.getDate().get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                            thisReoccurring.getDate().get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                        thisMonthReoccurringIncomeUpdated.add(thisReoccurring.clone());
                        thisReoccurring.getDate().add(Calendar.WEEK_OF_YEAR, 2);
                    }
                } else if (thisReoccurring.getReoccurring() == Transaction.ReoccurringType.MONTHLY) {
                    thisReoccurring.getDate().add(Calendar.MONTH, 1);
                    while (thisReoccurring.getDate().get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                            thisReoccurring.getDate().get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                        thisMonthReoccurringIncomeUpdated.add(thisReoccurring.clone());
                        thisReoccurring.getDate().add(Calendar.MONTH, 1);
                    }
                } else {
                    thisReoccurring.getDate().add(Calendar.MONTH, 2);
                    while (thisReoccurring.getDate().get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                            thisReoccurring.getDate().get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                        thisMonthReoccurringIncomeUpdated.add(thisReoccurring.clone());
                        thisReoccurring.getDate().add(Calendar.MONTH, 2);
                    }
                }
            } else {
                if (thisReoccurring.getReoccurring() == Transaction.ReoccurringType.BIWEEKLY) {
                    thisReoccurring.getDate().add(Calendar.WEEK_OF_YEAR, 2);
                    while (thisReoccurring.getDate().get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                            thisReoccurring.getDate().get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                        thisMonthReoccurringExpenseUpdated.add(thisReoccurring.clone());
                        thisReoccurring.getDate().add(Calendar.WEEK_OF_YEAR, 2);
                    }
                } else if (thisReoccurring.getReoccurring() == Transaction.ReoccurringType.MONTHLY) {
                    thisReoccurring.getDate().add(Calendar.MONTH, 1);
                    while (thisReoccurring.getDate().get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                            thisReoccurring.getDate().get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                        thisMonthReoccurringExpenseUpdated.add(thisReoccurring.clone());
                        thisReoccurring.getDate().add(Calendar.MONTH, 1);
                    }
                } else {
                    thisReoccurring.getDate().add(Calendar.MONTH, 2);
                    while (thisReoccurring.getDate().get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                            thisReoccurring.getDate().get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                        thisMonthReoccurringExpenseUpdated.add(thisReoccurring.clone());
                        thisReoccurring.getDate().add(Calendar.MONTH, 2);
                    }
                }
            }
        }
        Log.d("DATA MANIP", "CHECK 6");

    }

    private static void upToToday() throws CloneNotSupportedException {
        Log.d("DATA MANIP", "CHECK 7");
        for (Transaction aCurrentMonthTransaction : currentMonthTransactions) {
            String vendor = aCurrentMonthTransaction.getVendor();
            if (aCurrentMonthTransaction.getCredit() != 0.0) {
                int indexIncome = anticipatingTransaction(vendor, thisMonthReoccurringIncomeUpdated);
                if (indexIncome == -1) {
                    if (thisMonthOtherCredit.containsKey(vendor)) {
                        thisMonthOtherCredit.get(vendor).add(aCurrentMonthTransaction);
                    } else {
                        ArrayList<Transaction> list = new ArrayList<>();
                        list.add(aCurrentMonthTransaction);
                        thisMonthOtherCredit.put(vendor, list);
                    }
                } else {
                    thisMonthReoccurringIncomeUpdated.set(indexIncome, aCurrentMonthTransaction);
                }
            } else {
                int indexExpense = anticipatingTransaction(vendor, thisMonthReoccurringExpenseUpdated);
                if (indexExpense == -1) {
                    if (thisMonthOtherDebit.containsKey(vendor)) {
                        thisMonthOtherDebit.get(vendor).add(aCurrentMonthTransaction);
                    } else {
                        ArrayList<Transaction> list = new ArrayList<>();
                        list.add(aCurrentMonthTransaction);
                        thisMonthOtherDebit.put(vendor, list);
                    }
                    if (aCurrentMonthTransaction.getDate().get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH))
                        todayTransaction.add(aCurrentMonthTransaction);
                } else {
                    thisMonthReoccurringExpenseUpdated.set(indexExpense, aCurrentMonthTransaction);
                }
            }
        }
        Log.d("DATA MANIP", "CHECK 8");
        Log.d("DATA MANIP", "this month trans" + thisMonthReoccurringIncomeUpdated.get(0).toString());

        thisMonthUnexpectedExpenseTransactionsAmount = filterUnexpectedTransaction(currentMonthTransactions, "expense");
        thisMonthUnexpectedIncomeTransactionsAmount = filterUnexpectedTransaction(currentMonthTransactions, "income");
        Log.d("DATA MANIP", "CHECK 9");
    }

    private static int anticipatingTransaction(String vendor, ArrayList<Transaction> transactions) {
        int i = 0;
        while (i < transactions.size()) {
            if (vendor.equals(transactions.get(i).getVendor()) && transactions.get(i).getOccurred() == Transaction.OccurredType.ANTICIPATED) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }

    private static double filterUnexpectedTransaction (ArrayList<Transaction> transactions, String tranType) {
        Log.d("DATA MANIP", "CHECK FILTERUNEX");
        Log.d("DATA MANIP", "this month trans" + getThisMonthReoccurringExpenseUpdated().get(0).toString());
        int i = 0;
        int n = 0;
        double total = 0.0;
        ArrayList<Transaction> results = new ArrayList<>();
        Log.d("DATA MANIP", "CHECh 19");
        Log.d("TRAN TYPE", tranType);
        Log.d("TRAN size", String.valueOf(transactions.size()));
        Log.d("TRAN first", String.valueOf(transactions.get(0).toString()));
        Log.d("TRAN reoccring", String.valueOf(transactions.get(0).getReoccurring().toString()));

        while (i < transactions.size()) {
            if (tranType.equals("income")) {
                if ((transactions.get(i).getCredit() != 0) &&
                        (transactions.get(i).getReoccurring() == Transaction.ReoccurringType.NOT)) {
                    Log.d("DATA MANIP", "CHECh 20");

                    total += transactions.get(i).getCredit();
                    results.add(n, transactions.get(i));
                    n++;
                }
            } else {
                if ((transactions.get(i).getDebit() != 0) &&
                        (transactions.get(i).getReoccurring() == Transaction.ReoccurringType.NOT)) {

                    total += transactions.get(i).getDebit();
                    results.add(n, transactions.get(i));
                    n++;
                }
            }
            i++;
        }
        Log.d("MY FUNC - EXPENSE", results.get(0).toString());
        Log.d("DATA MANIP", "CHECh 22");
        if (tranType.equals("income")) {
            thisMonthUnexpectedIncome = results;
        } else {
            thisMonthUnexpectedExpense = results;
        }
        Log.d("DATA MANIP", "CHECK 10");
        Log.d("DATA MANIP", thisMonthUnexpectedExpense.get(0).toString());

        return total;
    }

    public static void addBalanceToTotalBalance(double balance){
        totalBalance = totalBalance + balance;
        Log.d("DataManipulation","Deposit or LOC ADDED: Now totBalance is: " +totalBalance);
    }
    public static void subtractBalanceToTotalBalance(double balance){
        totalBalance = totalBalance - balance;
        Log.d("DataManipulation", "Subtraced CreditCard: Now totBalance is:  " + totalBalance);

    }

    public static double getTotalBalance() {
        return totalBalance;
    }
}