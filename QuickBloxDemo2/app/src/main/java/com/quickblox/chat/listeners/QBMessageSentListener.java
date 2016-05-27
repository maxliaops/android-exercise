package com.quickblox.chat.listeners;

import com.quickblox.chat.QBChat;
import com.quickblox.chat.model.QBChatMessage;

public interface QBMessageSentListener {

   void processMessageSent(QBChat var1, QBChatMessage var2);

   void processMessageFailed(QBChat var1, QBChatMessage var2);
}
