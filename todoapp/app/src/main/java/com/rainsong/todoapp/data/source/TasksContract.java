package com.rainsong.todoapp.data.source;

import android.provider.BaseColumns;

/**
 * Created by maxliaops on 16-11-28.
 */

public class TasksContract {
    private TasksContract() {

    }

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_ENTRY_ID = "entryId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }
}
