package com.quickblox.chat;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.listeners.QBRosterListener;
import com.quickblox.chat.listeners.QBSubscriptionListener;
import com.quickblox.chat.model.QBPresence;
import com.quickblox.chat.model.QBRosterEntry;
import com.quickblox.chat.model.QBRosterGroup;
import com.quickblox.core.exception.QBRuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.packet.RosterPacket;

public class QBRoster {

   private final Set subscriptionListeners;
   private final Set rosterListeners;
   private List incomingPresencesQueue;
   private XMPPConnection connection;
   private Roster roster;
   private QBRoster.SubscriptionMode subscriptionMode;


   protected QBRoster(XMPPConnection connection, QBRoster.SubscriptionMode mode, QBSubscriptionListener subscriptionListener) {
      this.subscriptionListeners = new CopyOnWriteArraySet();
      this.rosterListeners = new CopyOnWriteArraySet();
      this.connection = connection;
      if(mode == null) {
         this.subscriptionMode = QBRoster.SubscriptionMode.manual;
      } else {
         this.subscriptionMode = mode;
      }

      this.removeSubscriptionListener(subscriptionListener);
      this.addSubscriptionListener(subscriptionListener);
      QBRoster.SubscribeFilter subscribeFilter = new QBRoster.SubscribeFilter((QBRoster.NamelessClass1366439254)null);
      QBRoster.SubscribePacketListener subscribePacketListener = new QBRoster.SubscribePacketListener((QBRoster.NamelessClass1366439254)null);
      connection.addAsyncStanzaListener(subscribePacketListener, subscribeFilter);
      QBRoster.UnsubscribeFilter unsubscribeFilter = new QBRoster.UnsubscribeFilter((QBRoster.NamelessClass1366439254)null);
      QBRoster.UnsubscribePacketListener unsubscribePacketListener = new QBRoster.UnsubscribePacketListener((QBRoster.NamelessClass1366439254)null);
      connection.addAsyncStanzaListener(unsubscribePacketListener, unsubscribeFilter);
      this.roster = Roster.getInstanceFor(connection);
      if(this.incomingPresencesQueue != null) {
         List var8 = this.incomingPresencesQueue;
         synchronized(this.incomingPresencesQueue) {
            if(this.incomingPresencesQueue.size() > 0) {
               Iterator i$ = this.incomingPresencesQueue.iterator();

               while(i$.hasNext()) {
                  Stanza p = (Stanza)i$.next();

                  try {
                     subscribePacketListener.processPacket(p);
                  } catch (SmackException.NotConnectedException var13) {
                     var13.printStackTrace();
                  }
               }

               this.incomingPresencesQueue = null;
            }
         }
      }

      this.roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
      this.roster.addRosterListener(new QBRoster.RosterListenerImpl((QBRoster.NamelessClass1366439254)null));
   }

   protected QBRoster(XMPPConnection connection) {
      this(connection, (QBRoster.SubscriptionMode)null, (QBSubscriptionListener)null);
   }

   public void reload() throws SmackException.NotLoggedInException, SmackException.NotConnectedException {
      this.roster.reload();
   }

   public QBRoster.SubscriptionMode getSubscriptionMode() {
      return this.subscriptionMode;
   }

   public void setSubscriptionMode(QBRoster.SubscriptionMode subscriptionMode) {
      this.subscriptionMode = subscriptionMode;
   }

   public QBRosterGroup createGroup(String name) throws SmackException.NotLoggedInException {
      return new QBRosterGroup(this.roster.createGroup(name));
   }

   public QBRosterGroup getGroup(String name) {
      RosterGroup group = this.roster.getGroup(name);
      return group != null?new QBRosterGroup(group):null;
   }

   public int getGroupCount() {
      return this.roster.getGroupCount();
   }

   public Collection getGroups() {
      HashSet allGroups = new HashSet();
      Iterator i$ = this.roster.getGroups().iterator();

      while(i$.hasNext()) {
         RosterGroup group = (RosterGroup)i$.next();
         allGroups.add(new QBRosterGroup(group));
      }

      return Collections.unmodifiableCollection(allGroups);
   }

   public void createEntry(int userId, String[] groups) throws XMPPException, SmackException.NotLoggedInException, SmackException.NotConnectedException, SmackException.NoResponseException {
      this.roster.createEntry(JIDHelper.INSTANCE.getJid(userId), (String)null, groups);
   }

   public void removeEntry(QBRosterEntry entry) throws XMPPException, SmackException.NotLoggedInException, SmackException.NotConnectedException, SmackException.NoResponseException {
      this.roster.removeEntry(entry.getRosterEntry());
   }

