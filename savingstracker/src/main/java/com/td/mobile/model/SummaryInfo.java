package com.td.mobile.model;


public class SummaryInfo{

	private double Balance;
	private double AvailableCredit;
	private double AvailableBalance;
	private boolean CanTransferFrom;
	private boolean CanTransferTo;
	private double MinimumPayment;
	private double LastPayment;
	private double InterestRate;
	private String AccountID;
	private String AccountNO;
	
	
	
	
	public double getMinimumPayment() {
		return MinimumPayment;
	}
	public void setMinimumPayment(double minimumPayment) {
		MinimumPayment = minimumPayment;
	}
	public double getLastPayment() {
		return LastPayment;
	}
	public void setLastPayment(double lastPayment) {
		LastPayment = lastPayment;
	}
	public double getInterestRate() {
		return InterestRate;
	}
	public void setInterestRate(double interestRate) {
		InterestRate = interestRate;
	}
	public double getBalance() {
		return Balance;
	}
	public void setBalance(double balance) {
		Balance = balance;
	}
	public double getAvailableBalance() {
		return AvailableBalance;
	}
	public void setAvailableBalance(double availableBalance) {
		AvailableBalance = availableBalance;
	}
	public boolean isCanTransferFrom() {
		return CanTransferFrom;
	}
	public void setCanTransferFrom(boolean canTransferFrom) {
		CanTransferFrom = canTransferFrom;
	}
	public boolean isCanTransferTo() {
		return CanTransferTo;
	}
	public void setCanTransferTo(boolean canTransferTo) {
		CanTransferTo = canTransferTo;
	}
	public String getAccountID() {
		return AccountID;
	}
	public void setAccountID(String accountID) {
		AccountID = accountID;
	}
	public String getAccountNO() {
		return AccountNO;
	}
	public void setAccountNO(String accountNO) {
		AccountNO = accountNO;
	}
	public double getAvailableCredit() {
		return AvailableCredit;
	}
	public void setAvailableCredit(double availableCredit) {
		AvailableCredit = availableCredit;
	}
	
}
