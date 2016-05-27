package com.quickblox.chat;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.QBAbstractChat;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBIsTypingListener;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.listeners.QBMessageSentListener;
import com.quickblox.chat.model.QBChatMarkersExtension;
import com.quickblox.chat.model.QBChatMessage;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.carbons.packet.CarbonExtension;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.forward.packet.Forwarded;

public class QBPrivateChat extends QBAbstractChat {

   private final Set messageListeners = new CopyOnWriteArraySet();
   private final Set isTypingListeners = new CopyOnWriteArraySet();
   private Set messageSentListeners = new CopyOnWriteArraySet();
   private QBPrivateChatManager privateChatManager;
   private int participant;
   private String dialogId;


   QBPrivateChat(QBPrivateChatManager privateChatManager, int userId) {
      this.privateChatManager = privateChatManager;
      this.participant = userId;
   }

   public String getDialogId() {
      return this.dialogId;
   }

   public int getParticipant() {
      return this.participant;
   }

   public void sendMessage(String text) throws SmackException.NotConnectedException {
      QBChatMessage message = new QBChatMessage();
      message.setBody(text);
      this.sendMessage(message);
   }

   public void sendMessage(QBChatMessage message) throws SmackException.NotConnectedException {
      Object dialogIdValue = message.getProperty("dialog_id");
      if(dialogIdValue != null) {
         this.dialogId = String.valueOf(dialogIdValue);
      }

      this.privateChatManager.sendMessage(message, this);
   }

   public void sendIsTypingNotification() throws XMPPException, SmackException.NotConnectedException {
      this.privateChatManager.sendChatState(JIDHelper.INSTANCE.getJid(this.participant), Message.Type.chat, ChatState.composing);
   }

   public void sendStopTypingNotification() throws XMPPException, SmackException.NotConnectedException {
      this.privateChatManager.sendChatState(JIDHelper.INSTANCE.getJid(this.participant), Message.Type.chat, ChatState.paused);
   }

   @Deprecated
   public void readMessage(String messageID) throws XMPPException, SmackException.NotConnectedException {
      this.privateChatManager.sendReadStatus(Integer.valueOf(this.getParticipant()), messageID, (String)null);
   }

   public void readMessage(QBChatMessage message) throws XMPPException, SmackException.NotConnectedException, IllegalStateException {
      if(message.getSenderId() == null) {
         message.setSenderId(Integer.valueOf(this.getParticipant()));
      }

      if(message.getId() == null) {
         throw new IllegalStateException("Id is null");
      } else {
         this.privateChatManager.sendReadStatus(message);
      }
   }