   public int getEntryCount() {
      return this.roster.getEntryCount();
   }

   public Collection getEntries() {
      HashSet allEntries = new HashSet();
      Iterator i$ = this.roster.getEntries().iterator();

      while(i$.hasNext()) {
         RosterEntry entry = (RosterEntry)i$.next();
         allEntries.add(new QBRosterEntry(entry));
      }

      return Collections.unmodifiableCollection(allEntries);
   }

   public int getUnfiledEntryCount() {
      return this.roster.getUnfiledEntryCount();
   }

   public Collection getUnfiledEntries() {
      HashSet allEntries = new HashSet();
      Iterator i$ = this.roster.getUnfiledEntries().iterator();

      while(i$.hasNext()) {
         RosterEntry entry = (RosterEntry)i$.next();
         allEntries.add(new QBRosterEntry(entry));
      }

      return Collections.unmodifiableCollection(allEntries);
   }

   public QBRosterEntry getEntry(int userId) {
      RosterEntry entry = this.roster.getEntry(JIDHelper.INSTANCE.getJid(userId));
      return entry != null?new QBRosterEntry(entry):null;
   }

   public boolean contains(int userId) {
      return this.roster.contains(JIDHelper.INSTANCE.getJid(userId));
   }

   public QBPresence getPresence(int userId) {
      Presence pr = this.roster.getPresence(JIDHelper.INSTANCE.getJid(userId));
      return new QBPresence(pr);
   }

   public void sendPresence(QBPresence presence) throws SmackException.NotConnectedException {
      this.connection.sendStanza(QBPresence.toSmackPresence(presence));
   }

   private void sendSmackPresence(String user, Presence.Type type) throws SmackException.NotConnectedException {
      Presence presencePacket = new Presence(type);
      presencePacket.setTo(user);
      this.connection.sendStanza(presencePacket);
   }

   public void addSubscriptionListener(QBSubscriptionListener listener) {
      if(listener != null) {
         this.subscriptionListeners.add(listener);
      }
   }

   public void removeSubscriptionListener(QBSubscriptionListener listener) {
      this.subscriptionListeners.remove(listener);
   }

   public void addRosterListener(QBRosterListener listener) {
      if(listener != null) {
         this.rosterListeners.add(listener);
      }
   }

   public void removeRosterListener(QBRosterListener listener) {
      this.rosterListeners.remove(listener);
   }

   public void confirmSubscription(int userId) throws SmackException.NotConnectedException, SmackException.NotLoggedInException, XMPPException, SmackException.NoResponseException {
      boolean isContainEntry = this.roster.contains(JIDHelper.INSTANCE.getJid(userId));
      this.sendSmackPresence(JIDHelper.INSTANCE.getJid(userId), Presence.Type.subscribed);
      if(!isContainEntry) {
         this.createEntry(userId, (String[])null);
      } else if(this.subscriptionMode == QBRoster.SubscriptionMode.mutual) {
         this.subscribe(userId);
      }

   }

   public void subscribe(int userId) throws SmackException.NotConnectedException {
      this.sendSmackPresence(JIDHelper.INSTANCE.getJid(userId), Presence.Type.subscribe);
   }

   public void unsubscribe(int userId) throws SmackException.NotConnectedException {
      this.sendSmackPresence(JIDHelper.INSTANCE.getJid(userId), Presence.Type.unsubscribe);
   }

   public void reject(int userId) throws SmackException.NotConnectedException {
      this.sendSmackPresence(JIDHelper.INSTANCE.getJid(userId), Presence.Type.unsubscribed);
   }

   public static enum SubscriptionMode {

      manual("manual", 0),
      mutual("mutual", 1);
      // $FF: synthetic field
      private static final QBRoster.SubscriptionMode[] $VALUES = new QBRoster.SubscriptionMode[]{manual, mutual};


      private SubscriptionMode(String var1, int var2) {}

   }

   private class SubscribePacketListener implements StanzaListener {

      private SubscribePacketListener() {}

      public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
         try {
            this.processSubscription((Presence)packet);
         } catch (SmackException.NotLoggedInException var3) {
            throw new QBRuntimeException("You have not logged in");
         }
      }

