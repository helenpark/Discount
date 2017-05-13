package com.td.innovate.savingstracker.tracker;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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

import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.savingstracker.HoloCircularProgressBar;
import com.td.innovate.savingstracker.R;
import com.td.innovate.savingstracker.onboard.OnBoardActivity;
import com.td.innovate.savingstracker.pyf.PYFSetupActivity;

import java.util.Locale;

public class CashFlowTrackerMonthlyFragment extends Fragment {
    private TextView projectCF;
    private ProgressBar incomesProgressBar, expensesProgressBar, pyfProgressBar, totalProgressBar;
    private TextView incomesAmount, expensesAmount, pyfAmount, totalAmount, amountInfo;
    private LinearLayout incomesLay, expensesLay, pyfLay;
    private HoloCircularProgressBar mHoloCircularProgressBar;
    private static final Typeface DEFAULT_FONT = Typeface.create("sans-serif-light", Typeface.NORMAL);
    private ObjectAnimator myProgressBarAnimator;
    private View view;
    private double incomeTotal;
    private double expenseTotal;
    private double PYFTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cash_flow_tracker_monthly, container, false);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        incomeTotal = DataManipulation.getThisMonthReoccurringIncomeUpdatedAmount() + DataManipulation.getThisMonthOtherCreditAmount();
        expenseTotal = DataManipulation.getThisMonthReoccurringExpenseUpdatedAmount() + DataManipulation.getThisMonthOtherDebitAmount();
        PYFTotal = DataManipulation.getPYFAmount();
        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initializeViewElements();
                        initializeMonthlyTracker();
                        initializeTextViewAmount();
                        initializeHorizontalBar();
                    }
                });
            }
        });
        worker.start();
    }

    private void initializeViewElements() {
        incomesProgressBar = (ProgressBar) view.findViewById(R.id.incomesProgressBar);
        expensesProgressBar = (ProgressBar) view.findViewById(R.id.expensesProgressBar);
        pyfProgressBar = (ProgressBar) view.findViewById(R.id.pyfProgressBar);
        totalProgressBar = (ProgressBar) view.findViewById(R.id.totalProgressBar);
        incomesAmount = (TextView) view.findViewById(R.id.incomesAmount);
        expensesAmount = (TextView) view.findViewById(R.id.expensesAmount);
        totalAmount = (TextView) view.findViewById(R.id.totalAmount);
        amountInfo = (TextView) view.findViewById(R.id.monthlyRemainingText);
        incomesLay = (LinearLayout) view.findViewById(R.id.incomeBar);
        expensesLay = (LinearLayout) view.findViewById(R.id.expensesBar);
        pyfLay = (LinearLayout) view.findViewById(R.id.pyfBar);
        pyfAmount = (TextView) view.findViewById(R.id.pyfAmount);
        projectCF = (TextView) view.findViewById(R.id.projectedCF);
        mHoloCircularProgressBar = (HoloCircularProgressBar) view.findViewById(R.id.progressBar);
        if (PYFTotal == 0) {
            Drawable icAdd = getActivity().getResources().getDrawable(R.drawable.ic_add);
            icAdd.setBounds(0, 0, 50, 50);
            pyfAmount.setTextColor(getResources().getColor(R.color.middle_grey));
            pyfAmount.setCompoundDrawables(null, null, icAdd, null);
        } else {
            pyfAmount.setTextColor(getResources().getColor(R.color.progress_yellow));
            pyfAmount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0);
        }
    }

    private void initializeTextViewAmount() {
        incomesAmount.setText("$" + String.format("%,d", (int) incomeTotal));
        expensesAmount.setText("-$" + String.format("%,d", (int) expenseTotal));
        pyfAmount.setText("-$" + String.format("%,d", (int) PYFTotal));
        totalAmount.setText("$" + String.format("%,d", (int) (incomeTotal - expenseTotal - PYFTotal)));
        // used to determine month name
        String monthName = String.format(Locale.CANADA, "%tB", DataManipulation.getNow()).toUpperCase();
        amountInfo.setText(monthName + "\n" + "CASH FLOW");
        amountInfo.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void initializeMonthlyTracker() {
        onClickEvents();
        projectCF.setText("$" + String.format("%,d", (int) (incomeTotal - expenseTotal - PYFTotal)));
        projectCF.setTypeface(DEFAULT_FONT);
        projectCF.setTextColor(this.getResources().getColor(R.color.progress_green));
        projectCF.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 38);

        if (myProgressBarAnimator != null) {
            myProgressBarAnimator.cancel();
        }

        mHoloCircularProgressBar.setMarkerColor(getResources().getColor(R.color.progress_yellow));
        mHoloCircularProgressBar.setMarkerProgress((float) (PYFTotal / incomeTotal));

        mHoloCircularProgressBar.setProgressColor(getResources().getColor(R.color.progress_red));
        mHoloCircularProgressBar.setProgressBackgroundColor(this.getResources().getColor(R.color.progress_green));

        animate(mHoloCircularProgressBar, null, (float) ((expenseTotal + PYFTotal) / incomeTotal), (float) (PYFTotal / incomeTotal), 1000);
    }

    private void onClickEvents() {

        incomesLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = new Intent(getActivity(), IncomeExpenseListActivity.class);
                launch.putExtra("Income or Expense", 1);
                startActivity(launch);
            }
        });

        expensesLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = new Intent(getActivity(), IncomeExpenseListActivity.class);
                launch.putExtra("Income or Expense", 0);
                startActivity(launch);
            }
        });

        pyfLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PYFTotal == 0) {
                    Intent onBoard = new Intent(getActivity(), OnBoardActivity.class);
                    onBoard.putExtra("To OnBoard", "From Tracker");
                    startActivity(onBoard);
                } else {
                    Intent setPYF = new Intent(getActivity(), PYFSetupActivity.class);
                    startActivity(setPYF);
                }
            }
        });
    }

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

    //set progress bar, progress for Income, Expense, and PYF
    private void initializeHorizontalBar() {
        incomesProgressBar.setMax((int) incomeTotal);
        incomesProgressBar.setProgress((int) (incomeTotal));

        expensesProgressBar.setMax((int) incomeTotal);
        expensesProgressBar.setProgress((int) expenseTotal);

        pyfProgressBar.setMax((int) incomeTotal);
        pyfProgressBar.setProgress((int) PYFTotal);

        totalProgressBar.setMax((int) incomeTotal);
        totalProgressBar.setProgress((int) (incomeTotal - expenseTotal - PYFTotal));
    }

    public interface OnFragmentInteractionListener {

    }
}