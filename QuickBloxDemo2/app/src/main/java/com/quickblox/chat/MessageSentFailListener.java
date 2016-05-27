package com.quickblox.chat;

import org.jivesoftware.smack.packet.Message;

interface MessageSentFailListener {

   void processMessageSentFailed(Message var1);
}
