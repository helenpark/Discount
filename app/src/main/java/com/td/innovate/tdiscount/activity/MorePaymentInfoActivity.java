package com.td.innovate.tdiscount.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.td.innovate.savingstracker.DataManipulation;
import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.model.Profile;

public class MorePaymentInfoActivity extends AppCompatActivity {

    private double itemCost;
    private double moneyBefore;
    private double moneyAfter;

    private TextView titleTV;
    private TextView descriptionTV;

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_payment_info);

        profile = new Profile(this);

        titleTV = (TextView) findViewById(R.id.moreInfoTitle);
        descriptionTV = (TextView) findViewById(R.id.moreInfoDescription);

        String description = "";

        Bundle extras = getIntent().getExtras();
        itemCost = extras.getDouble("itemCost");
        moneyBefore = profile.getBalanceBeforePurchase();
        moneyAfter = profile.getBalanceBeforePurchase() - itemCost;

        description += "You currently have " + getPriceString(moneyBefore);
        description += ". If you buy this item, which costs " + getPriceString(itemCost);
        description += ", then you will have " + getPriceString(moneyAfter) + " left.";

        if (moneyAfter >= 0) {
            titleTV.setText("Why you probably can afford this...");
            description += " As a result, you can afford this item.";
        } else {
            titleTV.setText("Why you probably can't afford this...");
            description += " As a result, you can't afford this item.";
        }

        double thisMonthReoccurringExpenseAmount = DataManipulation.getThisMonthReoccurringExpenseUpdatedAmount();
        double thisMonthReoccurringIncomeAmount = DataManipulation.getThisMonthReoccurringIncomeUpdatedAmount();

        description += "\n\nYour recurring expenses add up to " + getPriceString(thisMonthReoccurringExpenseAmount);
        description += " and your recurring income adds up to " + getPriceString(thisMonthReoccurringIncomeAmount) + ".";

        descriptionTV.setText(description);

        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getPriceString(double value) {
        return String.format("$%.2f", value);
    }
}
