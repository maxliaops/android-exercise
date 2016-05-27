package com.quickblox.chat;

import com.quickblox.chat.QBChat;
import com.quickblox.chat.listeners.QBMessageSentListener;
import java.util.Collection;

public abstract class QBAbstractChat implements QBChat {

   public abstract void addMessageSentListener(QBMessageSentListener var1);

   public abstract void removeMessageSentListener(QBMessageSentListener var1);

   public abstract Collection getMessageSentListeners();
}
