package com.td.innovate.tdiscount.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.model.CreditCard;

import java.util.ArrayList;

/**
 * Created by helenpark on 2015-11-16.
 */
public class PaymentAdapter extends CustomArrayAdapter<CreditCard> {


    public PaymentAdapter(Context context, ArrayList<CreditCard> creditCards) {
        super(context, R.layout.payment_list_item, creditCards);
    }

    public void fillViewHolder(Object viewHolder, CreditCard creditCard) {
        Context context = getContext();
        ViewHolder vh = (ViewHolder) viewHolder;

        //TODO: ADJUST for actual object from bank -> currently hardcoded
        vh.payMainTitle.setText(creditCard.getName());
        vh.payMainDesc.setText("Cashback: " + creditCard.getCashback() + " ANR:" + creditCard.getAnnualPurchaseRate());

        if(creditCard.isRecommenedToUser()){
            vh.linearLayout.setBackgroundColor(Color.parseColor("#2BA93F"));
            vh.payMainTitle.setTextColor(Color.WHITE);
            vh.payMainDesc.setTextColor(Color.parseColor("#97CE9E"));
        }

        //TODO: add in a statement here to set to proper icon
    }

    public Object getViewHolder(View rowView) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.payMainTitle = (TextView) rowView.findViewById(R.id.payment_option_title);
        viewHolder.payMainDesc = (TextView)rowView.findViewById(R.id.payment_option_desc);

        viewHolder.linearLayout= (LinearLayout)rowView.findViewById(R.id.payment_list_layout);
        return viewHolder;
    }

    private final class ViewHolder{
        TextView payMainTitle;
        TextView payMainDesc;
        LinearLayout linearLayout;
    }
}