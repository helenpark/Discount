package com.td.innovate.savingstracker.wearable;

import android.content.Context;

/**
 * Created by cassiadeering on 14-12-09.
 */


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.ImageReference;
import android.view.Gravity;

/**
 * Constructs fragments as requested by the GridViewPager. For each row a
 * different background is provided.
 */
public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;

    public SampleGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
    }

    static final int[] BG_IMAGES = new int[] {
            R.drawable.web_link_icon,
//            R.drawable.debug_background_2,
//            R.drawable.debug_background_3,
//            R.drawable.debug_background_4,
//            R.drawable.debug_background_5
    };

    /** A simple container for static data in each page */
    private static class Page {
        int titleRes;
        int textRes;
        int iconRes;
        int cardGravity = Gravity.BOTTOM;
        boolean expansionEnabled = true;
        float expansionFactor = 1.0f;
        int expansionDirection = CardFragment.EXPAND_DOWN;

        public Page(boolean expansionEnabled, float expansionFactor) {
            this.expansionEnabled = expansionEnabled;
            this.expansionFactor = expansionFactor;
        }
        public Page(int titleRes, int textRes, boolean expansion) {
            this(titleRes, textRes, 0);
            this.expansionEnabled = expansion;
        }

        public Page(int titleRes, int textRes, boolean expansion, float expansionFactor) {
            this(titleRes, textRes, 0);
            this.expansionEnabled = expansion;
            this.expansionFactor = expansionFactor;
        }

        public Page(int titleRes, int textRes, int iconRes) {
            this.titleRes = titleRes;
            this.textRes = textRes;
            this.iconRes = iconRes;

        }



//        public Page(int titleRes, int textRes, int iconRes, int gravity) {
//            this.titleRes = titleRes;
//            this.textRes = textRes;
//            this.iconRes = iconRes;
//            this.cardGravity = gravity;
//        }


    }

    private final Page[][] PAGES = {
            {
//                    new Page(R.string.monthly_cash_flow, R.string.monthly_cash_flow, R.drawable.web_link_icon,
//                            Gravity.CENTER_VERTICAL),
//                    new Page(true, 10),
//                    new Page(true, 10),
//                    new Page(true, 10),
//                    new Page(true, 10),
                    new Page(true, 10),
                    new Page(true, 10),
                    new Page(true, 10)

            }
    };

    @Override
    public Fragment getFragment(int row, int col) {
        Page page = PAGES[row][col];
//        String title = page.titleRes != 0 ? mContext.getString(page.titleRes) : null;
//        String text = page.textRes != 0 ? mContext.getString(page.textRes) : null;
        //CardFragment fragment = CircularBarWatchFragment.create(title, text, page.iconRes);
        CardFragment fragment = null;

        switch (col) {

            case 0:
//                fragment.setArguments(this);

//                fragment = new HorizontalBarWatchFragment();
                fragment = new CircularBarWatchFragment();
                break;
            case 1:
//                fragment = new PayYourselfFirstWatchFragment();
                fragment = new HorizontalBarWatchFragment();
                break;
            case 2:
                fragment = new PayYourselfFirstWatchFragment();
                break;
//            case 2:
//                fragment = new CircularBarWatchFragment();
//                break;
//            case 3:
//                fragment = new HorizontalBarWatchFragment();
//                break;
//            case 4:
//                fragment = new PayYourselfFirstWatchFragment();
//                break;
//            case 5:
//                fragment = new CustomCardFrameFourthScreen();
//                break;
            default:
                //fragment = new CircularBarWatchFragment();
                break;

        }
        // Advanced settings
        fragment.setCardGravity(page.cardGravity);
        fragment.setExpansionEnabled(page.expansionEnabled);
        fragment.setExpansionDirection(page.expansionDirection);
        fragment.setExpansionFactor(page.expansionFactor);
        return fragment;
    }



    @Override
    public ImageReference getBackground(int row, int column) {
        return ImageReference.forDrawable(BG_IMAGES[row % BG_IMAGES.length]);
    }

    @Override
    public int getRowCount() {
        return PAGES.length;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return PAGES[rowNum].length;
    }




}
