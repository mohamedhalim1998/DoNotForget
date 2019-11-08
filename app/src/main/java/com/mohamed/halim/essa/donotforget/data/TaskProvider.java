package com.mohamed.halim.essa.donotforget.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.mohamed.halim.essa.donotforget.helpers.AlarmMangerHelper;

public class TaskProvider<NonNull> extends ContentProvider {
    // uriMatcher object to match it to the given uri
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    // uri matcher code for all tasks
    private static final int TASKS = 100;
    // uri matcher code for one task
    private static final int TASKS_ID = 101;

    static {
        // add a uri match rfor all the tasks
        matcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TASKS_PATH, TASKS);
        // add a uri matcher for one task
        matcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TASKS_PATH + "/#", TASKS_ID);
    }

    private SQLiteOpenHelper sqLiteOpenHelper;

    @Override
    public boolean onCreate() {
        sqLiteOpenHelper = new TaskDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = matcher.match(uri);
        SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase();
        Cursor c;
        switch (match) {
            case TASKS:
                c =  database.query(TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TASKS_ID:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                c = database.query(TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        final int match = matcher.match(uri);
        switch (match) {
            case TASKS:
                return TaskContract.TaskEntry.CONTENT_LIST_TYPE;
            case TASKS_ID:
                return TaskContract.TaskEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = matcher.match(uri);
        long id;
        switch (match) {
            case TASKS:
                id  = insetData(values);
                break;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        String task = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_NAME);
        long time = values.getAsLong(TaskContract.TaskEntry.COLUMN_TASK_TIME);
        AlarmMangerHelper.setAlarm(getContext(), task, time, id);
        return ContentUris.withAppendedId(uri, id);
    }


    private long insetData(ContentValues values) {
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        return database.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowDeleted = 0;
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        int match = matcher.match(uri);
        switch (match) {
            case TASKS:
                rowDeleted =  database.delete(TaskContract.TaskEntry.TABLE_NAME,
                selection,
                selectionArgs);
                break;
            case TASKS_ID:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted =  database.delete(TaskContract.TaskEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);

        }
        if(rowDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return rowDeleted;
        }
        throw new IllegalStateException("Error can't delete");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowUpdated = 0;
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        int match = matcher.match(uri);
        switch (match) {
            case TASKS:
                rowUpdated =  database.update(TaskContract.TaskEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TASKS_ID:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowUpdated =  database.update(TaskContract.TaskEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);

        }
        if(rowUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri , null);
            return rowUpdated;
        }
        throw new IllegalStateException("Error can't update");

    }
}
