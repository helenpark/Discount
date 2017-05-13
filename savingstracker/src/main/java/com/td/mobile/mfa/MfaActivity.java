package com.td.mobile.mfa;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.innovate.savingstracker.R;
import com.td.mobile.controllers.BaseController;
import com.td.mobile.helpers.NavigationManager;
import com.td.mobile.mfa.MfaHelper.MfaCodes;
import com.td.mobile.model.ResponseInfo;
import com.td.mobile.model.User;
import com.td.mobile.nextgen.restful.RestCallAsyncTask;
import com.td.mobile.nextgen.restful.RestRequest;
import com.td.mobile.nextgen.restful.RestResponse;
import com.td.mobile.nextgen.restful.Session;
import com.td.mobile.nextgen.view.ViewBuilder;
import com.td.mobile.utils.Consts;
import com.td.mobile.utils.TDLog;
import com.td.mobile.utils.Utils;

import org.json.JSONObject;

public class MfaActivity extends BaseController {

	private TextView mQuestion, mloginBtnText;
	private EditText mAnswer;
	private LinearLayout mLoginBtn;
	private MfaHelper mfaHelper;
	public static final String MFA_EXTRA = "MFA_EXTRA";
	private View mfaErrorMessage;
	private View mfaAnswerErrorMessage;
	private View mfaAnswerUnderline;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mfaHelper = (MfaHelper) getIntent().getExtras().get(MFA_EXTRA);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mfa);
		
//		populateDrawerMenu();
		
