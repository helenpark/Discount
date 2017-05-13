package com.td.mobile.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.td.innovate.savingstracker.R;
import com.td.mobile.adapters.AccountActivityListAdapter;
import com.td.mobile.adapters.AccountActivitySectionAdapter;
import com.td.mobile.adapters.IListAdaptor;
import com.td.mobile.controllers.AccountDetailActivity;
import com.td.mobile.controllers.BaseController;
import com.td.mobile.helpers.AccountDetailRestHelper;
import com.td.mobile.listeners.IAccountDetailRestListener;
import com.td.mobile.managers.DataManager;
import com.td.mobile.model.Account;
import com.td.mobile.model.AccountActivity;
import com.td.mobile.model.AccountActivity.Order;
import com.td.mobile.model.AccountActivity.SortingColumn;
import com.td.mobile.model.AccountDetail;
import com.td.mobile.model.AccountHelper;
import com.td.mobile.model.AccountHelper.AccountType;
import com.td.mobile.model.AccountHelper.CurrecnyType;
//import com.td.mobile.nextgen.bills.PayBillsFragmentActivity;
import com.td.mobile.nextgen.restful.RestRequestListener;
import com.td.mobile.nextgen.restful.RestResponse;
//import com.td.mobile.nextgen.transfers.MyAccountsTransferFragmentActivity;
//import com.td.mobile.nextgen.transfers.TransferModes;
import com.td.mobile.nextgen.view.ViewBuilder;
import com.td.mobile.utils.TDLog;
import com.td.mobile.utils.Utils;

import java.text.NumberFormat;
import java.util.ArrayList;

