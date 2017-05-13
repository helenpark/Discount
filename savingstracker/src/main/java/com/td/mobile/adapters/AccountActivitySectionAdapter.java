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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivitySectionAdapter extends ArrayAdapter<AccountActivity> implements IListAdaptor {

	private Context mContext;
	private ArrayList<AccountActivity> mItems;
	private ArrayList<ArrayList<AccountActivity> > sectionStatements = new ArrayList<ArrayList<AccountActivity>>();
	private Map<Integer,String> sectionTitleMapping = new HashMap<Integer, String>();
	private List<Object> mSectionedList ;
	private boolean hasAdditionalActivity;
	private OnClickListener loadMoreListener;


	public AccountActivitySectionAdapter(Context context, ArrayList<AccountActivity> items, boolean hasAdditionActivity, OnClickListener loadMoreListener) {

		super(context, R.layout.item_account_activity_list, items);

		sectionTitleMapping.put(0,context.getResources().getString(R.string.current_statement));
		sectionTitleMapping.put(1,context.getResources().getString(R.string.previous_statement));
		sectionTitleMapping.put(2,context.getResources().getString(R.string.second_last_statement));
		sectionTitleMapping.put(3,context.getResources().getString(R.string.last_statement));
		
		mContext = context;
		mItems = items;
		this.hasAdditionalActivity = hasAdditionActivity;
		this.loadMoreListener = loadMoreListener;
		populateStatementsMap();
		createListWithSections();
	}

	private void populateStatementsMap(){

		for(AccountActivity activity : mItems){
			int statementNumber =  activity.getStatement();
			getSection(statementNumber).add(activity);
		}
	}
	
	private ArrayList<AccountActivity> getSection(int statementNumber) {
		for (int i=0; i < sectionStatements.size(); i++) {
			if(sectionStatements.get(i).get(0).getStatement() == statementNumber) {
				return sectionStatements.get(i);
			}
		}
		ArrayList<AccountActivity> section = new ArrayList<AccountActivity>();
		sectionStatements.add(section);
		return section;
	}

	private void createListWithSections(){

		mSectionedList = new ArrayList<Object>();
		for(int i=0 ; i< sectionStatements.size() ; i++){
			mSectionedList.add(sectionTitleMapping.get(i));
			List<AccountActivity> list = sectionStatements.get(i);
			
			for(int j=0 ;j<list.size();j++){
				mSectionedList.add(list.get(j));
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView;
		if (hasAdditionalActivity && position == mSectionedList.size()) {
			itemView = ((ViewGroup)inflater.inflate(R.layout.account_detail_loadmore_btn, parent,false));
			itemView.findViewById(R.id.loadMore).setOnClickListener(loadMoreListener);
		}
		else {
			if( mSectionedList.get(position) instanceof String){
	
				View sectionView = inflater.inflate(R.layout.view_account_activity_list_section, parent,false);
				TextView sectionTitle = (TextView)( sectionView.findViewById(R.id.sectionTitle));
				sectionTitle.setText((String)mSectionedList.get(position));
				return sectionView;
			}
	
			itemView = inflater.inflate(R.layout.item_account_activity_list, parent,false);
	
			AccountActivity accountActivity = (AccountActivity) mSectionedList.get(position);
	
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
			return mSectionedList.size()+1;
		}
		else
			return mSectionedList.size();
	}


	@Override
	public void sort(SortingColumn col, Order order,List<AccountActivity> activity) {

		for(int i=0 ; i< sectionStatements.size() ; i++){
			AccountActivity.sort(col, order, sectionStatements.get(i));
		}
		createListWithSections();

	}


}
