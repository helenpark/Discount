package com.td.mobile.model;

public class AccountHelper {

	public static final String BANKING_CODE = "B";
	public static final String CREDIT_CODE = "C";
	public static final String INVESTMENT_CODE=  "I"; 

	public static final String ACCOUNT_TYPE_DEPOSIT = "D";
	public static final String CURRENCY_CAD = "CAD";
	public static final String CURRENCY_USD = "USD";


	public static enum AccountType{

		Deposit,Credit,LOC
	}

	public static enum CurrecnyType{

		CAD,USD
	}


	public static AccountType getAccountType(Account account){

		AccountType type = null;
		String classificationCode = account.getClassificationCD();
		String typeCode = account.getAccountTypeCD();
		if(classificationCode == null )
			return null;


		if(classificationCode.equals(BANKING_CODE) && typeCode != null && typeCode.equals(ACCOUNT_TYPE_DEPOSIT)){
			type = AccountType.Deposit;
		}
		else if(classificationCode.equals(CREDIT_CODE) && account.getAccountAttributes().isVISAInd()){
			type = AccountType.Credit;
		}
		else if(classificationCode.equals(CREDIT_CODE) && account.getAccountAttributes().isLiabilityInd() && typeCode.equals(ACCOUNT_TYPE_DEPOSIT) && !account.getAccountAttributes().isVISAInd()){
			type = AccountType.LOC;
		}

		return type;
	}


	public static CurrecnyType getCurrencyType(Account account){

		CurrecnyType currency = null;
		String currencyCode = account.getCurrencyCD();
		if(currencyCode == null)
			return null;
		if(currencyCode.equals(CURRENCY_CAD)){
			currency = CurrecnyType.CAD;
		}
		else if (currencyCode.equals(CURRENCY_USD)){
			currency = CurrecnyType.USD;
		}
		return currency;
	}


}
