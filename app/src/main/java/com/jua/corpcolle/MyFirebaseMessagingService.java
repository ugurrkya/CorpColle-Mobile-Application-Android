package com.jua.corpcolle;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

    }
     public void showNotification(String title, String message){
         Intent intent = new Intent(this, ClientMainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         PendingIntent pendingIntent = PendingIntent.getActivity(this,999, intent, PendingIntent.FLAG_ONE_SHOT);
         if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
             NotificationChannel channel =
                     new NotificationChannel("Notifications","Notifications", NotificationManager.IMPORTANCE_DEFAULT);
             NotificationManager manager = getSystemService(NotificationManager.class);
             manager.createNotificationChannel(channel);
         }


         NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(this, "Notifications");
                notificationbuilder.setSmallIcon(R.drawable.messageout);
                notificationbuilder.setContentTitle(title);
                notificationbuilder.setContentText(message);
                notificationbuilder.setAutoCancel(true);
                notificationbuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(999,notificationbuilder.build());
    }
}
