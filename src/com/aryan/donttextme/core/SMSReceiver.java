package com.aryan.donttextme.core;

/**
 * Created by Shayan on 11/18/13.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        String MSG_TYPE = intent.getAction();


        if (MSG_TYPE.equals("android.provider.Telephony.SMS_RECEIVED")) {

            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];
            for (int n = 0; n < messages.length; n++) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            }

            DataBaseManager db = new DataBaseManager(context);
            if (db.isInBlackListAndNotInWhiteList(smsMessage[0])) {
                Toast toast = Toast.makeText(context, "BLOCKED Received SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
                toast.show();
                abortBroadcast();
            }


        }
//        else if (MSG_TYPE.equals("android.provider.Telephony.SEND_SMS")) {
//	        Toast toast = Toast.makeText(context,"SMS SENT: "+MSG_TYPE , Toast.LENGTH_LONG);
//	        toast.show();
//	        abortBroadcast();
//	        for(int i=0;i<8;i++)
//	        {
//	            System.out.println("Blocking SMS **********************");
//	        }
//
//        } else {
//
//            Toast toast = Toast.makeText(context, "SIN ELSE: " + MSG_TYPE, Toast.LENGTH_LONG);
//            toast.show();
//            abortBroadcast();
//            for (int i = 0; i < 8; i++) {
//                System.out.println("Blocking SMS **********************");
//            }
//        }

    }

}
