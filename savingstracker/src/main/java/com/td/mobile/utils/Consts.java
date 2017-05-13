package com.td.mobile.utils;

import java.util.HashMap;

public final class Consts {
	public static final String TIME = "time=";
	public static final String NUM_RES = "numresults=";
	public static final int  MAX_RES = 3;
	public static String TD_BASE_URL = "http://m.tdbank.com/net/get8.ashx?";
	public static String LONGTITUDE = "longitude=";
	public static String LATITUDE = "latitude=";
	public static String SEARCH_RADIUS = "searchradius=";
	public static String SEARCH_UNIT = "searchunit=";
	public static String LOCATION_TYPES = "locationtypes=";
	public static String NUM_RESULTSS = "numresults=";
	public static String JSON = "Json=y";
	public static final String GOOGLE_MAP_URL="http://maps.google.com/maps?daddr=";
	public static final String GOOGLE_MAP_CLASS="com.google.android.apps.maps";
	public static final String GOOGLE_MAP_ACTIVITY= "com.google.android.maps.MapsActivity";
	public static final String SEARCH_KEY_SHARED_PREFERENCES = "location_searches";
	public static final HashMap<String, String> ACCOUNT_CLASS;
	    static
	    {
	    	ACCOUNT_CLASS = new HashMap<String, String>();
	    	ACCOUNT_CLASS.put("B", "accountsColumnHeadersBanking");
	    	ACCOUNT_CLASS.put("I", "accountsColumnHeadersInvesting");
	    	ACCOUNT_CLASS.put("C", "accountsColumnHeadersCredit");
	    }
	public static final int ATM = 1;
	public static final int BRANCH = 2;
	public static final int BRANCH_ATM = 3;
	public static final int TDW = 4;
	public static final String POST = "post";
	public static final String GET = "get";
	public static final String LOG_TAG="TAG";
	public static final boolean LOCAL_JSON=false;
	public static final String SvcAuthenticateLoginRq="SvcAuthenticateLoginRq";
	public static final String CALL_PHONE_NUMBER_TEL_PREFIX = "tel:";

	public static final String DeviceID="DeviceID";
	public static final String MessageID = "MessageID";
	public static final int JELLY_BEAN=16;
	public static final String Cookie="Cookie";
	public static final String SetCookie="Set-Cookie";
	
	public static final String ERROR_UNKNOWN="UNKNOWN";
	public static final String INVALIDATE_SESSION_ERROR="302";
	public static final String EMT_SETUP_REQUIRED = "IMTUNREG";
	public static final String ACCOUNT = "ACCOUNT";
	public static final String USD="USD";
	public static final String CAD="CAD";
	public static final int PAGE_SELECTION_DELAY=500;
	public static final String BROKERAGE_ACCOUNT_INFO = "BROKERAGE_ACCOUNT_INFO";
}
