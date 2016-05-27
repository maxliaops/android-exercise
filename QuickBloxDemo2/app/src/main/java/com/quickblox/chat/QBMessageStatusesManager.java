package com.quickblox.chat;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.Manager;
import com.quickblox.chat.listeners.QBMessageStatusListener;
import com.quickblox.chat.model.QBChatMarkersExtension;
import com.quickblox.chat.model.QBChatMessageExtension;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

public class QBMessageStatusesManager extends Manager {

   private static final Map INSTANCES = new WeakHashMap();
   private Set messageStatusListeners = new CopyOnWriteArraySet();


   public QBMessageStatusesManager(XMPPConnection connection) {
      super(connection);
      StanzaFilter readOrDeliveredPacketsFilter = new StanzaFilter() {
         public boolean accept(Stanza packet) {
            if(!(packet instanceof Message)) {
               return false;
            } else {
               Message packetMessage = (Message)packet;
               if(!packetMessage.getType().equals(Message.Type.chat)) {
                  return false;
               } else {
                  QBChatMarkersExtension chatMarkersExtension = (QBChatMarkersExtension)packetMessage.getExtension("urn:xmpp:chat-markers:0");
                  if(chatMarkersExtension != null) {
                     QBChatMarkersExtension.ChatMarker marker = chatMarkersExtension.getMarker();
                     if(marker == QBChatMarkersExtension.ChatMarker.displayed || marker == QBChatMarkersExtension.ChatMarker.received) {
                        return true;
                     }
                  }

                  return false;
               }
            }
         }
      };
      connection.addAsyncStanzaListener(new StanzaListener() {
         public void processPacket(Stanza packet) {
            QBMessageStatusesManager.this.processReadOrDeliveredPacket(packet);
         }
      }, readOrDeliveredPacketsFilter);
      INSTANCES.put(connection, this);
   }

   static synchronized QBMessageStatusesManager getInstanceFor(XMPPConnection connection) {
      QBMessageStatusesManager manager = (QBMessageStatusesManager)INSTANCES.get(connection);
      if(manager == null) {
         manager = new QBMessageStatusesManager(connection);
      }

      return manager;
   }

   private void processReadOrDeliveredPacket(Stanza packet) {
      QBChatMarkersExtension chatMarkersExtension = (QBChatMarkersExtension)packet.getExtension("urn:xmpp:chat-markers:0");
      if(chatMarkersExtension != null) {
         String messageId = chatMarkersExtension.getMessageId();
         QBChatMarkersExtension.ChatMarker marker = chatMarkersExtension.getMarker();
         Integer userId = Integer.valueOf(JIDHelper.INSTANCE.parseUserId(packet.getFrom()));
         String elementName = "extraParams";
         String namespace = "jabber:client";
         QBChatMessageExtension messageExtension = (QBChatMessageExtension)packet.getExtension(elementName, namespace);
         String dialogId = null;
         if(messageExtension != null && messageExtension.getProperties() != null) {
            dialogId = (String)messageExtension.getProperties().get("dialog_id");
         }

         if(marker == QBChatMarkersExtension.ChatMarker.displayed) {
            this.processIncomingReadStatus(messageId, dialogId, userId);
         } else if(marker == QBChatMarkersExtension.ChatMarker.received) {
            this.processIncomingDeliveredStatus(messageId, dialogId, userId);
         }
      }

   }

   protected void processIncomingReadStatus(String messageId, String dialogId, Integer userId) {
      Iterator i$ = this.messageStatusListeners.iterator();

      while(i$.hasNext()) {
         QBMessageStatusListener listener = (QBMessageStatusListener)i$.next();
         listener.processMessageRead(messageId, dialogId, userId);
      }

   }

   protected void processIncomingDeliveredStatus(String messageId, String dialogId, Integer userId) {
      Iterator i$ = this.messageStatusListeners.iterator();

      while(i$.hasNext()) {
         QBMessageStatusListener listener = (QBMessageStatusListener)i$.next();
         listener.processMessageDelivered(messageId, dialogId, userId);
      }

   }

   public void addMessageStatusListener(QBMessageStatusListener listener) {
      if(listener != null) {
         this.messageStatusListeners.add(listener);
      }
   }

   public void removeMessageStatusListener(QBMessageStatusListener listener) {
      this.messageStatusListeners.remove(listener);
   }

   public Collection getMessageStatusListeners() {
      return Collections.unmodifiableCollection(this.messageStatusListeners);
   }

}
