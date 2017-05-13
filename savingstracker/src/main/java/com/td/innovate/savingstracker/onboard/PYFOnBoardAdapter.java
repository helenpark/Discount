package com.td.innovate.savingstracker.onboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.td.innovate.savingstracker.onboard.LoadingFragment;
import com.td.innovate.savingstracker.onboard.PYFExplanationFragment;

/**
 * Created by David on 14-11-20. :)
 */
public class PYFOnBoardAdapter extends FragmentPagerAdapter {
    public PYFOnBoardAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new PYFExplanationFragment();
            case 1:
                return new LoadingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}