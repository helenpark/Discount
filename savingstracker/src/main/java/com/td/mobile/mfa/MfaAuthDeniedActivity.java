package com.td.mobile.mfa;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.td.innovate.savingstracker.R;
import com.td.mobile.controllers.BaseController;
//import com.td.mobile.controllers.MainController;
import com.td.mobile.utils.Utils;

public class MfaAuthDeniedActivity extends BaseController {


	private TextView mErrorMessage;
	
	String four16;
	String eight66;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mfa_auth_denied);
		
		populateDrawerMenu();

		setActionBarCustomView();


		mErrorMessage = (TextView) findViewById(R.id.errorText);
		
		four16 = getResources().getString(R.string.securityQuestionAuthDeniedButtonCall1416);
		eight66 = getResources().getString(R.string.securityQuestionAuthDeniedButtonCall1866);
		String call = getResources().getString(R.string.securityQuestionAuthDeniedButtonCall);
		
		Button four16Button = (Button)findViewById(R.id.btn_call_416);
		
		Button eight66Button = (Button)findViewById(R.id.btn_call_866);
		
		four16Button.setText(call+" "+four16);
		eight66Button.setText(call+" "+eight66);

	}


	@Override
	protected void onResume(){
		super.onResume();
		String s=getString(R.string.mfaAuthDeniedMessage);
		mErrorMessage.setText(s);
	}


	public void on866Click(View v){
		dialNumber(eight66);
	}


	public void on416Click(View v){
		dialNumber(four16);
	}
	
	private void dialNumber(String number){
		
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:"+number));
		Utils.startActivity(this, intent);
		
	}
	
	protected void setActionBarCustomView() {
		ActionBar actionBar = getActionBar();
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.actionbar_function_nav, null);
		TextView titleTxt = (TextView) view.findViewById(R.id.title);
		if (titleTxt != null) {
			titleTxt.setText(R.string.securityQuestionAuthDeniedPageHeader);
		}

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(view);
	}


	@Override
	public void onBackPressed() {

//		 launchActivity(MainController.class);
	}


}



