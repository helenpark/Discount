package com.td.innovate.tdiscount.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.td.innovate.tdiscount.fragment.ListViewFragment;
import com.td.innovate.tdiscount.fragment.ReviewFragment;
import com.td.innovate.tdiscount.fragment.StoreFragment;

/**
 * Created by zunairsyed on 2015-11-16.
 */


public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return  StoreFragment.newInstance(position);
        } else if (position == 1) {
            return  ReviewFragment.newInstance(position);
        }
        else {
            return ListViewFragment.newInstance(1);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Offers";
            case 1:
                return "Reviews";
        }
        return "";
    }
}



