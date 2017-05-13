package com.td.mobile.nextgen.restful;

import android.content.Context;

import com.td.mobile.utils.Consts;
import com.td.mobile.utils.TDLog;
import com.td.mobile.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class RestRequest {
	private String servicePath;
	private String envelopeHeader;
	private JSONObject playLoad;
	private HashMap<String,String> headerParams;

	final static public String SvcBillPaymentGetAccessCardsRq="SvcBillPaymentGetAccessCardsRq";
	final static public String SvcAccountInquiryGetAccountDetailsRq="SvcAccountInquiryGetAccountDetailsRq";
	final static public String SvcAccountInquiryGetLocalCreditAccountDetailsRq="SvcAccountInquiryGetLocalCreditAccountDetailsRq";
	final static public String SvcBillPaymentGetPayeeRq="SvcBillPaymentGetPayeeRq";
	final static public String SvcBillPaymentGetPaymentPartiesRq="SvcBillPaymentGetPaymentPartiesRq";
	final static public String SvcBillPaymentConfirmPaymentRq="SvcBillPaymentConfirmPaymentRq";
	final static public String SvcBillPaymentSendPaymentRq="SvcBillPaymentSendPaymentRq";
	final static public String SvcAuthenticateLoginRq="SvcAuthenticateLoginRq";
	final static public String SvcAccountInquiryGetFinancialSummaryRq="SvcAccountInquiryGetFinancialSummaryRq";
	final static public String SvcVerifyMFARq="SvcVerifyMfaRq";

	final static public String SvcBillPaymentGetUpcomingBillsRq="SvcBillPaymentGetUpcomingPaymentsRq";
	final static public String SvcBillPaymentCancelUpcomingPaymentRq="SvcBillPaymentCancelUpcomingPaymentRq";

	//logout
	final static public String SvcAuthenticateLogoutRq="SvcAuthenticateLogoutRq";

	// Interac E Transfer

	final static public String SvcEmailMoneyTransferGetSendEMTSendersRq="SvcEmailMoneyTransferGetSendEMTSendersRq";
	final static public String SvcEmailMoneyTransferGetSendPartiesRq="SvcEmailMoneyTransferGetSendPartiesRq";	
	final static public String SvcEmailMoneyTransferConfirmEMTSendRq="SvcEmailMoneyTransferConfirmEMTSendRq";
	final static public String SvcEmailMoneyTransferSubmitEMTSendRq="SvcEmailMoneyTransferSubmitEMTSendRq";
	final static public String SvcEmailMoneyTransferGetPendingEMTSendersRq="SvcEmailMoneyTransferGetPendingEMTSendersRq";
	final static public String SvcEmailMoneyTransferGetPendingEMTListRq="SvcEmailMoneyTransferGetPendingEMTListRq";
	final static public String SvcEmailMoneyTransferGetReclaimAccountsRq="SvcEmailMoneyTransferGetReclaimAccountsRq";

	final static public String SvcEmailMoneyTransferSubmitDepositEMTReclaimRq="SvcEmailMoneyTransferSubmitDepositEMTReclaimRq";


	final static public String SvcEmailMoneyTransferSubmitDepositEMTReclaimRs="SvcEmailMoneyTransferSubmitDepositEMTReclaimRs";

	final static public String SvcEmailMoneyTransferVerifyReceiverEMTReceiveRq="SvcEmailMoneyTransferVerifyReceiverEMTReceiveRq";
	final static public String SvcEmailMoneyTransferGetConfirmationEMTReceiveRq="SvcEmailMoneyTransferGetConfirmationEMTReceiveRq";
	final static public String SvcEmailMoneyTransferSubmitDepositEMTReceiveRq="SvcEmailMoneyTransferSubmitDepositEMTReceiveRq";
	final static public String SvcEmailMoneyTransferGetConfirmationEMTDeclineRq="SvcEmailMoneyTransferGetConfirmationEMTDeclineRq";
	final static public String SvcEmailMoneyTransferSubmitEMTDeclineRq="SvcEmailMoneyTransferSubmitEMTDeclineRq";

	final static public String SvcFundTransferGetFromAccountsRq = "SvcFundTransferGetFromAccountsRq";
	final static public String SvcFundTransferGetToAccountsRq = "SvcFundTransferGetToAccountsRq";
	final static public String SvcFundTransferConfirmFundTransferRq = "SvcFundTransferConfirmFundTransferRq";
	final static public String SvcFundTransferMakeFundTransferRq = "SvcFundTransferMakeFundTransferRq";

	// Investing 
	final static public String SvcInvestmentInquiryGetBrokerageAccountDetailsRq = "SvcInvestmentInquiryGetBrokerageAccountDetailsRq";
	final static public String SvcInvestmentInquiryGetHoldingsRq = "SvcInvestmentInquiryGetHoldingsRq";
	final static public String SvcInvestmentInquiryGetTradingActivitiesRq = "SvcInvestmentInquiryGetTradingActivitiesRq";
	final static public String SvcInvestmentInquiryGetOrderListRq = "SvcInvestmentInquiryGetOrderListRq";
	final static public String SvcInvestmentInquiryGetOrderDetailsRq = "SvcInvestmentInquiryGetOrderDetailsRq";
	
	
	public RestRequest() {
		super();
	}

	public RestRequest(String servicePath, String envelopeHeader, JSONObject playLoad) {
		super();
		this.servicePath = servicePath;
		this.envelopeHeader = envelopeHeader;
		this.playLoad = playLoad;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
	public String getEnvelopeHeader() {
		return envelopeHeader;
	}
	public void setEnvelopeHeader(String envelopeHeader) {
		this.envelopeHeader = envelopeHeader;
	}
	public JSONObject getPlayLoad() {
		return playLoad;
	}
	public void setPlayLoad(JSONObject playLoad) {
		this.playLoad = playLoad;
	}
	public HashMap<String, String> getHeaderParams() {
		return headerParams;
	}

	public void setHeaderParams(HashMap<String, String> headerParams) {
		this.headerParams = headerParams;
	}

	static public JSONObject createPlayload(Context ctx, HashMap <String, String> params){
		JSONObject playload = new JSONObject();
		try {
			if(params!=null && params.size()>0){
				Set<String> keys=params.keySet();
				Iterator<String> iter=keys.iterator();
				while(iter.hasNext()){
					String aKey=iter.next();
					playload.put(aKey, params.get(aKey));
				}
			}
		} catch (JSONException e) {
			TDLog.d(Consts.LOG_TAG, e.getMessage(), e);
		}
		return playload;
	}

	public HttpHeaders createHeader(){
		HttpHeaders requestHeaders = new HttpHeaders();
		String[] s=servicePath.split("/");
		int k=s.length;
		String service=s[k-3]+".rest."+s[k-2];
		SimpleDateFormat aFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String timestamp=aFormat.format(new Date());		
		requestHeaders.set("com.td.wsw.services."+service+".TracabilityID", UUID.randomUUID().toString());
		requestHeaders.set("com.td.wsw.services."+service+".MessageID", Utils.getMessageID());
		requestHeaders.set("com.td.wsw.services."+service+".CorrelationID", UUID.randomUUID().toString());
		requestHeaders.set("com.td.wsw.services."+service+".TimeStamp", timestamp);
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.set("Connection", "Close");
		return requestHeaders;
	}

}
