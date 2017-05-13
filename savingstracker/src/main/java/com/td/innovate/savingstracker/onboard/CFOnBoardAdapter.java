package com.td.innovate.savingstracker.onboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by David on 14-11-12. :)
 */
public class CFOnBoardAdapter extends FragmentPagerAdapter {
    public CFOnBoardAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new TDCashFlowIntroductionFragment();
            case 1:
                return new IncomeFragment();
            case 2:
                return new ExpensesFragment();
            case 3:
                return new PYFExplanationFragment();
            case 4:
                return new LoadingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 5;
    }
}