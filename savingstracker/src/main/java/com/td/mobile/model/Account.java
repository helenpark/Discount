package com.td.mobile.model;

import java.io.Serializable;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	private String AccountID;
	private double AccountBalance;
	private String AccountNO;
	private String AccountDescription;
	private String BalanceSatusCD;

	private double availableCredit;


	private String AccountTypeCD;
	private String CurrencyCD;
	private String ClassificationCD;
	private boolean ShowDetiailsIND;
	private AccountAttributes AccountAttributes;

	public String getAccountID() {
		return AccountID;
	}


	public void setAccountID(String accountID) {
		AccountID = accountID;
	}


	public double getAccountBalance() {
		return AccountBalance;
	}


	public void setAccountBalance(double accountBalance) {
		AccountBalance = accountBalance;
	}


	public String getAccountNO() {
		return AccountNO;
	}


	public void setAccountNO(String accountNO) {
		AccountNO = accountNO;
	}


	public String getAccountDescription() {
		return AccountDescription;
	}

	public String getAccountDescWithNumber(){
		return getAccountDescription()+" "+getAccountNO();
	}

	public void setAccountDescription(String accountDescription) {
		AccountDescription = accountDescription;
	}


	public String getBalanceSatusCD() {
		return BalanceSatusCD;
	}


	public void setBalanceSatusCD(String balanceSatusCD) {
		BalanceSatusCD = balanceSatusCD;
	}


	public String getAccountTypeCD() {
		return AccountTypeCD;
	}


	public void setAccountTypeCD(String accountTypeCD) {
		AccountTypeCD = accountTypeCD;
	}


	public String getCurrencyCD() {
		return CurrencyCD;
	}


	public void setCurrencyCD(String currencyCD) {
		CurrencyCD = currencyCD;
	}


	public String getClassificationCD() {
		return ClassificationCD;
	}


	public void setClassificationCD(String classificationCD) {
		ClassificationCD = classificationCD;
	}


	public boolean isShowDetiailsIND() {
		return ShowDetiailsIND;
	}


	public void setShowDetiailsIND(boolean showDetiailsIND) {
		ShowDetiailsIND = showDetiailsIND;
	}


	public AccountAttributes getAccountAttributes() {
		return AccountAttributes;
	}


	public void setAccountAttributes(AccountAttributes accountAttributes) {
		AccountAttributes = accountAttributes;
	}


	public static class AccountAttributes implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private boolean AssetInd;
		private boolean LiabilityInd;
		private boolean VISAInd;

		public boolean isAssetInd() {
			return AssetInd;
		}
		public void setAssetInd(boolean assetInd) {
			AssetInd = assetInd;
		}
		public boolean isLiabilityInd() {
			return LiabilityInd;
		}
		public void setLiabilityInd(boolean liabilityInd) {
			LiabilityInd = liabilityInd;
		}
		public boolean isVISAInd() {
			return VISAInd;
		}
		public void setVISAInd(boolean vISAInd) {
			VISAInd = vISAInd;
		}		
	}

	public double getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(double availableCredit) {
		this.availableCredit = availableCredit;
	}

	@Override
	public boolean equals(Object o) {
		boolean aRtn=false;
		try {
			Account aAcct = (Account)o;
			aRtn = aAcct.getAccountID().equalsIgnoreCase(getAccountID());
		} catch (Exception e) {
		}
		return aRtn;
	}

}

