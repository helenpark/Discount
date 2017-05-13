package com.td.innovate.savingstracker.tracker;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.td.innovate.savingstracker.DataManipulation;

/**
 * Created by Cassia Deering on 14-12-11. :)
 */
public class ListenerService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String messageToSend = DataManipulation.getThisMonthReoccurringIncomeUpdatedAmount() + "," +
                DataManipulation.getThisMonthReoccurringExpenseUpdatedAmount() + "," +
                DataManipulation.getPYFAmount() + "," +
                DataManipulation.getThisMonthOtherCreditAmount() + "," +
                DataManipulation.getThisMonthOtherDebitAmount();
        new SummaryActivity.SendMessageOnOtherThread(messageToSend, null).start();
    }
}
