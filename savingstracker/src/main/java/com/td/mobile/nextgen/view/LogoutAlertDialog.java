package com.td.mobile.nextgen.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.td.innovate.savingstracker.R;
import com.td.mobile.nextgen.restful.RestCallAsyncTask;
import com.td.mobile.nextgen.restful.RestEndPoint;
import com.td.mobile.nextgen.restful.RestRequest;
import com.td.mobile.nextgen.restful.RestResponse;
import com.td.mobile.nextgen.restful.Session;
import com.td.mobile.utils.TDLog;
import com.td.mobile.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class LogoutAlertDialog {

	Activity activity;

	public LogoutAlertDialog(Activity activity) {
		this.activity = activity;
	}

	public void logout() {

		if (Session.getInstance().isUserLoggedIn()) {

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle(R.string.logoutConfirmPageHeader);
			builder.setMessage(R.string.logoutConfirmCopy)
					.setPositiveButton(R.string.logoutConfirmButtonLogout,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									logoutOnServer();

								}

							})
					.setNegativeButton(R.string.logoutConfirmButtonCancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User cancelled the dialog
								}
							});
			builder.show();

		} else {
			Toast.makeText(activity, "No User Logged In", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void logoutOnServer() {

		final ProgressDialog progress = ViewBuilder
				.buildProgressSpinner(activity);
		progress.show();

		String url = RestEndPoint.getInstance().getLogout();
		HashMap<String, String> params = new HashMap<String, String>();

		//params.put("MessageID", Utils.getUniqueMessgaeId(activity));
		params.put("NativeAppVersion", "6.1.1");
		params.put("LanguageCD", "EN");

		JSONObject playload = RestRequest.createPlayload(activity, params);
		RestRequest request = new RestRequest(url, RestRequest.SvcAuthenticateLogoutRq, playload);

		RestCallAsyncTask<RestResponse> task = new RestCallAsyncTask<RestResponse>(
				RestResponse.class, activity) {

			@Override
			protected void onPostExecute(RestResponse result) {
				//super.onPostExecute(result);

				progress.dismiss();
				try {
					/*if (result != null) {

					}*/
					
					Session.getInstance().logoutOnApplication();

					Utils.startActivity(activity, LogoutSuccessActivity.class);
					activity.finish();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					TDLog.e("Upcoming Bills parsing Json", e.getMessage(), e);
				}
			}

		};
		task.execute(request);

	}

}
