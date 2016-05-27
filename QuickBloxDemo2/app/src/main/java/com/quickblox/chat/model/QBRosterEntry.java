package com.quickblox.chat.model;

import com.quickblox.chat.JIDHelper;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.packet.RosterPacket;

public class QBRosterEntry {

   private RosterEntry rosterEntry;


   public QBRosterEntry(RosterEntry rosterEntry) {
      this.rosterEntry = rosterEntry;
   }

   public Integer getUserId() {
      return Integer.valueOf(JIDHelper.INSTANCE.parseUserId(this.rosterEntry.getUser()));
   }

   public RosterPacket.ItemType getType() {
      return this.rosterEntry.getType();
   }

   public RosterPacket.ItemStatus getStatus() {
      return this.rosterEntry.getStatus();
   }

   public boolean equals(Object object) {
      return this == object?true:(object != null && object instanceof QBRosterEntry?this.getUserId().equals(((QBRosterEntry)object).getUserId()):false);
   }

   public String toString() {
      StringBuilder buf = new StringBuilder();
      int user = JIDHelper.INSTANCE.parseUserId(this.rosterEntry.getUser());
      buf.append(user);
      return buf.toString();
   }

   public boolean equalsDeep(Object obj) {
      if(this == obj) {
         return true;
      } else if(obj == null) {
         return false;
      } else if(this.getClass() != obj.getClass()) {
         return false;
      } else {
         QBRosterEntry other = (QBRosterEntry)obj;
         if(this.getStatus() == null) {
            if(other.getStatus() != null) {
               return false;
            }
         } else if(!this.getStatus().equals(other.getStatus())) {
            return false;
         }

         if(this.getType() == null) {
            if(other.getType() != null) {
               return false;
            }
         } else if(!this.getType().equals(other.getType())) {
            return false;
         }

         if(this.getUserId() == null) {
            if(other.getUserId() != null) {
               return false;
            }
         } else if(!this.getUserId().equals(other.getUserId())) {
            return false;
         }

         return true;
      }
   }

   public RosterEntry getRosterEntry() {
      return this.rosterEntry;
   }
}
