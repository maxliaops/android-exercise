//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.chat;

import android.os.Bundle;
import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.QBAbstractChat;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBGroupChatManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBIsTypingListener;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.listeners.QBMessageSentListener;
import com.quickblox.chat.listeners.QBParticipantListener;
import com.quickblox.chat.model.QBChatMarkersExtension;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBPresence;
import com.quickblox.chat.model.QBChatMarkersExtension.ChatMarker;
import com.quickblox.chat.utils.ThreadTask;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.CommonUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

public class QBGroupChat extends QBAbstractChat {
    private static final String CREATE_ROOM_FIRST_CONTROL_MESSAGE = "Welcome! You created new Multi User Chat Room. Room is locked now. Configure it please!";
    private static final String CREATE_ROOM_LAST_CONTROL_MESSAGE = "Room is now unlocked";
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Set<QBMessageListener> messageListeners = new CopyOnWriteArraySet();
    private Set<QBMessageSentListener> messageSentListeners = new CopyOnWriteArraySet();
    private final Set<QBIsTypingListener> isTypingListeners = new CopyOnWriteArraySet();
    private final Set<QBParticipantListener> participantListeners = new CopyOnWriteArraySet();
    private QBGroupChatManager groupChatManager;
    private MultiUserChat multiUserChat;
    private String jid;
    private String dialogId;

    QBGroupChat(QBGroupChatManager roomChatManager, String jid) {
        this.groupChatManager = roomChatManager;
        this.jid = jid;
        this.multiUserChat = MultiUserChatManager.getInstanceFor(roomChatManager.connection()).getMultiUserChat(jid);
        this.dialogId = JIDHelper.INSTANCE.dialogIdFromRoomJid(jid);
    }

    public String getDialogId() {
        return this.dialogId;
    }

    public void join(DiscussionHistory discussionHistory, QBEntityCallback callback) {
        new QBGroupChat.JoinRoomTask(discussionHistory, callback);
    }

    public synchronized void join(DiscussionHistory discussionHistory) throws XMPPException, SmackException, IllegalStateException {
        String currentUserJid = this.groupChatManager.connection().getUser();
        if(currentUserJid == null) {
            throw new IllegalStateException("You have not logged in");
        } else {
            String nickname = String.valueOf(JIDHelper.INSTANCE.parseUserId(currentUserJid));
            this.multiUserChat.join(nickname, (String)null, discussionHistory, (long)SmackConfiguration.getDefaultPacketReplyTimeout());
            this.multiUserChat.addParticipantListener(new QBGroupChat.ParticipantListener());
        }
    }

    public void leave() throws XMPPException, NotConnectedException {
        this.multiUserChat.leave();
    }

    public void sendMessage(String text) throws NotConnectedException {
        if(!this.isJoined()) {
            throw new IllegalStateException("You have not joined chat room!");
        } else {
            QBChatMessage msg = new QBChatMessage();
            msg.setBody(text);
            this.sendMessage(msg);
        }
    }

    public void sendMessage(QBChatMessage message) throws NotConnectedException, IllegalStateException {
        if(!this.isJoined()) {
            throw new IllegalStateException("You have not joined chat room!");
        } else {
            Message smackMessage = message.getSmackMessage();
            smackMessage.setTo(this.jid);
            smackMessage.setType(Type.groupchat);
            this.groupChatManager.sendMessage(message, this);
        }
    }

    MultiUserChat getInternalChat() {
        return this.multiUserChat;
    }

    public void sendMessageWithoutJoin(QBChatMessage message) throws NotConnectedException, IllegalStateException {
        this.groupChatManager.sendMessage(message, this);
    }

    public void sendIsTypingNotification() throws XMPPException, NotConnectedException {
        this.groupChatManager.sendChatState(this.jid, Type.groupchat, ChatState.composing);
    }

    public void sendStopTypingNotification() throws XMPPException, NotConnectedException {
        this.groupChatManager.sendChatState(this.jid, Type.groupchat, ChatState.paused);
    }

    public void addIsTypingListener(QBIsTypingListener listener) {
        if(listener != null) {
            this.isTypingListeners.add(listener);
        }
    }

