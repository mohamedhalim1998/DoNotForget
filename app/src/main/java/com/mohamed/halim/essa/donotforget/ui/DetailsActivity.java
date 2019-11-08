package com.mohamed.halim.essa.donotforget.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import com.mohamed.halim.essa.donotforget.R;
import com.mohamed.halim.essa.donotforget.data.TaskContract;
import com.mohamed.halim.essa.donotforget.helpers.AlarmMangerHelper;

public class DetailsActivity extends AppCompatActivity {

    private EditText mTask;
    private EditText mDescription;
    private TimePicker mTaskTime;
    private RadioButton mPriorityHigh;
    private RadioButton mPriorityAvg;
    private RadioButton mPriorityLow;

    private Uri mTaskUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent i = getIntent();
        mTask = findViewById(R.id.task_details);
        mDescription = findViewById(R.id.task_description);
        mTaskTime = findViewById(R.id.task_time_details);
        mPriorityHigh = findViewById(R.id.priority_high_rb);
        mPriorityAvg = findViewById(R.id.priority_avg_rb);
        mPriorityLow = findViewById(R.id.priority_low_rb);

        if (i != null) {
            mTaskUri = i.getData();
            populateViews();
        }
    }

    /**
     * populate the views of the activity by the data
     */
    private void populateViews() {
        Cursor c = getContentResolver().query(mTaskUri, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int columnNameIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME);
            int columnTimeIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_TIME);
            int columnDescriptionIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DECSRIPTION);
            int columnPriority = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY);
            mTask.setText(c.getString(columnNameIndex));
            long time = c.getLong(columnTimeIndex);
            mTaskTime.setCurrentHour(((int) time / 60));
            mTaskTime.setCurrentMinute((int) time % 60);
            mDescription.setText(c.getString(columnDescriptionIndex));
            setPriority(c.getInt(columnPriority));

            c.close();
        }
    }

    private void setPriority(int i) {
        switch (i) {
            case TaskContract.TaskEntry.PRIORITY_HIGH:
                mPriorityHigh.setChecked(true);
                break;
            case TaskContract.TaskEntry.PRIORITY_AVERAGE:
                mPriorityAvg.setChecked(true);
                break;
            case TaskContract.TaskEntry.PRIORITY_LOW:
                mPriorityLow.setChecked(true);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_activity_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_done:
                updateTask();
                finish();
                return true;
            case R.id.action_delete:
                deleteTask();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTask() {
        long id = ContentUris.parseId(mTaskUri);
        getContentResolver().delete(mTaskUri, null, null);

        AlarmMangerHelper.dismissAlarm(this, id);
    }

    private void updateTask() {
        String task = mTask.getText().toString();
        String description = mDescription.getText().toString();
        long time = mTaskTime.getCurrentHour() * 60 + mTaskTime.getCurrentMinute();
        long id = ContentUris.parseId(mTaskUri);
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, task);
        values.put(TaskContract.TaskEntry.COLUMN_TASK_TIME, time);
        values.put(TaskContract.TaskEntry.COLUMN_TASK_DECSRIPTION, description);
        values.put(TaskContract.TaskEntry.COLUMN_PRIORITY, getPriority());
        AlarmMangerHelper.setAlarm(this, task, time, id);
        getContentResolver().update(mTaskUri, values, null, null);
    }

    private int getPriority() {
        if (mPriorityHigh.isChecked()) {
            return TaskContract.TaskEntry.PRIORITY_HIGH;
        } else if (mPriorityAvg.isChecked()) {
            return TaskContract.TaskEntry.PRIORITY_AVERAGE;
        } else if (mPriorityLow.isChecked()) {
            return TaskContract.TaskEntry.PRIORITY_LOW;
        }
        return 0;
    }
}