//		setActionBarCustomView();

		mQuestion = (TextView) findViewById(R.id.mfa_question);
		mAnswer = (EditText) findViewById(R.id.mfa_answer);
		mLoginBtn = (LinearLayout) findViewById(R.id.mfa_login_btn);
		mloginBtnText = (TextView) findViewById(R.id.mfa_login_btn_txt);
		mfaAnswerErrorMessage = (View) findViewById(R.id.mfa_answer_error_message);
		mfaAnswerErrorMessage.setVisibility(View.GONE);
		mfaErrorMessage = (View) findViewById(R.id.mfa_error_message);
		mfaErrorMessage.setVisibility(View.GONE);
		mfaAnswerUnderline = (View)findViewById(R.id.answer_underline);
		
		
		mLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onLoginClicked();
			}
		});
		
		mAnswer.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				toggleLoginButton();
			}
		});


	}

	@Override
	protected void onResume(){
		super.onResume();
		mQuestion.setText(mfaHelper.responseInfo.getChallenge().getQuestion());
	}
	

	public void toggleLoginButton() {
		String answer = mAnswer.getText().toString();
		
				
		if( answer != null && answer.length()>= 1){
			mLoginBtn
			.setBackgroundResource(R.drawable.rounded_corner_middle_green);
			mloginBtnText.setTextColor(getResources().getColor(R.color.white));
		} else {
			mLoginBtn
			.setBackgroundResource(R.drawable.rounded_corner_disabled);
			mloginBtnText.setTextColor(getResources().getColor(R.color.dark_grey));
		}
	}

	private void onLoginClicked(){
		hideErrorMessages();
		String answer = mAnswer.getText().toString();
		String questionID=(mfaHelper.responseInfo!=null && mfaHelper.responseInfo.getChallenge()!=null)? 
				mfaHelper.responseInfo.getChallenge().getQuestionID():null;
				
		if( questionID!=null && answer != null && answer.length()>= 1){
			RestRequest request = mfaHelper.verifyMfaQuestion(this,answer, mfaHelper.responseInfo.getChallenge().getQuestionID());
			final ProgressDialog progessDialog = ViewBuilder.buildProgressSpinner(this);

			RestCallAsyncTask<RestResponse> task = new RestCallAsyncTask<RestResponse>(RestResponse.class,this) {

				@Override
				protected void onPostExecute(RestResponse result) {
					//super.onPostExecute(result);
					progessDialog.dismiss();
					mAnswer.setText("");
					if (result == null  || result.hasHttpError() || result.getServerStatusCode()!=null) {
						if(!handleChallengeInvalidatingSession(result) && !handleChallengeRetry(result) && !handleChallengeError(result)){
							String errorCode= (result!=null)? result.getServerStatusCode(): null;
							showErrorMessage(errorCode);							
						} 
					} else {
						String s= result!=null ? result.toString(): "";
						TDLog.d(Consts.LOG_TAG, "result=" + s);
						processResponse(result);
					}
				}
			};
			progessDialog.show();
			task.execute(request);
		}

	}


	private void processResponse(RestResponse result){

		try {

			JSONObject obj = (JSONObject) result.getPayload();
			int statusCode = Integer.parseInt(result.getStatusCode());
			
			if (statusCode == 0) {
				String deviceID=obj.getString(Consts.DeviceID);
				if(deviceID!=null){
					SharedPreferences sharedPreferences = getSharedPreferences(Consts.DeviceID,Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					editor.putString(Consts.DeviceID, deviceID);
					editor.commit();
				}
				
				Session.getInstance().put("user", new User());
				NavigationManager.launchAccountSummarActivity(this);
				finish();
			} 
			else if (statusCode == 1) {

			} 
			else if (statusCode == 2) {

			} 
		} 
		catch (Exception e) {
			TDLog.d(Consts.LOG_TAG, e.getMessage(), e);
		}

	}

	protected void showAnswerErrorMessage(String errorCode) {
		mfaAnswerErrorMessage.setVisibility(View.VISIBLE);
		//errorImg.setVisibility(View.VISIBLE);
	//	mAnswer.setTextColor(getResources().getColor(R.color.red));
		mfaAnswerUnderline.setBackgroundColor(getResources().getColor(R.color.red));
		TextView textView = (TextView) mfaAnswerErrorMessage
				.findViewById(R.id.errorText);
		String message = getString(Utils.getResourceIDByName(this, errorCode));
		textView.setText(message);
	}

	protected void showErrorMessage(String errorCode) {
		mfaErrorMessage.setVisibility(View.VISIBLE);
		TextView textView = (TextView) mfaErrorMessage.findViewById(R.id.errorText);
		String message = getString(Utils.getResourceIDByName(this, errorCode));
		textView.setText(message);
	}
	
	protected void hideErrorMessages(){
		mfaAnswerErrorMessage.setVisibility(View.GONE);
		mfaErrorMessage.setVisibility(View.GONE);
		mfaAnswerUnderline.setBackgroundColor(getResources().getColor(R.color.middle_grey));
		mAnswer.setTextColor(getResources().getColor(R.color.black));
	}
	
	protected boolean handleChallengeRetry(RestResponse resp){
		boolean aRtn=false;
		try {
			if(resp!=null && resp.getServerStatusCode()!=null){
				mfaHelper=new MfaHelper(resp.getPayload().toString());
				ResponseInfo aInfo=mfaHelper.responseInfo;
				if(aInfo!=null){
					String s=aInfo.getChallenge().getQuestion();
					mQuestion.setText(s);
					showAnswerErrorMessage(resp.getServerStatusCode());
					aRtn=true;
				}
			}
		} catch (Exception e) {
		}		
		return aRtn;
	}

	protected boolean handleChallengeError(RestResponse resp){
		boolean aRtn=false;
		try {
			if(resp!=null && resp.getServerStatusCode()!=null){
				mfaHelper=new MfaHelper(resp.getPayload().toString());
				ResponseInfo aInfo=mfaHelper.responseInfo;
				if(aInfo!=null){
					String s=aInfo.getReasonCD();
					if(ResponseInfo.ReasonCodes.MLOCK.equalsIgnoreCase(s) || ResponseInfo.ReasonCodes.MTO.equalsIgnoreCase(s)){
						aRtn=true;
						NavigationManager.launchMfaActivity(this, mfaHelper);
						finish();
					}
				}
			}
		} catch (Exception e) {
		}		
		return aRtn;
	}
	
	protected boolean handleChallengeInvalidatingSession(RestResponse resp){
		boolean aRtn=false;
		try {
			if(resp!=null && Consts.INVALIDATE_SESSION_ERROR.equalsIgnoreCase(resp.getHttpError())){
				mfaHelper=new MfaHelper("");
				mfaHelper.state= MfaCodes.TIMEOUT;
				aRtn=true;
				NavigationManager.launchMfaActivity(this, mfaHelper);
			}
		} catch (Exception e) {
		}		
		return aRtn;
	}

	protected void setActionBarCustomView() {
		ActionBar actionBar = getActionBar();
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.actionbar_function_nav, null);
		TextView titleTxt = (TextView) view.findViewById(R.id.title);
		if (titleTxt != null) {
			titleTxt.setText(R.string.securityQuestionPageHeader);
		}

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(view);
	}
	
}
