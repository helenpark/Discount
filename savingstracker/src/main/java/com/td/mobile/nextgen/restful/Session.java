package com.td.mobile.nextgen.restful;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.td.innovate.savingstracker.R;
import com.td.mobile.controllers.LoginController;
import com.td.mobile.utils.TDLog;
import com.td.mobile.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class Session extends HashMap<String, Object>{
	static private Session instance;

	public static Session getInstance() {
		if(instance==null){
			synchronized(Session.class){
				if(instance==null){
					instance=new Session();
				}
			}
		}
		return instance;
	}


	private Session() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void logoutOnApplication(){
		remove("user");
		//remove("JSESSIONID");
	}
	
	public boolean isUserLoggedIn(){
		return containsKey("user");
	}

	private Session(int capacity, float loadFactor) {
		super(capacity, loadFactor);
		// TODO Auto-generated constructor stub
	}


	private Session(int capacity) {
		super(capacity);
		// TODO Auto-generated constructor stub
	}


	private Session(Map map) {
		super(map);
		// TODO Auto-generated constructor stub
	}
	
	
	public void sessionTimeout(final Context cxt) {
		sessionTimeout(cxt, cxt.getResources().getString(R.string.applicationTimeoutTitle),cxt.getResources().getString(R.string.applicationTimeoutMessage),
				cxt.getResources().getString(R.string.applicationTimeoutDismissBtnLabel));
	} 
	
	public void sessionTimeout(final Context cxt, String title, String message, String yesButtonLabel) {

		logoutOnApplication();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
		builder.setTitle(title);
		builder.setCancelable(false); 
		builder.setMessage(message)
				.setPositiveButton(yesButtonLabel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								
								Utils.startActivity(cxt, LoginController.class);
							}

						});
				
		AlertDialog dlg  = builder.show();
		
		dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(final DialogInterface dialog) {
				TDLog.i("dismissed", "dismissed");
			}
			});

} 
	
}
	



