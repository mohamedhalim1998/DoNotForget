package com.mohamed.halim.essa.donotforget.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mohamed.halim.essa.donotforget.data.TaskContract.TaskEntry;

import static android.content.ContentValues.TAG;

public class TaskDbHelper extends SQLiteOpenHelper {
    // Name of the database file
    private static final String DATABASE_NAME = "task.db";


     // Database version. If you change the database schema, you must increment the database version.

    private static final int DATABASE_VERSION = 4;

    /**
     * Constructs a new instance of TaskDbHelper.
     *
     * @param context of the app
     */
    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TASKS_TABLE =  "CREATE TABLE " + TaskEntry.TABLE_NAME + " ("
                + TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskEntry.COLUMN_TASK_NAME + " TEXT, "
                + TaskEntry.COLUMN_TASK_DECSRIPTION + " TEXT, "
                + TaskEntry.COLUMN_PRIORITY + " REAL , "
                + TaskEntry.COLUMN_TASK_TIME + " REAL);";
        Log.e(TAG, "onCreate: " + SQL_CREATE_TASKS_TABLE );
        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS "+ TaskEntry.TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
