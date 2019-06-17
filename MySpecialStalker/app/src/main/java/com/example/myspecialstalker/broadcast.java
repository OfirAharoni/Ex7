package com.example.myspecialstalker;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class broadcast extends BroadcastReceiver {
    private static final String SENT  = "SMS_SENT";
    private static final String DELIVERED  = "SMS_DELIVERED";
    public static final String SENT_EXTRA = "Sent";
    public static final String DELIVERED_EXTRA = "Delivered";
    public static final String SEND_NOTI_TITLE = "SEND";
    public static final String SEND_NOTI_TEXT = "Sending message...";
    public static final int SEND_NOTI_ID = 1;
    public static final String INPUT_EXTRA = "inputExtra";


    @Override
    public void onReceive(Context context, Intent intent) {

        String calling_dest = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

        // Create Broadcast Receiver for sending messages
        BroadcastReceiver sendSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        startService(arg0, SENT_EXTRA);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(arg0, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(arg0, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(arg0, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(arg0, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        // Create Broadcast Receiver for delivering messages
        BroadcastReceiver deliverSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        startService(arg0, DELIVERED_EXTRA);
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(arg0, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        // Initialize the broadcast receivers with appropriate intent filter
        context.getApplicationContext().registerReceiver(sendSMS, new IntentFilter(SENT));
        context.getApplicationContext().registerReceiver(deliverSMS, new IntentFilter(DELIVERED));

        SmsManager smsManager = SmsManager.getDefault();
        String text = MainActivity.sms_text.getText().toString() + calling_dest;
        String sms_dest = MainActivity.sms_num.getText().toString();

        // send SMS
        smsManager.sendTextMessage(sms_dest, null, text, sentPI, deliveredPI);

        // Create and show notification for sending message
        NotificationsIntentService.createNotification(context, SEND_NOTI_TITLE, SEND_NOTI_TEXT, SEND_NOTI_ID);
        }



    public void startService(Context context, String Extra)
    {
        Intent serviceIntent = new Intent(context, NotificationsIntentService.class);
        serviceIntent.putExtra(INPUT_EXTRA, Extra);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