      private void processSubscription(Presence presence) throws SmackException.NotConnectedException, SmackException.NotLoggedInException {
         if(QBRoster.this.roster == null) {
            if(QBRoster.this.incomingPresencesQueue == null) {
               QBRoster.this.incomingPresencesQueue = new ArrayList();
            }

            synchronized(QBRoster.this.incomingPresencesQueue) {
               QBRoster.this.incomingPresencesQueue.add(presence);
            }
         } else {
            String user = presence.getFrom();
            boolean autoConfirmed = false;
            if(QBRoster.this.subscriptionMode == QBRoster.SubscriptionMode.mutual && QBRoster.this.roster.contains(user)) {
               boolean isSubscribed = QBRoster.this.roster.getEntry(user).getType() == RosterPacket.ItemType.to;
               if(isSubscribed) {
                  QBRoster.this.sendSmackPresence(user, Presence.Type.subscribed);
                  autoConfirmed = true;
               }
            }

            if(!autoConfirmed) {
               this.notifySubscriptionListeners(presence);
            }

         }
      }

      private void notifySubscriptionListeners(Presence presence) throws SmackException.NotConnectedException {
         int userId = JIDHelper.INSTANCE.parseUserId(presence.getFrom());
         Iterator i$ = QBRoster.this.subscriptionListeners.iterator();

         while(i$.hasNext()) {
            QBSubscriptionListener listener = (QBSubscriptionListener)i$.next();
            listener.subscriptionRequested(userId);
         }

      }

      // $FF: synthetic method
      SubscribePacketListener(QBRoster.NamelessClass1366439254 x1) {
         this();
      }
   }

   // $FF: synthetic class
   static class NamelessClass1366439254 {
   }

   private class RosterListenerImpl implements RosterListener {

      private RosterListenerImpl() {}

      public void entriesAdded(Collection strings) {
         Iterator i$ = QBRoster.this.rosterListeners.iterator();

         while(i$.hasNext()) {
            QBRosterListener listener = (QBRosterListener)i$.next();
            listener.entriesAdded(this.getUsersId(strings));
         }

      }

      public void entriesUpdated(Collection strings) {
         Iterator i$ = QBRoster.this.rosterListeners.iterator();

         while(i$.hasNext()) {
            QBRosterListener listener = (QBRosterListener)i$.next();
            listener.entriesUpdated(this.getUsersId(strings));
         }

      }

      public void entriesDeleted(Collection strings) {
         Iterator i$ = QBRoster.this.rosterListeners.iterator();

         while(i$.hasNext()) {
            QBRosterListener listener = (QBRosterListener)i$.next();
            listener.entriesDeleted(this.getUsersId(strings));
         }

      }

      public void presenceChanged(Presence presence) {
         Iterator i$ = QBRoster.this.rosterListeners.iterator();

         while(i$.hasNext()) {
            QBRosterListener listener = (QBRosterListener)i$.next();
            this.notifyPresenceChangedListener(presence, listener);
         }

      }

      private void notifyPresenceChangedListener(Presence presence, QBRosterListener listener) {
         listener.presenceChanged(new QBPresence(presence));
      }

      private List getUsersId(Collection users) {
         ArrayList usersIds = new ArrayList();
         Iterator i$ = users.iterator();

         while(i$.hasNext()) {
            String entry = (String)i$.next();
            usersIds.add(Integer.valueOf(JIDHelper.INSTANCE.parseUserId(entry)));
         }

         return usersIds;
      }

      // $FF: synthetic method
      RosterListenerImpl(QBRoster.NamelessClass1366439254 x1) {
         this();
      }
   }

   private static class SubscribeFilter implements StanzaFilter {

      private SubscribeFilter() {}

      public boolean accept(Stanza packet) {
         if(packet instanceof Presence) {
            Presence presence = (Presence)packet;
            if(presence.getType() == Presence.Type.subscribe) {
               return true;
            }
         }

         return false;
      }

      // $FF: synthetic method
      SubscribeFilter(QBRoster.NamelessClass1366439254 x0) {
         this();
      }
   }

   private class UnsubscribePacketListener implements StanzaListener {

      private UnsubscribePacketListener() {}

      public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
         String user = packet.getFrom();
         QBRoster.this.sendSmackPresence(user, Presence.Type.unsubscribed);
      }

      // $FF: synthetic method
      UnsubscribePacketListener(QBRoster.NamelessClass1366439254 x1) {
         this();
      }
   }

   private static class UnsubscribeFilter implements StanzaFilter {

      private UnsubscribeFilter() {}

      public boolean accept(Stanza packet) {
         if(packet instanceof Presence) {
            Presence presence = (Presence)packet;
            if(presence.getType() == Presence.Type.unsubscribe) {
               return true;
            }
         }

         return false;
      }

      // $FF: synthetic method
      UnsubscribeFilter(QBRoster.NamelessClass1366439254 x0) {
         this();
      }
   }
}
