//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.chat;

import android.content.Context;
import android.os.Bundle;
import com.quickblox.chat.JIDHelper;
//import com.quickblox.chat.QBGroupChatManager;
import com.quickblox.chat.QBMessageStatusesManager;
import com.quickblox.chat.QBPingManager;
import com.quickblox.chat.QBPrivacyListsManager;
//import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.QBReconnectionManager;
import com.quickblox.chat.QBRoster;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.QBVideoChatWebRTCSignalingManager;
import com.quickblox.chat.listeners.QBSubscriptionListener;
import com.quickblox.chat.model.MobileV3IQ;
import com.quickblox.chat.model.QBChatMessage;
//import com.quickblox.chat.model.QBDialog;
//import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.model.QBChatMessageExtension.Provider;
//import com.quickblox.chat.query.QueryCreateMessage;
//import com.quickblox.chat.query.QueryDeleteMessage;
//import com.quickblox.chat.query.QueryDeleteMessages;
//import com.quickblox.chat.query.QueryGetCountDialogs;
//import com.quickblox.chat.query.QueryGetCountMessage;
//import com.quickblox.chat.query.QueryGetDialogs;
//import com.quickblox.chat.query.QueryGetMessages;
//import com.quickblox.chat.query.QueryGetUnreadMessages;
//import com.quickblox.chat.query.QueryUpdateMessage;
import com.quickblox.chat.utils.ThreadTask;
import com.quickblox.core.QBEntityCallback;
//import com.quickblox.core.QBRequestCanceler;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Lo;
import com.quickblox.core.helper.StringifyArrayList;
//import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.model.QBUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.AlreadyLoggedInException;
import org.jivesoftware.smack.SmackException.ConnectionException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.Roster.SubscriptionMode;
import org.jivesoftware.smack.sasl.SASLErrorException;
import org.jivesoftware.smack.sm.predicates.ForEveryStanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration.Builder;
import org.jivesoftware.smackx.carbons.CarbonManager;
import org.jivesoftware.smackx.delay.provider.DelayInformationProvider;

public class QBChatService extends BaseService {
    private static QBChatService instance;
    private static XMPPTCPConnectionConfiguration configuration;
    private static long defaultConnectionTimeout;
    private static long defaultAutoSendPresenceInterval;
    private XMPPTCPConnection connection;
    private static String defaultResource;
    private QBUser currentUser;
    private QBRoster roster;
//    private QBPrivateChatManager privateChatManager;
//    private QBGroupChatManager groupChatManager;
    private QBMessageStatusesManager statusesManager;
    private QBVideoChatWebRTCSignalingManager videoChatWebRTCSignalingManager;
    private QBPrivacyListsManager privacyListsManager;
    private CarbonManager carbonManager;
    private QBPingManager pingManager;
    private QBSystemMessagesManager systemMessagesManager;
    private boolean useStreamManagement = false;
    private boolean useSmResumption = false;
    private int preferredResumptionTime;
    private final Collection<ConnectionListener> connectionListeners = new CopyOnWriteArrayList();
    private boolean reconnectionAllowed = true;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Runnable stanzaSender = new QBChatService.StanzaSender();
    private ScheduledFuture senderHandler;

    private QBChatService() {
        QBSettings.getInstance().checkInit();
        ProviderManager.addExtensionProvider("extraParams", "jabber:client", new Provider());
        ProviderManager.addExtensionProvider("delay", "urn:xmpp:delay", new DelayInformationProvider());
        ProviderManager.addExtensionProvider("markable", "urn:xmpp:chat-markers:0", new com.quickblox.chat.model.QBChatMarkersExtension.Provider());
        ProviderManager.addExtensionProvider("received", "urn:xmpp:chat-markers:0", new com.quickblox.chat.model.QBChatMarkersExtension.Provider());
        ProviderManager.addExtensionProvider("displayed", "urn:xmpp:chat-markers:0", new com.quickblox.chat.model.QBChatMarkersExtension.Provider());
        this.connectionListeners.add(new QBReconnectionManager());
    }

    /** @deprecated */
    public static boolean isInitialized() {
        return defaultResource != null;
    }

