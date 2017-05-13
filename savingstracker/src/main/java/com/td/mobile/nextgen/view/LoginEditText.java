package com.td.mobile.nextgen.view;

import android.content.Context;
import android.util.AttributeSet;

import com.td.innovate.savingstracker.R;
import com.td.mobile.controllers.LoginController;

public class LoginEditText extends CustomEditText{
	
	public static int CLEAR_ACTION = 0;
	public static int ACTION_SHEET_ACTION = 1;
	
	int action;
	LoginController activity;
	
	public LoginEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void doAction() {
		if(action == CLEAR_ACTION){
			setText(""); 
		}
		else{
			activity.showAccessCard();
		}
	}

	@Override
	public void setAction(int action) {
		this.action = action;
		if(action == CLEAR_ACTION){
			setBypassValidation(false);
			toggleButton(R.drawable.delete_icon);
		}
		else{
			setBypassValidation(true);
			toggleButton(R.drawable.caret_down);
		}
	}

	public LoginController getActivity() {
		return activity;
	}

	public void setActivity(LoginController activity) {
		this.activity = activity;
	}

}
