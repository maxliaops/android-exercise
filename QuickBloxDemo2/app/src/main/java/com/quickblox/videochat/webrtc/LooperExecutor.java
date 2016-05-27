//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.videochat.webrtc;

import android.annotation.TargetApi;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import com.quickblox.videochat.webrtc.util.Logger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

public class LooperExecutor extends Thread implements Executor {
    private static final String TAG = "LooperExecutor";
    private static final Logger LOGGER = Logger.getInstance("RTCClient");
    private final Object looperStartedEvent;
    private String executorOwner;
    private LooperExecutor.ExecuteCondition executeCondition;
    private Handler handler;
    private volatile boolean running;
    private long threadId;
    private volatile boolean stopping;
    private Set<Object> customers;
    private Set<LooperExecutor.ExecutorLifecycleListener> lifecycleListeners;

    public LooperExecutor() {
        this.looperStartedEvent = new Object();
        this.handler = null;
        this.running = false;
        this.stopping = false;
        this.customers = new HashSet();
        this.lifecycleListeners = new HashSet();
    }

    public LooperExecutor(LooperExecutor.ExecuteCondition executeCondition) {
        this();
        this.executeCondition = executeCondition;
    }

    public void setExecutionCondition(LooperExecutor.ExecuteCondition executeCondition) {
        this.executeCondition = executeCondition;
    }

    public LooperExecutor(Class classOwner) {
        this();
        this.executorOwner = classOwner.getSimpleName();
        LOGGER.d("LooperExecutor", "Create looper executor on thread: " + Thread.currentThread().getId() + " for " + this.executorOwner);
    }

    public void run() {
        Looper.prepare();
        Object var1 = this.looperStartedEvent;
        synchronized(this.looperStartedEvent) {
            LOGGER.d("LooperExecutor", "Looper thread started.");
            this.handler = new Handler();
            this.threadId = Thread.currentThread().getId();
            LOGGER.d("LooperExecutor", "Looper thread started on thread." + this.threadId);
            this.looperStartedEvent.notify();
        }

        Looper.loop();
    }

    public synchronized void requestStart() {
        LOGGER.d("LooperExecutor", "Request Looper start. On " + this.executorOwner);
        if(!this.running) {
            this.running = true;
            this.handler = null;
            this.start();
            Object var1 = this.looperStartedEvent;
            synchronized(this.looperStartedEvent) {
                while(this.handler == null) {
                    try {
                        this.looperStartedEvent.wait();
                    } catch (InterruptedException var4) {
                        LOGGER.e("LooperExecutor", "Can not start looper thread");
                        this.running = false;
                    }
                }

            }
        }
    }

    public boolean isStarted() {
        return this.running;
    }

    public boolean isStopped() {
        return this.stopping;
    }

    public synchronized void requestStop() {
        LOGGER.d("LooperExecutor", "Request Looper stop. On " + this.executorOwner);
        if(this.running) {
            if(this.customers != null && this.customers.size() > 0) {
                LOGGER.d("LooperExecutor", "Can\'t stop tasks execution. Task execution customers list not empty");
            } else {
                this.stopping = true;
                this.handler.post(new Runnable() {
                    @TargetApi(18)
                    public void run() {
                        if(VERSION.SDK_INT < 18) {
                            Looper.myLooper().quit();
                        } else {
                            Looper.myLooper().quitSafely();
                        }

                        LooperExecutor.LOGGER.d("LooperExecutor", "Looper thread finished.");
                        Iterator i$ = LooperExecutor.this.lifecycleListeners.iterator();

                        while(i$.hasNext()) {
                            LooperExecutor.ExecutorLifecycleListener listener = (LooperExecutor.ExecutorLifecycleListener)i$.next();
                            listener.onExecutorStop();
                        }

                    }
                });
            }
        }
    }

    public boolean checkOnLooperThread() {
        return Thread.currentThread().getId() == this.threadId;
    }

    public synchronized void execute(Runnable runnable) {
        if(this.executeCondition == null || this.executeCondition.isExecutionAllow()) {
            LOGGER.d("LooperExecutor", "Request Looper execute.");
            if(!this.running) {
                LOGGER.w("LooperExecutor", "Running looper executor without calling requestStart()");
                return;
            }

            if(this.stopping) {
                LOGGER.w("LooperExecutor", "looper executor has been finished!");
                return;
            }

            if(Thread.currentThread().getId() == this.threadId) {
                runnable.run();
                LOGGER.d("LooperExecutor", "EXECUTE.Run on thread:" + this.threadId + " for " + this.executorOwner);
            } else {
                LOGGER.d("LooperExecutor", "POST.Run on thread:" + this.threadId + " for " + this.executorOwner);
                this.handler.post(runnable);
            }
        }

    }

    public void addExecutorCustomer(Object customer) {
        this.customers.add(customer);
    }

    public void removeExecutorCustomer(Object customer) {
        this.customers.remove(customer);
    }

    public void addExecutorLifecycleListener(LooperExecutor.ExecutorLifecycleListener listener) {
        this.lifecycleListeners.add(listener);
    }

    public void removeExecutorLifecycleListener(LooperExecutor.ExecutorLifecycleListener listener) {
        this.lifecycleListeners.remove(listener);
    }

    interface ExecutorLifecycleListener {
        void onExecutorStop();
    }

    interface ExecuteCondition {
        boolean isExecutionAllow();
    }
}