public class AccountDetailPageFragment extends Fragment implements
		OnScrollListener, IAccountDetailRestListener {

	private static final String CLASS_NAME = AccountDetailPageFragment.class
			.getName();

	private View mView;
	private ViewGroup activityContent;
	private ViewGroup emptyContent;
	private ViewGroup errorContent;
	private TextView accountDescription;
	private TextView currentBalance;
	private TextView currentBalanceLabel;
	private TextView availableBalanceLabel;
	private TextView availableBalance;
	private TextView errorHeader;
	private TextView errorText;
	private ImageView dateSortImage;
	private ImageView amountSortImage;
	private ImageView descriptionSortImage;
	private ListView accountActivityList;
	private View dateHeader;
	private View amtHeader;
	private View descHeader;
//	private TextView loadMore;
	private IListAdaptor adapter;
	private TextView dateColumn, descriptionColumn, amountColumn;
	private Order dateColumnOrder, descriptionColumnOrder, amountColumnOrder;
//	private boolean shyMenuEnabled;
	private AccountType type;
	private CurrecnyType currecny;
	private Account account;
	private int startIndex = 0;
	private final int INITIAL_REQUEST_COUNT = 10;
	private final int ACTIVITIES_REQUEST_COUNT = 10;
	private LinearLayout pageIndicatorLayout;
	private String errorMessage;
	private int currentIndex=0;
	
	private OnClickListener loadMore = new OnClickListener() {

		@Override
		public void onClick(View v) {
			currentIndex = startIndex;
			requestAccountDetail(
					getActivity(),
					account,
					startIndex,
					ACTIVITIES_REQUEST_COUNT,
					type,
					(IAccountDetailRestListener) AccountDetailPageFragment.this);

		}};

	public static final String INDEX = "index";

	private int PageIndex;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TDLog.d(CLASS_NAME, String.format("onCreateView(%s) %s", account.getAccountNO(), PageIndex));

		mView = inflater.inflate(R.layout.fragment_account_detail_view,
				container, false);
		init();

		// View could already be destroyed. Reinitialized the view.
		updateUI();

		return mView;
	}

	private void init() {

//		shyMenuEnabled = false;

		
		accountDescription = (TextView) mView.findViewById(R.id.account_desc);
		currentBalance = (TextView) mView.findViewById(R.id.current_balance);
		
		errorContent = (ViewGroup) mView.findViewById(R.id.error_fragment);
		errorHeader = (TextView) mView.findViewById(R.id.error_header);
		errorText = (TextView) mView.findViewById(R.id.error_text);
		errorHeader.setText("");
		errorText.setText("");
		availableBalance = (TextView) mView
				.findViewById(R.id.available_balance);
		availableBalanceLabel = (TextView) mView
				.findViewById(R.id.available_balance_label);
		currentBalanceLabel = (TextView) mView
				.findViewById(R.id.current_balance_label);
//		loadMore = (TextView) mView.findViewById(R.id.loadMore);
		activityContent = (ViewGroup) mView
				.findViewById(R.id.account_activity_content);
		emptyContent = (ViewGroup) mView
				.findViewById(R.id.empty_activity_content);

		accountActivityList = (ListView) mView
				.findViewById(R.id.account_activity_list);
		dateHeader = (View) mView.findViewById(R.id.dateHeader);
		descHeader = (View) mView.findViewById(R.id.descHeader);
		amtHeader = (View) mView.findViewById(R.id.amtHeader);

		dateColumn = (TextView) mView.findViewById(R.id.dateColumn);
		descriptionColumn = (TextView) mView
				.findViewById(R.id.descriptionColumn);
		amountColumn = (TextView) mView.findViewById(R.id.amountColumn);

//		ImageView transferFrom = (ImageView) mView
//				.findViewById(R.id.transferFrom);
//		ImageView transferTo = (ImageView) mView.findViewById(R.id.transferTo);
//		ImageView payBill = (ImageView) mView.findViewById(R.id.payBill);
		
		dateSortImage = (ImageView) mView.findViewById(R.id.dateSortImage);
		descriptionSortImage = (ImageView) mView.findViewById(R.id.descriptionSortImage);
		amountSortImage = (ImageView) mView.findViewById(R.id.amountSortImage);
		descriptionSortImage.setVisibility(View.INVISIBLE);
		amountSortImage.setVisibility(View.INVISIBLE);

		dateColumnOrder = descriptionColumnOrder = amountColumnOrder = Order.ASCENDING;

		accountActivityList.setOnScrollListener(this);
//KY-
		/*
		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestAccountDetail(
						getActivity(),
						account,
						startIndex,
						ACTIVITIES_REQUEST_COUNT,
						type,
						(IAccountDetailRestListener) AccountDetailPageFragment.this);

			}
		});
*/




//		transferFrom.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				launchTransferActivity(TransferModes.FROM_MODE);
//			}
//		});
//
//		transferTo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				launchTransferActivity(TransferModes.TO_MODE);
//			}
//		});
//
//		payBill.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				launchPayBillActivity();
//			}
//		});

		OnClickListener dateOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.sort(
						SortingColumn.DATE,
						dateColumnOrder,
						DataManager.getInstance().getAccountsDetail()
								.get(account.getAccountNO())
								.getAccountActivity());
				dateColumnOrder = toogleOrder(dateColumnOrder);
				if (dateColumnOrder == Order.ASCENDING) {
					dateSortImage.setImageDrawable(getResources().getDrawable(R.drawable.sort_des));
				}
				else
					dateSortImage.setImageDrawable(getResources().getDrawable(R.drawable.sort_asc));
				dateHeader.setBackgroundColor(getResources().getColor(R.color.middle_green));
				amtHeader.setBackgroundColor(getResources().getColor(R.color.ultra_dark_green));
				descHeader.setBackgroundColor(getResources().getColor(R.color.ultra_dark_green));
				
				dateSortImage.setVisibility(View.VISIBLE);
				amountSortImage.setVisibility(View.INVISIBLE);
				descriptionSortImage.setVisibility(View.INVISIBLE);
				updateAdapter();
			}
		};
		
		dateColumn.setOnClickListener(dateOnClickListener);
		dateSortImage.setOnClickListener(dateOnClickListener);

		OnClickListener descriptionOnClickListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				adapter.sort(
						SortingColumn.DESCRIPTION,
						descriptionColumnOrder,
						DataManager.getInstance().getAccountsDetail()
								.get(account.getAccountNO())
								.getAccountActivity());
				descriptionColumnOrder = toogleOrder(descriptionColumnOrder);
				if (descriptionColumnOrder == Order.ASCENDING) {
					descriptionSortImage.setImageDrawable(getResources().getDrawable(R.drawable.sort_des));
				}
				else
					descriptionSortImage.setImageDrawable(getResources().getDrawable(R.drawable.sort_asc));
				descHeader.setBackgroundColor(getResources().getColor(R.color.middle_green));
				amtHeader.setBackgroundColor(getResources().getColor(R.color.ultra_dark_green));
				dateHeader.setBackgroundColor(getResources().getColor(R.color.ultra_dark_green));
				
				descriptionSortImage.setVisibility(View.VISIBLE);
				amountSortImage.setVisibility(View.INVISIBLE);
				dateSortImage.setVisibility(View.INVISIBLE);
				updateAdapter();
			}
		};
		descriptionColumn.setOnClickListener(descriptionOnClickListener);
		descriptionSortImage.setOnClickListener(descriptionOnClickListener);
		
		OnClickListener amountOnClickListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				adapter.sort(
						SortingColumn.AMOUNT,
						amountColumnOrder,
						DataManager.getInstance().getAccountsDetail()
								.get(account.getAccountNO())
								.getAccountActivity());
				amountColumnOrder = toogleOrder(amountColumnOrder);
				amountSortImage.setVisibility(View.VISIBLE);
				if (amountColumnOrder == Order.ASCENDING) {
					amountSortImage.setImageDrawable(getResources().getDrawable(R.drawable.sort_des));
				}
				else
					amountSortImage.setImageDrawable(getResources().getDrawable(R.drawable.sort_asc));
				amtHeader.setBackgroundColor(getResources().getColor(R.color.middle_green));
				descHeader.setBackgroundColor(getResources().getColor(R.color.ultra_dark_green));
				dateHeader.setBackgroundColor(getResources().getColor(R.color.ultra_dark_green));
				
				descriptionSortImage.setVisibility(View.INVISIBLE);
				dateSortImage.setVisibility(View.INVISIBLE);
				updateAdapter();
			}
		};
		amountColumn.setOnClickListener(amountOnClickListener);
		amountSortImage.setOnClickListener(amountOnClickListener);

		pageIndicatorLayout = (LinearLayout) mView
				.findViewById(R.id.pageIndicator);

		for (int i = 0; i < DataManager.getInstance().getFilteredAccounts()
				.size(); i++) {

			ImageView img = new ImageView(getActivity());
			img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			if (i == PageIndex) {
				img.setImageResource(R.drawable.active_indicator);
			} else {
				img.setImageResource(R.drawable.inactive_indicator);
			}
			pageIndicatorLayout.addView(img);
		}

	}

	public Account getAccount() {
		return account;
	}

