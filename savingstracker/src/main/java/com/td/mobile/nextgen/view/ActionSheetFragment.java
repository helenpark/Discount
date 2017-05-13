package com.td.mobile.nextgen.view;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.td.innovate.savingstracker.R;
import com.td.mobile.utils.Consts;
import com.td.mobile.utils.TDLog;

abstract public class ActionSheetFragment<T> extends DialogFragment {
	private boolean editMode = false;
	private boolean hasEditMode = false;
	private T mResult = null;
	private LinearLayout commandButton = null;
	private OnClickListener commandButtonListener = null;
	private Button headerButton = null;
	private CustomTitleConfig customTitleConfig;
	private CustomContentConfig customContentConfig;
	private ActionSheetArrayAdapter<T> adapter;
	private View mContentView = null;

	static public class CustomTitleConfig {

		protected int layoutResource;
		protected int buttonResource;
		protected String nonEditLblResource;
		protected String editLblResource;

		protected OnClickListener onClickListener;

		public CustomTitleConfig(int layoutResource, int buttonResource,
				OnClickListener onClickListener) {
			super();
			this.layoutResource = layoutResource;
			this.buttonResource = buttonResource;
			this.onClickListener = onClickListener;
		}

		public String getNonEditLblResource() {
			return nonEditLblResource;
		}

		public void setNonEditLblResource(String nonEditLblResource) {
			this.nonEditLblResource = nonEditLblResource;
		}

		public String getEditLblResource() {
			return editLblResource;
		}

		public void setEditLblResource(String editLblResource) {
			this.editLblResource = editLblResource;
		}

		public OnClickListener getOnClickListener() {
			return onClickListener;
		}

		public void setOnClickListener(OnClickListener onClickListener) {
			this.onClickListener = onClickListener;
		}

	}

	static public class CustomContentConfig {
		protected int layoutResource;
		protected int listViewResource;

		public CustomContentConfig(int layoutResource, int listViewResource) {
			super();
			this.layoutResource = layoutResource;
			this.listViewResource = listViewResource;
		}
	}

	public ActionSheetFragment() {
		super();
	}

	public ActionSheetFragment(boolean hasEditMode,
			CustomTitleConfig customTitleConfig,
			CustomContentConfig customContentConfig,
			ActionSheetArrayAdapter<T> adapter) {
		super();
		this.hasEditMode = hasEditMode;
		this.customTitleConfig = customTitleConfig;
		this.customContentConfig = customContentConfig;
		this.adapter = adapter;

	}

