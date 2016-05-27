package com.quickblox.chat;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.Manager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBChatMessageExtension;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

public class QBSystemMessagesManager extends Manager {

   private static final Map INSTANCES = new WeakHashMap();
   private Set systemMessagesListeners = new CopyOnWriteArraySet();


   private QBSystemMessagesManager(XMPPConnection connection) {
      super(connection);
      StanzaFilter systemMessagesPacketFilter = new StanzaFilter() {
         public boolean accept(Stanza packet) {
            if(!(packet instanceof Message)) {
               return false;
            } else {
               Message packetMessage = (Message)packet;
               if(!packetMessage.getType().equals(Message.Type.headline)) {
                  return false;
               } else {
                  String elementName = "extraParams";
                  String namespace = "jabber:client";
                  QBChatMessageExtension extension = (QBChatMessageExtension)packetMessage.getExtension(elementName, namespace);
                  if(extension != null) {
                     Map properties = extension.getProperties();
                     String moduleIdentifier = (String)properties.get("moduleIdentifier");
                     if(moduleIdentifier.equals("SystemNotifications")) {
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
            QBSystemMessagesManager.this.processSystemMessages((Message)packet);
         }
      }, systemMessagesPacketFilter);
      INSTANCES.put(connection, this);
   }

   static synchronized QBSystemMessagesManager getInstanceFor(XMPPConnection connection) {
      QBSystemMessagesManager manager = (QBSystemMessagesManager)INSTANCES.get(connection);
      if(manager == null) {
         manager = new QBSystemMessagesManager(connection);
      }

      return manager;
   }

   public void sendSystemMessage(QBChatMessage message) throws SmackException.NotConnectedException, IllegalStateException {
      if(message.getRecipientId() == null) {
         throw new IllegalStateException("Recipient ID is null");
      } else {
         message.setProperty("moduleIdentifier", "SystemNotifications");
         Message smackMessage = message.getSmackMessage();
         String toJid = JIDHelper.INSTANCE.getJid(message.getRecipientId().intValue());
         smackMessage.setTo(toJid);
         smackMessage.setType(Message.Type.headline);
         this.connection().sendStanza(smackMessage);
      }
   }

   private void processSystemMessages(Message systemMessage) {
      QBChatMessage qbSystemMessage = new QBChatMessage(systemMessage);
      qbSystemMessage.removeProperty("moduleIdentifier");
      Iterator i$;
      QBSystemMessageListener listener;
      if(systemMessage.getType() == Message.Type.error) {
         i$ = this.systemMessagesListeners.iterator();

         while(i$.hasNext()) {
            listener = (QBSystemMessageListener)i$.next();
            listener.processError(new QBChatException(systemMessage.getError()), qbSystemMessage);
         }
      } else {
         i$ = this.systemMessagesListeners.iterator();

         while(i$.hasNext()) {
            listener = (QBSystemMessageListener)i$.next();
            listener.processMessage(qbSystemMessage);
         }
      }

   }

   public void addSystemMessageListener(QBSystemMessageListener listener) {
      if(listener != null) {
         this.systemMessagesListeners.add(listener);
      }
   }

   public void removeSystemMessageListener(QBSystemMessageListener listener) {
      this.systemMessagesListeners.remove(listener);
   }

   public Collection getSystemMessageListeners() {
      return Collections.unmodifiableCollection(this.systemMessagesListeners);
   }

}
