package com.td.mobile.helpers;

import android.content.Context;

import com.td.mobile.nextgen.restful.RestCallAsyncTask;
import com.td.mobile.nextgen.restful.RestEndPoint;
import com.td.mobile.nextgen.restful.RestRequest;
import com.td.mobile.nextgen.restful.RestRequestListener;
import com.td.mobile.nextgen.restful.RestResponse;
import com.td.mobile.utils.TDLog;

import org.json.JSONObject;

import java.util.HashMap;

public class AccountDetailRestHelper {
	private static final String CLASS_NAME = AccountDetailRestHelper.class.getName();
	private Context context;
	private RestRequestListener listener;
	
	final private String AccountKey="AccountKey";
	final private String AccountNumber="AccountNumber";
	final private String StartIndex="StartIndex";
	final private String NumberOfEntries="NumberOfEntries";
	final private String ReturnSummaryInfoIndicator="ReturnSummaryInfoIndicator";
	
	public AccountDetailRestHelper(Context context, RestRequestListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	
	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}


	public RestRequestListener getListener() {
		return listener;
	}


	public void setListener(RestRequestListener listener) {
		this.listener = listener;
	}

	public void requestGetLocalCreditCardAccount(String accountKey, String accountNumber, String startIndex, String numberOfEntries) {		
		HashMap<String, String> params= new HashMap<String, String>();
		params.put(AccountKey,accountKey);
		params.put(AccountNumber, accountNumber);
		params.put(StartIndex, startIndex);
		params.put(NumberOfEntries, numberOfEntries);
		params.put(ReturnSummaryInfoIndicator, "true");

		JSONObject playload= RestRequest.createPlayload(context, params);
		RestRequest request=new RestRequest(RestEndPoint.getInstance().getCreditCardAccountDetails(),
				RestRequest.SvcAccountInquiryGetLocalCreditAccountDetailsRq,
				playload);
		
		executeRequest(request);
	}

	public void requestGetCreditCardAccount(String accountKey, String accountNumber, String startIndex, String numberOfEntries) {		
		HashMap<String, String> params= new HashMap<String, String>();
		params.put(AccountKey,accountKey);
		params.put(AccountNumber, accountNumber);
		params.put(StartIndex, startIndex);
		params.put(NumberOfEntries, numberOfEntries);
		params.put(ReturnSummaryInfoIndicator, "true");

		JSONObject playload= RestRequest.createPlayload(context, params);
		RestRequest request=new RestRequest(RestEndPoint.getInstance().getCreditCardAccountDetails(),
				RestRequest.SvcAccountInquiryGetAccountDetailsRq,
				playload);
		
		executeRequest(request);
	}

	public void requestDepositAccount(String accountKey, String accountNumber, String startIndex, String numberOfEntries) {		
		HashMap<String, String> params= new HashMap<String, String>();
		params.put(AccountKey,accountKey);
		params.put(AccountNumber, accountNumber);
		params.put(StartIndex, startIndex);
		params.put(NumberOfEntries, numberOfEntries);
		params.put(ReturnSummaryInfoIndicator, "true");

		JSONObject playload= RestRequest.createPlayload(context, params);
		RestRequest request=new RestRequest(RestEndPoint.getInstance().getDepositAccountDetails(),
				RestRequest.SvcAccountInquiryGetAccountDetailsRq,
				playload);
		
		executeRequest(request);
	}

	protected void executeRequest(RestRequest request){
		RestCallAsyncTask<RestResponse> task = new RestCallAsyncTask<RestResponse>(RestResponse.class, context) {

			@Override
			protected void onPostExecute(RestResponse result) {
				super.onPostExecute(result);
				String s=(result!=null)? result.toString(): "";
				TDLog.d(CLASS_NAME, "result=" + s);
				if(getListener()!=null){
					getListener().onResult(result);
				}
				
			}
		};

		task.execute(request);
	}

}
