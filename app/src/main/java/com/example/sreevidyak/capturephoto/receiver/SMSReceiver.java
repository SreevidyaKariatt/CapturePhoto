package com.example.sreevidyak.capturephoto.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.sreevidyak.capturephoto.R;
import com.example.sreevidyak.capturephoto.ui.SmsActivity;

/**
 * Created by Sreevidya K on 7/26/2017.
 */

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages ;
        if(bundle != null){
            if (Build.VERSION.SDK_INT >= 19) {
                messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            } else {
                Object pdus[] = (Object[]) bundle.get("pdus");
                messages = new SmsMessage[pdus.length];
                for(int i = 0; i < pdus.length; i++){
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
            }

            for(SmsMessage currentMessage : messages){

                //Build the content of notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentTitle(currentMessage.getDisplayOriginatingAddress());
                builder.setContentText(currentMessage.getDisplayMessageBody());
                builder.setSmallIcon(R.drawable.ic_message_black_24dp);
                builder.setTicker(currentMessage.getDisplayMessageBody());
                builder.setAutoCancel(true);

                //Provide the explicit intent for the notification
                Intent intent1 = new Intent(context, SmsActivity.class);
                intent1.putExtra("Sender",currentMessage.getDisplayOriginatingAddress());
                intent1.putExtra("Message",currentMessage.getDisplayMessageBody());
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                //Add the  backstack using taskbuilder and set the intent to pending intent
                PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                //notification through notification manager
                Notification notification = builder.build();
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(124,notification);
            }
        }
    }
}