    synchronized void connect() throws IOException, SmackException, XMPPException {
        if(!this.isConnected()) {
            if(this.connection == null) {
                this.connection = new XMPPTCPConnection(configuration);
                this.connection.setUseStreamManagement(this.useStreamManagement);
                this.connection.setUseStreamManagementResumption(this.useSmResumption);
                if(this.preferredResumptionTime > 0) {
                    this.connection.setPreferredResumptionTime(this.preferredResumptionTime);
                }

                this.connection.addConnectionListener(new QBChatService.QBConnectionListener());
                this.connection.addRequestAckPredicate(ForEveryStanza.INSTANCE);
            }

            this.connection.connect();
        }

    }

    /** @deprecated */
    @Deprecated
    public static synchronized void init(Context context) {
        init(context, 0, ProxyInfo.forDefaultProxy());
    }

    /** @deprecated */
    @Deprecated
    public static synchronized void init(Context context, int port, ProxyInfo proxy) {
        defaultResource = QBSettings.getInstance().getChatDefaultResource();
    }

    public static synchronized QBChatService getInstance() {
        if(instance == null) {
            instance = new QBChatService();
        }

        return instance;
    }

    public static boolean isDebugEnabled() {
        return SmackConfiguration.DEBUG;
    }

    public static void setDebugEnabled(boolean debugEnabled) {
        SmackConfiguration.DEBUG = debugEnabled;
    }

    public static void setDefaultPacketReplyTimeout(int timeout) {
        SmackConfiguration.setDefaultPacketReplyTimeout(timeout);
    }

    public static int getDefaultPacketReplyTimeout() {
        return SmackConfiguration.getDefaultPacketReplyTimeout();
    }

    public static long getDefaultConnectionTimeout() {
        return defaultConnectionTimeout;
    }

    public static void setDefaultConnectionTimeout(long connectTimeout) {
        defaultConnectionTimeout = connectTimeout;
    }

    public static long getDefaultAutoSendPresenceInterval() {
        return defaultAutoSendPresenceInterval;
    }

    public static void setDefaultAutoSendPresenceInterval(long intervalInSeconds) {
        defaultAutoSendPresenceInterval = intervalInSeconds;
    }

    public synchronized void login(QBUser user) throws XMPPException, IOException, SmackException {
        if(defaultResource == null) {
            init((Context)null, 0, (ProxyInfo)null);
        }

        this.login(user, defaultResource);
    }

    public synchronized void login(QBUser user, String resource) throws XMPPException, IOException, SmackException {
        this.validateUser(user);
        if(this.isLoggedIn()) {
            throw new AlreadyLoggedInException();
        } else {
            String chatServerEndpoint = QBSettings.getInstance().getChatEndpoint();
            JIDHelper.INSTANCE.setChatServerEndpoint(chatServerEndpoint);
            if(configuration == null) {
                Builder jidLocalpart = XMPPTCPConnectionConfiguration.builder();
                jidLocalpart.setSecurityMode(SecurityMode.disabled);
                jidLocalpart.setServiceName(chatServerEndpoint);
                configuration = jidLocalpart.build();
                Roster.setDefaultSubscriptionMode(SubscriptionMode.manual);
                XMPPTCPConnection.setUseStreamManagementResumptionDefault(false);
            }

            Lo.g("Connecting to chat: " + chatServerEndpoint);
            this.connect();
            if(this.isConnected() && !this.isAuthenticated()) {
                String jidLocalpart1 = JIDHelper.INSTANCE.getJidLocalpart(user);
                Lo.g("Connected. Login to chat, currentUser JID: " + jidLocalpart1 + ", resource: " + resource);
                this.connection.login(jidLocalpart1, user.getPassword(), resource);
                this.currentUser = user;
                this.startAutoSendPresence(defaultAutoSendPresenceInterval);
            }

            this.initStreamManagementListeners();
        }
    }

    private void initStreamManagementListeners() {
//        this.connection.addStanzaAcknowledgedListener(this.getPrivateChatManager());
//        this.connection.addStanzaAcknowledgedListener(this.getGroupChatManager());
    }

    public void login(QBUser user, QBEntityCallback callback) {
        if(defaultResource == null) {
            init((Context)null, 0, (ProxyInfo)null);
        }

        this.login(user, defaultResource, callback);
    }

