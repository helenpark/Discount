package com.td.mobile.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AccountActivity{

	private String Date;
	private Double  Amount;
	private String Description;
	private Integer Statement;

	public AccountActivity(){	
	}

	public AccountActivity(String date  ,Double amount , String description){	

		Date = date;
		Amount =  amount;
		Description = description;

	}

	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public Double getAmount() {
		return Amount;
	}
	public void setAmount(Double amount) {
		Amount = amount;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}

	public Integer getStatement() {
		return Statement;
	}

	public void setStatement(Integer statement) {
		Statement = statement;
	}

	public static enum Order{

		ASCENDING , DESCENDING 
	}

	public static enum SortingColumn{

		DATE , DESCRIPTION , AMOUNT ,RECIPIENT
	}

	public static void sort(final SortingColumn col, final Order order , List<AccountActivity> activity){

		Collections.sort((List<AccountActivity>) activity, new Comparator<AccountActivity>() {

			@Override
			public int compare(AccountActivity lhs, AccountActivity rhs) {

				if (col.equals(SortingColumn.AMOUNT)){
/*
					if(order.equals(Order.ASCENDING)){
						return lhs.getAmount().compareTo(rhs.getAmount());
					}
					else {
						return ( (lhs.getAmount().compareTo(rhs.getAmount())) * -1 );
					}
					*/
					
					if(order.equals(Order.ASCENDING))
						return (int) (Math.abs(lhs.getAmount())-Math.abs(rhs.getAmount()));
					else
						return (int )((Math.abs(lhs.getAmount())-Math.abs(rhs.getAmount())) * -1) ;
				}
				else if (col.equals(SortingColumn.DATE)){

					if(order.equals(Order.ASCENDING)){
						return lhs.getDate().compareTo(rhs.getDate());
					}
					else {
						return ( (lhs.getDate().compareTo(rhs.getDate())) * -1 );
					}
				}
				else if (col.equals(SortingColumn.DESCRIPTION)){

					if(order.equals(Order.ASCENDING)){
						return lhs.getDescription().compareTo(rhs.getDescription());
					}
					else {
						return ( (lhs.getDescription().compareTo(rhs.getDescription())) * -1 );
					}
				}
				return 0;
			}
		});
	}

}