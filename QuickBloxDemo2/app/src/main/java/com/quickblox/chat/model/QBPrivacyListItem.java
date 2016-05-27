package com.quickblox.chat.model;

import com.quickblox.chat.JIDHelper;

public class QBPrivacyListItem {

   private QBPrivacyListItem.Type type;
   private String valueForType;
   boolean allow;


   public boolean isAllow() {
      return this.allow;
   }

   public void setAllow(boolean allow) {
      this.allow = allow;
   }

   public String getValueForType() {
      return this.valueForType;
   }

   public void setValueForType(String valueForType) {
      this.valueForType = this.type.generateValueForType(valueForType);
   }

   public QBPrivacyListItem.Type getType() {
      return this.type;
   }

   public void setType(QBPrivacyListItem.Type type) {
      this.type = type;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder(QBPrivacyListItem.class.getSimpleName());
      stringBuilder.append("{").append(" type").append("=").append(this.getType()).append(", valueForType").append("=").append(this.getValueForType()).append(", isAllow").append("=").append(this.isAllow()).append("}");
      return stringBuilder.toString();
   }

   public static enum Type {

      USER_ID("USER_ID", 0, (QBPrivacyListItem.NamelessClass149347234)null) {
         public String generateValueForType(String value) {
            if(JIDHelper.INSTANCE.isChatJid(value)) {
               return value;
            } else {
               int userID = JIDHelper.INSTANCE.parseUserId(value);
               return userID != -1?JIDHelper.INSTANCE.getJid(userID):value;
            }
         }
      },
      GROUP_USER_ID("GROUP_USER_ID", 1, (QBPrivacyListItem.NamelessClass149347234)null) {
         public String generateValueForType(String value) {
            if(JIDHelper.INSTANCE.isMucJid(value)) {
               return value;
            } else {
               int userID = JIDHelper.INSTANCE.parseUserId(value);
               return userID != -1?JIDHelper.INSTANCE.generateMucDomainResource(String.valueOf(userID)):value;
            }
         }
      },
      GROUP("GROUP", 2),
      SUBSCRIPTION("SUBSCRIPTION", 3);
      // $FF: synthetic field
      private static final QBPrivacyListItem.Type[] $VALUES = new QBPrivacyListItem.Type[]{USER_ID, GROUP_USER_ID, GROUP, SUBSCRIPTION};


      private Type(String var1, int var2) {}

      public String generateValueForType(String value) {
         return value;
      }

      // $FF: synthetic method
      Type(String x0, int x1, QBPrivacyListItem.NamelessClass149347234 x2) {
         this(x0, x1);
      }

   }

   // $FF: synthetic class
   static class NamelessClass149347234 {
   }
}
