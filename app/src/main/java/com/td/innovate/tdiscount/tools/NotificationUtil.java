package com.td.innovate.tdiscount.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.td.innovate.tdiscount.R;

/**
 * Created by zunairsyed on 2015-11-26.
 */
public class NotificationUtil {

    public static final String TAG = "NotificationUtils";
    private static final int NOTIFICATION_DEFAULT_ON = 1000;
    private static final int NOTIFICATION_DEFAULT_OFF = 4000;
    private static final int NOTIFICATION_DEFAULT_COLOR = Color.YELLOW;

    public static void notificatePush(Context context, int notificationId, String tickerText, String contentTitle, String contentText, Intent intent) {
        Notification.Builder mBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.td_icon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setTicker(tickerText)
                .setStyle(new Notification.BigTextStyle().bigText(contentTitle) );



        // Because clicking the notification opens a new ("special") activity, there's no need to create an artificial back stack.
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOnlyAlertOnce(true);

        // Gets an instance of the NotificationManager service
        NotificationManager notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        notifyMgr.notify(notificationId, mBuilder.build());
    }
}