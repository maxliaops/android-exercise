package com.quickblox.chat.model;

import java.io.IOException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class QBChatMarkersExtension implements ExtensionElement {

   public static final String ELEMENT_NAME_MARKABLE = "markable";
   public static final String ELEMENT_NAME_RECEIVED = "received";
   public static final String ELEMENT_NAME_DISPLAYED = "displayed";
   public static final String NAMESPACE = "urn:xmpp:chat-markers:0";
   private static final String ATTRIBUTE_ID = "id";
   private QBChatMarkersExtension.ChatMarker marker;
   private String messageId;


   QBChatMarkersExtension(QBChatMarkersExtension.ChatMarker marker) {
      this(marker, (String)null);
   }

   public QBChatMarkersExtension(QBChatMarkersExtension.ChatMarker marker, String id) {
      this.marker = marker;
      this.messageId = id;
   }

   public QBChatMarkersExtension.ChatMarker getMarker() {
      return this.marker;
   }

   public void setMarker(QBChatMarkersExtension.ChatMarker marker) {
      this.marker = marker;
   }

   public String getMessageId() {
      return this.messageId;
   }

   public void setMessageId(String messageId) {
      this.messageId = messageId;
   }

   public String getElementName() {
      return this.marker == QBChatMarkersExtension.ChatMarker.displayed?"displayed":(this.marker == QBChatMarkersExtension.ChatMarker.received?"received":"markable");
   }

   public String getNamespace() {
      return "urn:xmpp:chat-markers:0";
   }

   public CharSequence toXML() {
      XmlStringBuilder buf = new XmlStringBuilder();
      buf.halfOpenElement(this.getElementName()).xmlnsAttribute(this.getNamespace());
      if(this.marker != QBChatMarkersExtension.ChatMarker.markable) {
         buf.attribute("id", this.messageId);
      }

      buf.closeEmptyElement();
      return buf;
   }

   public static enum ChatMarker {

      markable("markable", 0),
      received("received", 1),
      displayed("displayed", 2);
      // $FF: synthetic field
      private static final QBChatMarkersExtension.ChatMarker[] $VALUES = new QBChatMarkersExtension.ChatMarker[]{markable, received, displayed};


      private ChatMarker(String var1, int var2) {}

   }

   public static class Provider extends ExtensionElementProvider {

      public QBChatMarkersExtension parseExtension(XmlPullParser parser) throws Exception {
         String id = null;

         QBChatMarkersExtension.ChatMarker chatMarker;
         try {
            chatMarker = QBChatMarkersExtension.ChatMarker.valueOf(parser.getName());
            id = parser.getAttributeValue((String)null, "id");
         } catch (Exception var5) {
            chatMarker = QBChatMarkersExtension.ChatMarker.markable;
         }

         return new QBChatMarkersExtension(chatMarker, id);
      }

      public QBChatMarkersExtension parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
         String id = null;

         QBChatMarkersExtension.ChatMarker chatMarker;
         try {
            chatMarker = QBChatMarkersExtension.ChatMarker.valueOf(parser.getName());
            id = parser.getAttributeValue((String)null, "id");
         } catch (Exception var6) {
            chatMarker = QBChatMarkersExtension.ChatMarker.markable;
         }

         return new QBChatMarkersExtension(chatMarker, id);
      }
   }
}