    public void login(final QBUser user, final String resource, final QBEntityCallback<Void> callback) {
        this.validateUser(user);
        QBChatService.Task var10001 = new QBChatService.Task() {
            public void performInAsync() {
                ArrayList responseException1;
                try {
                    QBChatService.this.login(user, resource);
                    callback.onSuccess((Void)null, Bundle.EMPTY);
                } catch (AlreadyLoggedInException var3) {
                    responseException1 = new ArrayList();
                    responseException1.add("You have already logged in chat");
                    callback.onError(new QBResponseException(responseException1));
                } catch (ConnectionException var4) {
                    responseException1 = new ArrayList();
                    responseException1.add("Connection failed. Please check your internet connection.");
                    callback.onError(new QBResponseException(responseException1));
                } catch (SASLErrorException var5) {
                    responseException1 = new ArrayList();
                    responseException1.add("Authentication failed, check user\'s ID and password");
                    callback.onError(new QBResponseException(responseException1));
                } catch (Exception var6) {
                    QBResponseException responseException = new QBResponseException(var6.getMessage());
                    responseException.initCause(var6);
                    callback.onError(responseException);
                }

            }
        };
    }

    public QBUser getUser() {
        return this.currentUser;
    }

    private void validateUser(QBUser user) {
        if(user.getId() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User\'s id and password can\'t be null");
        }
    }

    public void logout(final QBEntityCallback<Void> callback) {
        QBChatService.Task var10001 = new QBChatService.Task() {
            public void performInAsync() {
                try {
                    QBChatService.this.logout();
                    if(callback != null) {
                        callback.onSuccess((Void)null, Bundle.EMPTY);
                    }
                } catch (NotConnectedException var3) {
                    if(callback != null) {
                        ArrayList errors = new ArrayList();
                        errors.add("You have not connected");
                        callback.onError(new QBResponseException(errors));
                    }
                }

            }
        };
    }

    public synchronized void logout() throws NotConnectedException {
        if(this.isConnected()) {
            this.stopAutoSendPresence();
            this.connection.disconnect();
            this.clearUserConnection();
        } else {
            throw new NotConnectedException();
        }
    }

    public void enterInactiveState() throws NotConnectedException {
        MobileV3IQ mobileIQ = new MobileV3IQ(true);
        mobileIQ.setType(Type.set);
        this.connection.sendStanza(mobileIQ);
    }

    public void enterActiveState() throws NotConnectedException {
        MobileV3IQ mobileIQ = new MobileV3IQ(false);
        mobileIQ.setType(Type.set);
        this.connection.sendStanza(mobileIQ);
    }

    public void startAutoSendPresence(long intervalInSeconds) {
        if(this.stanzaSender != null) {
            this.senderHandler = this.scheduler.scheduleAtFixedRate(this.stanzaSender, intervalInSeconds, intervalInSeconds, TimeUnit.SECONDS);
        }

    }

    public void stopAutoSendPresence() {
        if(this.senderHandler != null && !this.senderHandler.isCancelled()) {
            this.senderHandler.cancel(true);
            this.senderHandler = null;
        }

    }

    public synchronized void destroy() {
        this.logout((QBEntityCallback)null);
        this.connectionListeners.clear();
        this.clearUserConnection();
    }

    private void clearUserConnection() {
        this.stopAutoSendPresence();
        this.clearStreamManagementListeners();
        this.connection = null;
        this.roster = null;
        this.currentUser = null;
//        this.privateChatManager = null;
        this.videoChatWebRTCSignalingManager = null;
//        this.groupChatManager = null;
        this.privacyListsManager = null;
        this.pingManager = null;
        this.statusesManager = null;
        this.systemMessagesManager = null;
        this.carbonManager = null;
    }

    private void clearStreamManagementListeners() {
//        if(this.privateChatManager != null) {
//            this.privateChatManager.clear();
//            this.connection.removeStanzaAcknowledgedListener(this.privateChatManager);
//        }
//
//        if(this.groupChatManager != null) {
//            this.groupChatManager.clear();
//            this.connection.removeStanzaAcknowledgedListener(this.groupChatManager);
//        }

    }

    public synchronized QBRoster getRoster() {
        return this.getRoster((com.quickblox.chat.QBRoster.SubscriptionMode)null, (QBSubscriptionListener)null);
    }

    public synchronized QBRoster getRoster(com.quickblox.chat.QBRoster.SubscriptionMode mode, QBSubscriptionListener subscriptionListener) {
        boolean loggedIn = this.isLoggedIn();
        boolean rosterNull = this.roster == null;
        if(loggedIn && rosterNull) {
            this.roster = new QBRoster(this.connection, mode, subscriptionListener);
        }

        return this.roster;
    }

//    public synchronized QBPrivateChatManager getPrivateChatManager() {
//        if(this.isLoggedIn() && this.privateChatManager == null) {
//            this.privateChatManager = QBPrivateChatManager.getInstanceFor(this.connection);
//        }
//
//        return this.privateChatManager;
//    }

