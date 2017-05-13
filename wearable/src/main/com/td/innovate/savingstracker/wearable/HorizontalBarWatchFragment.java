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
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Cassia Deering on 14-12-10. :)
 */
public class HorizontalBarWatchFragment extends CardFragment {
    TextView income_text;
    TextView expenses_text;
    TextView pyf_text;
    TextView cash_flow_text;

    ProgressBar incomeBar;
    ProgressBar expensesBar;
    ProgressBar pyfBar;
    ProgressBar cashFlowBar;

    public HorizontalBarWatchFragment() {
        super();
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_horizontal_bar_watch, container, false);

        income_text = (TextView) view.findViewById(R.id.income_text);
        pyf_text = (TextView) view.findViewById(R.id.pyf_text);
        expenses_text = (TextView) view.findViewById(R.id.expenses_text);
        cash_flow_text = (TextView) view.findViewById(R.id.cash_flow_text);

        incomeBar = (ProgressBar) view.findViewById(R.id.incomeProgressBar);
        expensesBar = (ProgressBar) view.findViewById(R.id.expensesProgressBar);
        cashFlowBar = (ProgressBar) view.findViewById(R.id.cashFlowProgressBar);
        pyfBar = (ProgressBar) view.findViewById(R.id.pyfProgressBar);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(updateTextViews, new IntentFilter("PYF_DATA_RECEIVED")); //this.updateTextViews
    }

    private BroadcastReceiver updateTextViews = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double income, expenses, otherCredit, otherDebit, pyfAmountCurrent, cashFlow;

            Bundle numbers = intent.getExtras();
            if (numbers != null) {

                String[] money =(String[]) numbers.get("NUMBERS");

                income = Double.parseDouble(money[0]);
                expenses = Double.parseDouble(money[1]);
                pyfAmountCurrent = Double.parseDouble(money[2]);
                otherCredit = Double.parseDouble(money[3]);
                otherDebit = Double.parseDouble(money[4]);
                cashFlow = income - expenses - otherDebit + otherCredit - pyfAmountCurrent;
                expenses = expenses + otherDebit;

                DataStorage.setIncome(income);
                DataStorage.setExpenses(expenses);
                DataStorage.setPyfAmountCurrent(pyfAmountCurrent);
                DataStorage.setCashFlow(cashFlow);

//                Log.d("CCFSS", "expenses " + DataStorage.getExpenses());
//                Log.d("CCFSS", "income " + DataStorage.getIncome());
//                Log.d("CCFSS", "otherDebit " + otherDebit);
//                Log.d("CCFSS", "otherCredit " + otherCredit);
//                Log.d("CCFSS", "pyfAmtCurrent " + pyfAmountCurrent);

            }

            income_text.setText("$" + DataStorage.getIncome());
            expenses_text.setText("$" + DataStorage.getExpenses());
            pyf_text.setText("$" + DataStorage.getPyfAmountCurrent());
            cash_flow_text.setText("$" + DataStorage.getCashFlow());

            incomeBar.setMax((int) DataStorage.getIncome());
            incomeBar.setProgress((int) (DataStorage.getIncome()));

            expensesBar.setMax((int) DataStorage.getIncome());
            expensesBar.setProgress( (int) DataStorage.getExpenses());

            pyfBar.setMax((int) DataStorage.getIncome());
            pyfBar.setProgress((int) DataStorage.getPyfAmountCurrent());

            cashFlowBar.setMax((int) DataStorage.getIncome());
            cashFlowBar.setProgress((int)(DataStorage.getIncome() - DataStorage.getExpenses() - DataStorage.getPyfAmountCurrent()));
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateTextViews);
    }
}



