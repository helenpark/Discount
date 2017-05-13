package com.td.mobile.managers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.td.innovate.savingstracker.DataManipulation;
import com.td.mobile.model.Account;
//import com.td.mobile.model.AccountActivity;
//import com.td.mobile.model.AccountDetail;
import com.td.mobile.model.AccountActivity;
import com.td.mobile.model.AccountDetail;
import com.td.mobile.model.AccountHelper;
import com.td.mobile.model.AccountHelper.AccountType;
//import com.td.mobile.model.SummaryInfo;
import com.td.mobile.model.SummaryInfo;
import com.td.mobile.nextgen.restful.RestResponse;
import com.td.mobile.utils.TDLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static final String CLASS_NAME = DataManager.class.getName();
    private static DataManager manager = null;
    private List<Account> accounts;
    private List<Account> filteredAccounts;
    private static List<Account> creditAccounts;
    private List<Account> investingAccounts;
    private Map<String, AccountDetail> accountsDetail = new HashMap<String, AccountDetail>();


    private DataManager() {
    }

    public synchronized static DataManager getInstance() {

        if (manager == null) {
            manager = new DataManager();
        }
        return manager;
    }

    public static AccountDetail populateAccountDetail(RestResponse response) {
        Log.d("credit add","was this even called 2");
        System.out.println(response.getRaw().toString());


        AccountDetail accountDetail = new AccountDetail();
        Gson gson = new Gson();

        try {
            JSONObject result = (JSONObject) ((JSONObject) response.getPayload());
            if (result == null)
                return accountDetail;

            accountDetail.setHasAdditionalActivity(result.optBoolean("HasAdditionalActivity"));
            JSONObject summaryJason = result.getJSONObject(AccountDetail.JasonLabels.summaryInfo);
            SummaryInfo summaryInfo = gson.fromJson(summaryJason.toString(), SummaryInfo.class);

            Log.d("credit add", "Credit Accounts is null: " + (creditAccounts == null));
            if(creditAccounts!=null) {
                Log.d("credit add","going thru for loop");

                for (Account creditAccount : creditAccounts) {
                    Log.d("credit add","Adding credit card");

                    if (creditAccount != null) {
                        Log.d("credit add","credit Card name: "+creditAccount.getAccountDescription());
                        Log.d("credit add","credit account id: "+creditAccount.getAccountID());
                        Log.d("credit add", "summary info id: " + summaryInfo.getAccountID());

                        if (creditAccount.getAccountID().equals(summaryInfo.getAccountID())) {
                          creditAccount.setAvailableCredit(summaryInfo.getAvailableCredit());
                            Log.d("credit add", "DONEEEE");
                        }

                    }else{
                        int i=1/0;
                    }
                }

            }else{
                int i=1/0;
            }


            accountDetail.setSummaryInfo(summaryInfo);


            JSONArray activityJson = result.getJSONArray(AccountDetail.JasonLabels.accountActivity);
            ArrayList<AccountActivity> activityList = new ArrayList<AccountActivity>();
            int size = (activityJson != null) ? activityJson.length() : 0;
            for (int i = 0; i < size; i++) {
                JSONObject activity = activityJson.getJSONObject(i);
                AccountActivity accountActivity = gson.fromJson(activity.toString(), AccountActivity.class);
                activityList.add(accountActivity);
            }
            accountDetail.setAccountActivity(activityList);

        } catch (JsonSyntaxException e) {
            TDLog.d(CLASS_NAME, e.getMessage(), e);
        } catch (JSONException e) {
            TDLog.e(CLASS_NAME, e.getMessage(), e);
        }

        return accountDetail;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;

        creditAccounts = new ArrayList<Account>();
        filteredAccounts = new ArrayList<Account>();
        for (Account account : accounts) {

            Log.d("DataMangerAccounts", "this is: "+account.getAccountDescription());


            AccountType accountType = AccountHelper.getAccountType(account);
            if (accountType == AccountType.Deposit || accountType == AccountType.Credit || accountType == AccountType.LOC) {
                filteredAccounts.add(account);
            }

            if (accountType == AccountType.Credit) {
                creditAccounts.add(account);

                //Are we sure about this?? Need to look at this again
                Log.d("DataMangerAccounts", "subtracted amount: " + account.getAccountBalance());
                DataManipulation.subtractBalanceToTotalBalance(account.getAccountBalance());

            }else if(accountType == AccountType.Deposit){
                Log.d("DataMangerAccounts", "added amount: " + account.getAccountBalance());
                DataManipulation.addBalanceToTotalBalance(account.getAccountBalance());
            }
        }
    }

    public void setInvestingAccounts(List<Account> accounts) {

        investingAccounts = new ArrayList<Account>();
        for (Account account : accounts) {
            if (account.getClassificationCD().equals("I") && account.getAccountTypeCD().equals("B")) {
                investingAccounts.add(account);
            }
        }
    }

    public List<Account> getInvestingAccounts() {
        return investingAccounts;
    }

    public int getInvestingAccountsIndex(Account account) {
        for (int i = 0; i < this.investingAccounts.size(); i++) {
            if (account == investingAccounts.get(i))
                return i;
        }
        return -1;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Account> getFilteredAccounts() {
        return filteredAccounts;
    }

    public List<Account> getCreditAccounts() {
        return creditAccounts;
    }


    public int getFilteredAccountsIndex(Account account) {
        for (int i = 0; i < this.filteredAccounts.size(); i++) {
            if (account == filteredAccounts.get(i))
                return i;
        }
        return -1;
    }


    public int getCreditAccountsIndex(Account account) {
        for (int i = 0; i < this.creditAccounts.size(); i++) {
            if (account == creditAccounts.get(i))
                return i;
        }
        return -1;
    }


    public Map<String, AccountDetail> getAccountsDetail() {
        return accountsDetail;
    }

    public void updateAccountDetailForAccount(Account account, AccountDetail newDetail) {
        List<AccountActivity> list = newDetail.getAccountActivity();
        for (AccountActivity activity : list) {
            accountsDetail.get(account.getAccountNO()).getAccountActivity().add(activity);
        }

        accountsDetail.get(account.getAccountNO()).setHasAdditionalActivity(newDetail.isHasAdditionalActivity());
        accountsDetail.get(account.getAccountNO()).setSummaryInfo(newDetail.getSummaryInfo());
    }

    public void setAccountDetailForAccount(Account account, AccountDetail detail) {
        accountsDetail.put(account.getAccountNO(), detail);
    }

    public void setAccountsDetail(Map<String, AccountDetail> accountsDetail) {
        this.accountsDetail = accountsDetail;
    }
}