    public synchronized QBMessageStatusesManager getMessageStatusesManager() {
        if(this.isLoggedIn() && this.statusesManager == null) {
            this.statusesManager = QBMessageStatusesManager.getInstanceFor(this.connection);
        }

        return this.statusesManager;
    }

//    public synchronized QBGroupChatManager getGroupChatManager() {
//        if(this.isLoggedIn() && this.groupChatManager == null) {
//            this.groupChatManager = QBGroupChatManager.getInstanceFor(this.connection);
//        }
//
//        return this.groupChatManager;
//    }

    public synchronized QBVideoChatWebRTCSignalingManager getVideoChatWebRTCSignalingManager() {
        if(this.isLoggedIn() && this.videoChatWebRTCSignalingManager == null) {
            this.videoChatWebRTCSignalingManager = QBVideoChatWebRTCSignalingManager.getInstanceFor(this.connection);
        }

        return this.videoChatWebRTCSignalingManager;
    }

    public synchronized QBPrivacyListsManager getPrivacyListsManager() {
        if(this.isLoggedIn() && this.privacyListsManager == null) {
            this.privacyListsManager = QBPrivacyListsManager.getInstanceFor(this.connection);
        }

        return this.privacyListsManager;
    }

    public synchronized QBPingManager getPingManager() {
        if(this.isLoggedIn() && this.pingManager == null) {
            this.pingManager = QBPingManager.getInstanceFor(this.connection);
        }

        return this.pingManager;
    }

    public synchronized QBSystemMessagesManager getSystemMessagesManager() {
        if(this.isLoggedIn() && this.systemMessagesManager == null) {
            this.systemMessagesManager = QBSystemMessagesManager.getInstanceFor(this.connection);
        }

        return this.systemMessagesManager;
    }

    public boolean isLoggedIn() {
        return this.connection != null && this.connection.isConnected() && this.connection.isAuthenticated();
    }

    boolean isConnected() {
        return this.connection != null && this.connection.isConnected();
    }

    boolean isAuthenticated() {
        return this.connection != null && this.connection.isAuthenticated();
    }

    public void addConnectionListener(ConnectionListener connectionListener) {
        if(connectionListener != null) {
            this.connectionListeners.add(connectionListener);
        }
    }

    public void removeConnectionListener(ConnectionListener connectionListener) {
        this.connectionListeners.remove(connectionListener);
    }

    public boolean isReconnectionAllowed() {
        return this.reconnectionAllowed;
    }

    public void setReconnectionAllowed(boolean reconnectionAllowed) {
        this.reconnectionAllowed = reconnectionAllowed;
    }

    public void setUseStreamManagement(boolean useSm) {
        if(this.connection == null) {
            this.useStreamManagement = useSm;
        }

    }

    public boolean isStreamManagementEnabled() {
        return this.useStreamManagement;
    }

    public void setUseStreamManagementResumption(boolean useSmResumption) {
        if(this.connection == null) {
            this.useSmResumption = useSmResumption;
        }

    }

    public void setPreferredResumptionTime(int resumptionTime) {
        if(this.connection == null) {
            this.preferredResumptionTime = resumptionTime;
        }

    }

    Collection<ConnectionListener> getConnectionListeners() {
        return Collections.unmodifiableCollection(this.connectionListeners);
    }

    private boolean checkSession() throws NotLoggedInException {
        if(!this.isLoggedIn()) {
            throw new NotLoggedInException();
        } else {
            return true;
        }
    }

    public void enableCarbons() throws XMPPException, SmackException {
        if(this.carbonManager == null) {
            this.carbonManager = CarbonManager.getInstanceFor(this.connection);
        }

        this.carbonManager.enableCarbons();
    }

    public void disableCarbons() throws XMPPException, SmackException {
        if(this.carbonManager == null) {
            this.carbonManager = CarbonManager.getInstanceFor(this.connection);
        }

        this.carbonManager.disableCarbons();
    }

