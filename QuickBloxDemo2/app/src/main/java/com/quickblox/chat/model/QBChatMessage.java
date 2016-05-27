package com.quickblox.chat.model;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatMarkersExtension;
import com.quickblox.chat.model.QBChatMessageExtension;
import com.quickblox.chat.utils.MongoDBObjectId;
import com.quickblox.core.helper.Lo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.delay.packet.DelayInformation;

public class QBChatMessage implements Serializable {

   private String _id;
   private String dialogId;
   private long dateSent = 0L;
   private String body;
   @Deprecated
   private Integer read;
   private Collection readIds;
   private Collection deliveredIds;
   private Integer recipientId;
   private Integer senderId;
   private boolean markable = false;
   private boolean delayed = false;
   private Map properties;
   private Collection attachments;
   private boolean saveToHistory = false;
   private QBChatMessageExtension packetExtension;
   private HashMap complexProperties;


   public QBChatMessage() {
      this._id = MongoDBObjectId.get().toString();
   }

   public String getId() {
      return this._id;
   }

   public void setId(String id) {
      this._id = id;
   }

   public String getDialogId() {
      return this.dialogId;
   }

   public void setDialogId(String dialogId) {
      this.dialogId = dialogId;
   }

   public Integer getSenderId() {
      return this.senderId;
   }

   public void setSenderId(Integer senderId) {
      this.senderId = senderId;
   }

   public boolean isDelayed() {
      return this.delayed;
   }

   public Integer getRecipientId() {
      return this.recipientId;
   }

   public void setRecipientId(Integer recipientId) {
      this.recipientId = recipientId;
   }

   public String getBody() {
      return this.body;
   }

   public void setBody(String body) {
      this.body = body;
   }

   public boolean isMarkable() {
      return this.markable;
   }

   public void setMarkable(boolean markable) {
      this.markable = markable;
   }

   public Collection getAttachments() {
      return this.attachments;
   }

   public void setAttachments(Collection attachments) {
      this.attachments = attachments;
   }

   public boolean addAttachment(QBAttachment attachment) {
      if(this.attachments == null) {
         this.attachments = new ArrayList();
      }

      return this.attachments.add(attachment);
   }

   public boolean removeAttachment(QBAttachment attachment) {
      return this.attachments != null?this.attachments.remove(attachment):false;
   }

   public Collection getReadIds() {
      return this.readIds;
   }

   public void setReadIds(Collection readIds) {
      this.readIds = readIds;
   }

   public Collection getDeliveredIds() {
      return this.deliveredIds;
   }

   public void setDeliveredIds(Collection deliveredIds) {
      this.deliveredIds = deliveredIds;
   }

   public Object getProperty(String name) {
      return this.properties == null?null:this.properties.get(name);
   }

   public Collection getPropertyNames() {
      return this.properties == null?null:this.properties.keySet();
   }

   public Map getProperties() {
      return this.properties;
   }

   public String setProperty(String name, String value) {
      if(this.properties == null) {
         this.properties = new HashMap();
      }

      return (String)this.properties.put(name, value);
   }

   public String removeProperty(String name) {
      return this.properties == null?null:(String)this.properties.remove(name);
   }

   public Object setComplexProperty(String name, Object value) {
      if(this.complexProperties == null) {
         this.complexProperties = new HashMap();
      }

      Object prev = this.complexProperties.put(name, value);
      return prev;
   }

   public Object removeComplexProperty(String name) {
      return this.complexProperties != null?this.complexProperties.remove(name):null;
   }

   public long getDateSent() {
      return this.dateSent;
   }

   public void setDateSent(long dateSent) {
      this.dateSent = dateSent;
   }

   public void setSaveToHistory(boolean saveToHistory) {
      this.saveToHistory = saveToHistory;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder(this.getClass().getSimpleName());
      stringBuilder.append("{").append("id").append("=").append(this.getId()).append(", sender_id").append("=").append(this.getSenderId()).append(", recipient_id").append("=").append(this.getRecipientId()).append(", body").append("=").append(this.getBody()).append(", properties").append("=").append(this.getProperties()).append(", attachments").append("=").append(this.getAttachments()).append(", dialogId").append("=").append(this.getDialogId()).append(", dateSent").append("=").append(this.getDateSent()).append(", markable").append("=").append(this.isMarkable()).append(", readIds").append("=").append(this.getReadIds()).append(", deliveredIds").append("=").append(this.getDeliveredIds()).append("}");
      return stringBuilder.toString();
   }

