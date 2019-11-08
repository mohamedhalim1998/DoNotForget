package com.mohamed.halim.essa.donotforget.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mohamed.halim.essa.donotforget.adapters.InsertDialog;
import com.mohamed.halim.essa.donotforget.R;
import com.mohamed.halim.essa.donotforget.adapters.TaskCursorAdapter;
import com.mohamed.halim.essa.donotforget.data.TaskContract;
import com.mohamed.halim.essa.donotforget.helpers.AlarmMangerHelper;
import com.mohamed.halim.essa.donotforget.helpers.NotificationHelper;

public class MainActivity extends AppCompatActivity implements InsertDialog.AddTaskListener, LoaderManager.LoaderCallbacks<Cursor> {
    // log tag
    private final String TAG = MainActivity.class.getSimpleName();
    private final String INSERT_DIALOG_TAG = "insert dialog";
    private ListView mTasksList;
    CursorAdapter mAdapter = new TaskCursorAdapter(this, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton mAddActionButton = findViewById(R.id.fab);
        mTasksList = findViewById(R.id.tasks_list);

        NotificationHelper.createTaskNotificationChannel(this);

        mTasksList.setAdapter(mAdapter);
        getLoaderManager().initLoader(100, null, this);

        mTasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                i.setData(ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id));
                startActivity(i);
            }
        });
        mAddActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInsertDialog();
            }
        });
    }

    /**
     * open the dialog for insert a new task
     */
    private void openInsertDialog() {

        InsertDialog insertDialog = new InsertDialog(this);
        insertDialog.show(getSupportFragmentManager(), INSERT_DIALOG_TAG);
    }

    /**
     * add a task to the data base and set the alarm to send a notification
     * @param task : the task to store
     * @param time : the time for the task
     */
    @Override
    public void addTask(String task,long time) {


        ContentValues values = new ContentValues();

        values.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, task);
        values.put(TaskContract.TaskEntry.COLUMN_TASK_TIME, time);
        values.put(TaskContract.TaskEntry.COLUMN_PRIORITY, TaskContract.TaskEntry.DEFAULT_PRIORITY);
        getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, values);
    }



    /**
     * load the cursor for the data
     * @param id : loader id
     * @param args : bundle extras
     * @return cursor
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TASK_NAME,
                TaskContract.TaskEntry.COLUMN_PRIORITY,
                TaskContract.TaskEntry.COLUMN_TASK_TIME};
        return new CursorLoader(this,
                TaskContract.TaskEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void DummyDelete(View view) {
        getContentResolver().delete(TaskContract.TaskEntry.CONTENT_URI, null, null);
    }
}