    public boolean getCarbonsEnabled() {
        if(this.carbonManager == null) {
            this.carbonManager = CarbonManager.getInstanceFor(this.connection);
        }

        return this.carbonManager.getCarbonsEnabled();
    }

//    public static QBRequestCanceler getChatDialogs(QBDialogType type, QBRequestGetBuilder requestBuilder, QBEntityCallback<ArrayList<QBDialog>> callback) {
//        QueryGetDialogs query = new QueryGetDialogs(type);
//        query.setRequestBuilder(requestBuilder);
//        return new QBRequestCanceler(query.performAsyncWithCallback(callback));
//    }

//    public static ArrayList<QBDialog> getChatDialogs(QBDialogType type, QBRequestGetBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
//        QueryGetDialogs query = new QueryGetDialogs(type);
//        query.setRequestBuilder(requestBuilder);
//        return (ArrayList)query.perform(returnedBundle);
//    }

//    public static Integer getChatDialogsCount(QBRequestGetBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
//        QueryGetCountDialogs query = new QueryGetCountDialogs();
//        query.setRequestBuilder(requestBuilder);
//        return (Integer)query.perform(returnedBundle);
//    }

//    public static QBRequestCanceler getChatDialogsCount(QBRequestGetBuilder requestBuilder, QBEntityCallback<Integer> callback) {
//        QueryGetCountDialogs query = new QueryGetCountDialogs();
//        query.setRequestBuilder(requestBuilder);
//        return new QBRequestCanceler(query.performAsyncWithCallback(callback));
//    }

//    public static QBRequestCanceler getDialogMessages(QBDialog dialog, QBRequestGetBuilder requestBuilder, QBEntityCallback<ArrayList<QBChatMessage>> callback) {
//        QueryGetMessages query = new QueryGetMessages(dialog, requestBuilder);
//        query.setRequestBuilder(requestBuilder);
//        return new QBRequestCanceler(query.performAsyncWithCallback(callback));
//    }

//    public static ArrayList<QBChatMessage> getDialogMessages(QBDialog dialog, QBRequestGetBuilder requestBuilder, Bundle returnedBundle) throws QBResponseException {
//        QueryGetMessages query = new QueryGetMessages(dialog, requestBuilder);
//        query.setRequestBuilder(requestBuilder);
//        return (ArrayList)query.perform(returnedBundle);
//    }

//    public static QBRequestCanceler getDialogMessagesCount(String dialogID, QBEntityCallback<Integer> callback) {
//        QueryGetCountMessage query = new QueryGetCountMessage(dialogID);
//        return new QBRequestCanceler(query.performAsyncWithCallback(callback));
//    }

//    public static Integer getDialogMessagesCount(String dialogID, Bundle returnedBundle) throws QBResponseException {
//        QueryGetCountMessage query = new QueryGetCountMessage(dialogID);
//        return (Integer)query.perform(returnedBundle);
//    }

//    public static QBRequestCanceler markMessagesAsRead(String dialogId, StringifyArrayList<String> messageIds, QBEntityCallback<Void> callback) {
//        QueryUpdateMessage updateDialogMessage = new QueryUpdateMessage(dialogId, messageIds);
//        return new QBRequestCanceler(updateDialogMessage.performAsyncWithCallback(callback));
//    }

//    public static Void markMessagesAsRead(String dialogId, StringifyArrayList<String> messageIds) throws QBResponseException {
//        QueryUpdateMessage updateDialogMessage = new QueryUpdateMessage(dialogId, messageIds);
//        return (Void)updateDialogMessage.perform((Bundle)null);
//    }

//    public static QBRequestCanceler deleteMessage(String messageId, QBEntityCallback<Void> callback) {
//        QueryDeleteMessage queryDeleteDialogMessage = new QueryDeleteMessage(messageId);
//        return new QBRequestCanceler(queryDeleteDialogMessage.performAsyncWithCallback(callback));
//    }

//    public static Void deleteMessage(String messageId) throws QBResponseException {
//        QueryDeleteMessage queryDeleteDialogMessage = new QueryDeleteMessage(messageId);
//        return (Void)queryDeleteDialogMessage.perform((Bundle)null);
//    }

//    public static QBRequestCanceler deleteMessages(Set<String> messageIDs, QBEntityCallback<Void> callback) {
//        QueryDeleteMessages queryDeleteDialogMessages = new QueryDeleteMessages(messageIDs);
//        return new QBRequestCanceler(queryDeleteDialogMessages.performAsyncWithCallback(callback));
//    }

//    public static Void deleteMessages(Set<String> messageIDs) throws QBResponseException {
//        QueryDeleteMessages queryDeleteDialogMessages = new QueryDeleteMessages(messageIDs);
//        return (Void)queryDeleteDialogMessages.perform((Bundle)null);
//    }

//    public static QBRequestCanceler createMessage(QBChatMessage message, QBEntityCallback<QBChatMessage> callback) {
//        QueryCreateMessage query = new QueryCreateMessage(message);
//        return new QBRequestCanceler(query.performAsyncWithCallback(callback));
//    }

//    public static QBChatMessage createMessage(QBChatMessage message) throws QBResponseException {
//        QueryCreateMessage query = new QueryCreateMessage(message);
//        return (QBChatMessage)query.perform((Bundle)null);
//    }

//    public static QBRequestCanceler getTotalUnreadMessagesCount(Set<String> dialogIDs, QBEntityCallback<Integer> callback) {
//        QueryGetUnreadMessages query = new QueryGetUnreadMessages(dialogIDs);
//        return new QBRequestCanceler(query.performAsyncWithCallback(callback));
//    }

//    public static Integer getTotalUnreadMessagesCount(Set<String> dialogIDs, Bundle returnedBundle) throws QBResponseException {
//        QueryGetUnreadMessages query = new QueryGetUnreadMessages(dialogIDs);
//        return (Integer)query.perform(returnedBundle);
//    }

