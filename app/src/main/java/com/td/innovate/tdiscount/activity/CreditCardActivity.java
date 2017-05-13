package com.td.innovate.tdiscount.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.adapter.PaymentAdapter;
import com.td.innovate.tdiscount.model.CreditCard;
import com.td.innovate.tdiscount.model.Profile;
import com.td.innovate.tdiscount.service.AIService;
import com.td.mobile.controllers.LoginController;
import com.td.mobile.managers.DataManager;
import com.td.mobile.model.Account;

import java.util.ArrayList;
import java.util.Collections;

public class CreditCardActivity extends AppCompatActivity {

    private Context context;
    private Profile profile;

    boolean noCreditCards = false;
    ListView goodOptionsListview;
    ArrayList<CreditCard> minimumAPRCards;
    ArrayList<CreditCard> maximumCashBackWithMinimumAPRCreditCards;
    ArrayList<CreditCard> maxTotalRewardsCards;
    ArrayList<CreditCard> bestCreditCard;
    double minPrice;
    boolean isCategoryAvailable = false;
    String category = "groceries";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        getSupportActionBar().setTitle("Payment Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        context = this;
        profile = new Profile(this);

        if(! LoginController.isDemoDataLogin){
            if(DataManager.getInstance().getCreditAccounts().size() == 0){
                noCreditCards = true;
            }else{
                noCreditCards = false;
            }
        }

        goodOptionsListview = (ListView) findViewById(R.id.good_payment_options_offer_listview);

        //----------Testing---------//
        if (!LoginController.isDemoDataLogin  && !noCreditCards) {
            Log.d("PAYMENT Activity", " My Cash flow was: " + DataManipulation.getXMonthAgoCashFlow(2));
            Log.d("PAYMENT Activity", " My Cash flow was: " + DataManipulation.getXMonthAgoCashFlow(2));
            Log.d("PAYMENT Activity", " My Cash vendor was: " + DataManipulation.allTransactions.get(0).getVendor());

            Log.d("PAYMENT Activity", " credit card length: " + DataManager.getInstance().getCreditAccounts().size());
            Log.d("PAYMENT Activity", " credit card 0:$$   " + DataManager.getInstance().getCreditAccounts().get(0).getAccountBalance());
            Log.d("PAYMENT Activity", " credit card 0:desc   " + DataManager.getInstance().getCreditAccounts().get(0).getAccountDescription());
            Log.d("PAYMENT Activity", " credit card 0:descWithNum   " + DataManager.getInstance().getCreditAccounts().get(0).getAccountDescWithNumber());
            Log.d("PAYMENT Activity", " credit card 0:ID   " + DataManager.getInstance().getCreditAccounts().get(0).getAccountID());
            Log.d("PAYMENT Activity", " credit card 0:CD   " + DataManager.getInstance().getCreditAccounts().get(0).getAccountTypeCD());
            Log.d("PAYMENT Activity", " credit card 0:ClassCD   " + DataManager.getInstance().getCreditAccounts().get(0).getClassificationCD());
            Log.d("PAYMENT Activity", " credit card 0:Currency   " + DataManager.getInstance().getCreditAccounts().get(0).getCurrencyCD());
            Log.d("PAYMENT Activity", " credit card 0:isVisaind   " + DataManager.getInstance().getCreditAccounts().get(0).getAccountAttributes().isVISAInd());
            Log.d("PAYMENT Activity", " credit card 0:BalanceStatusCD   " + DataManager.getInstance().getCreditAccounts().get(0).getBalanceSatusCD());
            Log.d("PAYMENT Activity", " credit card 0:getAvailCredit   " + DataManager.getInstance().getCreditAccounts().get(0).getAvailableCredit());

            for (int i = 0; i < DataManager.getInstance().getCreditAccounts().size(); i++) {
                Log.d("PAYMENT Activity", " My Cash vendor was: " + DataManipulation.allTransactions.get(0).getVendor());
            }
        }
        //----------Testing---------//

        minPrice = Double.parseDouble(MainActivity.mainProduct.getOffers().get(0).getPrice());

        if(! noCreditCards || LoginController.isDemoDataLogin) {

            if (isCategoryAvailable) {
                algorithmForWithRewardsBestCreditCard();
            } else {
                algorithmForWithoutRewardsBestCreditCard();
            }

            if (isCategoryAvailable) {
                //for later
            } else {
                PaymentAdapter adapt = new PaymentAdapter(context, maximumCashBackWithMinimumAPRCreditCards);
                goodOptionsListview.setAdapter(adapt);
            }
        }else{
            Toast.makeText(context, "You don't have any credit cards to buy with", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void algorithmForWithoutRewardsBestCreditCard() {
        Log.d("AI SERVICE", "going through credit card algorithm");

        //Note: the code ain't efficient lol

        minimumAPRCards = new ArrayList<CreditCard>();
        maximumCashBackWithMinimumAPRCreditCards = new ArrayList<CreditCard>();
        Log.d(" [PAYMENT ACTIVITY] ", " PROFILE CREDIT = " + profile.getCreditCards().get(0).toString());
        ArrayList<CreditCard> creditCards;

        Log.d("AI SERVICE", "LoginController.isDemoDataLogin: " + LoginController.isDemoDataLogin);

        if (LoginController.isDemoDataLogin) {
            creditCards = profile.getCreditCards();
        } else {
            creditCards = new ArrayList<CreditCard>();

            for (Account creditAccount : DataManager.getInstance().getCreditAccounts()) {
                Log.d("AI SERVICE", "harcoded size : " + profile.getAllTDHardcodedCreditCards().size());

                CreditCard hardcodedMatchingCard = null;
                for(CreditCard harcodedCreditCard : profile.getAllTDHardcodedCreditCards()){
                    Log.d("AI SERVICE", "creditAccountName: "+creditAccount.getAccountDescription() + "   VS   " + harcodedCreditCard.getName());

                    if(harcodedCreditCard.getName().equalsIgnoreCase(creditAccount.getAccountDescription())){
                        hardcodedMatchingCard = harcodedCreditCard;
                    }
                }

                creditCards.add(new CreditCard(creditAccount.getAccountDescription(),
                        (creditAccount.getAvailableCredit() + creditAccount.getAccountBalance()),
                        creditAccount.getAccountBalance(), hardcodedMatchingCard.getAnnualPurchaseRate(), hardcodedMatchingCard.getCashback(),
                        hardcodedMatchingCard.getRewards(), hardcodedMatchingCard.getRewardsCategory()));

                Log.d("AI SERVICE", "Added fake data to real card: " + creditCards.get(0).toString());
                Log.d("AI SERVICE", "harcoded to string : "+ hardcodedMatchingCard.toString() );


            }

        }


        Log.d("AI SERVICE", "is first one null: " + (creditCards.get(0) == null));
        Log.d("AI SERVICE", "How much money left to spend in month: " + profile.getBalanceBeforePurchase());

        for (CreditCard creditCard : creditCards) {

            if (creditCard.getCreditBalance() < creditCard.getCreditLimit()) {

                if (creditCard.getCreditBalance() + minPrice  <= creditCard.getCreditLimit() +  profile.getBalanceBeforePurchase()) {

                    if (minimumAPRCards.isEmpty()) {
                        minimumAPRCards.add(creditCard);
                    } else {

                        if (minimumAPRCards.get(0).getAnnualPurchaseRate().equals(creditCard.getAnnualPurchaseRate())) {
                            minimumAPRCards.add(creditCard);
                        } else if (Math.min(creditCard.getAnnualPurchaseRate(), minimumAPRCards.get(0).getAnnualPurchaseRate()) == creditCard.getAnnualPurchaseRate()) {
                            minimumAPRCards.clear();
                            minimumAPRCards.add(creditCard);
                        }

                    }

                    Log.d("AI Service", "lenthg: " + maximumCashBackWithMinimumAPRCreditCards.size() + " current cards:  " + creditCard.toString());

                    if (maximumCashBackWithMinimumAPRCreditCards.isEmpty()) {
                        maximumCashBackWithMinimumAPRCreditCards.add(creditCard);
                    } else {

                        for (int i = 0; i < maximumCashBackWithMinimumAPRCreditCards.size(); i++) {
                            if (Math.max(creditCard.getCashback(), maximumCashBackWithMinimumAPRCreditCards.get(i).getCashback()) == creditCard.getCashback()) {
                                maximumCashBackWithMinimumAPRCreditCards.add(i, creditCard);
                                i = maximumCashBackWithMinimumAPRCreditCards.size() + 100;
                                Log.d("AI Service", "BROKEN OUT OF LOOP. STUCK IN LOOP??");
                            }
                        }

                        if (!maximumCashBackWithMinimumAPRCreditCards.contains(creditCard)) {
                            maximumCashBackWithMinimumAPRCreditCards.add(creditCard);
                            Log.d("AI Service", "added this at the end: " + creditCard.toString());

                        }

                    }
                }
            }

        }


        //We may need minimum APR for later, so don't remove right now
        //------For Testing-----//
        for (CreditCard creditCard : minimumAPRCards) {
            Log.d("AI Service", "lenthg: " + minimumAPRCards.size() + " minimumAPR cards:  " + creditCard.toString());
        }

        for (CreditCard creditCard : maximumCashBackWithMinimumAPRCreditCards) {
            Log.d("AI Service", "lenthg: " + maximumCashBackWithMinimumAPRCreditCards.size() + "  maximumCashBack:  " + creditCard.toString());
        }
        //------For Testing-----//


        for (int i = 0; i < maximumCashBackWithMinimumAPRCreditCards.size(); i++) {
            for (int j = i + 1; j < maximumCashBackWithMinimumAPRCreditCards.size(); j++) {


                Log.d("AI Service", "i card: " + maximumCashBackWithMinimumAPRCreditCards.get(i).toString()
                        + " j card:  " + maximumCashBackWithMinimumAPRCreditCards.get(j).toString());

                if (maximumCashBackWithMinimumAPRCreditCards.get(i).getCashback().doubleValue() == maximumCashBackWithMinimumAPRCreditCards.get(j).getCashback().doubleValue()) {


                    Log.d("AI Service", "got here 1");

                    if (maximumCashBackWithMinimumAPRCreditCards.get(i).getAnnualPurchaseRate().doubleValue() > maximumCashBackWithMinimumAPRCreditCards.get(j).getAnnualPurchaseRate().doubleValue()) {
                        Log.d("AI Service", "SWAPPED");

                        Collections.swap(maximumCashBackWithMinimumAPRCreditCards, i, j);
                    }
                }
            }
        }

        for (CreditCard creditCard : maximumCashBackWithMinimumAPRCreditCards) {
            creditCard.setIsRecommenedToUser(true);
            Log.d("AI Service", "lenthg: " + maximumCashBackWithMinimumAPRCreditCards.size()
                    + " GOOD ARRAY cards:  " + creditCard.toString());

        }

        for (CreditCard creditCard : creditCards) {
            if (!doesElementExistInList(maximumCashBackWithMinimumAPRCreditCards, creditCard)) {
                creditCard.setIsRecommenedToUser(false);
                maximumCashBackWithMinimumAPRCreditCards.add(creditCard);
            }
        }

    }

    /*
    We will not do with rewards for now until we confirm the attributes we can get back from td api
     */

    private void algorithmForWithRewardsBestCreditCard() {
        Log.d("AI SERVICE", "going through credit card algorithm with rewards");

        profile = AIService.profile;

        minimumAPRCards = new ArrayList<CreditCard>();
        maxTotalRewardsCards = new ArrayList<CreditCard>();

        ArrayList<CreditCard> creditCards = profile.getCreditCards();
        Log.d("AI SERVICE", "is first one null: " + (creditCards.get(0) == null));

        for (CreditCard creditCard : creditCards) {

            if (creditCard.getCreditBalance() < creditCard.getCreditLimit()) {
                Log.d("AI SERVICE", "credit card candiate1: : " + creditCard.getName());

                if (creditCard.getCreditBalance() + minPrice <= creditCard.getCreditLimit()) {
                    Log.d("AI SERVICE", "credit card candiate2: : " + creditCard.getName() + " issame 1: " + creditCard.getRewardsCategory());

                    if (category.equals(creditCard.getRewardsCategory())) {
                        Log.d("AI SERVICE", "credit card candiate3: : " + creditCard.getName());

                        if (maxTotalRewardsCards.isEmpty()) {
                            maxTotalRewardsCards.add(creditCard);
                            Log.d("AI Service", "WAS EMPTY AND ADDED credit card:  " + creditCard.toString());
                        } else {
                            Log.d("AI Service", "maxTotalRewardsCards.get(0).getCashbackAndRewards():  " + maxTotalRewardsCards.get(0).getCashbackAndRewards());
                            Log.d("AI Service", "creditCard.getCashbackAndRewards().doubleValue():  " + creditCard.getCashbackAndRewards());

                            if ((maxTotalRewardsCards.get(0).getCashbackAndRewards())
                                    == (creditCard.getCashbackAndRewards())) {
                                Log.d("AI Service", " adding this card:  " + creditCard.toString());
                                maxTotalRewardsCards.add(creditCard);
                            } else if (Math.max(creditCard.getCashbackAndRewards(), maxTotalRewardsCards.get(0).getCashbackAndRewards()) == creditCard.getCashbackAndRewards()) {
                                Log.d("AI Service", " clearing and adding this card:  " + creditCard.toString());
                                maxTotalRewardsCards.clear();
                                maxTotalRewardsCards.add(creditCard);
                            }

                        }

                    }

                }
            }

        }


        ArrayList<CreditCard> maxTotalRewardsWithMinAPRCards = new ArrayList<CreditCard>();


        for (int i = 0; i < maxTotalRewardsCards.size(); i++) {
            CreditCard creditCard = maxTotalRewardsCards.get(i);

            if (maxTotalRewardsWithMinAPRCards.isEmpty()) {
                maxTotalRewardsWithMinAPRCards.add(creditCard);
            } else {

                if (maxTotalRewardsWithMinAPRCards.get(0).getAnnualPurchaseRate().equals(creditCard.getAnnualPurchaseRate())) {
                    maxTotalRewardsWithMinAPRCards.add(creditCard);
                } else if (Math.min(creditCard.getAnnualPurchaseRate(), maxTotalRewardsWithMinAPRCards.get(0).getAnnualPurchaseRate()) == creditCard.getAnnualPurchaseRate()) {
                    maxTotalRewardsWithMinAPRCards.clear();
                    maxTotalRewardsWithMinAPRCards.add(creditCard);
                }

            }
        }


        //------For Testing-----//
        for (CreditCard creditCard : maxTotalRewardsCards) {
            Log.d("AI Service", "lenthg: " + maxTotalRewardsCards.size() + "  maximumCashBack:  " + creditCard.toString());
        }

        for (CreditCard creditCard : minimumAPRCards) {
            Log.d("AI Service", "lenthg: " + minimumAPRCards.size() + " minimumAPR cards:  " + creditCard.toString());
        }
        //------For Testing-----//


        if (!maxTotalRewardsWithMinAPRCards.isEmpty()) {
            //       bestCreditCard = maxTotalRewardsWithMinAPRCards.get(0);
            Toast.makeText(this, "BUY WITH " + bestCreditCard.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "LOL YOU CAN't afford this", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean doesElementExistInList(ArrayList<CreditCard> list, CreditCard card) {

        for (CreditCard creditCard : list) {
            Log.d("AI Service", "Comparing: " + creditCard.toString() + " With " + card.toString());

            if (card.toString().equals(creditCard.toString())) {
                Log.d("AI Service", "WAS SAME RETURN TRUE");
                return true;
            }

        }
        Log.d("AI Service", "WAS NOT SAME RETURN FALSE");

        return false;
    }
}
