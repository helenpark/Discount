package com.td.mobile.nextgen.view;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.innovate.savingstracker.R;
import com.td.mobile.controllers.BaseController;
//import com.td.mobile.controllers.MainController;

public class LogoutSuccessActivity extends BaseController {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.logout_view);
				
		populateDrawerMenu();	
		
		
		setActionBarCustomView(R.string.logoutSuccessPageHeader);
	}
	
	protected void setActionBarCustomView(int titleResource) {
		ActionBar actionBar = getActionBar();
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.actionbar_function_nav, null);
		TextView titleTxt = (TextView) view.findViewById(R.id.title);
		if (titleTxt != null) {
			titleTxt.setText(titleResource);
		}

		ImageView logout = (ImageView) view.findViewById(R.id.actionBarLogout);
		logout.setVisibility(View.INVISIBLE);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(view);
	}
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
//		launchActivity(MainController.class);
	}
	
	
	
}
