package com.rainsong.todoapp.data.source;

import android.os.Handler;

import com.google.common.collect.Lists;
import com.rainsong.todoapp.data.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maxliaops on 16-11-22.
 */

public class TasksDataSource {
    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static TasksDataSource INSTANCE;

    private final static Map<String, Task> TASKS_SERVICE_DATA;

    public interface LoadTasksCallback {
        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    static {
        TASKS_SERVICE_DATA = new HashMap<>();
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    public static TasksDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TasksDataSource();
        }
        return INSTANCE;
    }

    private TasksDataSource() {
    }


    private static void addTask(String title, String description) {
        Task task = new Task(title, description);
        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    public void getTasks(final LoadTasksCallback callback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Task> tasks = Lists.newArrayList(TASKS_SERVICE_DATA.values());
                callback.onTasksLoaded(tasks);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }
}
