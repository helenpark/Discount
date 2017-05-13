package com.td.innovate.savingstracker.tracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.td.innovate.savingstracker.tracker.CashFlowTrackerDailyFragment;
import com.td.innovate.savingstracker.tracker.CashFlowTrackerMonthlyFragment;

/**
 * Created by David on 14-11-07. :)
 */
public class DailyMonthlyTrackerViewPagerAdapter extends FragmentPagerAdapter {
    public DailyMonthlyTrackerViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        if (index == 0) {
            return new CashFlowTrackerDailyFragment();
        } else {
            return new CashFlowTrackerMonthlyFragment();
        }
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}