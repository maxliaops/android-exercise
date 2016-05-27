package com.quickblox.chat;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBChatMessageExtension;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.chatstates.ChatState;

public class QBMessageStatusesSender {

   void sendReadStatus(XMPPConnection connection, QBChatMessage originMessage) throws SmackException.NotConnectedException {
      this.sendReadStatus(connection, originMessage.getSenderId(), originMessage.getId(), originMessage.getDialogId());
   }

   void sendReadStatus(XMPPConnection connection, Integer senderId, String originMessageID, String dialogId) throws SmackException.NotConnectedException {
      Message asmackMessage = QBChatMessage.buildDeliveredOrReadStatusMessage(false, senderId, originMessageID, dialogId);
      connection.sendStanza(asmackMessage);
   }

   void sendDeliveredStatus(XMPPConnection connection, Message originMessage) throws SmackException.NotConnectedException {
      String senderJid = originMessage.getFrom();
      Integer senderId = null;
      if(senderJid != null) {
         if(JIDHelper.INSTANCE.isChatJid(senderJid)) {
            senderId = Integer.valueOf(JIDHelper.INSTANCE.parseUserId(senderJid));
         } else {
            senderId = JIDHelper.INSTANCE.parseRoomOccupant(senderJid);
         }
      }

      String elementName = "extraParams";
      String namespace = "jabber:client";
      QBChatMessageExtension extension = (QBChatMessageExtension)originMessage.getExtension(elementName, namespace);
      String dialogId = null;
      if(extension != null && extension.getProperties() != null) {
         dialogId = (String)extension.getProperties().get("dialog_id");
      }

      Message asmackMessage = QBChatMessage.buildDeliveredOrReadStatusMessage(true, senderId, originMessage.getStanzaId(), dialogId);

      try {
         connection.sendStanza(asmackMessage);
      } catch (SmackException.NotConnectedException var11) {
         var11.printStackTrace();
      }

   }

   void sendDeliveredStatus(XMPPConnection connection, QBChatMessage originMessage) throws SmackException.NotConnectedException {
      Message asmackMessage = QBChatMessage.buildDeliveredOrReadStatusMessage(true, originMessage.getSenderId(), originMessage.getId(), originMessage.getDialogId());

      try {
         connection.sendStanza(asmackMessage);
      } catch (SmackException.NotConnectedException var5) {
         var5.printStackTrace();
      }

   }

   void sendChatState(XMPPConnection connection, String to, Message.Type type, ChatState chatState) throws SmackException.NotConnectedException {
      Message message = QBChatMessage.buildTypingStatusMessage(to, type, chatState);
      connection.sendStanza(message);
   }
}
