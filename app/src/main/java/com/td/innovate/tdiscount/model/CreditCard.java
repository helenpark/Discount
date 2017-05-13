package com.td.innovate.tdiscount.model;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zunairsyed on 2015-11-17.
 */
public class CreditCard implements Serializable {

    private String name = "";
    private Double creditLimit;
    private Double creditBalance;
    private Double annualPurchaseRate;
    private Double cashback;

    private boolean isRecommenedToUser = false;

    private Double rewards;
    private double cashbackAndRewards;

    private String rewardsCategory;


    public CreditCard(String name, Double creditLimit,
                      Double creditBalance, Double annualPurchaseRate,
                      Double cashback, Double rewards, String rewardsCategory ) {
        this.name = name;
        this.creditLimit = creditLimit;
        this.creditBalance = creditBalance;
        this.annualPurchaseRate = annualPurchaseRate;
        this.cashback = cashback;
        this.rewards = rewards;
        this.rewardsCategory = rewardsCategory;

        if(cashback!= null && rewards!=null){
            cashbackAndRewards = cashback.doubleValue() + rewards.doubleValue();
        }

    }

    public String getName() {
        return name;
    }

    public Double getAnnualPurchaseRate() {
        return annualPurchaseRate;
    }

    public Double getCreditBalance() {
        return creditBalance;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public Double getCashback() {
        return cashback;
    }

    public Double getRewards() {
        return rewards;
    }

    public String getRewardsCategory() {
        return rewardsCategory;
    }

    public double getCashbackAndRewards() {
        Log.d("IS NULL","WHAT IS VALUE OF THIS: "+cashbackAndRewards + "    AND WHAT was other stuff: "+ toString() );
        return cashbackAndRewards;
    }

    public boolean isRecommenedToUser() {
        return isRecommenedToUser;
    }

    public void setIsRecommenedToUser(boolean isRecommenedToUser) {
        this.isRecommenedToUser = isRecommenedToUser;
    }

    @Override
    public String toString() {
        return "name: " + name
                + "  anr: "+annualPurchaseRate
                + "  cashback: "+cashback;

    }
}
