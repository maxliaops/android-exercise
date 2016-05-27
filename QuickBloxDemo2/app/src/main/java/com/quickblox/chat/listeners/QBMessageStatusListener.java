package com.quickblox.chat.listeners;


public interface QBMessageStatusListener {

   void processMessageDelivered(String var1, String var2, Integer var3);

   void processMessageRead(String var1, String var2, Integer var3);
}
