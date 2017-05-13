package com.td.mobile.nextgen.restful;

import android.content.Context;
import android.os.AsyncTask;

import com.td.mobile.utils.Consts;
import com.td.mobile.utils.TDLog;
import com.td.mobile.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

abstract public class RestCallAsyncTask<T> extends AsyncTask<RestRequest, Void, T> {
	private Class<T> type;
	private Context mContext;
	
	private static String UNAUTHORIZED = "401";
	private static String MOVED = "302";
	
	static final HashMap<String, String> RqToFileMap=new HashMap<String, String>();
	{
		RqToFileMap.put(RestRequest.SvcBillPaymentGetAccessCardsRq, "getAccessCards.json");
		RqToFileMap.put(RestRequest.SvcBillPaymentGetPaymentPartiesRq, "getPaymentParties.json");
		RqToFileMap.put(RestRequest.SvcBillPaymentConfirmPaymentRq, "confirmPayment.json");
		RqToFileMap.put(RestRequest.SvcBillPaymentSendPaymentRq, "sendPayment.json");
		RqToFileMap.put(RestRequest.SvcAuthenticateLoginRq, "rememberme.json");
		RqToFileMap.put(RestRequest.SvcVerifyMFARq, "mfa/mlock.json");
		
		RqToFileMap.put(RestRequest.SvcAccountInquiryGetFinancialSummaryRq, "localData.json");
		RqToFileMap.put(RestRequest.SvcBillPaymentGetUpcomingBillsRq, "upcomingBills.json");
		RqToFileMap.put(RestRequest.SvcBillPaymentCancelUpcomingPaymentRq, "upcomingBillsCancel.json");
		
		RqToFileMap.put(RestRequest.SvcFundTransferGetFromAccountsRq, "getFromAccounts.json");
		RqToFileMap.put(RestRequest.SvcFundTransferGetToAccountsRq, "getToAccounts.json");
		RqToFileMap.put(RestRequest.SvcFundTransferConfirmFundTransferRq, "confirmMyAccountsTransfer.json");
		RqToFileMap.put(RestRequest.SvcFundTransferMakeFundTransferRq, "makeMyAccountsTransfer.json");
		
		
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferGetSendEMTSendersRq, "interac/getEMTSenders.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferGetSendPartiesRq, "interac/getSendParties.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferConfirmEMTSendRq, "interac/confirmEMTSend.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferSubmitEMTSendRq, "interac/sumbitEMTSend.json");

		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferGetPendingEMTSendersRq, "interac/getPendingEMTSenders.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferGetPendingEMTListRq, "interac/getPendingEMTList.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferGetReclaimAccountsRq, "interac/getReclaimAccounts.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferSubmitDepositEMTReclaimRq, "interac/submitDepositEMTReclaim.json");

		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferVerifyReceiverEMTReceiveRq, "interac/verifyReceiverEMTReceive.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferGetConfirmationEMTReceiveRq, "interac/getConfirmationEMTReceive.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferSubmitDepositEMTReceiveRq, "interac/submitDepositEMTReceive.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferGetConfirmationEMTDeclineRq, "interac/getConfirmationEMTDecline.json");
		RqToFileMap.put(RestRequest.SvcEmailMoneyTransferSubmitEMTDeclineRq, "interac/confirmEMTDecline.json");
		RqToFileMap.put(RestRequest.SvcAccountInquiryGetAccountDetailsRq, "accountDetail-0.json");
		RqToFileMap.put(RestRequest.SvcAccountInquiryGetLocalCreditAccountDetailsRq, "creditCardAccountDetail.json");
		
		RqToFileMap.put(RestRequest.SvcInvestmentInquiryGetBrokerageAccountDetailsRq, "investing/brokerage1.json");
		RqToFileMap.put(RestRequest.SvcInvestmentInquiryGetHoldingsRq, "investing/holdings.json");
		RqToFileMap.put(RestRequest.SvcInvestmentInquiryGetTradingActivitiesRq, "investing/activities.json");
		
		RqToFileMap.put(RestRequest.SvcInvestmentInquiryGetOrderListRq, "investing/orders_list.json");
	}
	
	public RestCallAsyncTask(Context ctx){
		mContext=ctx;
	}
	
    public RestCallAsyncTask(Class<T> cls, Context ctx){
    	this(ctx);
    	type= cls;
    }
    Class<T> getType(){return type;}
    

	@Override
	protected T doInBackground(RestRequest... params) {
		T aRtn=null;
		if(Utils.getBypassLogin()){
			aRtn=(T)loadJsonFromAsset(params);
		} else {
			aRtn=loadJsonFromRestService(params);
		}
		
		return aRtn;
	}