   public int hashCode() {
      return this.getId().hashCode();
   }

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(o != null && this.getClass() == o.getClass()) {
         QBChatMessage qbChatMessage = (QBChatMessage)o;
         return this.getId().equals(qbChatMessage.getId());
      } else {
         return false;
      }
   }

   public QBChatMessage(Message smackMessage) {
      String elementName = "extraParams";
      String namespace = "jabber:client";
      QBChatMessageExtension extension = (QBChatMessageExtension)smackMessage.getExtension(elementName, namespace);
      if(extension != null) {
         this.attachments = extension.getAttachments();
         this.properties = extension.getProperties();
      }

      QBChatMarkersExtension chatMarkersExtension = (QBChatMarkersExtension)smackMessage.getExtension("urn:xmpp:chat-markers:0");
      this.markable = chatMarkersExtension != null && QBChatMarkersExtension.ChatMarker.markable.equals(chatMarkersExtension.getMarker());
      String _packetId = smackMessage.getStanzaId();
      this._id = MongoDBObjectId.isValid(_packetId)?_packetId:String.valueOf(this.getProperty("message_id"));
      this.dialogId = String.valueOf(this.getProperty("dialog_id"));
      String dateSentValue = String.valueOf(this.getProperty("date_sent"));
      if(dateSentValue != null) {
         try {
            this.dateSent = Long.valueOf(dateSentValue).longValue();
         } catch (NumberFormatException var12) {
            try {
               Double senderJid = Double.valueOf(Double.parseDouble(dateSentValue));
               this.dateSent = senderJid.longValue();
            } catch (NumberFormatException var11) {
               Lo.g("Can\'t parse the \'date_sent\' value " + dateSentValue + ": " + var11.getLocalizedMessage());
            }
         }
      }

      DelayInformation delayInformation = (DelayInformation)smackMessage.getExtension("delay", "urn:xmpp:delay");
      if(delayInformation != null) {
         this.delayed = true;
      }

      String senderJid1 = smackMessage.getFrom();
      if(senderJid1 != null) {
         if(JIDHelper.INSTANCE.isChatJid(senderJid1)) {
            this.senderId = Integer.valueOf(JIDHelper.INSTANCE.parseUserId(senderJid1));
         } else {
            this.senderId = JIDHelper.INSTANCE.parseRoomOccupant(senderJid1);
         }
      }

      if(this.senderId != null) {
         ArrayList recipientJid = new ArrayList(1);
         recipientJid.add(this.senderId);
         this.readIds = recipientJid;
         this.deliveredIds = recipientJid;
      }

      String recipientJid1 = smackMessage.getTo();
      if(recipientJid1 != null) {
         if(JIDHelper.INSTANCE.isChatJid(recipientJid1)) {
            this.recipientId = Integer.valueOf(JIDHelper.INSTANCE.parseUserId(recipientJid1));
         } else {
            this.recipientId = JIDHelper.INSTANCE.parseRoomOccupant(recipientJid1);
         }
      }

      this.body = smackMessage.getBody();
   }

   public void setQBChatUnMarkedMessageExtension(QBChatMessageExtension packetExtension) {
      this.packetExtension = packetExtension;
   }

   public void removeQBChatUnMarkedMessageExtension() {
      this.packetExtension = null;
   }

   public Message getSmackMessage() {
      Message asmackMessage = new Message();
      asmackMessage.setStanzaId(this._id);
      asmackMessage.setBody(this.body);
      if(this.markable) {
         QBChatMarkersExtension chatMarkersExtension = new QBChatMarkersExtension(QBChatMarkersExtension.ChatMarker.markable);
         asmackMessage.addExtension(chatMarkersExtension);
      }

      if(this.attachments != null || this.properties != null || this.dateSent > 0L || this.saveToHistory || this.dialogId != null) {
         if(this.packetExtension == null) {
            this.packetExtension = new QBChatMessageExtension();
         }

         if(this.attachments != null) {
            this.packetExtension.addAttachments(this.attachments);
         }

         if(this.properties != null) {
            this.packetExtension.setProperties(this.properties);
         }

         if(this.complexProperties != null) {
            this.packetExtension.setComplexProperty(this.complexProperties);
         }

         if(this.dateSent > 0L) {
            this.packetExtension.setProperty("date_sent", String.valueOf(this.dateSent));
         }

         if(this.saveToHistory) {
            this.packetExtension.setProperty("save_to_history", "1");
         }

         if(this.dialogId != null) {
            this.packetExtension.setProperty("dialog_id", this.dialogId);
         }

         asmackMessage.addExtension(this.packetExtension);
      }

      return asmackMessage;
   }

   public static Message buildDeliveredOrReadStatusMessage(boolean delivered, Integer senderId, String originMessageID, String dialogId) {
      Message asmackMessage = new Message();
      asmackMessage.setStanzaId(MongoDBObjectId.get().toString());
      asmackMessage.setType(Message.Type.chat);
      asmackMessage.setTo(JIDHelper.INSTANCE.getJid(senderId.intValue()));
      QBChatMarkersExtension extension = new QBChatMarkersExtension(delivered?QBChatMarkersExtension.ChatMarker.received:QBChatMarkersExtension.ChatMarker.displayed, originMessageID);
      asmackMessage.addExtension(extension);
      if(dialogId != null) {
         QBChatMessageExtension packetExtension = new QBChatMessageExtension();
         packetExtension.setProperty("dialog_id", dialogId);
         asmackMessage.addExtension(packetExtension);
      }

      return asmackMessage;
   }

   public static Message buildTypingStatusMessage(String to, Message.Type type, ChatState chatState) {
      Message message = new Message();
      message.setStanzaId(MongoDBObjectId.get().toString());
      message.setType(type);
      message.setTo(to);
      ChatStateExtension extension = new ChatStateExtension(chatState);
      message.addExtension(extension);
      return message;
   }
}
