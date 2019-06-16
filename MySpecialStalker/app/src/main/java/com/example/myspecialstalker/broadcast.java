package com.example.myspecialstalker;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class broadcast extends BroadcastReceiver {
    private static final String TAG  = "CallBroadcastReceiver";
    private static final String SENT  = "SMS_SENT";
    private static final String DELIVERED  = "SMS_DELIVERED";
    public static final String CHANNEL_1_ID = "channel1";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent Received: " + intent.getAction());

        String calling_dest = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

        BroadcastReceiver sendSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //TODO: remove Log
                        Log.i(TAG, "message sent successfully!");
                        Toast.makeText(arg0, "SMS sent",
                                Toast.LENGTH_SHORT).show();
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

        // DELIVERY BroadcastReceiver
        BroadcastReceiver deliverSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //TODO: remove Log
                        Log.i(TAG, "SMS delivered");
                        Toast.makeText(arg0, "message received successfully!",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(arg0, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        // Initialize the broadcast receivers
        context.getApplicationContext().registerReceiver(sendSMS, new IntentFilter(SENT));
        context.getApplicationContext().registerReceiver(deliverSMS, new IntentFilter(DELIVERED));

        SmsManager smsManager = SmsManager.getDefault();
        String text = MainActivity.sms_text.getText().toString() + calling_dest;
        String sms_dest = MainActivity.sms_num.getText().toString();

        // send SMS
        smsManager.sendTextMessage(sms_dest, null, text, sentPI, deliveredPI);
        Log.i(TAG, "sending message...");


    }

}