	public ActionSheetFragment(View contentView) {
		super();
		this.hasEditMode = false;
		this.customTitleConfig = null;
		this.customContentConfig = null;
		this.adapter = null;
		this.mContentView = contentView;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(),
				android.R.style.Theme_Holo_Dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
		if (getCustomTitleConfig() != null) {
			builder.setCustomTitle(getCustomTitleView());
		}
		ActionSheetArrayAdapter<T> adapter = getAdapter();
		if (getCustomContentConfig() == null && mContentView == null) {
			builder.setSingleChoiceItems(adapter, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							TDLog.d(Consts.LOG_TAG, "selection=" + which);
							ActionSheetArrayAdapter<T> adapter = getAdapter();
							mResult = adapter.getItem(which);
							adapter.setSelection(which);
							AsyncTask<T, Void, T> task = new AsyncTask<T, Void, T>() {

								@Override
								protected T doInBackground(T... params) {
									/*
									 * try {
									 * TDLog.d(Consts.LOG_TAG,"Thread sleep");
									 * Thread.sleep(0); } catch
									 * (InterruptedException e) { }
									 */
									return null;
								}

								@Override
								protected void onPostExecute(T result) {
									ActionSheetFragment.this
											.onDismissWithResult(
													ActionSheetFragment.this
															.getDialog(),
													mResult);
								}

							};
							task.execute();
						}
					});
		} else if (getCustomContentConfig() != null && mContentView == null) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View view = inflater.inflate(
					getCustomContentConfig().layoutResource, null);
			commandButton = (LinearLayout) view
					.findViewById(R.id.commandButton);

			ListView listView = (ListView) view
					.findViewById(getCustomContentConfig().listViewResource);
			int k = getAdapter().getCount();
			if (k > 3) {
				ViewGroup.LayoutParams lp = listView.getLayoutParams();
				lp.height = 150 * 3;
				listView.setLayoutParams(lp);
			}
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View arg1,
						int position, long arg3) {
					ActionSheetArrayAdapter<T> adapter = getAdapter();
					mResult = adapter.getItem(position);
					adapter.setSelection(position);
					AsyncTask<T, Void, T> task = new AsyncTask<T, Void, T>() {

						@Override
						protected T doInBackground(T... params) {
							/*
							 * try { TDLog.d(Consts.LOG_TAG,"Thread sleep");
							 * Thread.sleep(0); } catch (InterruptedException e)
							 * { }
							 */
							return null;
						}

						@Override
						protected void onPostExecute(T result) {
							if (isHasEditMode() && isEditMode()) {
								ActionSheetFragment.this
										.onDismissWithEditResult(
												ActionSheetFragment.this
														.getDialog(), mResult);
							} else {
								ActionSheetFragment.this.onDismissWithResult(
										ActionSheetFragment.this.getDialog(),
										mResult);
							}
						}

					};
					task.execute();
				}
			});
			if (getCommandButton() != null
					&& getCommandButtonListener() != null) {
				getCommandButton().setOnClickListener(
						getCommandButtonListener());
			}
			builder.setView(view);
		} else if (mContentView != null) {
			builder.setView(mContentView);
		}

		AlertDialog dialog = builder.create();
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		window.requestFeature(DialogFragment.STYLE_NO_TITLE);
		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);

		getFragmentManager().popBackStack();
	}

	public CustomTitleConfig getCustomTitleConfig() {
		return customTitleConfig;
	}

	public CustomContentConfig getCustomContentConfig() {
		return customContentConfig;
	}

	protected View getCustomTitleView() {
		CustomTitleConfig headerConfig = getCustomTitleConfig();
		int titleResource = headerConfig.layoutResource;
		int buttonResource = headerConfig.buttonResource;
		OnClickListener listener = headerConfig.onClickListener;

		LayoutInflater inflater = getActivity().getLayoutInflater();
		// View
		// header=inflater.inflate(R.layout.bills_actionsheet_header_accesscard,
		// null);
		View header = inflater.inflate(titleResource, null);
		headerButton = (Button) header.findViewById(buttonResource);
		if (listener == null) {
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					ActionSheetFragment.this.dismiss();
				}
			};
		}
		headerButton.setOnClickListener(listener);
		return header;
	}

	public ActionSheetArrayAdapter<T> getAdapter() {
		return adapter;
	};

	protected void onDismissWithEditResult(DialogInterface dialog, T result) {
	}

	abstract protected void onDismissWithResult(DialogInterface dialog, T result);

	public void showFragment(FragmentManager fragmentManager) {
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.add(this, "dialog");
		fragmentTransaction.commit();
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void limitReachedAlert(Context cxt) {

		AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
		builder.setTitle(cxt.getString(R.string.AcessCardsLimitDialogTitle));
		builder.setCancelable(false);
		builder.setMessage(cxt.getString(R.string.AcessCardsLimitDialogMessage))
				.setPositiveButton(
						cxt.getString(R.string.AcessCardsLimitDialogButtonText),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}

						});

		builder.show();

	}

	public void setEditMode(boolean editMode) {
		if (isHasEditMode() && this.editMode != editMode) {
			if (editMode) {
				headerButton.setText(customTitleConfig.getNonEditLblResource());
				if (commandButton != null) {
					commandButton.setVisibility(View.VISIBLE);
				}
				adapter.setEditMode(true);
			} else {
				headerButton.setText(customTitleConfig.getEditLblResource());
				adapter.setEditMode(false);
			}
			this.editMode = editMode;
		}
	}

	public boolean isHasEditMode() {
		return hasEditMode;
	}

	public void setHasEditMode(boolean hasEditMode) {
		this.hasEditMode = hasEditMode;
	}

	public OnClickListener getCommandButtonListener() {
		return commandButtonListener;
	}

	public void setCommandButtonListener(
			OnClickListener commandButtonListener) {
		this.commandButtonListener = commandButtonListener;
	}

	public LinearLayout getCommandButton() {
		return commandButton;
	}

	public void setCommandButton(LinearLayout commandButton) {
		this.commandButton = commandButton;
	}

	public void setAdapter(ActionSheetArrayAdapter<T> adapter) {
		this.adapter = adapter;
	}

	public View getmContentView() {
		return mContentView;
	}

	public void setmContentView(View mContentView) {
		this.mContentView = mContentView;
	}

}
