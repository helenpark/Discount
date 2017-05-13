package com.td.innovate.savingstracker;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by David on 2014-10-21.
 * This is the main data type we use to store data of each transaction
 */
public class Transaction implements Cloneable, Comparable {
    private Calendar date;
    private String vendor;
    private Double debit;
    private Double credit;
    private ReoccurringType reoccurring;
    private OccurredType occurred;

    public enum ReoccurringType {NOT, BIWEEKLY, MONTHLY, BIMONTHLY}
    public enum OccurredType {ANTICIPATED, ARRIVED}

    //The Constructors
    public Transaction(Calendar date, String vendor, Double debit, Double credit, ReoccurringType reoccurring, OccurredType occurred) {
        this.date = date;
        this.vendor = clearVendorNames(vendor);
        this.debit = debit;
        this.credit = credit;
        this.reoccurring = reoccurring;
        this.occurred = occurred;
    }

    //Getters
    public Calendar getDate() {
        return date;
    }

    public String getVendor() {
        return vendor;
    }

    public Double getDebit() {
        return debit;
    }

    public Double getCredit() {
        return credit;
    }

    public ReoccurringType getReoccurring() {
        return reoccurring;
    }

    public OccurredType getOccurred() {
        return occurred;
    }

    //Overrides
    @Override
    public String toString() {
        return ("\n" + DateFormat.getInstance().format(date.getTime()) + " " + vendor + " Debit: " + debit + " Credit: " + credit + " " + reoccurring + " " + occurred);
    }

    @Override
    public Transaction clone() throws CloneNotSupportedException {
        Transaction newTransaction;
        newTransaction = (Transaction) super.clone();
        newTransaction.date = (Calendar) this.date.clone();
        newTransaction.vendor = this.vendor;
        newTransaction.debit = this.debit;
        newTransaction.credit = this.credit;
        newTransaction.reoccurring = this.reoccurring;
        newTransaction.occurred = this.occurred;
        return newTransaction;
    }

    @Override
    public int compareTo(Object transaction) {
        Calendar compareDate = ((Transaction) transaction).getDate();
        Calendar thisDate = this.getDate();
        compareDate.set(compareDate.get(Calendar.YEAR), compareDate.get(Calendar.MONTH), compareDate.get(Calendar.DAY_OF_MONTH));
        thisDate.set(thisDate.get(Calendar.YEAR), thisDate.get(Calendar.MONTH), thisDate.get(Calendar.DAY_OF_MONTH));
        if (thisDate.before(compareDate)) {
            return -1;
        } else if (compareDate.equals(thisDate)) {
            return 0;
        } else {
            return 1;
        }
    }

    //Private Functions for Constructor
    private String clearVendorNames(String primitiveVendorNames) {
        if (primitiveVendorNames.contains("CHQ#")) {
            return "CHEQUE";
        } else {
            String[] workingCopy = primitiveVendorNames.split(" ");
            StringBuilder stringNoNumber = new StringBuilder();
            for (String aWorkingCopy : workingCopy) {
                if ((!aWorkingCopy.equals("")) && (!aWorkingCopy.matches(".*\\d.*")))
                    stringNoNumber = stringNoNumber.append(" ").append(aWorkingCopy);
            }
            if (stringNoNumber.toString().equals("")) return primitiveVendorNames;
            return stringNoNumber.toString().trim();
        }
    }
}