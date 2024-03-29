package com.example.myspecialstalker;
// Created by Ofir Aharoni

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String READ_PHONE_STATE = "READ_PHONE_STATE";
    public static final String PROCESS_OUTGOING_CALLS = "PROCESS_OUTGOING_CALLS";
    public static final String SEND_SMS = "SEND_SMS";
    public static final String RECEIVE_SMS = "RECEIVE_SMS";

    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    public static final int MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS = 1;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 3;
    private static final String CHANNEL_ID = "NotificatioinChannel";
    private static final String CHANNEL_NAME = "SMS Notifications channel";
    public static final String SHARED_PREFERENCES = "shared preferences";
    public static final String SMS_NUM_KEY = "sms_num";
    public static final String SMS_TEXT_KEY = "sms_text";
    public static final String DEF_VALUE = "";
    public static final String DEF_TEXT_VALUE = "I'm going to call this number:";
    public static final String MESSAGE_FOR_FILLED_FIELDS = "The app is ready to send SMS messages.";
    public static final String MESSAGE_FOR_UNFILLED_FIELDS = "Please make sure all the fields are filled.";
    static public EditText sms_num;
    static public EditText sms_text;
    public TextView info_text;


    private Map<String, Boolean> permissions_map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sms_num = findViewById(R.id.sms_num);
        sms_text = findViewById(R.id.sms_text);
        info_text = findViewById(R.id.info_text);

        permissions_map.put(READ_PHONE_STATE, false);
        permissions_map.put(PROCESS_OUTGOING_CALLS, false);
        permissions_map.put(SEND_SMS, false);
        permissions_map.put(RECEIVE_SMS, false);


        initialize_widgets();

        // Check if READ_PHONE_STATE permission is granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

        }
        else {
            permissions_map.put(READ_PHONE_STATE, true);
            check_if_permissions_granted();
        }

        // Check if PROCESS_OUTGOING_CALLS permission is granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.PROCESS_OUTGOING_CALLS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                    MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS);
        }
        else {
            permissions_map.put(PROCESS_OUTGOING_CALLS, true);
            check_if_permissions_granted();
        }

        // Check if SEND_SMS permission is granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);

        }
        else {
            permissions_map.put(SEND_SMS, true);
            check_if_permissions_granted();
        }

        // Check if RECEIVE_SMS permission is granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_RECEIVE_SMS);

        }
        else {
            permissions_map.put(RECEIVE_SMS, true);
            check_if_permissions_granted();
        }

        createNotificationChannel();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissions_map.put(READ_PHONE_STATE, true);
                    check_if_permissions_granted();
                }

            }

            case MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissions_map.put(PROCESS_OUTGOING_CALLS, true);
                    check_if_permissions_granted();
                }
            }

            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissions_map.put(SEND_SMS, true);
                    check_if_permissions_granted();
                }
            }

            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissions_map.put(RECEIVE_SMS, true);
                    check_if_permissions_granted();
                }
            }
        }

    }

    /**
     * This function check if all the permissions were granted by the user.
     * if so, return true. Return false otherwise.
     */
    public void check_if_permissions_granted() {
        if (permissions_map.get(READ_PHONE_STATE) && permissions_map.get(PROCESS_OUTGOING_CALLS)
                && permissions_map.get(SEND_SMS) && permissions_map.get(RECEIVE_SMS)) {
            update_widgets();
        }
    }


    /**
     * This function initalize the 3 widgets - 2 EditText and one TextView.
     * The EditText widgets should be uneditable as long as the user not granting
     * permissions.
     */
    public void initialize_widgets() {
        sms_text.setEnabled(false);
        sms_num.setEnabled(false);
        update_tv();

        sms_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                update_tv();
                saveData();
            }
        });

        // set EditText change listener.
        sms_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                update_tv();
                saveData();
            }
        });

    }

    /**
     * This function will check if the user input is valid.
     * i.e: number format should be: "0550001122" or "+9723456789".
     * @return true if the input in valid. false otherwise.
     */
    public boolean check_sms_num(String sms_num)
    {
        if (sms_num.length() ==  11)
        {

            if (sms_num.charAt(0) != '+')
            {
                return false;
            }

            else return android.text.TextUtils.isDigitsOnly(sms_num.substring(1));
        }
        else if (sms_num.length() ==  10)
        {
            return android.text.TextUtils.isDigitsOnly(sms_num);
        }

        return false;
    }

    /**
     * This function will update the TextView according to the user input
     * in the EditText widgets.
     */
    public void update_tv()
    {
        if (check_sms_num(sms_num.getText().toString()) &&
        sms_text.getText().toString().length() > 0)
        {

            info_text.setText(MESSAGE_FOR_FILLED_FIELDS);
        }
        else
        {
            info_text.setText(MESSAGE_FOR_UNFILLED_FIELDS);
        }
    }

    /**
     * This function set the EditText widgets to be editable.
     */
    public void update_widgets() {
        sms_num.setEnabled(true);
        sms_text.setEnabled(true);
        loadData();
    }


    /**
     * This function save the input from the user with sharedPreferences.
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SMS_NUM_KEY, sms_num.getText().toString());
        if (sms_text.getText().toString().length() == 0)
        {
            editor.putString(SMS_TEXT_KEY, DEF_TEXT_VALUE);
        }
        else
        {
            editor.putString(SMS_TEXT_KEY, sms_text.getText().toString());
        }
        editor.apply();
    }

    /**
     * This function load data from sharedPreferences in other to obtain
     * previous input from the user.
     */
    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String load_sms_num = sharedPreferences.getString(SMS_NUM_KEY, DEF_VALUE);
        String load_sms_text = sharedPreferences.getString(SMS_TEXT_KEY, DEF_TEXT_VALUE);
        sms_num.setText(load_sms_num);
        sms_text.setText(load_sms_text);
    }

    /**
     * This function is responsible for creating a notification channel
     * for SMS sending process.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}

