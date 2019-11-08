package com.mohamed.halim.essa.donotforget.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mohamed.halim.essa.donotforget.ui.MainActivity;
import com.mohamed.halim.essa.donotforget.R;

public class NotificationHelper {

    public static final String NOTI_CH_ID = "Task notification";


    public static void createTaskNotificationChannel(Context c){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(NOTI_CH_ID
                    ,"Task Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes attributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build();
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            channel.setSound(sound, attributes);

            channel.enableVibration(true);
            NotificationManager manager = c.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public static void notifyTask(Context c,String taskName){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, NOTI_CH_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle(c.getString(R.string.task_notification_title));
        builder.setContentText(taskName);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sound);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);

        Intent i = new Intent(c, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(c,0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        notificationManager.notify(12, builder.build());
    }



}
