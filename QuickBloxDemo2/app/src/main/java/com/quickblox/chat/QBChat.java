package com.quickblox.chat;

import com.quickblox.chat.listeners.QBIsTypingListener;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.model.QBChatMessage;
import java.util.Collection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

public interface QBChat {

   String getDialogId();

   void sendMessage(String var1) throws SmackException.NotConnectedException;

   void sendMessage(QBChatMessage var1) throws SmackException.NotConnectedException;

   void addMessageListener(QBMessageListener var1);

   void removeMessageListener(QBMessageListener var1);

   Collection getMessageListeners();

   void sendIsTypingNotification() throws XMPPException, SmackException.NotConnectedException;

   void sendStopTypingNotification() throws XMPPException, SmackException.NotConnectedException;

   void addIsTypingListener(QBIsTypingListener var1);

   void removeIsTypingListener(QBIsTypingListener var1);

   Collection getIsTypingListeners();

   void readMessage(QBChatMessage var1) throws XMPPException, SmackException.NotConnectedException;

   void deliverMessage(QBChatMessage var1) throws XMPPException, SmackException.NotConnectedException;
}
