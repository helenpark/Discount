package com.td.mobile.listeners;

import com.td.mobile.model.AccountDetail;

public interface IAccountDetailRestListener extends BaseLisenters{

	public void onResponseComplete(AccountDetail detail);

	
}
