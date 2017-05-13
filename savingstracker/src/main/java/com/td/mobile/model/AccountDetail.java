package com.td.mobile.model;

import java.util.ArrayList;

public class AccountDetail {
	
	
	private SummaryInfo summaryInfo;
	private ArrayList<AccountActivity> accountActivity;
	private boolean HasAdditionalActivity;
	
	public SummaryInfo getSummaryInfo() {
		return summaryInfo;
	}
	public void setSummaryInfo(SummaryInfo summaryInfo) {
		this.summaryInfo = summaryInfo;
	}
	public ArrayList<AccountActivity> getAccountActivity() {
		return accountActivity;
	}
	public void setAccountActivity(ArrayList<AccountActivity> accountActivity) {
		this.accountActivity = accountActivity;
	}

	public static class JasonLabels{
		
		public static final String summaryInfo = "SummaryInfo";
		public static final String accountActivity = "AccountActivity";
		public static final String accountDetailResponseJasonObject = "SvcAccountInquiryGetDepositAccountDetailsRs";
		
	}

	public boolean isHasAdditionalActivity() {
		return HasAdditionalActivity;
	}
	public void setHasAdditionalActivity(boolean hasAdditionalActivity) {
		HasAdditionalActivity = hasAdditionalActivity;
	}
	
	
	
}