    static {
        defaultConnectionTimeout = (long)XMPPTCPConnectionConfiguration.DEFAULT_CONNECT_TIMEOUT;
        defaultAutoSendPresenceInterval = (long)XMPPTCPConnectionConfiguration.DEFAULT_CONNECT_TIMEOUT;
    }

    private class QBConnectionListener implements ConnectionListener {
        private QBConnectionListener() {
        }

        public void connected(XMPPConnection connection) {
            Iterator i$ = QBChatService.this.connectionListeners.iterator();

            while(i$.hasNext()) {
                ConnectionListener listener = (ConnectionListener)i$.next();
                listener.connected(connection);
            }

        }

        public void authenticated(XMPPConnection connection, boolean resumed) {
            Iterator i$ = QBChatService.this.connectionListeners.iterator();

            while(i$.hasNext()) {
                ConnectionListener listener = (ConnectionListener)i$.next();
                listener.authenticated(connection, resumed);
            }

        }

        public void connectionClosed() {
            Iterator i$ = QBChatService.this.connectionListeners.iterator();

            while(i$.hasNext()) {
                ConnectionListener listener = (ConnectionListener)i$.next();
                listener.connectionClosed();
            }

        }

        public void connectionClosedOnError(Exception e) {
            Iterator i$ = QBChatService.this.connectionListeners.iterator();

            while(i$.hasNext()) {
                ConnectionListener listener = (ConnectionListener)i$.next();
                listener.connectionClosedOnError(e);
            }

        }

        public void reconnectingIn(int seconds) {
            Iterator i$ = QBChatService.this.connectionListeners.iterator();

            while(i$.hasNext()) {
                ConnectionListener listener = (ConnectionListener)i$.next();
                listener.reconnectingIn(seconds);
            }

        }

        public void reconnectionSuccessful() {
            Iterator i$ = QBChatService.this.connectionListeners.iterator();

            while(i$.hasNext()) {
                ConnectionListener listener = (ConnectionListener)i$.next();
                listener.reconnectionSuccessful();
            }

        }

        public void reconnectionFailed(Exception e) {
            Iterator i$ = QBChatService.this.connectionListeners.iterator();

            while(i$.hasNext()) {
                ConnectionListener listener = (ConnectionListener)i$.next();
                listener.reconnectionFailed(e);
            }

        }
    }

    private class StanzaSender implements Runnable {
        private StanzaSender() {
        }

        public void run() {
            if(QBChatService.this.connection != null) {
                try {
                    QBChatService.this.connection.sendStanza(new Presence(org.jivesoftware.smack.packet.Presence.Type.available));
                } catch (NotConnectedException var2) {
                    Lo.g("Sent presence failed: " + (var2.getMessage() != null?var2.getMessage():""));
                }
            }

        }
    }

    private abstract class Task extends ThreadTask {
        public Task() {
            super((Bundle)null, QBChatService.this.scheduler);
        }
    }
}
