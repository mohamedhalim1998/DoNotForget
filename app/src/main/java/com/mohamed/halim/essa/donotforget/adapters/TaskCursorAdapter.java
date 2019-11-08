package com.mohamed.halim.essa.donotforget.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mohamed.halim.essa.donotforget.R;
import com.mohamed.halim.essa.donotforget.data.TaskContract;

public class TaskCursorAdapter extends CursorAdapter {
    public TaskCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView taskName = view.findViewById(R.id.task_name_tv);
        TextView taskTime = view.findViewById(R.id.task_time_tv);

        int columnNameIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);
        int columnTimeIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_TIME);
        int columnPriorityIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY);

        String nameValue = cursor.getString(columnNameIndex);
        long timeValue = cursor.getLong(columnTimeIndex);
        int priority = cursor.getInt(columnPriorityIndex);
        String timeString = formatTime(timeValue);
        taskName.setText(nameValue);
        taskTime.setText(timeString);
        view.setBackgroundColor(getMatchColor(priority, context));
    }

    private int getMatchColor(int priority, Context context) {
        switch (priority) {
            case TaskContract.TaskEntry.PRIORITY_HIGH:
                return context.getResources().getColor(R.color.priority_high);
            case TaskContract.TaskEntry.PRIORITY_LOW:
                return context.getResources().getColor(R.color.priority_low);
            default:
                return context.getResources().getColor(R.color.priority_average);
        }
    }

    private String formatTime(long timeValue) {
        return String.format("%02d:%02d", timeValue / 60, timeValue % 60);
    }
}
