package com.quickblox.chat.model;

import org.jivesoftware.smack.packet.IQ;

public class MobileV3IQ extends IQ {

   boolean enable;


   public MobileV3IQ(boolean enable) {
      super("mobile", "http://tigase.org/protocol/mobile#v3");
      this.enable = enable;
   }

   protected IQ.IQChildElementXmlStringBuilder getIQChildElementBuilder(IQ.IQChildElementXmlStringBuilder xml) {
      xml.attribute("enable", String.valueOf(this.enable));
      xml.rightAngleBracket();
      return xml;
   }
}
