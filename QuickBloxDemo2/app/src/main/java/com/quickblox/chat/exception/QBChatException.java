package com.quickblox.chat.exception;

import org.jivesoftware.smack.packet.XMPPError;

public class QBChatException extends Exception {

   private XMPPError error;


   public QBChatException(XMPPError error) {
      super(error.getConditionText());
      this.error = error;
   }

   public QBChatException(String errorMessage) {
      super(errorMessage);
   }

   public XMPPError.Condition getCondition() {
      return this.error.getCondition();
   }
}
