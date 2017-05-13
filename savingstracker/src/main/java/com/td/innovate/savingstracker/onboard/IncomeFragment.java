package com.td.innovate.savingstracker.onboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.td.innovate.savingstracker.R;
import com.td.innovate.savingstracker.TransactionsAdapter;
import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.savingstracker.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class IncomeFragment extends Fragment {
    private ListView incomeList;
    private ArrayList<Transaction> finalList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);
        finalList.clear();
        incomeList = (ListView) view.findViewById(R.id.incomeList);
        pendingAndCompleted(DataManipulation.getThisMonthReoccurringIncomeUpdated(), DataManipulation.getThisMonthOtherCredit());
        populateList();
        return view;
    }

    public interface OnFragmentInteractionListener {
    }

    private void pendingAndCompleted(ArrayList<Transaction> reoccurring, HashMap<String, ArrayList<Transaction>> other) {
        ArrayList<Transaction> pending = new ArrayList<>();
        ArrayList<Transaction> completed = new ArrayList<>();

        for (Transaction aReoccurring : reoccurring) {
            if (aReoccurring.getOccurred() == Transaction.OccurredType.ARRIVED) {
                completed.add(aReoccurring);
            } else {
                pending.add((aReoccurring));
            }
        }
        for (String vendorName : other.keySet()) {
            completed.addAll(other.get(vendorName));
        }
        if (!pending.isEmpty()) {
            Collections.sort(pending);
            finalList.add(new Transaction(Calendar.getInstance(), "PENDING", 1.0, 1.0, Transaction.ReoccurringType.NOT, Transaction.OccurredType.ARRIVED));
            finalList.addAll(pending);
        }
        if (!completed.isEmpty()) {
            Collections.sort(completed);
            finalList.add(new Transaction(Calendar.getInstance(), "COMPLETED", 2.0, 2.0, Transaction.ReoccurringType.NOT, Transaction.OccurredType.ARRIVED));
            finalList.addAll(completed);
        }
    }

    private void populateList(){
        TransactionsAdapter pendingAdapter = new TransactionsAdapter(getActivity(), finalList);
        incomeList.setAdapter(pendingAdapter);
    }
}