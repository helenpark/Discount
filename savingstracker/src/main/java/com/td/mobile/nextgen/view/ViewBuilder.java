package com.td.mobile.nextgen.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.td.innovate.savingstracker.R;

public class ViewBuilder {

	private ViewBuilder() {
		
	}
	
	static public ProgressDialog buildProgressSpinner(Context ctx){
		ProgressDialog aDialog=new ProgressDialog(ctx);
		aDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		aDialog.setMessage(ctx.getString(R.string.loading));
		aDialog.setIndeterminate(true);
		aDialog.setCancelable(false);
		aDialog.setCanceledOnTouchOutside(false);
	    return aDialog;
	}
	
//	static public ProgressDialog buildProgressSpinner(Context ctx, ICancelHandler handler){
//		CancelableProgressDialog aDialog=new ViewBuilder().new CancelableProgressDialog(ctx, handler);
//		aDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		aDialog.setMessage(ctx.getString(R.string.loading));
//		aDialog.setIndeterminate(true);
//	    return (ProgressDialog) aDialog;
//	}
	
//	private class CancelableProgressDialog extends ProgressDialog {
//		private ICancelHandler handler;
//		/*
//		private OnCancelListener cancelListener = new OnCancelListener() {
//
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				if (handler != null)
//					handler.onCancel();
//			}};
//		*/
//		private OnDismissListener dismissListener = new OnDismissListener() {
//
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				if (handler != null)
//					handler.onCancel();
//			}
//
//		};
//
//		private CancelableProgressDialog(Context ctx, ICancelHandler handler) {
//			super(ctx);
//			this.handler = handler;
////			this.setOnCancelListener(cancelListener);
//			this.setOnDismissListener(dismissListener);
//		}
//	}
}