    public void removeIsTypingListener(QBIsTypingListener listener) {
        this.isTypingListeners.remove(listener);
    }

    public Collection<QBIsTypingListener> getIsTypingListeners() {
        return Collections.unmodifiableCollection(this.isTypingListeners);
    }

    public void readMessage(QBChatMessage message) throws XMPPException, NotConnectedException, IllegalStateException {
        if(message.getSenderId() == null) {
            throw new IllegalStateException("SenderId is null");
        } else if(message.getId() == null) {
            throw new IllegalStateException("Id is null");
        } else {
            this.groupChatManager.sendReadStatus(message);
        }
    }

    public void deliverMessage(QBChatMessage message) throws XMPPException, NotConnectedException, IllegalStateException {
        if(message.getSenderId() == null) {
            throw new IllegalStateException("SenderId is null");
        } else if(message.getId() == null) {
            throw new IllegalStateException("Id is null");
        } else {
            this.groupChatManager.sendDeliveredStatus(message);
        }
    }

    public void addMessageListener(QBMessageListener listener) {
        if(listener != null) {
            this.messageListeners.add(listener);
        }
    }

    public void removeMessageListener(QBMessageListener listener) {
        this.messageListeners.remove(listener);
    }

    public Collection<QBMessageListener> getMessageListeners() {
        return Collections.unmodifiableCollection(this.messageListeners);
    }

    public Collection<QBMessageSentListener> getMessageSentListeners() {
        return Collections.unmodifiableCollection(this.messageSentListeners);
    }

    public void addMessageSentListener(QBMessageSentListener listener) {
        if(listener != null) {
            this.messageSentListeners.add(listener);
        }
    }

    public void removeMessageSentListener(QBMessageSentListener listener) {
        this.messageSentListeners.remove(listener);
    }

    public void addParticipantListener(QBParticipantListener participantListener) {
        if(participantListener != null) {
            this.participantListeners.add(participantListener);
        }

    }

    public void removeParticipantListener(QBParticipantListener participantListener) {
        this.participantListeners.remove(participantListener);
    }

    public Collection<QBParticipantListener> getParticipantListeners() {
        return Collections.unmodifiableCollection(this.participantListeners);
    }

    public boolean isJoined() {
        return this.multiUserChat.isJoined();
    }

    public String getJid() {
        return this.jid;
    }

    public void processIncomingMessage(Message message) {
        ChatStateExtension chatStateExtension = (ChatStateExtension)message.getExtension("http://jabber.org/protocol/chatstates");
        Integer messageListener;
        if(chatStateExtension == null) {
            QBChatMarkersExtension chatMarkersExtension1 = (QBChatMarkersExtension)message.getExtension("urn:xmpp:chat-markers:0");
            if(chatMarkersExtension1 != null) {
                ChatMarker i$1 = chatMarkersExtension1.getMarker();
                if(i$1 == ChatMarker.markable) {
                    messageListener = Integer.valueOf(JIDHelper.INSTANCE.parseUserId(message.getTo()));
                    Integer userFrom1 = JIDHelper.INSTANCE.parseRoomOccupant(message.getFrom());
                    Integer currentUserID1 = QBChatService.getInstance().getUser().getId();
                    if(messageListener.equals(currentUserID1) && !userFrom1.equals(currentUserID1)) {
                        try {
                            this.groupChatManager.sendDeliveredStatus(message);
                        } catch (NotConnectedException var9) {
                            var9.printStackTrace();
                        }
                    }
                }
            }

            Iterator i$2 = this.messageListeners.iterator();

            while(i$2.hasNext()) {
                QBMessageListener messageListener1 = (QBMessageListener)i$2.next();
                if(message.getType() == Type.error) {
                    messageListener1.processError(this, new QBChatException(message.getError()), new QBChatMessage(message));
                } else {
                    messageListener1.processMessage(this, new QBChatMessage(message));
                }
            }

        } else {
            String chatMarkersExtension = chatStateExtension.getElementName();
            ChatState i$ = ChatState.valueOf(chatMarkersExtension);
            messageListener = JIDHelper.INSTANCE.parseRoomOccupant(message.getFrom());
            Iterator userFrom;
            QBIsTypingListener currentUserID;
            if(i$ == ChatState.paused) {
                userFrom = this.isTypingListeners.iterator();

                while(userFrom.hasNext()) {
                    currentUserID = (QBIsTypingListener)userFrom.next();
                    currentUserID.processUserStopTyping(this, messageListener);
                }
            } else if(i$ == ChatState.composing) {
                userFrom = this.isTypingListeners.iterator();

                while(userFrom.hasNext()) {
                    currentUserID = (QBIsTypingListener)userFrom.next();
                    currentUserID.processUserIsTyping(this, messageListener);
                }
            }

        }
    }

