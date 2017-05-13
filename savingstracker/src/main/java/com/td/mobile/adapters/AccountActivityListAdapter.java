package com.td.mobile.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.td.innovate.savingstracker.R;
import com.td.mobile.model.AccountActivity;
import com.td.mobile.model.AccountActivity.Order;
import com.td.mobile.model.AccountActivity.SortingColumn;
import com.td.mobile.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountActivityListAdapter extends ArrayAdapter<AccountActivity> implements IListAdaptor {

	private Context mContext;
	private ArrayList<AccountActivity> mItems;
	private boolean hasAdditionalActivity;
	private OnClickListener loadMoreListener;



	public AccountActivityListAdapter(Context context, ArrayList<AccountActivity> items, boolean hasAdditionalActivity, OnClickListener loadMoreListener) {
		super(context, R.layout.item_account_activity_list, items);
		mContext = context;
		mItems = items;
		this.hasAdditionalActivity = hasAdditionalActivity;
		this.loadMoreListener = loadMoreListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView;
		if (hasAdditionalActivity && position == mItems.size()) {
			itemView = inflater.inflate(R.layout.account_detail_loadmore_btn, parent,false);
			itemView.findViewById(R.id.loadMore).setOnClickListener(loadMoreListener);
		}
		else {
			itemView = inflater.inflate(R.layout.item_account_activity_list, parent,false);
			
			AccountActivity accountActivity = (AccountActivity) mItems.get(position);
	
			TextView date = (TextView)( itemView.findViewById(R.id.date));
			TextView descriptipon = (TextView)( itemView.findViewById(R.id.description));
			TextView amount = (TextView)( itemView.findViewById(R.id.amount));
			
			
			Date dateObj = Utils.convertStringtoDate(accountActivity.getDate());
			CharSequence formattedDate = android.text.format.DateFormat.format("MMM dd", dateObj);
			
	
			date.setText(formattedDate);
			descriptipon.setText(accountActivity.getDescription());
			amount.setText(Utils.colorAmountText(Utils.getFormattedAmount(accountActivity.getAmount()), accountActivity.getAmount()));
		}
		return itemView;
	}
	
	@Override
	public int getCount() {
		if(hasAdditionalActivity) {
			return mItems.size()+1;
		}
		else
			return mItems.size();
	}

	@Override
	public void sort(SortingColumn col, Order order,List<AccountActivity> activity) {
		AccountActivity.sort(col, order, mItems);
		
	}
	
}
