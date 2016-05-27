package com.quickblox.chat.model;

import com.quickblox.chat.JIDHelper;
import com.quickblox.core.exception.QBRuntimeException;
import org.jivesoftware.smack.packet.Presence;

public class QBPresence {

   public static final String X_ELEMENT_NAME = "x";
   public static final String X_ELEMENT_NAMESPACE = "http://jabber.org/protocol/muc#user";
   private Integer userId;
   private String resource;
   private QBPresence.Type type;
   private String status;
   private QBPresence.Mode mode;
   private int priority;


   public QBPresence(Presence presence) {
      this.type = QBPresence.Type.online;
      this.priority = Integer.MIN_VALUE;
      String senderJid = presence.getFrom();
      if(JIDHelper.INSTANCE.isChatJid(senderJid)) {
         this.userId = Integer.valueOf(JIDHelper.INSTANCE.parseUserId(senderJid));
         this.resource = JIDHelper.INSTANCE.parseResource(senderJid);
      } else {
         this.userId = JIDHelper.INSTANCE.parseRoomOccupant(senderJid);
      }

      this.type = QBPresence.Type.fromSmackPresenceType(presence.getType());
      this.status = presence.getStatus();
      Presence.Mode smackPresenceMode = presence.getMode();
      if(smackPresenceMode != null) {
         this.mode = QBPresence.Mode.valueOf(smackPresenceMode.name());
      }

   }

   public QBPresence(QBPresence.Type type) {
      this.type = QBPresence.Type.online;
      this.priority = Integer.MIN_VALUE;
      this.type = type;
   }

   public QBPresence(QBPresence.Type type, String status, int priority, QBPresence.Mode mode) {
      this.type = QBPresence.Type.online;
      this.priority = Integer.MIN_VALUE;
      this.type = type;
      this.status = status;
      this.priority = priority;
      this.mode = mode;
   }

   public static Presence toSmackPresence(QBPresence presence) {
      Presence.Type presenceType = QBPresence.Type.toSmackPresenceType(presence.getType());
      if(presence.getMode() == null && presence.getStatus() == null && presence.getPriority() == Integer.MIN_VALUE) {
         return new Presence(presenceType);
      } else {
         Presence.Mode presenceMode = null;
         if(presence.getMode() != null) {
            presenceMode = Presence.Mode.valueOf(presence.getMode().toString());
         }

         return new Presence(presenceType, presence.getStatus(), presence.getPriority(), presenceMode);
      }
   }

   public String toString() {
      if(this.getUserId() == null) {
         return this.getClass().getSimpleName();
      } else {
         StringBuilder stringBuilder = new StringBuilder();
         stringBuilder.append("{").append("type").append("=").append(this.getType()).append(", user").append("=").append(this.getUserId() == null?"null":this.getUserId()).append(", status").append("=").append(this.getStatus() == null?"null":this.getStatus()).append("}");
         return stringBuilder.toString();
      }
   }

   public Integer getUserId() {
      return this.userId;
   }

   public String getResource() {
      return this.resource;
   }

   public QBPresence.Type getType() {
      return this.type;
   }

   public void setType(QBPresence.Type type) {
      this.type = type;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public QBPresence.Mode getMode() {
      return this.mode;
   }

   public void setMode(QBPresence.Mode mode) {
      this.mode = mode;
   }

   public int getPriority() {
      return this.priority;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   // $FF: synthetic class
   static class NamelessClass1810304116 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$quickblox$chat$model$QBPresence$Type;
      // $FF: synthetic field
      static final int[] $SwitchMap$org$jivesoftware$smack$packet$Presence$Type = new int[Presence.Type.values().length];


      static {
         try {
            $SwitchMap$org$jivesoftware$smack$packet$Presence$Type[Presence.Type.available.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$org$jivesoftware$smack$packet$Presence$Type[Presence.Type.unavailable.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         $SwitchMap$com$quickblox$chat$model$QBPresence$Type = new int[QBPresence.Type.values().length];

         try {
            $SwitchMap$com$quickblox$chat$model$QBPresence$Type[QBPresence.Type.online.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$quickblox$chat$model$QBPresence$Type[QBPresence.Type.offline.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum Mode {

      chat("chat", 0),
      available("available", 1),
      away("away", 2),
      xa("xa", 3),
      dnd("dnd", 4);
      // $FF: synthetic field
      private static final QBPresence.Mode[] $VALUES = new QBPresence.Mode[]{chat, available, away, xa, dnd};


      private Mode(String var1, int var2) {}

   }

   public static enum Type {

      online("online", 0),
      offline("offline", 1);
      // $FF: synthetic field
      private static final QBPresence.Type[] $VALUES = new QBPresence.Type[]{online, offline};


      private Type(String var1, int var2) {}

      public static Presence.Type toSmackPresenceType(QBPresence.Type type) {
         switch(QBPresence.NamelessClass1810304116.$SwitchMap$com$quickblox$chat$model$QBPresence$Type[type.ordinal()]) {
         case 1:
            return Presence.Type.available;
         case 2:
            return Presence.Type.unavailable;
         default:
            throw new QBRuntimeException("Incorrect presence type");
         }
      }

      public static QBPresence.Type fromSmackPresenceType(Presence.Type type) {
         switch(QBPresence.NamelessClass1810304116.$SwitchMap$org$jivesoftware$smack$packet$Presence$Type[type.ordinal()]) {
         case 1:
            return online;
         case 2:
            return offline;
         default:
            throw new QBRuntimeException("Incorrect presence type");
         }
      }

   }
}