//	private void launchTransferActivity(int mode) {
//
//		Intent intent = new Intent(getActivity(),
//				MyAccountsTransferFragmentActivity.class);
//		intent.putExtra("transferMode", mode);
//		intent.putExtra("externalTransferAccount", account);
//		Utils.startActivity(getActivity(), intent);
//	}
//
//	private void launchPayBillActivity() {
//		Intent intent = new Intent(getActivity(),
//				PayBillsFragmentActivity.class);
//		intent.putExtra(PayBillsFragmentActivity.ACCOUNT, account);
//		Utils.startActivity(getActivity(), intent);
//	}

	private void resetOrderForAllColumns() {
		dateColumnOrder = descriptionColumnOrder = amountColumnOrder = Order.ASCENDING;
	}

	private void setDefaultOrderingToDefaultColumn() {

		adapter.sort(SortingColumn.DATE, Order.DESCENDING, DataManager
				.getInstance().getAccountsDetail().get(account.getAccountNO())
				.getAccountActivity());
		dateColumnOrder = Order.ASCENDING;
		updateAdapter();
	}

	@Override
	public void onResume() {
		super.onResume();
		TDLog.d(CLASS_NAME, String.format("onResume(%s) %s hidden=%s", account.getAccountNO(), PageIndex, isHidden()));
		//buildUI();
	}

	public void buildUI() {
		updateUIHeader();
		if (DataManager.getInstance().getAccountsDetail().get(account.getAccountNO()) == null && ((AccountDetailActivity) getActivity()).getViewPager().getCurrentItem()==PageIndex) {
			requestAccountDetail(getActivity(), account, startIndex, INITIAL_REQUEST_COUNT, type, this);
		}
	}
	
	@Override 
	public void onPause() {
		super.onPause();

		TDLog.d(CLASS_NAME, String.format("onPause(%s) %s", account.getAccountNO(), PageIndex));
	}
	
	@Override 
	public void onStop() {
		super.onPause();

		TDLog.d(CLASS_NAME, String.format("onStop(%s) %s", account.getAccountNO(), PageIndex));
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.PageIndex = getArguments().getInt(INDEX);
		account = DataManager.getInstance().getFilteredAccounts()
				.get(PageIndex);
		
		TDLog.d(CLASS_NAME, String.format("onCreate(%s) %s", account.getAccountNO(), PageIndex));
		
		type = AccountHelper.getAccountType(account);
		if (type == AccountType.Deposit) {
			currecny = AccountHelper.getCurrencyType(account);
		}
	}

	private void updateAdapter() {

		adapter.notifyDataSetChanged();
	}

	public void onDestroyView() {
		super.onDestroyView();
		TDLog.d(CLASS_NAME, String.format("onDestroyView(%s) %s", account.getAccountNO(), PageIndex));
		adapter = null;
	}
	
	private void updateUI() {

		updateUIHeader();
		
		AccountDetail detail = DataManager.getInstance().getAccountsDetail().get(account.getAccountNO());
		if (detail == null)
			return;
		
		ArrayList<AccountActivity> accountActivities = detail.getAccountActivity();
		if (accountActivities != null) {
			if (adapter == null || adapter.getCount() != accountActivities.size()) {
				if (type == AccountType.Deposit || type == AccountType.LOC) {
					adapter = new AccountActivityListAdapter(getActivity(),
							accountActivities, detail.isHasAdditionalActivity(), loadMore);
				} else if (type == AccountType.Credit) {
					adapter = new AccountActivitySectionAdapter(getActivity(),
							accountActivities, detail.isHasAdditionalActivity(), loadMore);
//					((View) mView.findViewById(R.id.quick_links))
//							.setVisibility(View.GONE);
				}
				accountActivityList.setAdapter(adapter);
				resetOrderForAllColumns();
				setDefaultOrderingToDefaultColumn();
			}
		}
//KY-
/*
		if (detail.isHasAdditionalActivity() && isAtEndOfDetailList) {
			loadMore.setVisibility(View.VISIBLE);
		} else
			loadMore.setVisibility(View.GONE);
*/
		activityContent.setVisibility(View.VISIBLE);
		emptyContent.setVisibility(View.GONE);
		
		if (detail.getAccountActivity() == null || detail.getAccountActivity().size() == 0) {
			activityContent.setVisibility(View.GONE);
			emptyContent.setVisibility(View.VISIBLE);
		}
	}
	
	private void updateUIHeader() {
		if (errorMessage != null && !errorMessage.equals(""))  {
			errorContent.setVisibility(View.VISIBLE);
			errorHeader.setText(((BaseController)getActivity()).getStringResourceByName("headerErrorText"));
			errorText.setText(errorMessage);
		}
		accountDescription.setText(account.getAccountDescription() + " " + account.getAccountNO());
		
		AccountDetail detail = DataManager.getInstance().getAccountsDetail().get(account.getAccountNO());
		if (detail == null)
			return;
		
		NumberFormat format = NumberFormat.getCurrencyInstance();
		String currentBal = format.format(detail.getSummaryInfo().getBalance());
		String availableBal;
		if (type == AccountType.Credit)
			availableBal = format.format(detail.getSummaryInfo().getAvailableCredit());
		else
			availableBal = format.format(detail.getSummaryInfo().getAvailableBalance());

		currentBalance.setText(currentBal);
		availableBalance.setText(availableBal);

		currentBalanceLabel.setText(getCurrentLabelText());
		availableBalanceLabel.setText(getAvailableLabelText());
	}

	private Order toogleOrder(Order orderToToogle) {

		return orderToToogle.equals(Order.ASCENDING) ? Order.DESCENDING
				: Order.ASCENDING;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//KY-
		/*
		boolean f = isAtEndOfDetailList;

		if (visibleItemCount == 0)
			return;

		int currentLastItem = firstVisibleItem + visibleItemCount;
		if (currentLastItem >= totalItemCount) {
			isAtEndOfDetailList = true;
		}
		else
			isAtEndOfDetailList = false;
		
		
		TDLog.d(CLASS_NAME, String.format("onScroll currentLastItem=%s totalItemCount=%s isAtEnd=%s", currentLastItem, totalItemCount, isAtEndOfDetailList).toString());
		
		
		updateUI();
		*/
		/*
		 * if(totalItemCount > visibleItemCount ){ if(firstVisibleItem >0 &&
		 * !shyMenuEnabled){ enableShyMenu(true); shyMenuEnabled = true; } else
		 * if(firstVisibleItem ==0 && shyMenuEnabled){ enableShyMenu(false);
		 * shyMenuEnabled = false; } }
		 */
	}

	/*
	 * private void enableShyMenu(boolean flag) {
	 * 
	 * View quickLinks = (View) mView.findViewById(R.id.quick_links); if (flag)
	 * { quickLinks.setVisibility(View.GONE); } else {
	 * quickLinks.setVisibility(View.VISIBLE); }
	 * 
	 * }
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		/*
		if (scrollState == SCROLL_STATE_FLING )
			TDLog.d(CLASS_NAME, "SCROLL_STATE_FLING ");
		else if (scrollState == SCROLL_STATE_IDLE ){
			TDLog.d(CLASS_NAME, "SCROLL_STATE_IDLE ");
			updateUI();
		}
		else if (scrollState == SCROLL_STATE_TOUCH_SCROLL )
			TDLog.d(CLASS_NAME, "SCROLL_STATE_TOUCH_SCROLL ");
		*/
	}

	private String getCurrentLabelText() {

		String text = "";
		if (type == AccountType.Credit) {
			text = getActivity().getResources().getString(
					R.string.accountDetailCAAccountHeaderCurrentBalance);
		} else if (type == AccountType.Deposit) {
			if (currecny == CurrecnyType.CAD) {
				text = getActivity().getResources().getString(
						R.string.accountDetailCAAccountHeaderCurrentBalance);
			} else {
				text = getActivity().getResources().getString(
						R.string.accountDetailUSAccountHeaderCurrentUSDBalance);
			}
		} else if (type == AccountType.LOC) {
			text = getActivity().getResources().getString(
					R.string.accountDetailUSAccountHeaderCurrentUSDBalance);
		}

		return text;
	}

	private String getAvailableLabelText() {

		String text = "";

		if (type == AccountType.Credit) {
			text = getActivity().getResources().getString(
					R.string.accountDetailCreditCardHeaderAvailableCredit);
		} else if (type == AccountType.Deposit) {
			if (currecny == CurrecnyType.CAD) {
				text = getActivity().getResources().getString(
						R.string.accountDetailCAAccountHeaderAvailableBalance);
			} else {
				text = getActivity()
						.getResources()
						.getString(
								R.string.accountDetailUSAccountHeaderAvailableUSDBalance);
			}
		} else if (type == AccountType.LOC) {
			text = getActivity().getResources().getString(
					R.string.accountDetailCreditCardHeaderAvailableCredit);
		}

		return text;
	}

	@Override
	public void onResponseComplete(AccountDetail newDetail) {
		if (DataManager.getInstance().getAccountsDetail()
				.get(account.getAccountNO()) == null) {
			DataManager.getInstance().setAccountDetailForAccount(account,
					newDetail);
		} else {
			DataManager.getInstance().updateAccountDetailForAccount(account,
					newDetail);
		}
		startIndex = DataManager.getInstance().getAccountsDetail()
				.get(account.getAccountNO()).getAccountActivity().size();

		updateUI();
		accountActivityList.setSelection(currentIndex);
	}

	private void requestAccountDetail(Context context, Account account,
			int startIndex, int numberOfEntries, AccountType type,
			IAccountDetailRestListener listener) {

		AccountDetail detail = DataManager.getInstance().getAccountsDetail()
				.get(account.getAccountNO());
		final ProgressDialog progessDialog = ViewBuilder
				.buildProgressSpinner(context);
		AccountDetailRestHelper restHelper = new AccountDetailRestHelper(
				context, new RestRequestListener() {
					@Override
					public void onResult(RestResponse response) {
						progessDialog.dismiss();

						if (response != null) {
							BaseController activity = (BaseController) getActivity();
							try {
								if (response.hasHttpError()) {
									errorMessage = activity.getStringResourceByName("UNKNOWN");
									updateUI();
									return;
								}

								if (!response.hasError()) {
									errorMessage = null;
									AccountDetail detail = DataManager.populateAccountDetail(response);
									if (!Utils.getBypassLogin() && detail!= null && !detail.getSummaryInfo().getAccountNO().equals(AccountDetailPageFragment.this.account.getAccountNO()))
										return;
									((IAccountDetailRestListener) AccountDetailPageFragment.this).onResponseComplete(detail);
								} else {
									AccountDetail detail = DataManager.populateAccountDetail(response);
									if (detail != null && !Utils.getBypassLogin() && !detail.getSummaryInfo().getAccountNO().equals(AccountDetailPageFragment.this.account.getAccountNO()))
										return;
									((IAccountDetailRestListener) AccountDetailPageFragment.this).onResponseComplete(detail);
									detail = DataManager.getInstance().getAccountsDetail().get(AccountDetailPageFragment.this.account.getAccountNO());
									if (!response.getServerStatusCode().equalsIgnoreCase("AA112"))
										errorMessage = activity.getStringResourceByName(response.getServerStatusCode());
									updateUI();
								}

							} catch (Exception e) {
								TDLog.d(CLASS_NAME, e.getMessage(), e);
								AccountDetail detail = DataManager.getInstance().getAccountsDetail().get(AccountDetailPageFragment.this.account.getAccountNO());
								errorMessage = e.getMessage();
								updateUI();
							}
						}
					}
				});

		progessDialog.show();
		if (type == AccountType.Deposit || type == AccountType.LOC) {
			restHelper.requestDepositAccount(account.getAccountID(),
					account.getAccountNO(), String.valueOf(startIndex),
					String.valueOf(numberOfEntries));
		} else if (type == AccountType.Credit) {
			if (Utils.getBypassLogin()) {
				restHelper.requestGetLocalCreditCardAccount(account.getAccountID(),
						account.getAccountNO(), String.valueOf(startIndex),
						String.valueOf(numberOfEntries));
			}
			else {
				restHelper.requestGetCreditCardAccount(account.getAccountID(),
						account.getAccountNO(), String.valueOf(startIndex),
						String.valueOf(numberOfEntries));
			}
		}
	}
}
