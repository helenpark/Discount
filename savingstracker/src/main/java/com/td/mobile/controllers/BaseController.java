package com.td.mobile.controllers;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.innovate.savingstracker.R;
//import com.td.mobile.contacts.ContactsActivity;
//import com.td.mobile.etransfers.ETransferSendActivity;
import com.td.mobile.helpers.NavDrawerItem;
//import com.td.mobile.nextgen.bills.BillsLandingPageActivity;
//import com.td.mobile.nextgen.bills.PayBillsFragmentActivity;
//import com.td.mobile.nextgen.bills.UpcomingBillsFragmentActivity;
//import com.td.mobile.nextgen.locations.activities.FindLocationsActivity;
import com.td.mobile.nextgen.restful.Session;
//import com.td.mobile.nextgen.transfers.TransfersLandingPageActivity;
import com.td.mobile.nextgen.view.HeaderErrorViewFragment;
import com.td.mobile.nextgen.view.LogoutAlertDialog;
import com.td.mobile.utils.Utils;

import java.util.ArrayList;

public abstract class BaseController extends Activity {

	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	protected ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<NavDrawerItem> navDrawerItems;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	// slide menu items


	HeaderErrorViewFragment mErrorFragment;

	public String headerText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		headerText = getStringResourceByName("headerErrorText");

	}

	protected int getActionBarCustomViewResource(){
		return R.layout.actionbar_custom_view;
	}

	public void populateDrawerMenu() {
		// load slide menu items
		createNavigationDrawerItems();
		final ActionBar actionBar = getActionBar();
		actionBar.setCustomView(getActionBarCustomViewResource());

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);

		/*		if(!Session.getInstance().isUserLoggedIn()){
			View view = actionBar.getCustomView();
			ImageView img = (ImageView) view.findViewById(R.id.actionBarLogout);
			img.setVisibility(View.INVISIBLE);
		}*/

		mTitle = mDrawerTitle = getTitle();


		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		// setting the nav drawer list adapter

		mDrawerList.setAdapter(new FlyoutNavigationAdapter(this,
				R.layout.drawer_list_item, navDrawerItems));


		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

