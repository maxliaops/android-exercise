package com.quickblox.chat.listeners;

import com.quickblox.chat.QBChat;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.model.QBChatMessage;

public interface QBMessageListener {

   void processMessage(QBChat var1, QBChatMessage var2);

   void processError(QBChat var1, QBChatException var2, QBChatMessage var3);
}