	private HttpHeaders createHeaders(RestRequest request){
		HttpHeaders requestHeaders = request.createHeader();
		if(Consts.SvcAuthenticateLoginRq.equalsIgnoreCase(request.getEnvelopeHeader())==false){
			List<String> cookies=(List<String>) Session.getInstance().get(Consts.SetCookie);
			if((cookies!=null)){
				for(String cookie: cookies){
					requestHeaders.add(Consts.Cookie, cookie);
				}			
			}
		} else {
			requestHeaders.remove(Consts.Cookie);
		}
		HashMap<String,String> headerParams=request.getHeaderParams();
		if(headerParams!=null){
			Set<String> keys=headerParams.keySet();
			for(String key: keys){
				requestHeaders.add(key, headerParams.get(key));
			}
		}
		return requestHeaders;
	}

	private void setCookie(RestRequest request, HttpHeaders headers){
		if(Consts.SvcAuthenticateLoginRq.equalsIgnoreCase(request.getEnvelopeHeader())){
			List<String> cookies=headers.get(Consts.SetCookie);
			if(cookies!=null){
				Session.getInstance().put(Consts.SetCookie, cookies);
			};
		}
	}
	
	protected T loadJsonFromRestService(RestRequest... params) {
		T aRtn=null;
		RestRequest aRequest= (params!=null && params.length>0)? params[0]: null;
		if(aRequest==null){
			return null;
		}
		String url=aRequest.getServicePath();
		JSONObject message=new JSONObject();

		try {
			JSONObject request=aRequest.getPlayLoad();
			message.put(aRequest.getEnvelopeHeader(), request);
		} catch (JSONException e1) {
			TDLog.d(Consts.LOG_TAG, e1.getMessage(), e1);
		}
		
		HttpHeaders requestHeaders = createHeaders(aRequest);
		HttpEntity<String> requestEntity = new HttpEntity<String>(message.toString(), requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		
		ResponseEntity<String> responseEntity = null;
		
		try {
			TDLog.d(Consts.LOG_TAG, "requestEntity=" + requestEntity.toString());
			responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			TDLog.d(Consts.LOG_TAG, "responseEntity=" + responseEntity.toString());
			
			String result = responseEntity.getBody();
			HttpHeaders headers = responseEntity.getHeaders();
			setCookie(aRequest, headers);

			if(getType()!=null){
				aRtn=(T)new RestResponse(result, responseEntity.getStatusCode());
			} else {
				try{
					aRtn=(T)new JSONObject(result);
				} catch (Exception e){
					TDLog.e(Consts.LOG_TAG, e.getMessage(), e);
				}
			}		
		} catch (Exception e1) {
			
			JSONObject httpErrorObj = new JSONObject();
			
			try {
				
				if(e1 instanceof HttpServerErrorException)
					httpErrorObj.put("HttpError", ((HttpServerErrorException) e1).getStatusCode());
				
				else if(e1 instanceof HttpClientErrorException)
					httpErrorObj.put("HttpError", ((HttpClientErrorException) e1).getStatusCode());
				else
					httpErrorObj.put("HttpError", e1.getMessage());
				
				aRtn = (T) new RestResponse(httpErrorObj);
				
			} catch (JSONException e) {
				TDLog.e(Consts.LOG_TAG, e.getMessage(), e);
			}
			TDLog.e(Consts.LOG_TAG, e1.getMessage(), e1);
		}
		
		return aRtn;
	}
	
	protected T loadJsonFromAsset(RestRequest... params) {
		
		T aRtn=null;
		RestRequest aRequest= (params!=null && params.length>0)? params[0]: null;
		if(aRequest==null){
			return null;
		}
		String url=RqToFileMap.get(aRequest.getEnvelopeHeader());
		InputStream inputStream=null;
		StringBuffer strBuffer=new StringBuffer();
		HttpHeaders headers=new HttpHeaders();
		headers.add("Set-Cookie", "1234567890");
		setCookie(aRequest, headers);
		try {
			inputStream=mContext.getAssets().open(url);
			BufferedReader buffer=new BufferedReader(new InputStreamReader(inputStream));
			strBuffer=new StringBuffer();
			String s="";
			while((s=buffer.readLine())!=null){
				strBuffer.append(s);
			}
			
			RestResponse res=new RestResponse(strBuffer.toString(), HttpStatus.OK);
			if(res.getStatusCode().equalsIgnoreCase("0") || res.getStatusCode().equalsIgnoreCase("1")){
				TDLog.d(Consts.LOG_TAG, res.toString());
				if(getType() == RestResponse.class){
					aRtn=(T)res;
				} else {
					aRtn=(T)res.getRaw();
				}
			}
		} catch (Exception e) {
			TDLog.d(this.getClass().getName(), e.getMessage(), e);
		} finally {
			if(inputStream!=null) 
				try{
					inputStream.close();
				} catch (IOException e){
					e.printStackTrace();
				}
		}

		return aRtn;
	}

	@Override
	protected void onPostExecute(T result) {
		super.onPostExecute(result);
		
		
		if(result instanceof RestResponse && ((RestResponse) result).hasHttpError() &&  (((RestResponse) result).getHttpError().equals(UNAUTHORIZED) || ((RestResponse) result).getHttpError().equals(MOVED))){
			Session.getInstance().sessionTimeout(mContext);
			
		}
	}

}
