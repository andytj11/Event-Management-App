/*
Name: ANDY TJANDRA
Student ID: 32898460
email: andy0002@student.monash.edu
 */
package com.example.assignment1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_FILTER_EVENT = "SMS_FILTER_EVENT";
    public static final String SMS_FILTER_CATEGORY = "SMS_FILTER_CATEGORY";
    public static final String SMS_MSG_KEY = "SMS_MSG_KEY";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent eventMsgIntent = new Intent();
        Intent categoryMsgIntent = new Intent();
        eventMsgIntent.setAction(SMS_FILTER_EVENT);
        categoryMsgIntent.setAction(SMS_FILTER_CATEGORY);

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String message = currentMessage.getDisplayMessageBody();

            if (message != null && message.trim().toLowerCase().startsWith("event:")) {
                message = trimPrefix(message, "event:");
                eventMsgIntent.putExtra(SMS_MSG_KEY, message);
                context.sendBroadcast(eventMsgIntent);
            } else if (message != null && message.trim().toLowerCase().startsWith("category:")) {
                message = trimPrefix(message, "category:");
                categoryMsgIntent.putExtra(SMS_MSG_KEY, message);
                context.sendBroadcast(categoryMsgIntent);
            } else {
                Toast.makeText(context, "Invalid Message or Command!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String trimPrefix(String message, String prefix){
        if (message.toLowerCase().startsWith(prefix.toLowerCase())) {
            String trimmed = message.substring(prefix.length()).trim();
            return trimmed;
        } else {
            return message;
        }
    }
}