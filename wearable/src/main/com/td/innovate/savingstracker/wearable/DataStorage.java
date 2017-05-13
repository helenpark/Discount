package com.td.innovate.savingstracker.wearable;

/**
 * Created by Cassia Deering on 14-12-17.
 * This class is created to store the data being transmitted from phone
 */
public class DataStorage {
    private static double income;
    private static double expenses;
    private static double pyfAmountCurrent;
    private static double pyfAmountRecommended;
    private static double cashFlow;

    public static double getIncome() { return income; }

    public static double getExpenses() { return expenses; }

    public static double getPyfAmountCurrent() { return pyfAmountCurrent; }

    public static double getPyfAmountRecommended() { return pyfAmountRecommended; }

    public static double getCashFlow() { return cashFlow; }

    public static void setIncome(double inc) { income = (int) inc; }

    public static void setExpenses(double exp) { expenses = (int) exp; }

    public static void setPyfAmountCurrent(double pyf) { pyfAmountCurrent = (int) pyf; }

    public static void setPyfAmountRecommended(double pyf) {pyfAmountRecommended = (int) pyf; }

    public static void setCashFlow(double cf) { cashFlow = (int) cf; }
}