    void deliverRoomCreationMessage(Message message) {
        String controlBody = message.getBody();
        if(controlBody.equals("Welcome! You created new Multi User Chat Room. Room is locked now. Configure it please!")) {
            ;
        }

        if(controlBody.equals("Room is now unlocked")) {
            ;
        }

    }

    static boolean isRoomCreationMessage(Message msg) {
        String controlBody = msg.getBody();
        return controlBody == null?false:"Welcome! You created new Multi User Chat Room. Room is locked now. Configure it please!".equals(controlBody) || "Room is now unlocked".equals(controlBody);
    }

    public Collection<Integer> getOnlineUsers() throws XMPPException {
        ArrayList usersIds = new ArrayList();
        List occupantJids = this.multiUserChat.getOccupants();
        Iterator i$ = occupantJids.iterator();

        while(i$.hasNext()) {
            String occupantJid = (String)i$.next();
            Presence presence = this.multiUserChat.getOccupantPresence(occupantJid);
            if(presence.isAvailable()) {
                usersIds.add(JIDHelper.INSTANCE.parseRoomOccupant(occupantJid));
            }
        }

        return usersIds;
    }

    public void sendPresence(Map<String, String> extendedParameters) throws XMPPException, NotConnectedException, IllegalStateException {
        if(!this.isJoined()) {
            throw new IllegalStateException("You have not joined chat room!");
        } else {
            Presence presence = new Presence(org.jivesoftware.smack.packet.Presence.Type.available);
            int userId = QBChatService.getInstance().getUser().getId().intValue();
            String roomName = this.multiUserChat.getRoom();
            presence.setTo(JIDHelper.INSTANCE.getRoomJid(roomName) + "/" + userId);
            DefaultExtensionElement presenceExtension = new DefaultExtensionElement("x", "http://chat.quickblox.com/presence_extension");
            Iterator i$ = extendedParameters.keySet().iterator();

            while(i$.hasNext()) {
                String key = (String)i$.next();
                presenceExtension.setValue(key, (String)extendedParameters.get(key));
            }

            presence.addExtension(presenceExtension);
            this.groupChatManager.connection().sendStanza(presence);
        }
    }

    private class JoinRoomTask extends ThreadTask {
        private final QBEntityCallback callback;
        private final DiscussionHistory discussionHistory;

        public JoinRoomTask(DiscussionHistory discussionHistory, QBEntityCallback callback) {
            super((Bundle)null, QBGroupChat.this.executor);
            this.discussionHistory = discussionHistory;
            this.callback = callback;
        }

        public void performInAsync() {
            try {
                QBGroupChat.this.join(this.discussionHistory);
                CommonUtils.notifyEntityCallbackOnSuccess((Object)null, Bundle.EMPTY, this.callback);
            } catch (SmackException | XMPPException var3) {
                String error = var3.getMessage() != null?var3.getMessage():"Error occurred while joining to room";
                CommonUtils.notifyEntityCallbackOnError(new QBResponseException(error), this.callback);
            }

        }
    }

    private class ParticipantListener implements PresenceListener {
        private ParticipantListener() {
        }

        public void processPresence(Presence presence) {
            Iterator i$ = QBGroupChat.this.participantListeners.iterator();

            while(i$.hasNext()) {
                QBParticipantListener presenceListener = (QBParticipantListener)i$.next();
                presenceListener.processPresence(QBGroupChat.this, new QBPresence(presence));
            }

        }
    }
}
