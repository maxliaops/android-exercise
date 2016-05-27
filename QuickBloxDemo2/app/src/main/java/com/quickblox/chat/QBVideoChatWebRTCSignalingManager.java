package com.quickblox.chat;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.Manager;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingListener;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.core.helper.Lo;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

public class QBVideoChatWebRTCSignalingManager extends Manager {

   private static final Map INSTANCES = new WeakHashMap();
   private static final String TAG = QBVideoChatWebRTCSignalingManager.class.getSimpleName();
   private Map signalingMap = Collections.synchronizedMap(new HashMap());
   private Set signalingManagerListeners = new CopyOnWriteArraySet();
   private Set signalingListeners = new CopyOnWriteArraySet();


   private QBVideoChatWebRTCSignalingManager(XMPPConnection connection) {
      super(connection);
      StanzaFilter filter = MessageTypeFilter.HEADLINE;
      connection.addSyncStanzaListener(new StanzaListener() {
         public void processPacket(Stanza packet) {
            Lo.g("syncStanzaListener in " + QBVideoChatWebRTCSignalingManager.TAG);
            Message message = (Message)packet;
            int userId = JIDHelper.INSTANCE.parseUserId(message.getFrom());
            QBWebRTCSignaling signaling = QBVideoChatWebRTCSignalingManager.this.getSignaling(userId);
            if(signaling == null) {
               signaling = QBVideoChatWebRTCSignalingManager.this.createSignaling(message);
            }

            if(signaling != null) {
               QBVideoChatWebRTCSignalingManager.this.deliverMessage(signaling, message);
            }
         }
      }, filter);
      INSTANCES.put(connection, this);
   }

   static synchronized QBVideoChatWebRTCSignalingManager getInstanceFor(XMPPConnection connection) {
      QBVideoChatWebRTCSignalingManager manager = (QBVideoChatWebRTCSignalingManager)INSTANCES.get(connection);
      if(manager == null) {
         manager = new QBVideoChatWebRTCSignalingManager(connection);
      }

      return manager;
   }

   public QBWebRTCSignaling createSignaling(int userId, QBVideoChatSignalingListener listener) {
      QBWebRTCSignaling signaling = this.createSignaling(userId, true);
      signaling.addMessageListener(listener);
      return signaling;
   }

   private QBWebRTCSignaling createSignaling(int userId, boolean createdLocally) {
      QBWebRTCSignaling signaling = new QBWebRTCSignaling(this, userId);
      this.signalingMap.put(Integer.valueOf(userId), signaling);
      Iterator i$ = this.signalingManagerListeners.iterator();

      while(i$.hasNext()) {
         QBVideoChatSignalingManagerListener listener = (QBVideoChatSignalingManagerListener)i$.next();
         listener.signalingCreated(signaling, createdLocally);
      }

      return signaling;
   }

   public void closeSignaling(QBWebRTCSignaling signaling) {
      int userId = signaling.getParticipant();
      this.signalingMap.remove(Integer.valueOf(userId));
   }

   private QBWebRTCSignaling createSignaling(Message message) {
      String userJID = message.getFrom();
      return userJID == null?null:this.createSignaling(JIDHelper.INSTANCE.parseUserId(userJID), false);
   }

   public QBWebRTCSignaling getSignaling(int userId) {
      return (QBWebRTCSignaling)this.signalingMap.get(Integer.valueOf(userId));
   }

   public void addSignalingManagerListener(QBVideoChatSignalingManagerListener listener) {
      if(listener != null) {
         this.signalingManagerListeners.add(listener);
      }
   }

   public void addSignalingListener(QBVideoChatSignalingListener signalingListener) {
      if(signalingListener != null) {
         this.signalingListeners.add(signalingListener);
      }
   }

   public void removeSignalingManagerListener(QBVideoChatSignalingManagerListener listener) {
      this.signalingManagerListeners.remove(listener);
   }

   public Collection getSignalingListeners() {
      return Collections.unmodifiableCollection(this.signalingManagerListeners);
   }

   private void deliverMessage(QBWebRTCSignaling signaling, Message message) {
      signaling.deliver(message);
      Iterator i$ = this.signalingListeners.iterator();

      while(i$.hasNext()) {
         QBVideoChatSignalingListener signalingListener = (QBVideoChatSignalingListener)i$.next();
         signalingListener.processSignalMessage((QBSignaling)null, message);
      }

   }

   public void sendMessage(QBWebRTCSignaling signaling, Message message) throws SmackException.NotConnectedException {
      if(message.getFrom() == null) {
         message.setFrom(this.connection().getUser());
      }

      this.connection().sendStanza(message);
   }

}
