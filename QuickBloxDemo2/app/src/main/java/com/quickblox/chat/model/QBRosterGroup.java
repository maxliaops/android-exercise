package com.quickblox.chat.model;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.model.QBRosterEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;

public class QBRosterGroup {

   private RosterGroup rosterGroup;


   public QBRosterGroup(RosterGroup rosterGroup) {
      this.rosterGroup = rosterGroup;
   }

   public String getName() {
      return this.rosterGroup.getName();
   }

   public void setName(String name) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      this.rosterGroup.setName(name);
   }

   public int getEntryCount() {
      return this.rosterGroup.getEntryCount();
   }

   public Collection getEntries() {
      HashSet allEntries = new HashSet();
      Iterator i$ = this.rosterGroup.getEntries().iterator();

      while(i$.hasNext()) {
         RosterEntry entry = (RosterEntry)i$.next();
         allEntries.add(new QBRosterEntry(entry));
      }

      return Collections.unmodifiableCollection(allEntries);
   }

   public QBRosterEntry getEntry(int userId) {
      RosterEntry entry = this.rosterGroup.getEntry(JIDHelper.INSTANCE.getJid(userId));
      return new QBRosterEntry(entry);
   }

   public boolean contains(QBRosterEntry entry) {
      return this.rosterGroup.contains(entry.getRosterEntry());
   }

   public boolean contains(int userId) {
      return this.rosterGroup.getEntry(JIDHelper.INSTANCE.getJid(userId)) != null;
   }

   public void addEntry(QBRosterEntry entry) throws XMPPException, SmackException.NotConnectedException, SmackException.NoResponseException {
      this.rosterGroup.addEntry(entry.getRosterEntry());
   }

   public void removeEntry(QBRosterEntry entry) throws XMPPException, SmackException.NotConnectedException, SmackException.NoResponseException {
      this.rosterGroup.removeEntry(entry.getRosterEntry());
   }
}
