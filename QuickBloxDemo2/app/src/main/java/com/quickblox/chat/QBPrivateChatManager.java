//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.chat;

import android.os.Bundle;
import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.QBChatManager;
import com.quickblox.chat.QBMessageStatusesSender;
import com.quickblox.chat.QBPrivateChat;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.listeners.QBPrivateChatManagerListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.query.QueryCreateDialog;
import com.quickblox.chat.query.QueryDeleteDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBRequestCanceler;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Lo;
import java.util.ArrayList;
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

public class QBPrivateChatManager extends QBChatManager<QBPrivateChat> implements StanzaListener {
    private static final Map<XMPPConnection, QBPrivateChatManager> INSTANCES = new WeakHashMap();
    private Map<Integer, QBPrivateChat> chatsMap = Collections.synchronizedMap(new HashMap());
    private Set<QBPrivateChatManagerListener> chatManagerListeners = new CopyOnWriteArraySet();
    protected QBMessageStatusesSender messageStatusesSender;

    private QBPrivateChatManager(XMPPConnection connection) {
        super(connection);
        StanzaFilter filter = MessageTypeFilter.CHAT;
        connection.addSyncStanzaListener(new StanzaListener() {
            public void processPacket(Stanza packet) {
                QBPrivateChatManager.this.processIncomingPacket(packet);
            }
        }, filter);
        StanzaFilter filterError = MessageTypeFilter.ERROR;
        connection.addAsyncStanzaListener(new StanzaListener() {
            public void processPacket(Stanza packet) {
                if(!JIDHelper.INSTANCE.isRoomJid(packet.getFrom())) {
                    QBPrivateChatManager.this.processIncomingPacket(packet);
                }

            }
        }, filterError);
        INSTANCES.put(connection, this);
        this.messageStatusesSender = new QBMessageStatusesSender();
    }

    public QBPrivateChat createChat(int userId, QBMessageListener listener) {
        QBPrivateChat chat = this.createChat(userId, true);
        chat.addMessageListener(listener);
        return chat;
    }

    public QBPrivateChat getChat(int userId) {
        return (QBPrivateChat)this.chatsMap.get(Integer.valueOf(userId));
    }

    public boolean contains(int userId) {
        return this.chatsMap.containsKey(Integer.valueOf(userId));
    }

    public void addPrivateChatManagerListener(QBPrivateChatManagerListener listener) {
        if(listener != null) {
            this.chatManagerListeners.add(listener);
        }
    }

    public void removePrivateChatManagerListener(QBPrivateChatManagerListener listener) {
        this.chatManagerListeners.remove(listener);
    }

    public Collection<QBPrivateChatManagerListener> getPrivateChatManagerListeners() {
        return Collections.unmodifiableCollection(this.chatManagerListeners);
    }

    protected void sendReadStatus(QBChatMessage originMessage) throws NotConnectedException {
        this.messageStatusesSender.sendReadStatus(this.connection(), originMessage);
    }

    protected void sendReadStatus(Integer senderId, String originMessageID, String dialogId) throws NotConnectedException {
        this.messageStatusesSender.sendReadStatus(this.connection(), senderId, originMessageID, dialogId);
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

    protected void processIncomingPacket(Stanza packet) {
        Message message = (Message)packet;
        int userId = JIDHelper.INSTANCE.parseUserId(message.getFrom());
        QBPrivateChat chat = this.getChat(userId);
        if(chat == null) {
            chat = this.createChat(message);
        }

        if(chat != null) {
            chat.processIncomingMessage(message);
        }
    }

    static synchronized QBPrivateChatManager getInstanceFor(XMPPConnection connection) {
        QBPrivateChatManager manager = (QBPrivateChatManager)INSTANCES.get(connection);
        if(manager == null) {
            manager = new QBPrivateChatManager(connection);
        }

        return manager;
    }

    private QBPrivateChat createChat(int userId, boolean createdLocally) {
        QBPrivateChat chat = new QBPrivateChat(this, userId);
        this.chatsMap.put(Integer.valueOf(userId), chat);
        Iterator i$ = this.chatManagerListeners.iterator();

        while(i$.hasNext()) {
            QBPrivateChatManagerListener listener = (QBPrivateChatManagerListener)i$.next();
            listener.chatCreated(chat, createdLocally);
        }

        return chat;
    }

    void closeChat(QBPrivateChat chat) {
        int userId = chat.getParticipant();
        this.chatsMap.remove(Integer.valueOf(userId));
    }

    private QBPrivateChat createChat(Message message) {
        String userJID = message.getFrom();
        return userJID == null?null:this.createChat(JIDHelper.INSTANCE.parseUserId(userJID), false);
    }

    protected void sendMessageInternal(QBChatMessage message, QBPrivateChat chat) throws NotConnectedException {
        Message smackMessage = message.getSmackMessage();
        String toJid = JIDHelper.INSTANCE.getJid(chat.getParticipant());
        smackMessage.setTo(toJid);
        smackMessage.setType(Type.chat);
        Lo.g("QBPrivateChatManager, sending a message with id " + message.getId());
        this.connection().sendStanza(smackMessage);
        this.putMessageToUnacknowledged(smackMessage, message);
    }

    protected QBPrivateChat getChatByOutcomingMessage(Message message) {
        return (QBPrivateChat)this.chatsMap.get(Integer.valueOf(JIDHelper.INSTANCE.parseUserId(message.getTo())));
    }

    public QBRequestCanceler createDialog(int participantId, QBEntityCallback<QBDialog> callback) {
        ArrayList occupantIds = new ArrayList();
        occupantIds.add(Integer.valueOf(participantId));
        QBDialog dialog = this.createDialogLocal((String)null, QBDialogType.PRIVATE, occupantIds);
        QueryCreateDialog queryCreateDialog = new QueryCreateDialog(dialog);
        return new QBRequestCanceler(queryCreateDialog.performAsyncWithCallback(callback));
    }

    public QBDialog createDialog(int participantId) throws QBResponseException {
        ArrayList occupantIds = new ArrayList();
        occupantIds.add(Integer.valueOf(participantId));
        QBDialog dialog = this.createDialogLocal((String)null, QBDialogType.PRIVATE, occupantIds);
        QueryCreateDialog queryCreateDialog = new QueryCreateDialog(dialog);
        return (QBDialog)queryCreateDialog.perform((Bundle)null);
    }

    private QBDialog createDialogLocal(String name, QBDialogType type, ArrayList<Integer> occupantIds) {
        QBDialog dialog = new QBDialog();
        dialog.setName(name);
        dialog.setType(type);
        dialog.setOccupantsIds(occupantIds);
        return dialog;
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
