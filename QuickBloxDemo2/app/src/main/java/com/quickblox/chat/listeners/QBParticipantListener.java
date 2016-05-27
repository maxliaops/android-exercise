package com.quickblox.chat.listeners;

import com.quickblox.chat.QBGroupChat;
import com.quickblox.chat.model.QBPresence;

public interface QBParticipantListener {

   void processPresence(QBGroupChat var1, QBPresence var2);
}
