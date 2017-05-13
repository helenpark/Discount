package com.td.innovate.tdiscount.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.td.innovate.tdiscount.R;
import com.td.mobile.controllers.AccountsSummaryController;
import com.td.mobile.controllers.BaseController;
import com.td.innovate.savingstracker.MainActivity;
import com.td.mobile.nextgen.restful.Session;


public class LoginActivity extends BaseController {

    EditText access_ET;
    EditText pass_ET;
    Button login_BTN;
    Button dummyLogin_BTN;
    double minPrice;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LoginActivity", "LoginActivity RESUMED");

        Log.d("IsLoggedin","IsLoggedin: "+ Session.getInstance().isUserLoggedIn());

        if(Session.getInstance().isUserLoggedIn()){
            Intent intent = new Intent(this, AccountsSummaryController.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LoginActivity", "LoginActivity PAUSED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//
        Log.d("IsLoggedin","IsLoggedin: "+ Session.getInstance().isUserLoggedIn());

        if(Session.getInstance().isUserLoggedIn()){
            Intent intent = new Intent(this, AccountsSummaryController.class);
            startActivity(intent);
        }


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setElevation(0);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        access_ET = (EditText) findViewById(R.id.access_card_edittext);
        pass_ET = (EditText) findViewById(R.id.password_field_edittext);
        login_BTN = (Button) findViewById(R.id.doneButton);
        dummyLogin_BTN = (Button) findViewById(R.id.dummy_data_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.minPrice = extras.getDouble("price");
        }
    }

    public void dummyDataLogin(View view){
        Toast.makeText(this, "DEMO DATA LOGIN", Toast.LENGTH_SHORT).show();
        access_ET.setText("20533374193");
        pass_ET.setText("password");

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("price", minPrice);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        access_ET.setText("");
        pass_ET.setText("");
    }

    public void doneLogIn(View view){
        Toast.makeText(this, "ACTUAL LOGIN", Toast.LENGTH_SHORT).show();
        launchActivity(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    public void launchActivity(View view) {
        final CharSequence[] listOfOptions = {"Your TD Credentials", "Demo Data"};

        AlertDialog.Builder CSVLogin = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        CSVLogin.setItems(listOfOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        loginTD();
                        break;
                    case 1:
                        loginTD();
//                        readCSV();
                        break;
                }
            }
        });
        CSVLogin.setTitle("Login Using...");
        AlertDialog dialog = CSVLogin.create();
        dialog.show();
    }


    private void loginTD() {

        // Restore activity state from shared preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Toast.makeText(this, "isSignedIn: "+preferences.getBoolean("isSignedIn", false), Toast.LENGTH_SHORT).show();

        if (preferences.getBoolean("isSignedIn", false)) {
            Intent restoreIntent = new Intent(this, MainActivity.class);
            startActivity(restoreIntent);
        }
        verifyUserLogin(AccountsSummaryController.class);
    }


}
