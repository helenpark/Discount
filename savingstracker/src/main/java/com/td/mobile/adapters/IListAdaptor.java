package com.td.mobile.adapters;

import android.widget.ListAdapter;

import com.td.mobile.model.AccountActivity;
import com.td.mobile.model.AccountActivity.Order;
import com.td.mobile.model.AccountActivity.SortingColumn;

import java.util.List;

public interface IListAdaptor extends ListAdapter{

	public void notifyDataSetChanged();
	
	public void sort(final SortingColumn col, final Order order, List<AccountActivity> activity);
	
}
