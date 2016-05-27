package com.quickblox.chat;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.QBVideoChatWebRTCSignalingManager;
import com.quickblox.chat.listeners.QBVideoChatSignalingListener;
import com.quickblox.chat.model.QBChatMessage;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

public class QBWebRTCSignaling extends QBSignaling {

   private final Set listeners = new CopyOnWriteArraySet();
   private QBVideoChatWebRTCSignalingManager signalingManager;
   private int participant;


   QBWebRTCSignaling(QBVideoChatWebRTCSignalingManager signalingManager, int userId) {
      this.signalingManager = signalingManager;
      this.participant = userId;
   }

   public int getParticipant() {
      return this.participant;
   }

   public Collection getListeners() {
      return Collections.unmodifiableCollection(this.listeners);
   }

   public void close() {
      this.signalingManager.closeSignaling(this);
      this.listeners.clear();
   }

   public void addMessageListener(QBVideoChatSignalingListener listener) {
      if(listener != null) {
         this.listeners.add(listener);
      }
   }

   public void removeMessageListener(QBVideoChatSignalingListener listener) {
      this.listeners.remove(listener);
   }

   void deliver(Message message) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         QBVideoChatSignalingListener listener = (QBVideoChatSignalingListener)i$.next();
         listener.processSignalMessage(this, message);
      }

   }

   public boolean equals(Object obj) {
      return obj instanceof QBWebRTCSignaling && this.participant == ((QBWebRTCSignaling)obj).getParticipant();
   }

   public void sendMessage(QBChatMessage message) throws SmackException.NotConnectedException {
      message.setDialogId((String)null);
      Message smackMessage = message.getSmackMessage();
      String toJid = JIDHelper.INSTANCE.getJid(this.participant);
      smackMessage.setTo(toJid);
      smackMessage.setType(Message.Type.headline);
      smackMessage.setStanzaId(String.valueOf(System.currentTimeMillis()));
      this.signalingManager.sendMessage(this, smackMessage);
   }

   void sendMessage(Message message) throws SmackException.NotConnectedException {
      String toJid = JIDHelper.INSTANCE.getJid(this.participant);
      message.setTo(toJid);
      message.setType(Message.Type.headline);
      this.signalingManager.sendMessage(this, message);
   }
}
