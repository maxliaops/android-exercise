//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.chat;

import com.quickblox.chat.Manager;
import com.quickblox.chat.MessageAcknowledgeFilter;
import com.quickblox.chat.MessageSentFailListener;
import com.quickblox.chat.QBAbstractChat;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.listeners.QBMessageSentListener;
import com.quickblox.chat.model.QBChatMessage;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.FlexibleStanzaTypeFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

public abstract class QBChatManager<CH extends QBAbstractChat> extends Manager implements StanzaListener, MessageSentFailListener {
    private final Map<String, Message> unacknowledgedSmackMessages = new ConcurrentHashMap();
    private final Map<String, QBChatMessage> unacknowledgedQBMessages = new ConcurrentHashMap();
    private final Map<String, Future> messagedAcknowledgedTimer = new ConcurrentHashMap();
    private final ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    private FlexibleStanzaTypeFilter<Message> messageAcknowledgeFilter = new MessageAcknowledgeFilter();

    public QBChatManager(XMPPConnection connection) {
        super(connection);
    }

    protected abstract void sendMessageInternal(QBChatMessage var1, CH var2) throws NotConnectedException;

    protected abstract CH getChatByOutcomingMessage(Message var1);

    protected StanzaFilter defineAcknowledgedStanzaFilter() {
        return MessageTypeFilter.CHAT;
    }

    public void processPacket(Stanza packet) throws NotConnectedException {
        if(packet instanceof Message && this.defineAcknowledgedStanzaFilter().accept(packet)) {
            this.notifyChatMessageSent((Message)packet);
        }

    }

    public synchronized void processMessageSentFailed(Message message) {
        this.cancelTimer(message.getStanzaId(), false);
        Message messageFailed = (Message)this.unacknowledgedSmackMessages.remove(message.getStanzaId());
        if(messageFailed != null) {
            QBChatMessage qbChatMessage = (QBChatMessage)this.unacknowledgedQBMessages.remove(message.getStanzaId());
            if(qbChatMessage != null) {
                QBAbstractChat chat = this.getChatByOutcomingMessage(messageFailed);
                if(chat != null) {
                    this.deliverMessageSentFailed((CH) chat, qbChatMessage);
                }

            }
        }
    }

    public void clear() {
        this.messagedAcknowledgedTimer.clear();
        this.threadPoolExecutor.shutdownNow();
    }

    protected void notifyChatMessageSent(Message message) {
        String messageId = message.getStanzaId();
        this.cancelTimer(messageId, true);
        Message unAckMessage = (Message)this.unacknowledgedSmackMessages.remove(messageId);
        if(unAckMessage != null) {
            QBChatMessage qbChatMessage = (QBChatMessage)this.unacknowledgedQBMessages.remove(messageId);
            if(qbChatMessage != null) {
                QBAbstractChat chat = this.getChatByOutcomingMessage(unAckMessage);
                if(chat != null) {
                    this.deliverMessageSent((CH) chat, qbChatMessage);
                }

            }
        }
    }

    protected void sendMessage(QBChatMessage chatMessage, CH chat) throws NotConnectedException {
        this.sendMessageInternal(chatMessage, chat);
    }

    protected void deliverMessageSent(CH chat, QBChatMessage chatMessage) {
        Iterator i$ = chat.getMessageSentListeners().iterator();

        while(i$.hasNext()) {
            QBMessageSentListener listener = (QBMessageSentListener)i$.next();
            listener.processMessageSent(chat, chatMessage);
        }

    }

    protected void deliverMessageSentFailed(CH chat, QBChatMessage chatMessage) {
        Iterator i$ = chat.getMessageSentListeners().iterator();

        while(i$.hasNext()) {
            QBMessageSentListener listener = (QBMessageSentListener)i$.next();
            listener.processMessageFailed(chat, chatMessage);
        }

    }

    protected void putMessageToUnacknowledged(Message smackMessage, QBChatMessage chatMessage) {
        if(QBChatService.getInstance().isStreamManagementEnabled()) {
            if(this.messageAcknowledgeFilter.accept(smackMessage)) {
                this.unacknowledgedSmackMessages.put(smackMessage.getStanzaId(), smackMessage);
                this.unacknowledgedQBMessages.put(smackMessage.getStanzaId(), chatMessage);
                this.scheduleTimerForMessage(smackMessage);
            }

        }
    }

    private void scheduleTimerForMessage(Message message) {
        ScheduledFuture scheduleMessageTimer = this.threadPoolExecutor.schedule(new QBChatManager.SendStatusTimer(message, this), QBChatService.getDefaultConnectionTimeout(), TimeUnit.MILLISECONDS);
        this.messagedAcknowledgedTimer.put(message.getStanzaId(), scheduleMessageTimer);
    }

    protected void cancelTimer(String messageId, boolean shouldCancel) {
        Future messageTimer = (Future)this.messagedAcknowledgedTimer.remove(messageId);
        if(shouldCancel && messageTimer != null && !messageTimer.isCancelled()) {
            messageTimer.cancel(true);
        }

    }

    private static class SendStatusTimer implements Runnable {
        WeakReference<MessageSentFailListener> weakReference;
        private Message message;

        public SendStatusTimer(Message message, MessageSentFailListener messageSendFailed) {
            this.message = message;
            this.weakReference = new WeakReference(messageSendFailed);
        }

        public void run() {
            MessageSentFailListener messageSendFailedListener = (MessageSentFailListener)this.weakReference.get();
            if(messageSendFailedListener != null) {
                messageSendFailedListener.processMessageSentFailed(this.message);
            }

        }
    }
}
