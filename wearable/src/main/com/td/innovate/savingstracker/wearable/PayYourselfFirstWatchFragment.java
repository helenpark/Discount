package com.td.innovate.savingstracker.wearable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by cassiadeering on 14-12-10.
 */
public class PayYourselfFirstWatchFragment extends CardFragment {

    TextView recommendedPyfText;

    public PayYourselfFirstWatchFragment(){
        super();
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pay_yourself_first_watch, container, false);
        recommendedPyfText = (TextView) view.findViewById(R.id.recommendedPyfAmount);

        return view;
        //return super.onCreateContentView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(updateTextViews, new IntentFilter("PYF_DATA_RECEIVED")); //this.updateTextViews
    }

    private BroadcastReceiver updateTextViews = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle numbers = intent.getExtras();
            if (numbers != null) {

                String[] money =(String[]) numbers.get("NUMBERS");
                double income, expenses, pyfAmountCurrent, recommendedPyfText;
                income = Double.parseDouble(money[0]);
                expenses = Double.parseDouble(money[1]);
                pyfAmountCurrent = Double.parseDouble(money[2]);
                recommendedPyfText = income * 0.1;

                DataStorage.setIncome(income);
                DataStorage.setExpenses(expenses);
                DataStorage.setPyfAmountCurrent(pyfAmountCurrent);
                DataStorage.setPyfAmountRecommended(recommendedPyfText);

            }
            recommendedPyfText.setText("$" + DataStorage.getPyfAmountRecommended());

        }

    };

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateTextViews);
    }
}
