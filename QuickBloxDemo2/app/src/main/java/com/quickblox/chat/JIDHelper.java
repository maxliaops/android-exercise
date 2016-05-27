package com.quickblox.chat;

import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;

public enum JIDHelper {

   INSTANCE("INSTANCE", 0);
   private String chatServerEndpoint = QBSettings.getInstance().getChatEndpoint();
   // $FF: synthetic field
   private static final JIDHelper[] $VALUES = new JIDHelper[]{INSTANCE};


   private JIDHelper(String var1, int var2) {}

   void setChatServerEndpoint(String chatServerEndpoint) {
      this.chatServerEndpoint = chatServerEndpoint;
   }

   public String getChatServerEndpoint() {
      return this.chatServerEndpoint;
   }

   public String getChatJidPostfix() {
      return "@" + this.chatServerEndpoint;
   }

   public String getMucServerEndpoint() {
      return "muc." + this.chatServerEndpoint;
   }

   public String getMucJidPostfix() {
      return "@muc." + this.chatServerEndpoint;
   }

   public String parseJid(String bareJid) {
      return bareJid.split("/")[0];
   }

   public int parseUserId(String jid) {
      try {
         return Integer.parseInt(this.parseJid(jid).split("-")[0]);
      } catch (NumberFormatException var3) {
         return -1;
      }
   }

   public String parseResource(String jid) {
      if(jid.contains("/")) {
         String[] jidSplit = jid.split("/");
         if(jidSplit.length > 1) {
            return jidSplit[1];
         }
      }

      return null;
   }

   public String getJidLocalpart(QBUser user) {
      return this.getJidLocalpart(user.getId().intValue());
   }

   public String getJidLocalpart(int userId) {
      String appId = QBSettings.getInstance().getApplicationId();
      return String.format("%s-%s", new Object[]{Integer.valueOf(userId), appId});
   }

   public String getJid(QBUser user) {
      return this.getJidLocalpart(user) + this.getChatJidPostfix();
   }

   public String getJid(int userId) {
      return this.getJid(new QBUser(Integer.valueOf(userId)));
   }

   public boolean isChatJid(String jid) {
      return jid.contains(this.getChatJidPostfix());
   }

   public Integer parseRoomOccupant(String jid) {
      Integer occupantId = null;
      if(jid.contains("/")) {
         try {
            occupantId = Integer.valueOf(jid.split("/")[1]);
         } catch (NumberFormatException var4) {
            return null;
         }
      }

      return occupantId;
   }

   public String dialogIdFromRoomJid(String roomJid) {
      String dialogId = null;

      try {
         dialogId = roomJid.split("@")[0].split("_")[1];
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return dialogId;
   }

   public String getRoomJid(String localPart) {
      return localPart + this.getMucJidPostfix();
   }

   public boolean isRoomJid(String jid) {
      return jid.contains(this.getMucJidPostfix());
   }

   public String generateMucDomainResource(String resource) {
      return this.generateDomainResource(this.getMucServerEndpoint(), resource);
   }

   public String generateDomainResource(String domain, String resource) {
      return String.format("%s/%s", new Object[]{domain, resource});
   }

   public boolean isMucJid(String jid) {
      return jid.contains(this.getMucServerEndpoint());
   }

}
