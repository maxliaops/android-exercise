//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.Manager;
import com.quickblox.chat.utils.ThreadTask;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;

public class QBPingManager extends Manager {
    private static final Map<XMPPConnection, QBPingManager> INSTANCES = new WeakHashMap();
    private final PingManager pingManager;
    private final Executor executor;

    private QBPingManager(XMPPConnection connection) {
        super(connection);
        this.executor = AsyncTask.SERIAL_EXECUTOR;
        this.pingManager = PingManager.getInstanceFor(connection);
    }

    public void addPingFailedListener(PingFailedListener pingFailedListener) {
        this.pingManager.registerPingFailedListener(pingFailedListener);
    }

    public void removePingFailedListener(PingFailedListener pingFailedListener) {
        this.pingManager.registerPingFailedListener(pingFailedListener);
    }

    public boolean pingServer() throws NotConnectedException {
        return this.pingManager.pingMyServer();
    }

    public void pingServer(final QBEntityCallback<Void> callback) {
        ThreadTask var10001 = new ThreadTask((Bundle)null, this.executor) {
            public void performInAsync() {
                boolean response = false;
                String error = null;

                try {
                    response = QBPingManager.this.pingManager.pingMyServer();
                } catch (NotConnectedException var4) {
                    error = "You have not connected";
                }

                if(callback != null) {
                    if(response) {
                        callback.onSuccess((Void)null, Bundle.EMPTY);
                    } else {
                        QBResponseException re = new QBResponseException(error != null?QBPingManager.this.errorsToList(new String[]{error}):QBPingManager.this.errorsToList(new String[]{"Server is unavailable"}));
                        callback.onError(re);
                    }
                }

            }
        };
    }

    public boolean pingUser(int userId) throws NotConnectedException {
        try {
            return this.pingManager.ping(JIDHelper.INSTANCE.getJid(userId));
        } catch (NoResponseException var3) {
            return false;
        }
    }

    public void pingUser(final int userId, final QBEntityCallback<Void> callback) {
        ThreadTask var10001 = new ThreadTask((Bundle)null, this.executor) {
            public void performInAsync() {
                boolean response = false;
                String error = null;

                try {
                    response = QBPingManager.this.pingUser(userId);
                } catch (NotConnectedException var4) {
                    error = "You have not connected";
                }

                if(callback != null) {
                    if(response) {
                        callback.onSuccess((Void)null, Bundle.EMPTY);
                    } else {
                        QBResponseException re = new QBResponseException(error != null?QBPingManager.this.errorsToList(new String[]{error}):QBPingManager.this.errorsToList(new String[]{"Server is unavailable"}));
                        callback.onError(re);
                    }
                }

            }
        };
    }

    public void setPingInterval(int pingInterval) {
        this.pingManager.setPingInterval(pingInterval);
    }

    static synchronized QBPingManager getInstanceFor(XMPPConnection connection) {
        QBPingManager manager = (QBPingManager)INSTANCES.get(connection);
        if(manager == null) {
            manager = new QBPingManager(connection);
        }

        return manager;
    }

    public List<String> errorsToList(String... errors) {
        return Arrays.asList(errors);
    }
}
