package com.td.mobile.controllers;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.wallet.Payments;
import com.rsa.mobilesdk.sdk.MobileAPI;
import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.savingstracker.R;
//import com.td.mobile.etransfers.ETransferReceiveActivity;
import com.td.innovate.savingstracker.Transaction;
import com.td.mobile.helpers.CustomCallback;
import com.td.mobile.helpers.NavigationManager;
import com.td.mobile.mfa.MfaHelper;
import com.td.mobile.model.User;
import com.td.mobile.nextgen.restful.RestCallAsyncTask;
import com.td.mobile.nextgen.restful.RestEndPoint;
import com.td.mobile.nextgen.restful.RestRequest;
import com.td.mobile.nextgen.restful.RestResponse;
import com.td.mobile.nextgen.restful.Session;
import com.td.mobile.nextgen.view.ActionSheetArrayAdapter;
import com.td.mobile.nextgen.view.ActionSheetFragment;
import com.td.mobile.nextgen.view.LoginEditText;
import com.td.mobile.nextgen.view.ViewBuilder;
import com.td.mobile.utils.Consts;
import com.td.mobile.utils.TDLog;
import com.td.mobile.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class LoginController
        extends BaseController implements CustomCallback {

    private final static String MaskedId = "MaskedId";
    private final static String EncryptedId = "EncryptedId";
    private final static String DeviceID = "DeviceID";
    private final static String NativeDeviceInfo = "NativeDeviceInfo";

    private final static String RecentAccessCard = "RecentAccessCard";

    private static final int STATUS_RESPONSE_OK = 0;
    private static final int STATUS_RESPONSE_ISSUE = 1;
    private static final int STATUS_SYSTEM_EXCEPTION = 2;

    public static ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
    private static final int MAX_ACCESS_CARDS = 15;

    private boolean isRememberMe;
    private LoginEditText mEdtLogin;
    private LoginEditText mEdtPassword;
    private Switch mSwRememberMe;
    private TextView mTxtVRememberMe, mloginBtnText;
    private LinearLayout mloginBtn;
    private LinearLayout mDemoBtn;
    private boolean isLoginEnabled;
    private boolean isAddLoginState;
    private boolean isActionSheetMode;
    public static boolean isDemoDataLogin = false;
    private String RID;
    private Bundle args;


    long startMillis = 0;
    int count = 0;

    Class targetClass;


    public LoginController() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        //	populateDrawerMenu();

        //	setActionBarCustomView(R.string.secure_login);

        init();

    }


    private boolean checkDeepLink() {

        Uri data = getIntent().getData();
        if (data != null) {

            RID = data.getQueryParameter("RID");

//			if(RID != null){
//
//				targetClass = ETransferReceiveActivity.class;
//
//				args = new Bundle();
//				args.putString(ETransferReceiveActivity.EXTRA_RID, RID);
//				return true;
//			}
        }
        return false;
    }

    protected void init() {
        super.init();
        if (!checkDeepLink()) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                targetClass = (Class) extras.get("targetClass");
            }
            if (targetClass == null) {
                targetClass = AccountsSummaryController.class;
            }
        }
        mDemoBtn = (LinearLayout) findViewById(R.id.demoBtn);
        mloginBtn = (LinearLayout) findViewById(R.id.loginBtn);

        mloginBtnText = (TextView) findViewById(R.id.loginBtnText);

        mEdtLogin = (LoginEditText) this.findViewById(R.id.loginEditText);



        mEdtLogin.setActivity(this);

        mEdtPassword = (LoginEditText) this.findViewById(R.id.password_input);

        mSwRememberMe = (Switch) findViewById(R.id.remember_switch);

        mTxtVRememberMe = (TextView) findViewById(R.id.remember_text);

        mSwRememberMe.setChecked(true);


        if (!getAccessCards().isEmpty()) {
            mEdtLogin.setAction(LoginEditText.ACTION_SHEET_ACTION);
            mEdtLogin.setText(getRecentAccessCard());
            hideRememberMe();
        }


        mDemoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isDemoDataLogin = true;
                //readCSV();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        mloginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isLoginEnabled) {
                    isDemoDataLogin = false;
                    login(v);
                }
            }
        });

        mEdtLogin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                toggleLoginButton();
            }
        });

        mEdtPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                toggleLoginButton();
            }
        });

        hideErrorFragment();
    }


    @Override
    public void onBackPressed() {

        if (isAddLoginState) {
            isAddLoginState = false;
            setActionBarCustomView(R.string.secure_login);
            mEdtLogin.setAction(LoginEditText.ACTION_SHEET_ACTION);
            if (!getAccessCards().isEmpty())
                mEdtLogin.setText(getAccessCards().get(0));
            mEdtPassword.setText("");
            hideRememberMe();
        } else
            super.onBackPressed();
    }

    @Override
    public void onResult(Object newParam) {
        if (newParam != null) {
            Utils.startActivity(this, AccountsSummaryController.class);
        } else {
            showErrorFragment(headerText, getString(R.string.secureLoginErrorCopy));
        }
    }

    protected void startAccountSummary() {
        Utils.startActivity(this, AccountsSummaryController.class);
    }

    public void login(View v) {
        String login = mEdtLogin.getText().toString();
        String password = mEdtPassword.getText().toString();
        isRememberMe = mSwRememberMe.isChecked();

        if (password == null || password.trim().length() < 1) {
            showErrorFragment(headerText, getString(R.string.secureLoginErrorCopy));
            return;
        }
        String url = RestEndPoint.getInstance().getLogin();
        JSONObject params = new JSONObject();
        try {

            String versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            params.put("NativeAppVersion", versionName);
            params.put("LanguageCD", "EN");
            boolean savedId = isSavedId(login);
            if (savedId) {
                login = getSavedId(login);
            }
            params.put("AccessAuthenticationNO", login);
            params.put("Password", password);
            params.put("ReturnEncryptedCredentialIND", isRememberMe);
            params.put("SavedIdIND", savedId);
            params.put(NativeDeviceInfo, getRSAInfo());
            String deviceId = getDeviceId();
            if (deviceId != null) {
                params.put(DeviceID, deviceId);
            }
        } catch (Exception e) {
            TDLog.d(Consts.LOG_TAG, e.getMessage(), e);
        }
        RestRequest aRequest = new RestRequest(url, "SvcAuthenticateLoginRq", params);
        HashMap<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("User-Agent", getTSOCode());
        aRequest.setHeaderParams(headerParams);
        doLogin(aRequest);
    }

    public void showAccessCard() {

        TDLog.d(Consts.LOG_TAG, "showAccessCard");
        List<String> data = getAccessCards();
        if (data == null || data.isEmpty()) {
            return;
        }

        ActionSheetFragment.CustomTitleConfig titleConfig = new ActionSheetFragment.CustomTitleConfig(
                R.layout.login_actionsheet_header_accesscard, R.id.bn_done,
                null);
        titleConfig
                .setEditLblResource(getString(R.string.secureLoginMultiButtonEdit));
        titleConfig
                .setNonEditLblResource(getString(R.string.secureLoginEditButtonDone));

        ActionSheetFragment.CustomContentConfig contentConfig = new ActionSheetFragment.CustomContentConfig(
                R.layout.actionsheet_content_view, R.id.listView);

        ActionSheetArrayAdapter<String> adapter = new ActionSheetArrayAdapter<String>(
                this, 0, data) {
            @Override
            protected View createItemView(final int position, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                View root = inflater.inflate(
                        R.layout.login_actionsheet_accesscard, null);
                TextView txtView = (TextView) root
                        .findViewById(R.id.txtAccessCard);
                txtView.setText(getItem(position));

                return root;
            }
        };

        final ActionSheetFragment<String> fragment;
        fragment = new ActionSheetFragment<String>(
                true, titleConfig, contentConfig, adapter) {

            @Override
            protected void onDismissWithResult(DialogInterface dialog,
                                               String result) {
                if (result != null) {
                    mEdtLogin.setText(result);
                    mEdtPassword.setText("");
                    isActionSheetMode = true;
                    dialog.dismiss();
                }
            }

            protected void onDismissWithEditResult(DialogInterface dialog,
                                                   String result) {
                if (result != null) {
                    TDLog.i("val", result);
                    SharedPreferences sharedPreferences = getSharedPreferences(
                            MaskedId, Context.MODE_PRIVATE);
                    Editor editor = sharedPreferences.edit();
                    editor.remove(result);
                    editor.commit();
                    getAdapter().remove(result);

                }
            }

        };

        OnClickListener modeListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean mode = fragment.isEditMode();
                fragment.setEditMode(!mode);
            }
        };
        fragment.getCustomTitleConfig().setOnClickListener(modeListener);


        OnClickListener commandListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getAccessCards().size() >= MAX_ACCESS_CARDS) {
                    fragment.limitReachedAlert(LoginController.this);
                } else {
                    isAddLoginState = true;
                    showRememberMe();
                //    setActionBarCustomView(R.string.addLoginPageHeader);
                    mEdtLogin.setAction(LoginEditText.CLEAR_ACTION);
                    mEdtLogin.setText("");
                    mEdtPassword.setText("");
                    mSwRememberMe.setChecked(false);
                    fragment.dismiss();
                }
            }
        };

        fragment.setCommandButtonListener(commandListener);

        fragment.showFragment(getFragmentManager());

    }

    protected void doLogin(RestRequest aRequest) {
        final ProgressDialog progessDialog = ViewBuilder
                .buildProgressSpinner(this);
        RestCallAsyncTask<RestResponse> task = new RestCallAsyncTask<RestResponse>(
                RestResponse.class, this.getApplicationContext()) {

            @Override
            protected void onPostExecute(RestResponse result) {
                super.onPostExecute(result);
                progessDialog.dismiss();

                if (result == null || result.hasHttpError()) {

                    showErrorFragment(headerText, getStringResourceByName("UNKNOWN"));
                    mEdtPassword.setText("");

                } else {
                    JSONObject obj = null;
                    try {
                        obj = (JSONObject) result.getPayload();
                        int statusCode = -1;
                        statusCode = Integer.parseInt(result.getStatusCode());
                        String serverStatusCode = result.getServerStatusCode();

                        switch (statusCode) {

                            case STATUS_RESPONSE_OK:
                                Session.getInstance().put("user", new User());

                                storeAccessCard(obj);

                                saveDeviceID(obj);
                                Intent intent = new Intent(LoginController.this, targetClass);
                                if (args != null) {
                                    intent.putExtras(args);
                                }

                                //Utils.startActivity(LoginController.this, intent);
                                finish();
                                break;

                            case STATUS_RESPONSE_ISSUE:

                                MfaHelper mfaHelper = new MfaHelper(obj.toString());
                                boolean required = mfaHelper.isMfaRequried();

                                if (required) {

                                    storeAccessCard(obj);

                                    NavigationManager.launchMfaActivity(LoginController.this, mfaHelper);

                                } else {
                                    showErrorFragment(headerText, getStringResourceByName(serverStatusCode));
                                    mEdtPassword.setText("");
                                }
                                break;

                            case STATUS_SYSTEM_EXCEPTION:
                                showErrorFragment(headerText, getStringResourceByName(serverStatusCode));
                                mEdtPassword.setText("");
                                break;

                            default:
                                showErrorFragment(headerText, getStringResourceByName("UNKNOWN"));
                                mEdtPassword.setText("");
                                break;

                        }
                    } catch (Exception e) {

                        TDLog.d(Consts.LOG_TAG, e.getMessage(), e);
                        showErrorFragment(headerText, e.getMessage());
                    }
                }
            }

        };
        progessDialog.show();
        task.execute(aRequest);
    }

    private void storeAccessCard(JSONObject obj) throws JSONException {

        if (isRememberMe || isActionSheetMode) {
            saveRecentAccessCard(storeRememberMe(obj));
            isActionSheetMode = false;
        }
    }

    private List<String> getAccessCards() {
        SharedPreferences sharedPreferences = getSharedPreferences(MaskedId,
                Context.MODE_PRIVATE);
        ArrayList<String> cards = new ArrayList<String>();
        if (sharedPreferences != null) {
            Map<String, ?> aRtn = sharedPreferences.getAll();
            if (aRtn != null) {
                Set<String> keys = aRtn.keySet();
                Object[] strArrays = keys.toArray();
                for (Object s : strArrays) {
                    cards.add(s.toString());
                }
            }
        }

        return cards;
    }


    private boolean isSavedId(String loginId) {
        SharedPreferences sharedPreferences = getSharedPreferences(MaskedId,
                Context.MODE_PRIVATE);
        return sharedPreferences.contains(loginId);
    }

    private String getSavedId(String maskedId) {
        SharedPreferences sharedPreferences = getSharedPreferences(MaskedId,
                Context.MODE_PRIVATE);
        String s = null;
        if (sharedPreferences != null) {
            s = sharedPreferences.getString(maskedId, maskedId);
        }
        return s;
    }

    protected String storeRememberMe(JSONObject aResponse) throws JSONException {
        String maskedId = aResponse.getString(MaskedId);
        String encryptedId = aResponse.getString(EncryptedId);
        if (maskedId != null && encryptedId != null) {
            savePreferences(maskedId, encryptedId);
        }
        return maskedId;
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(MaskedId,
                Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getRSAInfo() {
        MobileAPI mobileAPI = MobileAPI.getInstance(this);
        mobileAPI.initSDK(getSdkProperties());
        String rsa = mobileAPI.collectInfo();
        mobileAPI.destroy();
        return rsa;
    }

    private Properties getSdkProperties() {
        Properties properties = new Properties();

        properties.setProperty(MobileAPI.CONFIGURATION_KEY, ""
                + MobileAPI.COLLECT_ALL_DEVICE_DATA_AND_LOCATION);

        return properties;
    }

    private String getDeviceId() {
        SharedPreferences sharedPreferences = getSharedPreferences(DeviceID,
                Context.MODE_PRIVATE);
        String s = null;
        if (sharedPreferences != null) {
            s = sharedPreferences.getString(DeviceID, null);
        }
        return s;
    }

    protected void setActionBarCustomView(int titleResource) {
//        ActionBar actionBar = getActionBar();
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.actionbar_function_nav, null);
//        TextView titleTxt = (TextView) view.findViewById(R.id.title);
//        if (titleTxt != null) {
//            titleTxt.setText(titleResource);
//        }
//
//        ImageView logout = (ImageView) view.findViewById(R.id.actionBarLogout);
//        logout.setVisibility(View.INVISIBLE);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setCustomView(view);
    }

    public void hideRememberMe() {
        mSwRememberMe.setVisibility(View.GONE);
        mTxtVRememberMe.setVisibility(View.GONE);

        hideErrorFragment();
    }

    public void showRememberMe() {
        mSwRememberMe.setVisibility(View.VISIBLE);
        mTxtVRememberMe.setVisibility(View.VISIBLE);

        hideErrorFragment();
    }

    public void toggleLoginButton() {

        if (mEdtLogin.length() > 0 && mEdtPassword.length() > 0) {
            isLoginEnabled = true;
            mloginBtn
                    .setBackgroundResource(R.drawable.rounded_corner_middle_green);
            mloginBtnText.setTextColor(getResources().getColor(R.color.white));
        } else {
            isLoginEnabled = false;
            mloginBtn
                    .setBackgroundResource(R.drawable.rounded_corner_disabled);
            mloginBtnText.setTextColor(getResources().getColor(R.color.dark_grey));
        }
    }

    private void saveDeviceID(JSONObject obj) {
        try {
            String deviceID = obj.getString(Consts.DeviceID);
            if (deviceID != null) {
                SharedPreferences sharedPreferences = getSharedPreferences(Consts.DeviceID, Context.MODE_PRIVATE);
                Editor editor = sharedPreferences.edit();
                editor.putString(Consts.DeviceID, deviceID);
                editor.commit();
            }
        } catch (JSONException e) {
            TDLog.d(Consts.LOG_TAG, "Not DeviceID from login response");
        }
    }

    private String getRecentAccessCard() {
        SharedPreferences sharedPreferences = getSharedPreferences(RecentAccessCard,
                Context.MODE_PRIVATE);
        String s = "";
        if (sharedPreferences != null) {
            s = sharedPreferences.getString(RecentAccessCard, null);
        }
        return s;
    }

    private void saveRecentAccessCard(String accessCard) {

        SharedPreferences sharedPreferences = getSharedPreferences(RecentAccessCard, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(RecentAccessCard, accessCard);
        editor.commit();
    }

    public String getTSOCode() {
        WebView view = new WebView(this);
        String ua = view.getSettings().getUserAgentString();
        StringBuffer aBuf = new StringBuffer(ua);

        String manufacturer = android.os.Build.MANUFACTURER;
        String model = android.os.Build.MODEL;
        String release = android.os.Build.VERSION.RELEASE;
        aBuf.append(" TDDeviceType (");
        aBuf.append(manufacturer);
        aBuf.append("/");
        aBuf.append(model);
        aBuf.append(")");
        aBuf.append(" TDDeviceOS (android.os/");
        aBuf.append(release);
        aBuf.append(")");
        return aBuf.toString();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        mEdtPassword.setText("");
    }

    private void readCSV() {
        Log.d("LoginController","got here 1");

        String line;
        ArrayList<Transaction> allTransaction = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(com.td.innovate.savingstracker.R.raw.demo_data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                String[] entry = cleanUpData(line);
                if (!entry[1].equals("PAYMENT - THANK YOU") && !entry[1].contains("TFR")) {
                    Calendar date = Calendar.getInstance();
                    try {
                        date.setTime(new SimpleDateFormat("MM/dd/yy", Locale.CANADA).parse(entry[0]));
                    } catch (ParseException e) {
                        Log.e("MAINACTIVITY", "Creating Transaction: Date retrieving error.");
                        continue;
                    }
                    Transaction newTransaction = new Transaction(date, entry[1], Double.parseDouble(entry[2]), Double.parseDouble(entry[3]), Transaction.ReoccurringType.NOT, Transaction.OccurredType.ARRIVED);
                    allTransaction.add(newTransaction);
                }
            }
            Log.d("LoginController","got here 2");

            inputStream.close();
            reader.close();
        } catch (FileNotFoundException e) {
            Log.e("MAINACTIVITY", "File not found.");
        } catch (IOException e) {
            Log.e("MAINACTIVITY", "IO Exception.");
        }
        Log.d("LoginController","got here 3");

        Calendar current = Calendar.getInstance();
        Log.d("LoginController","got here 4");

        current.set(2014, Calendar.NOVEMBER, 7);
        DataManipulation.initializeData(allTransaction, current);
        Log.d("LoginController", "got here 5");

        transactionList = (ArrayList<Transaction>) allTransaction.clone();

        Log.d("LoginController", "got here 6");

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        Log.d("LoginController", "got here 7");

    }


    private String[] cleanUpData(String entry) {
        String[] oneLine = new String[4];
        StringBuilder data = new StringBuilder();
        Boolean inQuote = false;
        int workingEntry = 0;
        for (int i = 0; i < entry.length(); i++) {
            if (entry.charAt(i) == '"') {
                inQuote = !inQuote;
            } else if (entry.charAt(i) == ',') {
                if (!inQuote && !data.toString().equals("")) {
                    oneLine[workingEntry] = data.toString();
                    data = new StringBuilder();
                    workingEntry++;
                } else if (!inQuote) {
                    oneLine[workingEntry] = "0";
                    workingEntry++;
                }
            } else {
                data.append(entry.charAt(i));
            }
        }
        return oneLine;
    }
    
}
