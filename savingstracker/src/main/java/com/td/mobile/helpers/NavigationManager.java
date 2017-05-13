package com.td.mobile.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

//import com.td.mobile.controllers.AccountDetailActivity;
//import com.td.mobile.controllers.AccountsSummaryController;
//import com.td.mobile.etransfers.ETransferCancelActivity;
//import com.td.mobile.etransfers.ETransferCancelConfirmActivity;
//import com.td.mobile.etransfers.ETransferCancelReceiptActivity;
//import com.td.mobile.etransfers.ETransferCancelReclaimDepositToActivity;
//import com.td.mobile.etransfers.ETransferDeclineActivity;
//import com.td.mobile.etransfers.ETransferDeclineConfirmActivity;
//import com.td.mobile.etransfers.ETransferDeclineReceiptActivity;
//import com.td.mobile.etransfers.ETransferPendingTransfersActivity;
//import com.td.mobile.etransfers.ETransferReceiveConfirmActivity;
//import com.td.mobile.etransfers.ETransferReceiveReceiptActivity;
//import com.td.mobile.etransfers.ETransferSendActivity;
//import com.td.mobile.etransfers.ETransferSendConfirmActivity;
//import com.td.mobile.etransfers.ETransferSendReceiptActivity;
//import com.td.mobile.etransfers.ETransferSetupActivity;
import com.td.mobile.controllers.AccountDetailActivity;
import com.td.mobile.controllers.AccountsSummaryController;
import com.td.mobile.mfa.MfaActivity;
import com.td.mobile.mfa.MfaAuthDeniedActivity;
import com.td.mobile.mfa.MfaHelper;
import com.td.mobile.mfa.MfaHelper.MfaCodes;
import com.td.mobile.mfa.MfaSetupRequiredActivity;
import com.td.mobile.nextgen.restful.Session;
import com.td.mobile.utils.Utils;

public class NavigationManager {

	public static void launchAccountDetailActivity(Context context ,int accountIndex){

		Intent intent = new Intent(context, AccountDetailActivity.class);
		intent.putExtra(AccountDetailActivity.SELECTED_ACCOUNT_INDEX,accountIndex);
		Utils.startActivity(context, intent);

	}

	public static void launchMfaActivity(Context context , MfaHelper mfaHelper){

		Class c = null;

		if(mfaHelper.state == MfaCodes.REQUIRED){
			c = MfaActivity.class;
		}
		else if (mfaHelper.state == MfaCodes.BLOCKED){
			c = MfaAuthDeniedActivity.class;
		}
		else if(mfaHelper.state == MfaCodes.SETUP_REQUIRED){
			c = MfaSetupRequiredActivity.class;
		} else if (mfaHelper.state == MfaCodes.TIMEOUT){
			Session.getInstance().sessionTimeout(context);
		}
		if(c!=null){
			Intent intent = new Intent(context, c);
			intent.putExtra(MfaActivity.MFA_EXTRA, mfaHelper);
			Utils.startActivity(context, intent);
		}
	}



	public static void launchAccountSummarActivity(Context context){

		Utils.startActivity(context, AccountsSummaryController.class);
	}


//	public static void launchEtransferSendConfirmActivity(Context context , Bundle args){
//
//		Intent intent = new Intent(context, ETransferSendConfirmActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//
//	public static void launchEtransferSendReceiptActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferSendReceiptActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//	public static void launchEtransferSendActivity(Context context){
//
//		Utils.startActivity(context, ETransferSendActivity.class);
//	}
//
//	public static void launchEtransferPendingTransfersActivity(Context context){
//
//		Utils.startActivity(context, ETransferPendingTransfersActivity.class);
//	}
//
//	public static void launchEtransferCancelActivity(Context context , Bundle args){
//
//		Intent intent = new Intent(context, ETransferCancelActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//
//	}
//
//	public static void launchEtransferCancelConfirmActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferCancelConfirmActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//	public static void launchEtransferReclaimCancelToActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferCancelReclaimDepositToActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//	public static void launchEtransferCancelReceiptActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferCancelReceiptActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//	public static void launchEtransferReceiveConfirmActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferReceiveConfirmActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//	public static void launchEtransferReceiveReceiptActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferReceiveReceiptActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//	public static void launchEtransferDeclineActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferDeclineActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//	public static void launchEtransferDeclineConfirmActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferDeclineConfirmActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//	public static void launchEtransferDeclineReceiptActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferDeclineReceiptActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
//
//	public static void launchEtransferSetupActivity(Context context,Bundle args){
//
//		Intent intent = new Intent(context, ETransferSetupActivity.class);
//		intent.putExtras(args);
//		Utils.startActivity(context, intent);
//	}
}
