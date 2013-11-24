package com.aryan.donttextme.core;

/**
 * Created by Shayan on 11/18/13.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.aryan.donttextme.configuration.AppConfig;
import com.aryan.donttextme.util.StringUtil;

public class SMSReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        String MSG_TYPE = intent.getAction();


        if (MSG_TYPE.equals("android.provider.Telephony.SMS_RECEIVED")) {

            Bundle bundle = intent.getExtras();
            Object pdu[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[pdu.length];
            for (int n = 0; n < pdu.length; n++) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) pdu[n]);
            }

            Toast toast = Toast.makeText(context, "Received SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
            toast.show();

            DataBaseManager db = new DataBaseManager(context);
            db.AddToInbox(smsMessage, 48);
            if (db.isInWhiteList(smsMessage)) {
                toast = Toast.makeText(context, "This sender is in white list: " + smsMessage[0].getOriginatingAddress(), Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (db.isInBlackList(smsMessage)) {
                toast = Toast.makeText(context, "BLOCKED SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
                toast.show();
                abortBroadcast();
            } else if (PossiblyIsSpam(smsMessage[0])) {
                // user chooses
                toast = Toast.makeText(context, "Possibly Spam SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
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

    private boolean PossiblyIsSpam(SmsMessage message) {
        if (message.getOriginatingAddress().length() < AppConfig.REGULAR_PHONE_NUMBERS_LENGTH)
            return true;
        else if (!StringUtil.IsNumber(message.getOriginatingAddress()))
            return true;
        else if (isInSpamList(message.getOriginatingAddress()))
            return true;
        else
            return false;
    }

    private boolean isInSpamList(String sender){
        if(sender.equals(AppConfig.IRANCELL_A))
            return true;
        else
            return false;
    }


}
