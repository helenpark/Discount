package com.td.innovate.savingstracker.wearable;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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

import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

/**
 * Created by Cassia Deering on 14-12-10. :)
 */
public class CircularBarWatchFragment extends CardFragment {
    TextView cash_flow_text;
    HoloCircularProgressBar mHoloCircularProgressBar;

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circular_bar_watch, container, false);
        cash_flow_text = (TextView) view.findViewById(R.id.dailyRemaining);
        mHoloCircularProgressBar = (HoloCircularProgressBar) view.findViewById(R.id.watchCashFlowCircle);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(updateTextViews, new IntentFilter("PYF_DATA_RECEIVED"));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateTextViews);
    }

    private BroadcastReceiver updateTextViews = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double income, expenses, pyfAmountCurrent, cashFlow, otherCredit, otherDebit;
            Bundle numbers = intent.getExtras();
            if (numbers != null) {
                String[] money = (String[]) numbers.get("NUMBERS");
                income = Double.parseDouble(money[0]);
                expenses = Double.parseDouble(money[1]);
                pyfAmountCurrent = Double.parseDouble(money[2]);
                otherCredit = Double.parseDouble(money[3]);
                otherDebit = Double.parseDouble(money[4]);
                cashFlow = income - expenses - otherDebit + otherCredit - pyfAmountCurrent;
                DataStorage.setIncome(income);
                DataStorage.setExpenses(expenses);
                DataStorage.setPyfAmountCurrent(pyfAmountCurrent);
                DataStorage.setCashFlow(cashFlow);

                cash_flow_text.setText("$" + (int)cashFlow);
                mHoloCircularProgressBar.setMarkerColor(getResources().getColor(R.color.progress_yellow));
                mHoloCircularProgressBar.setMarkerProgress((float) (pyfAmountCurrent / (income + otherCredit)));
                mHoloCircularProgressBar.setProgressColor(getResources().getColor(R.color.progress_red));
                mHoloCircularProgressBar.setProgressBackgroundColor(getResources().getColor(R.color.progress_green));
                animate(mHoloCircularProgressBar, null, (float) ((expenses + otherDebit + pyfAmountCurrent) / (income + otherCredit)), (float) (pyfAmountCurrent / (income + otherCredit)), 1000);
            }
        }
    };

    private void animate(final HoloCircularProgressBar progressBar, final Animator.AnimatorListener listener, final float progress, final float markerProgress, final int duration) {

        ObjectAnimator mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);

        mProgressBarAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                progressBar.setProgress(progress);
                progressBar.setMarkerProgress(markerProgress);
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
            }

            @Override
            public void onAnimationStart(final Animator animation) {
            }
        });
        if (listener != null) {
            mProgressBarAnimator.addListener(listener);
        }
        mProgressBarAnimator.reverse();
        mProgressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        mProgressBarAnimator.start();
    }
}