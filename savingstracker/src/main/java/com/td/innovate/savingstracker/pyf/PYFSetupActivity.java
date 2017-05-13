package com.td.innovate.savingstracker.pyf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.savingstracker.MainActivity;
import com.td.innovate.savingstracker.R;

import java.util.Calendar;
import java.util.Locale;


public class PYFSetupActivity extends Activity {
    private final double oneMonthAgoValue = DataManipulation.getXMonthAgoCashFlow(1);
    private final double twoMonthAgoValue = DataManipulation.getXMonthAgoCashFlow(2);
    private final double threeMonthAgoValue = DataManipulation.getXMonthAgoCashFlow(3);
    private final double fourMonthAgoValue = DataManipulation.getXMonthAgoCashFlow(4);
    private final double maxValue = Math.max(0, Math.max(Math.max(oneMonthAgoValue, twoMonthAgoValue), Math.max(threeMonthAgoValue, fourMonthAgoValue)));
    private final double minValue = Math.min(0, Math.min(Math.min(oneMonthAgoValue, twoMonthAgoValue), Math.min(threeMonthAgoValue, fourMonthAgoValue)));
    private final double maxValueRounded = Math.ceil(maxValue / 100) * 100;
    private final double minValueRounded = Math.floor(minValue / 100) * 100;
    private double PYFAmount = DataManipulation.getPYFAmount();
    private final Calendar month = (Calendar) DataManipulation.getNow().clone();
    private float selectorBarHeightOffset;
    private TextView PYFAmountTextView;
    private EditText TopPYFAmountTextView;
    private View selector;
    private LinearLayout barGraphLayout;
    private LinearLayout selectorMovingZoneLayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pyfsetup, menu);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pyfsetup);

        if (PYFAmount == 0) PYFAmount = 100;
        setMonthName();
        setMonthBarHeight();
        selector = findViewById(R.id.selector);
        final LinearLayout sampleBar = (LinearLayout) findViewById(R.id.fourMonthAgoBar);
        sampleBar.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        selectorBarHeightOffset = selector.getHeight() / 2;
                        float barHeight = sampleBar.getMeasuredHeight();
                        float startingPosition = (float) ((maxValueRounded - PYFAmount) / (maxValueRounded - minValueRounded) * barHeight);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layoutParams.setMargins(0, (int) startingPosition, 0, 0);
                        selector.setLayoutParams(layoutParams);
                        return true;
                    }
                });
        dragToEditAmount();
        tapToEditAmount();
    }

    private void setMonthName() {
        month.add(Calendar.MONTH, -1);
        ((TextView) findViewById(R.id.oneMonthAgoText)).setText(String.format(Locale.CANADA, "%tb", month).toUpperCase());
        month.add(Calendar.MONTH, -1);
        ((TextView) findViewById(R.id.twoMonthAgoText)).setText(String.format(Locale.CANADA, "%tb", month).toUpperCase());
        month.add(Calendar.MONTH, -1);
        ((TextView) findViewById(R.id.threeMonthAgoText)).setText(String.format(Locale.CANADA, "%tb", month).toUpperCase());
        month.add(Calendar.MONTH, -1);
        ((TextView) findViewById(R.id.fourMonthAgoText)).setText(String.format(Locale.CANADA, "%tb", month).toUpperCase());
    }

    private void setMonthBarHeight() {
        if (oneMonthAgoValue >= 0) {
            findViewById(R.id.oneMonthAgoTopGap).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) ((maxValueRounded - oneMonthAgoValue) / (maxValueRounded - minValueRounded))));
            findViewById(R.id.oneMonthAgoPositiveBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (oneMonthAgoValue / (maxValueRounded - minValueRounded))));
            findViewById(R.id.oneMonthAgoNegativeBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (-minValueRounded / (maxValueRounded - minValueRounded))));
            findViewById(R.id.oneMonthAgoNegativeBar).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.oneMonthAgoTopGap).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            findViewById(R.id.oneMonthAgoPositiveBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (maxValueRounded / (maxValueRounded - minValueRounded))));
            findViewById(R.id.oneMonthAgoNegativeBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (-oneMonthAgoValue / (maxValueRounded - minValueRounded))));
            findViewById(R.id.oneMonthAgoPositiveBar).setVisibility(View.INVISIBLE);
        }
        if (twoMonthAgoValue >= 0) {
            findViewById(R.id.twoMonthAgoTopGap).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) ((maxValueRounded - twoMonthAgoValue) / (maxValueRounded - minValueRounded))));
            findViewById(R.id.twoMonthAgoPositiveBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (twoMonthAgoValue / (maxValueRounded - minValueRounded))));
            findViewById(R.id.twoMonthAgoNegativeBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (-minValueRounded / (maxValueRounded - minValueRounded))));
            findViewById(R.id.twoMonthAgoNegativeBar).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.twoMonthAgoTopGap).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            findViewById(R.id.twoMonthAgoPositiveBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (maxValueRounded / (maxValueRounded - minValueRounded))));
            findViewById(R.id.twoMonthAgoNegativeBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (-twoMonthAgoValue / (maxValueRounded - minValueRounded))));
            findViewById(R.id.twoMonthAgoPositiveBar).setVisibility(View.INVISIBLE);
        }
        if (threeMonthAgoValue >= 0) {
            findViewById(R.id.threeMonthAgoTopGap).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) ((maxValueRounded - threeMonthAgoValue) / (maxValueRounded - minValueRounded))));
            findViewById(R.id.threeMonthAgoPositiveBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (threeMonthAgoValue / (maxValueRounded - minValueRounded))));
            findViewById(R.id.threeMonthAgoNegativeBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (-minValueRounded / (maxValueRounded - minValueRounded))));
            findViewById(R.id.threeMonthAgoNegativeBar).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.threeMonthAgoTopGap).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            findViewById(R.id.threeMonthAgoPositiveBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (maxValueRounded / (maxValueRounded - minValueRounded))));
            findViewById(R.id.threeMonthAgoNegativeBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (-threeMonthAgoValue / (maxValueRounded - minValueRounded))));
            findViewById(R.id.threeMonthAgoPositiveBar).setVisibility(View.INVISIBLE);
        }
        if (fourMonthAgoValue >= 0) {
            findViewById(R.id.fourMonthAgoTopGap).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) ((maxValueRounded - fourMonthAgoValue) / (maxValueRounded - minValueRounded))));
            findViewById(R.id.fourMonthAgoPositiveBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (fourMonthAgoValue / (maxValueRounded - minValueRounded))));
            findViewById(R.id.fourMonthAgoNegativeBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (-minValueRounded / (maxValueRounded - minValueRounded))));
            findViewById(R.id.fourMonthAgoNegativeBar).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.fourMonthAgoTopGap).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            findViewById(R.id.fourMonthAgoPositiveBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (maxValueRounded / (maxValueRounded - minValueRounded))));
            findViewById(R.id.fourMonthAgoNegativeBar).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, (float) (-fourMonthAgoValue / (maxValueRounded - minValueRounded))));
            findViewById(R.id.fourMonthAgoPositiveBar).setVisibility(View.INVISIBLE);
        }
    }

    private void dragToEditAmount() {
        PYFAmountTextView = (TextView) findViewById(R.id.pyf_amount);
        ((TextView) findViewById(R.id.max)).setText("$" + String.format("%1$,.0f", maxValueRounded));
        ((TextView) findViewById(R.id.min)).setText("-$" + String.format("%1$,.0f", -minValueRounded));
        PYFAmountTextView.setText("$" + String.format("%1$,.0f", PYFAmount));
        TopPYFAmountTextView = (EditText) findViewById(R.id.app_description_text_view);
        TopPYFAmountTextView.setText("$" + String.format("%1$,.0f", PYFAmount));

        LinearLayout selectorMovingZone = (LinearLayout) findViewById(R.id.selectorMovingZone);
        selectorMovingZone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float y = motionEvent.getY();
                movingBar(y);
                return true;
            }
        });
    }

    private void movingBar(float y) {
        y -= selectorBarHeightOffset;
        final float barHeight = findViewById(R.id.fourMonthAgoBar).getHeight();
        final float minHeight = (float) ((maxValueRounded - maxValue) / (maxValueRounded - minValueRounded)) * barHeight;
        final float maxHeight = (float) (maxValueRounded / (maxValueRounded - minValueRounded)) * barHeight;
        if (y <= minHeight) y = minHeight;
        if (y >= maxHeight) y = maxHeight;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutParams.setMargins(0, (int) y, 0, 0);
        selector.setLayoutParams(layoutParams);
        PYFAmount = (maxHeight - y) / maxHeight * maxValueRounded;
        PYFAmountTextView.setText("$" + String.format("%1$,.0f", PYFAmount));
        TopPYFAmountTextView.setText("$" + String.format("%1$,.0f", PYFAmount));
    }

    private void tapToEditAmount() {
        final LinearLayout sampleBar = (LinearLayout) findViewById(R.id.fourMonthAgoBar);
        barGraphLayout = (LinearLayout) findViewById(R.id.barGraph);
        selectorMovingZoneLayout = (LinearLayout) findViewById(R.id.selectorMovingZone);
        TopPYFAmountTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String paymentAmount = TopPYFAmountTextView.getText().toString();
                TopPYFAmountTextView.setFocusableInTouchMode(true);
                barGraphLayout.clearFocus();
                selectorMovingZoneLayout.clearFocus();
                TopPYFAmountTextView.requestFocus();
                if (!paymentAmount.equals("")) {
                    TopPYFAmountTextView.setText("");
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(TopPYFAmountTextView, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        TopPYFAmountTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(TopPYFAmountTextView.getWindowToken(), 0);
                    if (TopPYFAmountTextView.getText().toString().equals("")) {
                        PYFAmount = 0;
                    } else {
                        PYFAmount = Double.parseDouble(TopPYFAmountTextView.getText().toString().replace(",", "").replace("$", ""));
                    }
                    if (PYFAmount > maxValue) {
                        Toast.makeText(PYFSetupActivity.this, "Over Limit, Try Again", Toast.LENGTH_SHORT).show();
                        PYFAmount = maxValue;
                    }
                    TopPYFAmountTextView.setText("$" + String.format("%1$,.0f", PYFAmount));
                    PYFAmountTextView.setText("$" + String.format("%1$,.0f", PYFAmount));

                    sampleBar.getViewTreeObserver().addOnPreDrawListener(
                            new ViewTreeObserver.OnPreDrawListener() {
                                public boolean onPreDraw() {
                                    float barHeight = sampleBar.getMeasuredHeight();
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    layoutParams.setMargins(0, (int) (barHeight * (maxValueRounded - PYFAmount) / (maxValueRounded - minValueRounded)), 0, 0);
                                    selector.setLayoutParams(layoutParams);
                                    return true;
                                }
                            });
                    TopPYFAmountTextView.setFocusableInTouchMode(false);
                }
            }
        });

        TopPYFAmountTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    TopPYFAmountTextView.setFocusableInTouchMode(false);
                    TopPYFAmountTextView.clearFocus();
                }
                return false;
            }
        });
    }

    public void launchConfirmationActivity(View view) {
        AlertDialog.Builder confirmationAlert = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        confirmationAlert
                .setTitle("Confirm")
                .setMessage("Your PYF Amount is set up as a recurring monthly transfer (simulation) which will occur at the beginning of each month.")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        DataManipulation.setPYFAmount(PYFAmount);
                        Intent toConfirmationPage = new Intent(PYFSetupActivity.this, PYFConfirmationActivity.class);
                        startActivity(toConfirmationPage);
                        finish();
                    }
                })
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog dialog = confirmationAlert.create();
        dialog.show();
        ((TextView) dialog.findViewById(android.R.id.message)).setTextSize(15);
    }
}