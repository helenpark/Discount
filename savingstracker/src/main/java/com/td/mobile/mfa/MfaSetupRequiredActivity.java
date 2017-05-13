package com.td.mobile.mfa;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.td.innovate.savingstracker.R;
import com.td.mobile.controllers.BaseController;
//import com.td.mobile.controllers.MainController;
import com.td.mobile.utils.Utils;

public class MfaSetupRequiredActivity extends BaseController {


	private TextView mErrorMessage;
	private Button mGotoEsayWebBtn;
	private Button mGoToWebBroker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mfa_setup_required);
		
		populateDrawerMenu();
		
		setActionBarCustomView();
		/*ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(false);
		actionBar.setTitle(getResources().getString(R.string.securityQuestionMFASetupRequiredPageHeader));
		actionBar.setIcon(R.drawable.header_caret);*/

		mErrorMessage = (TextView) findViewById(R.id.errorText);
		mGotoEsayWebBtn = (Button) findViewById(R.id.btn_easy_web);

		mGotoEsayWebBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				onEasyWebClick();
			}
		});



		mGoToWebBroker = (Button) findViewById(R.id.btn_web_broker);

		mGoToWebBroker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				onWebBrokerClick();
			}
		});
	}


	@Override
	protected void onResume(){
		super.onResume();
		String s=getString(R.string.mfaSetupRequiredMessage);
		mErrorMessage.setText(s);
	}
	
	
	protected void setActionBarCustomView() {
		ActionBar actionBar = getActionBar();
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.actionbar_function_nav, null);
		TextView titleTxt = (TextView) view.findViewById(R.id.title);
		if (titleTxt != null) {
			titleTxt.setText(R.string.securityQuestionMFASetupRequiredPageHeader);
		}

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(view);
	}

	
	
	private void onWebBrokerClick(){
		
		
		
		
	}
	
	
	private void onEasyWebClick(){
		
		
		
		
	}
	
	@Override
	public void onBackPressed() {
//		 Utils.startActivity(this, MainController.class);
		 return;	
	}

}
