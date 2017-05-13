package com.td.mobile.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.innovate.savingstracker.R;
import com.td.mobile.controllers.LoginController;
//import com.td.wsw.services.investmentinquiry.rest.model.common.JSONDollar;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Utils {
	private static String mUniqueID = null;
	private static boolean mBypassLogin = false;
	private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

	public synchronized static String getUniqueMessgaeId(Context context) {
		if (mUniqueID == null) {
			SharedPreferences sharedPrefs = context.getSharedPreferences(
					PREF_UNIQUE_ID, Context.MODE_PRIVATE);
			mUniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
			if (mUniqueID == null) {
				mUniqueID = UUID.randomUUID().toString();
				Editor editor = sharedPrefs.edit();
				editor.putString(PREF_UNIQUE_ID, mUniqueID);
				editor.commit();
			}
		}
		return mUniqueID;
	}

	public static String getMessageID(){
		return mUniqueID;
	}

	public static void  setMessageID(String newID){
		mUniqueID = newID;
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView, double adjustment) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);

			int itemHeight = view.getMeasuredHeight();

			totalHeight += (itemHeight + (itemHeight*adjustment));
		}
		LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static boolean detectTap(Context cxt, MotionEvent event) {
		int eventaction = event.getAction();
		if (eventaction == MotionEvent.ACTION_DOWN) {

			// get system current milliseconds
			long time = System.currentTimeMillis();
			long startMillis = 0;
			int count = 0;

			// if it is the first time, or if it has been more than 2 seconds
			// since the first tap ( so it is like a new try), we reset
			// everything
			if (startMillis == 0 || (time - startMillis > 2000)) {
				startMillis = time;
				count = 1;
			}
			// it is not the first, and it has been less than 3 seconds since
			// the first
			else { // time-startMillis< 2000
				count++;
			}

			if (count == 3) {
				Toast.makeText(cxt, "local json",
						Toast.LENGTH_LONG).show();
				return true;
			} 

		}
		return false;
	}

	public static boolean getBypassLogin() {
		// TODO Auto-generated method stub
		 //return mBypassLogin && Consts.LOCAL_JSON;
		return false;
	}
	 
	public static void setBypassLogin(boolean value) {
		mBypassLogin = value;
	}

	

	public static Date convertStringtoDate(String dateString){
		return convertStringtoDate(dateString, "yyyyMMdd");
	}

	public static Date convertStringtoDate(String dateString, String pattern){
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedDate;
	}

	public static String convertDatetoString(Date aDate){
		return convertDatetoString(aDate, "yyyyMMdd");
	}
	
	public static String convertDatetoString(Date aDate, String pattern){
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(aDate);
	}

	public static String convertDateToDateDisplay(Date aDate){
		SimpleDateFormat aFormat=new SimpleDateFormat("MMM dd, yyyy");
		return aFormat.format(aDate);
	}
	
	public static String convertStringToDateDisplay(String dateString){
		Date aDate=convertStringtoDate(dateString);
		return (aDate!=null) ? convertDateToDateDisplay(aDate): "";		
	}
	
	public static String getFormattedAmount(double amount, String currency) {

		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
		String formattedBalance = format.format(amount);
		if (currency.equalsIgnoreCase("usd")) {
			formattedBalance = "USD " + formattedBalance;
		}

		return formattedBalance;
	}

	public static String getFormattedAmount(double amount) {
		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
		String formattedBalance = format.format(amount);
		if(amount< 0){

			formattedBalance=formattedBalance.substring(1,formattedBalance.length()-1);
			formattedBalance = "-" + formattedBalance;
		}
		return formattedBalance;
	}

	static public int getResourceIDByName(Context ctx, String aString) {
		if(aString==null){
			return R.string.UNKNOWN;
		}
		String packageName = ctx.getPackageName();
		int resId = ctx.getResources().getIdentifier(aString, "string", packageName);
		return resId;
	}	
	
	static public SpannableStringBuilder formatCurrencyDisplay(double amount, String currency) {
		 if(currency.equalsIgnoreCase("usd")){
			return Utils.formatUSDCurrency(amount);
		 }
		 else
			 return Utils.colorAmountText(Utils.getFormattedAmount(amount), amount);
	}
	
	static public SpannableStringBuilder formatCANCurrency(double totalCAN) {
		return new SpannableStringBuilder("CAD ").append(Utils.colorAmountText(Utils.getFormattedAmount(totalCAN), totalCAN));
	}

	static public SpannableStringBuilder formatUSDCurrency(double totalUSD) {
			return new SpannableStringBuilder("USD ").append(Utils.colorAmountText(Utils.getFormattedAmount(totalUSD), totalUSD));
	}
	
	static public SpannableStringBuilder colorAmountText(String formattedAmount, double amount) {
		SpannableStringBuilder sb = new SpannableStringBuilder(formattedAmount);
		ForegroundColorSpan fcs;
		if (amount >= 0)
			fcs = new ForegroundColorSpan(Color.BLACK); 
		else
			fcs = new ForegroundColorSpan(Color.RED); 
	
		sb.setSpan(fcs, 0, formattedAmount.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
	
		return sb;
	
	}
	
	static public void startActivity(Context context, Class activity) {
		Intent intent = new Intent(context, activity);
		startActivity(context, intent);
	}

	
	static public void startLoginActivity(Context context, Class target) {
		Intent intent = new Intent(context, LoginController.class);
		intent.putExtra("targetClass", target);
		startActivity(context, intent);
	}
	
	static public void startActivity(Context context, Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
	
	static public String convertEstTimeToStatementDate(String estDate) {
		Date dt = Utils.convertStringtoDate(estDate, "yyyy-MM-dd'T'HH:mm:ssZ");
		return Utils.convertDatetoString(dt, "MMM dd, yyyy h:mm a");
	}
	
	
//	static public void setAmount(TextView view, JSONDollar amount) {
//		if (amount.getCurrency().equals(Consts.USD)) {
//			view.setText(Utils.formatUSDCurrency(amount.getAmount().doubleValue()));
//		}
//		else
//			view.setText(Utils.formatCANCurrency(amount.getAmount().doubleValue()));
//	}
//
//	static public void setRefreshTimeStamp(Activity activity, TextView timeStamp, String date) {
//		String time = Utils.convertEstTimeToStatementDate(date);
//		timeStamp.setText(activity.getResources().getString(R.string.str_Time_Stamp_substition, time));
//	}
}
