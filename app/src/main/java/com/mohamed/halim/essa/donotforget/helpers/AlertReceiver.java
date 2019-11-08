package com.mohamed.halim.essa.donotforget.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mohamed.halim.essa.donotforget.data.TaskContract;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra(TaskContract.TaskEntry.COLUMN_TASK_NAME);
        NotificationHelper.notifyTask(context, name);
    }


}