//		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//				R.drawable.ic_drawer, // nav menu toggle icon
//				R.string.app_name, // nav drawer open - description for
//				// accessibility
//				R.string.app_name // nav drawer close - description for
//				// accessibility
//				) {
//			public void onDrawerClosed(View view) {
//				getActionBar().setTitle(mTitle);
//				// calling onPrepareOptionsMenu() to show action bar
//				// icons
//				invalidateOptionsMenu();
//			}
//
//			public void onDrawerOpened(View drawerView) {
//				getActionBar().setTitle(mDrawerTitle);
//				// calling onPrepareOptionsMenu() to hide action bar
//				// icons
//				invalidateOptionsMenu();
//			}
//		};
//		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private class FlyoutNavigationAdapter extends ArrayAdapter<NavDrawerItem> {
		private ArrayList<NavDrawerItem> navItems;
		private int viewResourceID;

		public FlyoutNavigationAdapter(Context context, int viewResourceID, ArrayList<NavDrawerItem> navItems) {
			super(context, viewResourceID, navItems);

			this.navItems = navItems;
			this.viewResourceID = viewResourceID;
		}


		@Override
		public int getCount() {
			return navItems.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View navItemView = inflater.inflate(viewResourceID, parent, false);


			TextView textView = (TextView)( navItemView.findViewById(R.id.navText));
			textView.setText(navItems.get(position).getTitle());
			if (position == 0) {
				textView.setTypeface(null, Typeface.BOLD);
			}

			if (navItems.get(position).getIcon() != null) {
				ImageView imgView = (ImageView)( navItemView.findViewById(R.id.img));
				imgView.setImageDrawable(getResources().getDrawable(navItems.get(position).getIcon()));
				imgView.setVisibility(View.VISIBLE);
			}

			navItemView.setBackgroundColor(getResources().getColor(navItems.get(position).getBackgroundColor()));

			//KY- Will rewrite this code to separate menu items into sections
			/*
			if (position == 6 || position == 7 || position == 11) {
				View line = navItemView.findViewById(R.id.separator);
				line.setVisibility(View.VISIBLE);
			}
			 */

			return navItemView;
		}
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		ActionBar actionBar = getActionBar();
//		View view = actionBar.getCustomView();
//		ImageView img = (ImageView) view.findViewById(R.id.actionBarLogout);
//
//		if(!Session.getInstance().isUserLoggedIn()){
//			img.setVisibility(View.INVISIBLE);
//		}
//		else{
//			img.setVisibility(View.VISIBLE);
//		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void setActionBarCustomView(String title ,boolean backButton) {

		ActionBar actionBar = getActionBar();
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.actionbar_function_nav, null);
		TextView titleTxt = (TextView) view.findViewById(R.id.title);
		ImageView logout = (ImageView) view.findViewById(R.id.actionBarLogout);

		if (titleTxt != null) {
			titleTxt.setText(title);
		}
		if(!Session.getInstance().isUserLoggedIn())
			logout.setVisibility(View.INVISIBLE);

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(view);
	}

	private ArrayList<NavDrawerItem> createNavigationDrawerItems() {
		String[] mNavMenuTitles = getResources()
				.getStringArray(R.array.nav_drawer_items);

		navDrawerItems = new ArrayList<NavDrawerItem>(); 
		int i=0;
//
//		//Home
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i], R.drawable.home, MainController.class, false, R.color.flyout_home));
//
//		//Accounts
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+1], null, AccountsSummaryController.class, R.color.flyout_banking));
//
//		//Transfers
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+2], null, TransfersLandingPageActivity.class, R.color.flyout_banking));
//
//		//Bills
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+3], null, BillsLandingPageActivity.class, R.color.flyout_banking));
//
//		//Mobile Payment
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+4], null, null, R.color.flyout_banking));
//
//		//Mobile Deposit
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+5], null, null, R.color.flyout_banking));
//
//		//TD Direct Investing
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+6], null, null, R.color.flyout_banking));
//
//		//Cross border shopping
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+7], null, null, R.color.flyout_banking));
//
//		//Preferences
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+8], null, null, R.color.flyout_non_banking));
//
//		//TD Locations
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+9], null, FindLocationsActivity.class, false, R.color.flyout_non_banking));
//
//		//Contact TD
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+10], null, ContactsActivity.class, false, R.color.flyout_non_banking));
//
//		//Send feedback
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+11], null, null, R.color.flyout_non_banking));
//
//		//FAQ
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+12], null, null, R.color.flyout_non_banking));
//
//		//Privacy & Security
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+13], null, null, R.color.flyout_non_banking));
//
//		//Legal
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+14], null, null, R.color.flyout_non_banking));
//
//		//Important Information
//		navDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i+15], null, null, R.color.flyout_non_banking));

		return navDrawerItems;
	}

	private void displayView(int position) {
		// replace the main content by calling activities
		Intent intent = null;

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navDrawerItems.get(position).getTitle());

		if(navDrawerItems.get(position).getTargetClass()==null){
			return;
		}
		mDrawerLayout.closeDrawer(mDrawerList);

		if (!navDrawerItems.get(position).isRequiredLogin()) {
			Utils.startActivity(this, navDrawerItems.get(position).getTargetClass());
			return;
		}
		else {
			verifyUserLogin(navDrawerItems.get(position).getTargetClass());
			return;
		}
	}

	public void verifyUserLogin(Class target){

        Log.d("USERLOGIN","isLoggedIn: "+Session.getInstance().isUserLoggedIn());
        Toast.makeText(this, "is loggedin: "+ Session.getInstance().isUserLoggedIn(), Toast.LENGTH_SHORT).show();

		if(Session.getInstance().isUserLoggedIn()){
			Utils.startActivity(this, target);
		}
		else
			Utils.startLoginActivity(this, target);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */


	public void setErrorFragment(HeaderErrorViewFragment fragment){
		this.mErrorFragment = fragment;
	}

	public void hideErrorFragment(){
		if(!mErrorFragment.isHidden()){
			FragmentTransaction ft = getFragmentManager().beginTransaction();  
			ft.hide(mErrorFragment);
			ft.commit(); 
		}
	}

	public void showErrorFragment(){
		if(!mErrorFragment.isVisible()){
			FragmentTransaction ft = getFragmentManager().beginTransaction();  
			ft.show(mErrorFragment);  
			ft.commit();  
		}
	}

	public void showErrorFragment(String headerText, String messageText){
		if(headerText != null)
			mErrorFragment.setHeaderText(headerText);
		if(messageText!= null)
			mErrorFragment.setMessageText(messageText);

		showErrorFragment();
	}

	public String getStringResourceByName(String aString) {
		String aErrorString=aString;
		String result = "";
		String packageName = getPackageName();
		int resId;
		try{
			int index=aString.lastIndexOf(".");
			if(index>0){
				aErrorString=aString.substring(index+1);
			}
			resId = getResources().getIdentifier(aErrorString, "string", packageName);
			result = getString(resId);
		}catch(Exception e){
			result = getStringResourceByName("UNKNOWN");
		}

		return result;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
				&& mDrawerToggle != null) {
			// Sync the toggle state after onRestoreInstanceState has occurred
			mDrawerToggle.syncState();
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	public void logout(View v) {

		LogoutAlertDialog dialog = new LogoutAlertDialog(this);
		dialog.logout();
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

//	public void gotoHome(View v) {
//		Utils.startActivity(this, MainController.class);
//	}

	public void launchActivity(Class target){
		Utils.startActivity(this, target);
	}

//	public void launchHomeActivity(View v){
//		launchActivity(MainController.class);
//	}
//	public void launchEMTActivity(View v){
//		launchActivity(ETransferSendActivity.class);
//	}
//	public void launchTransfersActivity(View v){
//		launchActivity(TransfersLandingPageActivity.class);
//	}
//	public void launchPayBillsActivity(View v) {
//		launchActivity(PayBillsFragmentActivity.class);
//	}
//	public void launchUpcomingBillsActivity(View v) {
//		launchActivity(UpcomingBillsFragmentActivity.class);
//	}
//	public void launchFindLocationsActivity(View v){
//		launchActivity(FindLocationsActivity.class);
//	}
//	public void launchContactsActivity(View v){
//		launchActivity(ContactsActivity.class);
//	}
//	public void launchLoginActivity(View v){
//		launchActivity(LoginController.class);
//	}
//
//	public void launchTransfersLandingPageActivity(View v){
//		launchActivity(TransfersLandingPageActivity.class);
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// instead of inflating the menu return, don't inflate it to hide it
		return true;
	}

	protected void init(){
		setErrorFragment((HeaderErrorViewFragment) getFragmentManager().findFragmentById(R.id.error_fragment));
		hideErrorFragment();
	}
}
