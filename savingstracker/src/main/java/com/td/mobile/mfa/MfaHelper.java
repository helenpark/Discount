package com.td.mobile.mfa;

import android.content.Context;

import com.google.gson.Gson;
import com.td.mobile.model.ResponseInfo;
import com.td.mobile.nextgen.restful.RestEndPoint;
import com.td.mobile.nextgen.restful.RestRequest;
import com.td.mobile.utils.Consts;
import com.td.mobile.utils.TDLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MfaHelper implements Serializable{
	private static final long serialVersionUID = 1L;

	// private static final String Svc_Authenticate_Login_Rs = "SvcAuthenticateLoginRs";
	private static final String Response_Info = "ResponseInfo";
	// private JSONObject responseJson;
	private String responseString;
	public MfaCodes state;
	public ResponseInfo responseInfo;
	private final String CHALLENGE = "Challenge";
	private final String MFA_ANSWER = "Answer";
	private final String QUESTION_ID = "QuestionID";

	public MfaHelper(String obj){
		this.responseString = obj;
		this.responseInfo=populateResponseInfo(obj);
		this.isMfaRequried();
	}

	public static enum MfaCodes{
		NOT_REQUIRED,
		REQUIRED,
		BLOCKED,
		SETUP_REQUIRED,
		TIMEOUT
	}


	public boolean isMfaRequried(){

		boolean required = false;
		if(responseInfo !=null){
			String responseCode = responseInfo.getReasonCD();
			if( responseCode.equals(ResponseInfo.ReasonCodes.MCHALL) ){
				state = MfaCodes.REQUIRED;
				required =true;
			}
			else if (responseCode.equals(ResponseInfo.ReasonCodes.MLOCK)){
				state = MfaCodes.BLOCKED;
				required =true;
			}
			else if (responseCode.equals(ResponseInfo.ReasonCodes.MSETUP)){
				state = MfaCodes.SETUP_REQUIRED;
				required =true;
			}
			else if (responseCode.equalsIgnoreCase(ResponseInfo.ReasonCodes.MTO)){
				state = MfaCodes.TIMEOUT;
				required = true;
			}
		}
		return required;
	}


	public RestRequest verifyMfaQuestion(Context context,String answer, String questionId) {
	
		
		JSONObject playload= RestRequest.createPlayload(context, null);
		JSONObject aChild=new JSONObject();
		try {
			aChild.put(MFA_ANSWER, answer);
			aChild.put(QUESTION_ID, questionId);
			playload.put(CHALLENGE, aChild);
		} catch (JSONException e) {
		}
		RestRequest request=new RestRequest(RestEndPoint.getInstance().getVerifyMfa(),
				RestRequest.SvcVerifyMFARq,
				playload);

		return request;
	}

	static public  ResponseInfo populateResponseInfo(String objStr){

		ResponseInfo responseInfo= null;
		Gson gson = new Gson();
		try {
			JSONObject resp=new JSONObject(objStr);
			JSONObject rsInfo = (objStr != null) ? resp.getJSONObject(Response_Info) : null;
			if(rsInfo != null){
				responseInfo = gson.fromJson(rsInfo.toString(), ResponseInfo.class);
			}

		} catch (Exception e) {
			TDLog.d(Consts.LOG_TAG, e.getMessage());
		} 
		return responseInfo;
	}


}
