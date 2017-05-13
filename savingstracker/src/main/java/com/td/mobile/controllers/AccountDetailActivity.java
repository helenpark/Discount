package com.td.mobile.controllers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.td.innovate.savingstracker.R;
import com.td.mobile.adapters.AccountDetailFragmentPagerAdapter;
import com.td.mobile.fragments.AccountDetailPageFragment;
import com.td.mobile.managers.DataManager;

public class AccountDetailActivity extends BaseController {
	private final static String CLASS_NAME = AccountDetailActivity.class.getName();

	private AccountDetailFragmentPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	public static final String SELECTED_ACCOUNT_INDEX = "SELECTED_ACCOUNT_INDEX";
	private int currentPageIndex ;
	private int defaultStartIndex = 0;

	public AccountDetailActivity(){}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_detail);

		populateDrawerMenu();
				
		currentPageIndex = getIntent().getIntExtra(AccountDetailActivity.SELECTED_ACCOUNT_INDEX,defaultStartIndex);

		setActionBarCustomView(getResources().getString(R.string.action_bar_title_account_detail), true);

		init();
	}

	protected void init(){

		mViewPager = (ViewPager) findViewById(R.id.account_detail_view_pager);

		DataManager.getInstance().getAccountsDetail().clear();
		mPagerAdapter = new AccountDetailFragmentPagerAdapter(getFragmentManager(), mViewPager);

		for(int i=0; i< DataManager.getInstance().getFilteredAccounts().size();i++){
			Bundle args =new Bundle();
			args.putInt(AccountDetailPageFragment.INDEX, i);
			Fragment frg = Fragment.instantiate(this, AccountDetailPageFragment.class.getName(),args );
			mPagerAdapter.addPage((AccountDetailPageFragment)frg);
		}

		mPagerAdapter.selectPageToDisplay(currentPageIndex);
	}
	
	public ViewPager getViewPager() {
		return mViewPager;
	}






}
