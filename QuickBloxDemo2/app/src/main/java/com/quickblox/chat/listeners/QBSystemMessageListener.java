package com.quickblox.chat.listeners;

import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.model.QBChatMessage;

public interface QBSystemMessageListener {

   void processMessage(QBChatMessage var1);

   void processError(QBChatException var1, QBChatMessage var2);
}
