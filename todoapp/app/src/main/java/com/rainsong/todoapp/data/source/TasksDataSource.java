package com.rainsong.todoapp.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rainsong.todoapp.data.Task;
import com.rainsong.todoapp.data.source.TasksContract.TaskEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxliaops on 16-11-22.
 */

public class TasksDataSource {
    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static TasksDataSource INSTANCE;

    private TasksDbHelper mDbHelper;

    public interface LoadTasksCallback {
        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    public static TasksDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksDataSource(context);
        }
        return INSTANCE;
    }

    private TasksDataSource(Context context) {
        mDbHelper = new TasksDbHelper(context);
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }


    private void addTask(String title, String description) {
        Task task = new Task(title, description);
        saveTask(task);
    }

    public void saveTask(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());

        db.insert(TaskEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void getTasks(final LoadTasksCallback callback) {
        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry._ID,
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION
        };

        Cursor c = db.query(
                TaskEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry
                        .COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description = c.getString(c.getColumnIndexOrThrow(TaskEntry
                        .COLUMN_NAME_DESCRIPTION));
                Task task = new Task(title, description, itemId);
                tasks.add(task);
            }
        }
        if (c != null) {
            c.close();
        }
        if (tasks.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(tasks);
        }
    }
}
