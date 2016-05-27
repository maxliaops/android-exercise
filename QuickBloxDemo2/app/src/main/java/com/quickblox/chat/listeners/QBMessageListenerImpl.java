package com.quickblox.chat.listeners;

import com.quickblox.chat.QBChat;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.model.QBChatMessage;

public class QBMessageListenerImpl implements QBMessageListener {

   public void processMessage(QBChat sender, QBChatMessage message) {}

   public void processError(QBChat sender, QBChatException exception, QBChatMessage message) {}
}
