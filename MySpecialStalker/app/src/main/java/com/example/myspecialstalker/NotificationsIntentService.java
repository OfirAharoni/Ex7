package com.example.myspecialstalker;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationsIntentService extends IntentService {
    private static final String CHANNEL_ID = "NotificatioinChannel";
    public static final String TAG = "NotificationsIntentService";
    public static final String INPUT_EXTRA = "inputExtra";
    public static final String SENT_EXTRA = "Sent";
    public static final String SENT_NOTI_TITLE = "SENT";
    public static final String SENT_NOTI_TEXT = "Message has been sent.";
    public static final int SENT_NOTI_ID = 2;
    public static final int FOREGROUND_ID = 1;
    public static final String DELIVERED_EXTRA = "Delivered";
    public static final String DELIVERED_NOTI_TITLE = "DELIVERED";
    public static final String DELIVERED_NOTI_TEXT = "Message has been delivered.";
    public static final int DELIVERED_NOTI_ID = 3;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotificationsIntentService() {
        super(TAG);
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Gets data from the incoming Intent
        if (intent == null)
        {
            return;
        }
        String dataString = intent.getStringExtra(INPUT_EXTRA);

        if (dataString.equals(SENT_EXTRA)) {
            // create notification for sent message
            Notification notification = createNotification(this, SENT_NOTI_TITLE, SENT_NOTI_TEXT, SENT_NOTI_ID);
            if (notification != null)
            {
                startForeground(FOREGROUND_ID, notification);
            }
        }


        else if (dataString.equals(DELIVERED_EXTRA)) {
            // create notification for message delivered
                Notification notification = createNotification(this, DELIVERED_NOTI_TITLE, DELIVERED_NOTI_TEXT, DELIVERED_NOTI_ID);
                if (notification != null)
                {
                    startForeground(FOREGROUND_ID, notification);
                }
            }
        }

    static Notification createNotification(Context context, String title, String text, int id) {
        /**
         * This function will create and display notification for the given parameters.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(id, builder.build());

            return builder.build();
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
