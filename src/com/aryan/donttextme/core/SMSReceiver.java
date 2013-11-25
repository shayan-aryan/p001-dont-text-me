package com.aryan.donttextme.core;

/**
 * Created by Shayan on 11/18/13.
 */

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.aryan.donttextme.MainActivity;
import com.aryan.donttextme.R;
import com.aryan.donttextme.configuration.AppConfig;
import com.aryan.donttextme.util.StringUtil;

public class SMSReceiver extends BroadcastReceiver {

    private Context mContext;

    public void onReceive(Context context, Intent intent) {
        String MSG_TYPE = intent.getAction();


        if (MSG_TYPE.equals("android.provider.Telephony.SMS_RECEIVED")) {
            mContext = context;
            Bundle bundle = intent.getExtras();
            Object pdu[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[pdu.length];
            for (int n = 0; n < pdu.length; n++) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) pdu[n]);
            }

            Toast toast = Toast.makeText(context, "Received SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
            toast.show();

            DataBaseManager db = new DataBaseManager(context);
            //////////////////
            db.AddToInbox(smsMessage, 48);
            /*notifyBlockedMessage(smsMessage[0]);
            abortBroadcast();*/
            //////////////////
            if (db.isInWhiteList(smsMessage)) {
                /*toast = Toast.makeText(context, "This sender is in white list: " + smsMessage[0].getOriginatingAddress(), Toast.LENGTH_LONG);
                toast.show();*/
                return;
            } else if (db.isInBlackList(smsMessage)) {
                /*toast = Toast.makeText(context, "BLOCKED SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
                toast.show();*/
                notifyBlockedMessage(smsMessage[0]);
                abortBroadcast();
            } else if (isPossiblySpam(smsMessage[0])) {
                // user chooses
                /*toast = Toast.makeText(context, "Possibly Spam SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
                toast.show();*/
                notifyBlockedMessage(smsMessage[0]);
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

    private boolean isPossiblySpam(SmsMessage message) {
        String address = message.getOriginatingAddress();
        if (!StringUtil.IsNumber(address))
            return true;
        else if (!isMobileNumber(address))
            return true;
        else
            return false;
    }

    private boolean isInSpamList(String sender) {
        if (sender.equals(AppConfig.IRANCELL_A))
            return true;
        else
            return false;
    }

    private boolean isMobileNumber(String number) {
        String prefix = number.substring(0, 6);
        String[] mobilePrefixes = mContext.getResources().getStringArray(R.array.mobile_number_prefixes);
        for (int i = 0; i < mobilePrefixes.length; i++)
            if (prefix.equals(mobilePrefixes[i]))
                return true;

        return false;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void notifyBlockedMessage(SmsMessage message) {
        String title = mContext.getString(R.string.message_received);
        String body = message.getOriginatingAddress() + ":\r\n" + message.getMessageBody();
        Intent i = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
        Notification n = new Notification(R.drawable.notification, title + "\r\n" + body, System.currentTimeMillis());
        n.flags = Notification.FLAG_AUTO_CANCEL;
        n.setLatestEventInfo(mContext, title, body, pendingIntent);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }


}
