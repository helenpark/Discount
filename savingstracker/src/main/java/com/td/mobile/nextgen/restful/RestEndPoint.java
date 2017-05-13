package com.td.mobile.nextgen.restful;

public class RestEndPoint {
	static private RestEndPoint instance;
	// TODO
	// READ Endpoint from resource file

	//	static final String BASE_SERVICEPATH="http://tdl00678085:9080/wireless/rest";
	//	static final String BASE_SERVICEPATH="https://wireless90.dev.td.com/wireless/rest";
//	static final String BASE_SERVICEPATH="https://mpat1.tdgroup.com/wireless/rest";
	//	static final String BASE_SERVICEPATH="https://wireless90.sys.td.com/wireless/rest";
	static final String BASE_SERVICEPATH="https://mobileonline.pilot.td.com/wireless/rest";

	public RestEndPoint() {
		
		super();
	}

	public static RestEndPoint getInstance() {
		if(instance==null){
			synchronized(RestEndPoint.class){
				if(instance==null)
					instance=new RestEndPoint();
			}
		}
		return instance;
	}

	public String getLogin(){
		return BASE_SERVICEPATH+"/anonymous/authentication/v1/login";

		// return SYS90_SERVICEPATH+"/authentication/v1/login";
	}

	public String getFinancialSummary(){
		return BASE_SERVICEPATH+"/accountinquiry/v1/getfinancialsummary";
	}

	public String getAccessCard(){
		return BASE_SERVICEPATH+"/billpayment/v1/getaccesscards";
	}

	public String getPaymentParties(){
		return BASE_SERVICEPATH+"/billpayment/v1/getpaymentparties";
	}

	public String confirmPayment(){
		return BASE_SERVICEPATH+"/billpayment/v1/confirmpayment";
	}

	public String sendPayment(){
		return BASE_SERVICEPATH+"/billpayment/v1/sendpayment";
	}

	public String getCreditCardAccountDetails(){
		return BASE_SERVICEPATH+"/accountinquiry/v1/getcreditcardaccountdetails";
	}

	public String getDepositAccountDetails(){
		return BASE_SERVICEPATH+"/accountinquiry/v1/getdepositaccountdetails";
	}
	public String getUpcomingBills(){
		return BASE_SERVICEPATH+"/billpayment/v1/getupcomingpayments";
	}
	public String cancelUpcomingBill(){
		return BASE_SERVICEPATH+"/billpayment/v1/cancelupcomingpayment";
	}
	public String getVerifyMfa(){
		return BASE_SERVICEPATH+"/authentication/v1/verifymfa";
		// return SYS90_SERVICEPATH+"/authentication/v1/verifymfa";
	}

	public String getReceiveAccounts(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/getreceiveaccounts";
	}

	public String getConfirmationEMTReceive(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/getconfirmationemtreceive";
	}

	public String submitDepositEMTReceive(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/submitdepositemtreceive";
	}

	public String getConfirmationEMTDecline(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/getconfirmationemtdecline";
	}

	public String submitEMTDecline(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/submitemtdecline";
	}

	public String getEMTSenders(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/getemtsenders";
	}

	public String getEMTSendParties(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/getemtsendparties";
	}

	public String confirmEMTSend(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/confirmemtsend";
	}

	public String submitEMTSend(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/submitemtsend";
	}

	public String getPendingEMTSenders(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/getpendingemtsenders";
	}

	public String getPendingEMTList(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/getpendingemtlist";
	}

	public String getReclaimAccounts(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/getreclaimaccounts";
	}

	public String submitDepositEMTReclaim(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/submitdepositemtreclaim";
	}

	public String verifyReceiverEMTReceive(){
		return BASE_SERVICEPATH+"/emailmoneytransfer/v1/verifyreceiveremtreceive";
	}

	public String getFromAccounts() {
		return BASE_SERVICEPATH+"/fundtransfer/v1/getfromaccounts";
	}

	public String getToAccounts() {
		return BASE_SERVICEPATH+"/fundtransfer/v1/gettoaccounts";
	}

	public String getConfirmFundTransfer() {
		return BASE_SERVICEPATH+"/fundtransfer/v1/confirmfundtransfer";
	}

	public String getMakeFundTransfer() {
		return BASE_SERVICEPATH+"/fundtransfer/v1/makefundtransfer";
	}

	public String getLogout() {
		return BASE_SERVICEPATH+"/anonymous/authentication/v1/logout";
	}

	public String getBrokerageAccountDetails() {
		return BASE_SERVICEPATH+"/investmentinquiry/v1/getbrokerageaccountdetail";
	}

	public String getTradingActivities() {
		return BASE_SERVICEPATH+"/investmentinquiry/v1/gettradingactivities";
	}

	public String getHoldings() {
		return BASE_SERVICEPATH+"/investmentinquiry/v1/getholdings";
	}

	public String getOrdersList() {
		return BASE_SERVICEPATH+"/investmentinquiry/v1/getorderlist";
	}

	public String getOrderDetails() {
		return BASE_SERVICEPATH+"/investmentinquiry/v1/getorderdetails";
	}

	public String getMarketData() {
		// TODO Auto-generated method stub
		return "https://wireless90.dev.td.com/waw/brk/md/mobile/autosuggest?q=";
	}

}