   public void deliverMessage(QBChatMessage message) throws XMPPException, SmackException.NotConnectedException, IllegalStateException {
      if(message.getSenderId() == null) {
         message.setSenderId(Integer.valueOf(this.getParticipant()));
      }

      if(message.getId() == null) {
         throw new IllegalStateException("Id is null");
      } else {
         this.privateChatManager.sendDeliveredStatus(message);
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

   public Collection getMessageListeners() {
      return Collections.unmodifiableCollection(this.messageListeners);
   }

   public void addMessageSentListener(QBMessageSentListener listener) {
      if(listener != null) {
         this.messageSentListeners.add(listener);
      }
   }

   public void removeMessageSentListener(QBMessageSentListener listener) {
      this.messageSentListeners.remove(listener);
   }

   public Collection getMessageSentListeners() {
      return Collections.unmodifiableCollection(this.messageSentListeners);
   }

   public void addIsTypingListener(QBIsTypingListener listener) {
      if(listener != null) {
         this.isTypingListeners.add(listener);
      }
   }

   public void removeIsTypingListener(QBIsTypingListener listener) {
      this.isTypingListeners.remove(listener);
   }

   public Collection getIsTypingListeners() {
      return Collections.unmodifiableCollection(this.isTypingListeners);
   }

   public void close() {
      this.privateChatManager.closeChat(this);
      this.messageListeners.clear();
   }

   public void processIncomingMessage(Message message) {
      ChatStateExtension chatStateExtension = (ChatStateExtension)message.getExtension("http://jabber.org/protocol/chatstates");
      Integer i$;
      if(chatStateExtension != null) {
         String chatMarkersExtension1 = chatStateExtension.getElementName();
         ChatState carbonExtension2 = ChatState.valueOf(chatMarkersExtension1);
         i$ = Integer.valueOf(JIDHelper.INSTANCE.parseUserId(message.getFrom()));
         Iterator listener3;
         QBIsTypingListener qbChatMessage3;
         if(carbonExtension2 == ChatState.paused) {
            listener3 = this.isTypingListeners.iterator();

            while(listener3.hasNext()) {
               qbChatMessage3 = (QBIsTypingListener)listener3.next();
               qbChatMessage3.processUserStopTyping(this, i$);
            }
         } else if(carbonExtension2 == ChatState.composing) {
            listener3 = this.isTypingListeners.iterator();

            while(listener3.hasNext()) {
               qbChatMessage3 = (QBIsTypingListener)listener3.next();
               qbChatMessage3.processUserIsTyping(this, i$);
            }
         }

      } else {
         QBChatMarkersExtension chatMarkersExtension = (QBChatMarkersExtension)message.getExtension("urn:xmpp:chat-markers:0");
         if(chatMarkersExtension != null) {
            QBChatMarkersExtension.ChatMarker carbonExtension = chatMarkersExtension.getMarker();
            if(carbonExtension == QBChatMarkersExtension.ChatMarker.markable) {
               i$ = Integer.valueOf(JIDHelper.INSTANCE.parseUserId(message.getTo()));
               Integer listener = Integer.valueOf(JIDHelper.INSTANCE.parseUserId(message.getFrom()));
               Integer qbChatMessage = QBChatService.getInstance().getUser().getId();
               if(i$.equals(qbChatMessage) && !listener.equals(qbChatMessage)) {
                  try {
                     this.privateChatManager.sendDeliveredStatus(message);
                  } catch (SmackException.NotConnectedException var9) {
                     var9.printStackTrace();
                  }
               }
            } else if(carbonExtension == QBChatMarkersExtension.ChatMarker.received || carbonExtension == QBChatMarkersExtension.ChatMarker.displayed) {
               return;
            }
         }

         CarbonExtension carbonExtension1 = (CarbonExtension)message.getExtension("urn:xmpp:carbons:2");
         if(carbonExtension1 != null) {
            CarbonExtension.Direction i$1 = carbonExtension1.getDirection();
            if(i$1 == CarbonExtension.Direction.sent) {
               Forwarded listener2 = carbonExtension1.getForwarded();
               Stanza qbChatMessage1 = listener2.getForwardedPacket();
               message = (Message)qbChatMessage1;
            }
         }

         Iterator i$2 = this.getMessageListeners().iterator();

         while(i$2.hasNext()) {
            QBMessageListener listener1 = (QBMessageListener)i$2.next();
            QBChatMessage qbChatMessage2;
            if(message.getType() == Message.Type.error) {
               qbChatMessage2 = new QBChatMessage(message);
               if(qbChatMessage2.getDialogId() != null) {
                  this.dialogId = qbChatMessage2.getDialogId();
               }

               listener1.processError(this, new QBChatException(message.getError()), qbChatMessage2);
            } else {
               qbChatMessage2 = new QBChatMessage(message);
               if(qbChatMessage2.getDialogId() != null) {
                  this.dialogId = qbChatMessage2.getDialogId();
               }

               listener1.processMessage(this, qbChatMessage2);
            }
         }

      }
   }

   public boolean equals(Object obj) {
      return obj instanceof QBPrivateChat && this.participant == ((QBPrivateChat)obj).getParticipant();
   }
}
