package com.td.innovate.savingstracker.onboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.td.innovate.savingstracker.pyf.PYFSetupActivity;
import com.td.innovate.savingstracker.R;
import com.td.innovate.savingstracker.tracker.SummaryActivity;

public class OnBoardActivity extends FragmentActivity implements
        TDCashFlowIntroductionFragment.OnFragmentInteractionListener, IncomeFragment.OnFragmentInteractionListener,
        ExpensesFragment.OnFragmentInteractionListener, LoadingFragment.OnFragmentInteractionListener,
        PYFExplanationFragment.OnFragmentInteractionListener{
    private View CFIntro,income,expense,pyf;
    private ViewPager onBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        String flagString = getIntent().getStringExtra("To OnBoard");
        onBoard = (ViewPager) findViewById(R.id.onBoard);
        if (flagString.equals("From Login")) {
            findViewById(R.id.CFOnBoardProgressIndicator).setVisibility(View.VISIBLE);
            CFIntro = findViewById(R.id.CFIntro);
            income = findViewById(R.id.income);
            expense = findViewById(R.id.expense);
            pyf = findViewById(R.id.pyf);
            initializeCFOnBoardViewPager();
        } else if(flagString.equals("From Tracker")) {
            findViewById(R.id.CFOnBoardProgressIndicator).setVisibility(View.INVISIBLE);
            initializePYFOnBoardViewPager();
        }
    }

    private void initializeCFOnBoardViewPager() {
        CFIntro.setBackground(getResources().getDrawable(R.drawable.focus_dot_green));
        income.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
        expense.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
        pyf.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
        CFOnBoardAdapter myAdapter = new CFOnBoardAdapter(this.getSupportFragmentManager());
        onBoard.setAdapter(myAdapter);

        //on swiping the viewpager make respective tab selected
        onBoard.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    CFIntro.setBackground(getResources().getDrawable(R.drawable.focus_dot_green));
                    income.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    expense.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    pyf.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                }
                if (position == 1) {
                    CFIntro.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    income.setBackground(getResources().getDrawable(R.drawable.focus_dot_green));
                    expense.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    pyf.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                }
                if (position == 2) {
                    CFIntro.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    income.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    expense.setBackground(getResources().getDrawable(R.drawable.focus_dot_green));
                    pyf.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                }
                if (position == 3) {
                    CFIntro.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    income.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    expense.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    pyf.setBackground(getResources().getDrawable(R.drawable.focus_dot_green));
                }
                if (position == 4) {
                    expense.setBackground(getResources().getDrawable(R.drawable.focus_dot_grey));
                    Intent tracker = new Intent(OnBoardActivity.this, SummaryActivity.class);
                    startActivity(tracker);
                    finish();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void initializePYFOnBoardViewPager() {
        PYFOnBoardAdapter myAdapter = new PYFOnBoardAdapter(this.getSupportFragmentManager());
        onBoard.setAdapter(myAdapter);

        //on swiping the viewpager make respective tab selected
        onBoard.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Intent PYF = new Intent(OnBoardActivity.this, PYFSetupActivity.class);
                    startActivity(PYF);
                    finish();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

}