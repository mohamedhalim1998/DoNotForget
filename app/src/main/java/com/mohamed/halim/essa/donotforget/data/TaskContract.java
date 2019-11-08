package com.mohamed.halim.essa.donotforget.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class TaskContract {
    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.mohamed.halim.essa.donotforget";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    // path for tasks
    public static final String TASKS_PATH = "tasks";


    public static class TaskEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TASKS_PATH);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TASKS_PATH;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TASKS_PATH;

        // table name for tasks
        public static final String TABLE_NAME = "tasks";
        // Id for the task
        // Type : INTEGER
        public static final String _ID = BaseColumns._ID;
        // the name of the task
        // type : TEXT
        public static final String COLUMN_TASK_NAME = "name";

        // the time of the task
        // type : REAL
        public static final String COLUMN_TASK_TIME = "time";

        // the description of a task
        // type : TEXT
        public static final String COLUMN_TASK_DECSRIPTION = "description";

        // the priority of the task
        // type : INTEGER
        public static final String COLUMN_PRIORITY = "priority";

        // possible values for the priority field
        public static final int PRIORITY_HIGH = 1;
        public static final int PRIORITY_AVERAGE = 2;
        public static final int PRIORITY_LOW = 3;

        public static final int DEFAULT_PRIORITY = PRIORITY_AVERAGE;


    }


}
