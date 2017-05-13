package com.td.innovate.savingstracker.pyf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.td.innovate.savingstracker.MainActivity;
import com.td.innovate.savingstracker.R;


public class PYFConfirmationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pyfconfirmation);
        findViewById(R.id.socialHubIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socialHubApp(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pyfconfirmation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings){
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void backToTracker(View view) {
        finish();
    }

    public void socialHubApp(View view) {
        AlertDialog.Builder socialHub = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        socialHub
                .setTitle("Please Confirm")
                .setMessage("You are about to exit the Pay Yourself First App and launch the Social Hub App")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent toGoalSetting = PYFConfirmationActivity.this.getPackageManager().getLaunchIntentForPackage("com.td.innovate.socialfeed");
                        startActivity(toGoalSetting);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = socialHub.create();
        dialog.show();
        ((TextView) dialog.findViewById(android.R.id.message)).setTextSize(15);
    }
}
