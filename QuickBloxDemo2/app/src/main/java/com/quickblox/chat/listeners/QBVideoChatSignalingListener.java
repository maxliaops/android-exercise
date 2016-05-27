package com.quickblox.chat.listeners;

import com.quickblox.chat.QBSignaling;
import org.jivesoftware.smack.packet.Message;

public interface QBVideoChatSignalingListener {

   void processSignalMessage(QBSignaling var1, Message var2);
}
