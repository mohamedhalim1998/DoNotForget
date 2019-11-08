package com.mohamed.halim.essa.donotforget.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.mohamed.halim.essa.donotforget.data.TaskContract;

import java.util.Calendar;

public final class AlarmMangerHelper {

    private AlarmMangerHelper() {
    }

    /**
     * set the alarm for the the given task
     *
     * @param context : context for the calling component
     * @param task    : for the notification content
     * @param time    : for the notification
     */
    public static void setAlarm(Context context, String task, long time, long id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // send the content ro a Broadcast receiver
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra(TaskContract.TaskEntry.COLUMN_TASK_NAME, task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) id, intent, 0);
        int hours = (int) (time / 60);
        int minutes = (int) (time % 60);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        // setting the time for the next day
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        // setting the alarm manger from the current time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * dismiss the alarm if deleted
     * @param context : context for the calling component
     * @param id : for the request code
     */
    public static void dismissAlarm(Context context, long id){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) id, intent, 0);
        alarmManager.cancel(pendingIntent);
    }


}
