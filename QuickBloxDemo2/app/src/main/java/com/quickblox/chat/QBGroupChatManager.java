//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.chat;

import android.os.Bundle;
import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.QBChatManager;
import com.quickblox.chat.QBGroupChat;
import com.quickblox.chat.QBMessageStatusesSender;
import com.quickblox.chat.listeners.QBGroupChatManagerListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.query.QueryCreateDialog;
import com.quickblox.chat.query.QueryDeleteDialog;
import com.quickblox.chat.query.QueryUpdateDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBRequestCanceler;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestUpdateBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.chatstates.ChatState;

public class QBGroupChatManager extends QBChatManager<QBGroupChat> {
    private static final Map<XMPPConnection, QBGroupChatManager> INSTANCES = new WeakHashMap();
    private Map<String, QBGroupChat> chatsJidsMap = Collections.synchronizedMap(new HashMap());
    private Set<QBGroupChatManagerListener> chatManagerListeners = new CopyOnWriteArraySet();
    protected QBMessageStatusesSender messageStatusesSender;

    private QBGroupChatManager(XMPPConnection connection) {
        super(connection);
        StanzaFilter filter = MessageTypeFilter.GROUPCHAT;
        connection.addSyncStanzaListener(new StanzaListener() {
            public void processPacket(Stanza packet) {
                QBGroupChatManager.this.processIncomingPacket(packet);
            }
        }, filter);
        StanzaFilter filterError = MessageTypeFilter.ERROR;
        connection.addAsyncStanzaListener(new StanzaListener() {
            public void processPacket(Stanza packet) {
                if(JIDHelper.INSTANCE.isRoomJid(packet.getFrom())) {
                    QBGroupChatManager.this.processIncomingPacket(packet);
                }

            }
        }, filterError);
        INSTANCES.put(connection, this);
        this.messageStatusesSender = new QBMessageStatusesSender();
    }

    static synchronized QBGroupChatManager getInstanceFor(XMPPConnection connection) {
        QBGroupChatManager manager = (QBGroupChatManager)INSTANCES.get(connection);
        if(manager == null) {
            manager = new QBGroupChatManager(connection);
        }

        return manager;
    }

    public QBGroupChat createGroupChat(String jid) throws IllegalArgumentException {
        if(jid.isEmpty()) {
            throw new IllegalArgumentException("Room name and jid can\'t be null or empty");
        } else {
            QBGroupChat groupChat = new QBGroupChat(this, jid);
            this.putChat(groupChat, jid);
            return groupChat;
        }
    }

    public QBGroupChat getGroupChat(String jid) {
        return (QBGroupChat)this.chatsJidsMap.get(jid);
    }

    protected StanzaFilter defineAcknowledgedStanzaFilter() {
        return MessageTypeFilter.GROUPCHAT;
    }

    protected void sendMessageInternal(QBChatMessage chatMessage, QBGroupChat chat) throws NotConnectedException {
        Message smackMessage = chatMessage.getSmackMessage();
        smackMessage.setTo(chat.getJid());
        smackMessage.setType(Type.groupchat);
        chat.getInternalChat().sendMessage(smackMessage);
        this.putMessageToUnacknowledged(smackMessage, chatMessage);
    }

    protected QBGroupChat getChatByOutcomingMessage(Message message) {
        return (QBGroupChat)this.chatsJidsMap.get(message.getTo());
    }

    protected void sendReadStatus(QBChatMessage originMessage) throws NotConnectedException {
        this.messageStatusesSender.sendReadStatus(this.connection(), originMessage);
    }

    void sendDeliveredStatus(Message originMessage) throws NotConnectedException {
        this.messageStatusesSender.sendDeliveredStatus(this.connection(), originMessage);
    }

    void sendDeliveredStatus(QBChatMessage originMessage) throws NotConnectedException {
        this.messageStatusesSender.sendDeliveredStatus(this.connection(), originMessage);
    }

    void sendChatState(String to, Type type, ChatState chatState) throws NotConnectedException {
        this.messageStatusesSender.sendChatState(this.connection(), to, type, chatState);
    }

    private void processIncomingPacket(Stanza packet) {
        Message message = (Message)packet;
        String roomJid;
        if(message.getFrom().contains("/")) {
            roomJid = message.getFrom().split("/")[0];
        } else {
            roomJid = message.getFrom();
        }

        QBGroupChat chat = this.getGroupChat(roomJid);
        if(chat == null) {
            chat = this.createGroupChat(roomJid);
            this.putChat(chat, roomJid);
            if(QBGroupChat.isRoomCreationMessage(message)) {
                chat.deliverRoomCreationMessage(message);
                return;
            }

            Iterator i$ = this.chatManagerListeners.iterator();

            while(i$.hasNext()) {
                QBGroupChatManagerListener listener = (QBGroupChatManagerListener)i$.next();
                listener.chatCreated(chat);
            }
        } else if(QBGroupChat.isRoomCreationMessage(message)) {
            chat.deliverRoomCreationMessage(message);
            return;
        }

        chat.processIncomingMessage(message);
    }

    void putChat(QBGroupChat room, String jid) {
        this.chatsJidsMap.put(jid, room);
    }

    void removeChat(String jid) {
        this.chatsJidsMap.remove(jid);
    }

    public void addGroupChatManagerListener(QBGroupChatManagerListener listener) {
        if(listener != null) {
            this.chatManagerListeners.add(listener);
        }
    }

    public void removeGroupChatManagerListener(QBGroupChatManagerListener listener) {
        this.chatManagerListeners.remove(listener);
    }

    public Collection<QBGroupChatManagerListener> getGroupChatManagerListeners() {
        return Collections.unmodifiableCollection(this.chatManagerListeners);
    }

    public QBRequestCanceler createDialog(QBDialog dialog, QBEntityCallback<QBDialog> callback) {
        QueryCreateDialog queryCreateDialog = new QueryCreateDialog(dialog);
        return new QBRequestCanceler(queryCreateDialog.performAsyncWithCallback(callback));
    }

    public QBDialog createDialog(QBDialog dialog) throws QBResponseException {
        QueryCreateDialog queryCreateDialog = new QueryCreateDialog(dialog);
        return (QBDialog)queryCreateDialog.perform((Bundle)null);
    }

    public QBRequestCanceler updateDialog(QBDialog dialog, QBRequestUpdateBuilder requestBuilder, QBEntityCallback<QBDialog> callback) {
        QueryUpdateDialog queryUpdateDialog = new QueryUpdateDialog(dialog, requestBuilder);
        return new QBRequestCanceler(queryUpdateDialog.performAsyncWithCallback(callback));
    }

    public QBDialog updateDialog(QBDialog dialog, QBRequestUpdateBuilder requestBuilder) throws QBResponseException {
        QueryUpdateDialog queryUpdateDialog = new QueryUpdateDialog(dialog, requestBuilder);
        return (QBDialog)queryUpdateDialog.perform((Bundle)null);
    }

    public QBRequestCanceler deleteDialog(String dialogID, QBEntityCallback<Void> callback) {
        QueryDeleteDialog query = new QueryDeleteDialog(dialogID);
        return new QBRequestCanceler(query.performAsyncWithCallback(callback));
    }

    public Void deleteDialog(String dialogID) throws QBResponseException {
        QueryDeleteDialog query = new QueryDeleteDialog(dialogID);
        return (Void)query.perform((Bundle)null);
    }
}
