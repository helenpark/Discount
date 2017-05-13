package com.td.mobile.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.td.mobile.fragments.AccountDetailPageFragment;
import com.td.mobile.model.Account;
import com.td.mobile.utils.Consts;
import com.td.mobile.utils.TDLog;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class AccountDetailFragmentPagerAdapter extends FragmentPagerAdapter implements OnPageChangeListener {

	private final static String CLASS_NAME = AccountDetailFragmentPagerAdapter.class.getName();
	private ArrayList<AccountDetailPageFragment> mFragments = new ArrayList<AccountDetailPageFragment>();
	private ViewPager viewPager;
	private Handler handler;
	private ArrayList<Semaphore> pageLocks = new ArrayList<Semaphore>();

	public AccountDetailFragmentPagerAdapter(FragmentManager fm, ViewPager viewPager) {
		super(fm);
		this.viewPager = viewPager;
		handler = new Handler();
		viewPager.setAdapter(this);
		viewPager.setOnPageChangeListener(this);
	}
	
	public void selectPageToDisplay(int page) {
		//Create this method because onPageChangeListener doesn't report page 0
		viewPager.setCurrentItem(page);
		if (page == 0)
			onPageSelected(0);
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}
	
	public Account getAccountByPosition(int position) {
		return mFragments.get(position).getAccount();
	}
	
	@Override
	public int getCount() {
		return mFragments.size();
	}


	public void addPage(AccountDetailPageFragment fragment) {
		mFragments.add(fragment);
		pageLocks.add(new Semaphore(1));
		notifyDataSetChanged();

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_DRAGGING)
			TDLog.d(CLASS_NAME, "onPageScrollStateChanged = SCROLL_STATE_DRAGGING");
		else if (state == ViewPager.SCROLL_STATE_IDLE)
			TDLog.d(CLASS_NAME, "onPageScrollStateChanged = SCROLL_STATE_IDLE");
		else  if (state == ViewPager.SCROLL_STATE_SETTLING)
			TDLog.d(CLASS_NAME, "onPageScrollStateChanged = SCROLL_STATE_SETTLING");
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		TDLog.d(CLASS_NAME, String.format("onPageSelected(%s)", position));
		final int pos = position;
		if(pageLocks.get(pos).tryAcquire()) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					try {
						if(mFragments.get(pos).isResumed() && viewPager.getCurrentItem() == pos) {
								mFragments.get(pos).buildUI();
						}
					}
					finally {
						pageLocks.get(pos).release();
					}
				}}, Consts.PAGE_SELECTION_DELAY);
		}
	}    
}
