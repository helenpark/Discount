package com.td.mobile.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.savingstracker.R;
import com.td.innovate.savingstracker.Transaction;
import com.td.innovate.savingstracker.onboard.OnBoardActivity;
import com.td.mobile.helpers.AccountDetailRestHelper;
import com.td.mobile.managers.DataManager;
import com.td.mobile.model.Account;
import com.td.mobile.model.AccountActivity;
import com.td.mobile.model.AccountDetail;
import com.td.mobile.model.AccountHelper;
import com.td.mobile.nextgen.restful.RestCallAsyncTask;
import com.td.mobile.nextgen.restful.RestEndPoint;
import com.td.mobile.nextgen.restful.RestRequest;
import com.td.mobile.nextgen.restful.RestRequestListener;
import com.td.mobile.nextgen.restful.RestResponse;
import com.td.mobile.nextgen.view.ViewBuilder;
import com.td.mobile.utils.Consts;
import com.td.mobile.utils.TDLog;
import com.td.mobile.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AccountsSummaryController extends BaseController {


    private static final String CLASS_NAME = AccountsSummaryController.class.getName();
    private String headerText;

    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private int numberOfAccounts;
    private int numberOfResponsesReceived;



    private OnClickListener itemOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            launchAccountDetail(v);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("AccountsSummary", "AccountsSummary ONCREATE");

/*
//        Commenting out layout updating since we dont need to show the account listing for now. We may need this back,
//        so just commenting it out here.

        setContentView(R.layout.accounts_summary_view);
        populateDrawerMenu();
        setActionBarCustomView(getResources().getString(R.string.accounts_str), true);
        headerText = this.getStringResourceByName("headerErrorText");
        setErrorFragment((HeaderErrorViewFragment) getFragmentManager().findFragmentById(R.id.error_fragment));
        hideErrorFragment();
*/

        final ProgressDialog progress = ViewBuilder.buildProgressSpinner(this);
        progress.show();

        String url = RestEndPoint.getInstance().getFinancialSummary();
        JSONObject params = new JSONObject();
        RestRequest aRequest = new RestRequest(url,
                "SvcAccountInquiryGetFinancialSummaryRq", params);

        RestCallAsyncTask<RestResponse> task = new RestCallAsyncTask<RestResponse>(RestResponse.class,
                this) {

            @Override
            protected void onPostExecute(RestResponse result) {
                super.onPostExecute(result);

                if (result == null || result.hasHttpError()) {
                    showErrorFragment(headerText, getStringResourceByName("UNKNOWN"));
                } else {
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) result.getPayload());
                        JSONArray jsonArray = (obj != null) ? obj.getJSONArray("AccountInformation") : null;

                        Gson gson = new Gson();

                        if (jsonArray != null) {

                            System.out.println("Resonse: "+ jsonArray.toString());

                            Type listType = new TypeToken<List<Account>>() {
                            }.getType();

                            DataManager.getInstance().setAccounts((List<Account>) gson.fromJson(jsonArray.toString(), listType));
                        }

                        buildUI(progress);
                    } catch (JsonSyntaxException e) {
                        TDLog.d(CLASS_NAME, e.getMessage(), e);
                        showErrorFragment(headerText, getStringResourceByName("UNKNOWN"));
                    } catch (JSONException e) {
                        TDLog.e(CLASS_NAME, e.getMessage(), e);
                        showErrorFragment(headerText, getStringResourceByName("UNKNOWN"));
                    }
                }

            }
        };
        task.execute(aRequest);
    }

    public void buildUI(ProgressDialog progress) {
        if (DataManager.getInstance().getAccounts() == null || DataManager.getInstance().getAccounts().size() == 0)
            return;

        ArrayList<ArrayList<Account>> accountsByCategory = categorizeAccountType();

//        ListView summaryContent = (ListView) findViewById(R.id.summaryContent);
//        summaryContent.setAdapter(new SummaryContentAdapter(this, accountsByCategory));

        //Calculate number of accounts
        for (ArrayList<Account> categoryAccounts : accountsByCategory) {
            for (Account account : categoryAccounts) {
                if (AccountHelper.getAccountType(account) != null) {
                    numberOfAccounts++;

                }
            }

        }

        //Automate calls to get the details of all the accounts without the user clicking on them to get details
        for (ArrayList<Account> categoryAccounts : accountsByCategory) {
            for (Account account : categoryAccounts) {
                requestAccountDetail(this, account, 0, 99, AccountHelper.getAccountType(account), progress);
            }
        }

//        progress.dismiss(); Doing this after the last account Detail response arrives
    }

    private class SummaryContentAdapter extends ArrayAdapter<ArrayList<Account>> {
        private ArrayList<Object> content = new ArrayList<Object>();

        public SummaryContentAdapter(Context context, ArrayList<ArrayList<Account>> accountsByCategory) {
            super(context, R.layout.summary_list_item, accountsByCategory);
            populateContent(accountsByCategory);
        }

        private void populateContent(ArrayList<ArrayList<Account>> accountsByCategory) {
            for (ArrayList<Account> accounts : accountsByCategory) {
                content.add(getStringResourceByName(Consts.ACCOUNT_CLASS.get(accounts.get(0).getClassificationCD()))); //section header

                double totalCAN = 0.0;
                double totalUSD = 0.0;

                for (Account account : accounts) {
                    String currency = account.getCurrencyCD();

                    if (currency.equalsIgnoreCase("usd")) {
                        totalUSD += account.getAccountBalance();
                    } else {
                        totalCAN += account.getAccountBalance();
                    }
                    content.add(account);
                }
                double[] totals = new double[2];
                totals[0] = totalCAN;
                totals[1] = totalUSD;

                content.add(totals);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (content.get(position) instanceof String) {
                return createClassificationHeader((String) content.get(position));
            } else if (content.get(position) instanceof Account) {
                View itemView = getItemView(parent, (Account) content.get(position));
                itemView.setOnClickListener(itemOnClickListener);
                return itemView;
            } else {
                double totalCAN = ((double[]) content.get(position))[0];
                double totalUSD = ((double[]) content.get(position))[1];

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View totalView = inflater.inflate(R.layout.summary_total, null);
                TextView canTotal = (TextView) totalView.findViewById(R.id.canTotal);
                canTotal.setText(Utils.formatCANCurrency(totalCAN));
                canTotal.setVisibility(View.VISIBLE);
                if (totalUSD != 0) {
                    TextView usTotal = (TextView) totalView.findViewById(R.id.usTotal);
                    usTotal.setText(Utils.formatUSDCurrency(totalUSD));
                    usTotal.setVisibility(View.VISIBLE);
                }
                return totalView;
            }
        }

        @Override
        public int getCount() {
            return content.size();
        }
    }

    private ArrayList<ArrayList<Account>> categorizeAccountType() {
        ArrayList<ArrayList<Account>> categories = new ArrayList<ArrayList<Account>>();

        for (Account account : DataManager.getInstance().getAccounts()) {
            String category = account.getClassificationCD();
            ArrayList<Account> accounts = getAccountsByCategory(category, categories);
            if (accounts == null) {
                accounts = new ArrayList<Account>();
                categories.add(accounts);
            }
            accounts.add(account);
        }
        return categories;
    }

    private ArrayList<Account> getAccountsByCategory(String category, ArrayList<ArrayList<Account>> categories) {

        for (ArrayList<Account> accounts : categories) {
            if (accounts.get(0).getClassificationCD().equalsIgnoreCase(category)) {
                return accounts;
            }
        }
        return null;
    }

    private View createClassificationHeader(String classification) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.summary_classification_header, null);
        TextView textview = (TextView) (v.findViewById(R.id.classificationTexView));
        textview.setText(classification);

        return v;
    }

    private View getItemView(ViewGroup parent, Account account) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.summary_list_item, parent, false);
        itemView.setTag(account);

        ((TextView) (itemView.findViewById(R.id.accntDescrSum))).setText(account.getAccountDescription());

        ((TextView) itemView.findViewById(R.id.accntNumberSum)).setText(account.getAccountNO());

        ((TextView) itemView.findViewById(R.id.accntBalanceSum)).setText(Utils.formatCurrencyDisplay(account.getAccountBalance(), account.getCurrencyCD()));

        if (DataManager.getInstance().getFilteredAccountsIndex(account) == -1) {
            ImageView carretImg = (ImageView) itemView.findViewById(R.id.carret);
            carretImg.setVisibility(View.INVISIBLE);
        }

        return itemView;
    }

    private void launchAccountDetail(View view) {

        Account account = (Account) view.getTag();
        int index = DataManager.getInstance().getFilteredAccountsIndex(account);
        if (index == -1)
            return;

        Intent intent = new Intent(this, AccountDetailActivity.class);
        intent.putExtra(AccountDetailActivity.SELECTED_ACCOUNT_INDEX, DataManager.getInstance().getFilteredAccountsIndex(account));
        Utils.startActivity(this, intent);
    }

    private void requestAccountDetail(Context context, Account account,
                                      int startIndex, int numberOfEntries, final AccountHelper.AccountType type, final ProgressDialog progress) {

        AccountDetailRestHelper restHelper = new AccountDetailRestHelper(
                context, new RestRequestListener() {
            @Override
            public void onResult(RestResponse response) {
                numberOfResponsesReceived++;

                if (response != null) {

                    Log.d("TDSumm"," came here 1");
                    Log.d("credit add"," came here 1");

                    System.out.println(response.getRaw().toString());

                    try {
                        Log.d("credit add"," test 1");

                        if (response.hasHttpError()) {
                            Log.d("credit add"," test 1 ERROR");
                            Log.e(CLASS_NAME, "ERROR: The account detail response has an http error.");
                            return;
                        }
                        Log.d("credit add"," test 2 has error: " + (response.hasError()));

                        //if (!response.hasError()) {
                            Log.d("credit add"," came here 2");
                            AccountDetail detail = DataManager.populateAccountDetail(response);
                            Log.d("credit add"," test 3 ");

                            ArrayList<AccountActivity> list = detail.getAccountActivity();

                            for (AccountActivity activity : list) {
                                if (!activity.getDescription().contains("TFR") && !activity.getDescription().equals("PAYMENT - THANK YOU")) {
                                    String debit = "0";
                                    String credit = "0";
                                    Double amount = activity.getAmount();
                                    if (type == AccountHelper.AccountType.Deposit) {
                                        if (amount < 0) {
                                            debit = "" + -1 * amount;
                                        } else {
                                            credit = "" + amount;
                                        }
                                    } else {
                                        if (amount > 0) {
                                            debit = "" + amount;
                                        } else {
                                            credit = "" + -1 * amount;
                                        }
                                    }
                                    Calendar date = Calendar.getInstance();
                                    try {
                                        date.setTime(new SimpleDateFormat("yyyyMMdd", Locale.CANADA).parse(activity.getDate()));
                                    } catch (ParseException e) {
                                        Log.e(CLASS_NAME, "Creating Transaction: Date retrieving error.");
                                        continue;
                                    }
                                    Transaction transaction = new Transaction(date, activity.getDescription(), Double.parseDouble(debit), Double.parseDouble(credit), Transaction.ReoccurringType.NOT, Transaction.OccurredType.ARRIVED);
                                    transactions.add(transaction);
                                }
                            }
                      //  }

                    } catch (Exception e) {
                        Log.d("credit add"," test catch phsase");
                        TDLog.d(CLASS_NAME, e.getMessage(), e);
                    }
                }

                if (numberOfResponsesReceived == numberOfAccounts) {
                    LoginController.transactionList = transactions;

                    DataManipulation.setNow(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    Log.d("DataManipulation", "transactions null: " + (transactions == null));
                    DataManipulation.initializeData(transactions, Calendar.getInstance());
                    Log.d("AccountsSummary", "DAtaManup was called, cash flow: " + DataManipulation.getXMonthAgoCashFlow(3));

                    //Intent onBoard = new Intent(AccountsSummaryController.this, OnBoardActivity.class);
                    progress.dismiss();
                   //  onBoard.putExtra("To OnBoard", "From Login");
                 //   startActivity(onBoard);
                    finish();
                }

            }
        });

        if (type == AccountHelper.AccountType.Deposit || type == AccountHelper.AccountType.LOC) {
            restHelper.requestDepositAccount(account.getAccountID(),
                    account.getAccountNO(), String.valueOf(startIndex),
                    String.valueOf(numberOfEntries));

//            Log.d("credit","Deposit or LOC: " + account.getAccountBalance());
//            DataManipulation.addBalanceToTotalBalance(account.getAccountBalance());

        } else if (type == AccountHelper.AccountType.Credit) {
            if (Utils.getBypassLogin()) {
                restHelper.requestGetLocalCreditCardAccount(account.getAccountID(),
                        account.getAccountNO(), String.valueOf(startIndex),
                        String.valueOf(numberOfEntries));

            } else {
                restHelper.requestGetCreditCardAccount(account.getAccountID(),
                        account.getAccountNO(), String.valueOf(startIndex),
                        String.valueOf(numberOfEntries));
            }
        }
    }



}