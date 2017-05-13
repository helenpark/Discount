package com.td.innovate.savingstracker.tracker;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.td.innovate.savingstracker.HoloCircularProgressBar;
import com.td.innovate.savingstracker.R;
import com.td.innovate.savingstracker.DataManipulation;

import java.util.Calendar;
import java.util.Locale;

public class CashFlowTrackerDailyFragment extends Fragment {
    private static final Typeface DEFAULT_FONT = Typeface.create("sans-serif-light", Typeface.NORMAL);
    private HoloCircularProgressBar mHoloCircularProgressBar;
    private ObjectAnimator mProgressBarAnimator;
    private TextView dailyRemaining;
    private TextView dailyExpenseTotalText;
    private TextView dailyRemainingText;
    private ProgressBar expenseProgressBar;
    private LinearLayout expenseLay;
    private double dailyAllowanceAmount, dailyExpenseTotal,dailyRemainingAmount;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cash_flow_tracker_daily, container, false);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                double totalCredit = DataManipulation.getThisMonthReoccurringIncomeUpdatedAmount() + DataManipulation.getThisMonthOtherCreditAmount();
                double totalDebit = DataManipulation.getThisMonthReoccurringExpenseUpdatedAmount() + DataManipulation.getThisMonthOtherDebitAmount() + DataManipulation.getPYFAmount() - DataManipulation.getTodayTransactionAmount();
                dailyAllowanceAmount = (totalCredit - totalDebit) / (DataManipulation.getNow().getMaximum(Calendar.DAY_OF_MONTH) - DataManipulation.getNow().get(Calendar.DAY_OF_MONTH) + 1);
                dailyExpenseTotal = Math.abs(DataManipulation.getTodayTransactionAmount());
                dailyRemainingAmount = dailyAllowanceAmount - dailyExpenseTotal;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initializeViewElements();
                        initializingDailyTracker();
                        initializeBar();
                    }
                });
            }
        });
        worker.start();
    }

    private void initializeViewElements() {
        dailyRemaining = (TextView) view.findViewById(R.id.dailyRemaining);
        mHoloCircularProgressBar = (HoloCircularProgressBar) view.findViewById(R.id.holoCircularProgressBar);
        dailyExpenseTotalText = (TextView) view.findViewById(R.id.expenseNum);
        dailyRemainingText = (TextView) view.findViewById(R.id.dailyRemainingText);
        expenseProgressBar = (ProgressBar) view.findViewById(R.id.dailyExpenseProgressBar);
        expenseLay = (LinearLayout) view.findViewById(R.id.expenseBar);
    }

    private void initializingDailyTracker() {
        onClickEvents();

        dailyRemaining.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 38);
        dailyRemaining.setTypeface(DEFAULT_FONT);

        if (mProgressBarAnimator != null) {
            mProgressBarAnimator.cancel();
        }

        if (dailyAllowanceAmount <= 0){
            mHoloCircularProgressBar.setProgressColor(getResources().getColor(R.color.progress_red));
            mHoloCircularProgressBar.setProgressBackgroundColor(getResources().getColor(R.color.progress_red));
            dailyRemaining.setTextColor(this.getResources().getColor(R.color.progress_red));
            dailyRemaining.setText("-$" + String.format("%.0f", Math.abs(dailyAllowanceAmount)) + "/-$" + String.format("%.0f", Math.abs(dailyRemainingAmount)));
            dailyRemainingText.setTextColor(getResources().getColor(R.color.progress_red));
            dailyRemaining.setTextSize(32);
            dailyRemainingText.setText("BAD ALLOWANCE/OVER BUDGET\nFOR  " + (String.format(Locale.CANADA, "%tb", DataManipulation.getNow()) + " " +
                    String.format(Locale.CANADA, "%te", DataManipulation.getNow())).toUpperCase());
            dailyRemainingText.setGravity(Gravity.CENTER_HORIZONTAL);
            dailyExpenseTotalText.setText("-$" + String.format("%.0f", dailyExpenseTotal));
        }
        else if (dailyRemainingAmount < 0) {
            mHoloCircularProgressBar.setProgressColor(getResources().getColor(R.color.progress_red));
            mHoloCircularProgressBar.setProgressBackgroundColor(getResources().getColor(R.color.progress_green));
            dailyRemainingText.setTextColor(getResources().getColor(R.color.progress_red));
            dailyRemainingText.setText((String.format(Locale.CANADA, "%tb", DataManipulation.getNow()) + " " +
                    String.format(Locale.CANADA, "%te", DataManipulation.getNow())).toUpperCase() + "  OVER BUDGET");
            dailyRemaining.setText("-$" + String.format("%.0f", Math.abs(dailyRemainingAmount)));
            dailyRemaining.setTextColor(getResources().getColor(R.color.progress_red));
            dailyExpenseTotalText.setText("-$" + String.format("%.0f", dailyExpenseTotal));
            animate(mHoloCircularProgressBar, null, (float) (dailyExpenseTotal / dailyAllowanceAmount), 1000);
            mHoloCircularProgressBar.setMarkerProgress(0f);
        } else {
            mHoloCircularProgressBar.setProgressColor(getResources().getColor(R.color.progress_red));
            mHoloCircularProgressBar.setProgressBackgroundColor(getResources().getColor(R.color.progress_green));
            dailyRemaining.setTextColor(getResources().getColor(R.color.progress_green));
            dailyRemaining.setText("$" + String.format("%.0f", dailyRemainingAmount));
            dailyRemainingText.setText((String.format(Locale.CANADA, "%tb", DataManipulation.getNow()) + " " +
                    String.format(Locale.CANADA, "%te", DataManipulation.getNow())).toUpperCase() + "  REMAINING");
            dailyRemainingText.setTextColor(getResources().getColor(R.color.progress_green));
            dailyExpenseTotalText.setText("-$" + String.format("%.0f", dailyExpenseTotal));
            animate(mHoloCircularProgressBar, null, (float) (dailyExpenseTotal / dailyAllowanceAmount), 1000);
            mHoloCircularProgressBar.setMarkerProgress(0f);
        }
    }


    private void animate(final HoloCircularProgressBar progressBar, final Animator.AnimatorListener listener,
                         final float progress, final int duration) {

        ObjectAnimator mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);

        mProgressBarAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                progressBar.setProgress(progress);
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
        progressBar.setMarkerProgress(progress);
        mProgressBarAnimator.start();
    }

    private void initializeBar() {
        expenseProgressBar.setMax((int) Math.abs(dailyAllowanceAmount));
        expenseProgressBar.setProgress((int) Math.abs(dailyExpenseTotal));
    }

    private void onClickEvents() {

        expenseLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = new Intent(getActivity(), IncomeExpenseListActivity.class);
                launch.putExtra("Income or Expense", 2);
                startActivity(launch);
            }
        });
    }

    public interface OnFragmentInteractionListener {
    }
}