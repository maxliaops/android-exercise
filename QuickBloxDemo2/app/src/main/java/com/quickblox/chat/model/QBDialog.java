package com.quickblox.chat.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.model.QBEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QBDialog extends QBEntity {

   @SerializedName("_id")
   private String dialogId;
   @SerializedName("last_message")
   private String lastMessage;
   @SerializedName("last_message_date_sent")
   private long lastMessageDateSent;
   @SerializedName("last_message_user_id")
   private Integer lastMessageUserId;
   @SerializedName("photo")
   private String photo;
   @SerializedName("user_id")
   private Integer userId;
   @SerializedName("xmpp_room_jid")
   private String roomJid;
   @SerializedName("unread_messages_count")
   private Integer unreadMessageCount;
   private String name;
   @SerializedName("occupants_ids")
   private ArrayList occupantsIds;
   private Integer type;
   @SerializedName("data")
   QBDialogCustomData customData;


   public QBDialog() {}

   public QBDialog(String id) {
      this.dialogId = id;
   }

   public String getDialogId() {
      return this.dialogId;
   }

   public void setDialogId(String dialogId) {
      this.dialogId = dialogId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public QBDialogType getType() {
      return QBDialogType.parseByCode(this.type.intValue());
   }

   public void setType(QBDialogType type) {
      this.type = Integer.valueOf(type.getCode());
   }

   public Integer getUserId() {
      return this.userId;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   public ArrayList getOccupants() {
      return this.occupantsIds;
   }

   public void setOccupantsIds(ArrayList occupantsIds) {
      this.occupantsIds = occupantsIds;
   }

   public String getLastMessage() {
      return this.lastMessage;
   }

   public void setLastMessage(String lastMessage) {
      this.lastMessage = lastMessage;
   }

   public Integer getLastMessageUserId() {
      return this.lastMessageUserId;
   }

   public void setLastMessageUserId(Integer lastMessageUserId) {
      this.lastMessageUserId = lastMessageUserId;
   }

   public long getLastMessageDateSent() {
      return this.lastMessageDateSent;
   }

   public void setLastMessageDateSent(long lastMessageDateSent) {
      this.lastMessageDateSent = lastMessageDateSent;
   }

   public Integer getUnreadMessageCount() {
      return this.unreadMessageCount;
   }

   public void setUnreadMessageCount(Integer unreadMessageCount) {
      this.unreadMessageCount = unreadMessageCount;
   }

   public String getRoomJid() {
      return this.roomJid;
   }

   public void setRoomJid(String roomJid) {
      this.roomJid = roomJid;
   }

   public String getPhoto() {
      return this.photo;
   }

   public void setPhoto(String photo) {
      this.photo = photo;
   }

   @Deprecated
   public Map getData() {
      return this.getCustomData() != null?this.getCustomData().getFields():null;
   }

   @Deprecated
   public void setData(HashMap data) {
      this.setCustomData(new QBDialogCustomData(data));
   }

   public QBDialogCustomData getCustomData() {
      return this.customData;
   }

   public void setCustomData(QBDialogCustomData customData) {
      this.customData = customData;
   }

   public Integer getRecipientId() {
      if(this.getType() != QBDialogType.PRIVATE) {
         return Integer.valueOf(-1);
      } else {
         Iterator i$ = this.occupantsIds.iterator();

         Integer userId;
         do {
            if(!i$.hasNext()) {
               return Integer.valueOf(-1);
            }

            userId = (Integer)i$.next();
         } while(userId == QBChatService.getInstance().getUser().getId());

         return userId;
      }
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder(QBDialog.class.getSimpleName());
      stringBuilder.append("{").append("id").append("=").append(this.getDialogId()).append(", created_at").append("=").append(this.getFCreatedAt()).append(", updated_at").append("=").append(this.getFUpdatedAt()).append(", last_msg_user_id").append("=").append(this.getLastMessageUserId()).append(", occupants_ids").append("=").append(this.getOccupants()).append(", last_message").append("=").append(this.getLastMessage()).append(", last_message_date_sent").append("=").append(this.getLastMessageDateSent()).append(", type").append("=").append(this.getType()).append(", name").append("=").append(this.getName()).append(", room_jid").append("=").append(this.getRoomJid()).append(", user_id").append("=").append(this.getUserId()).append(", photo").append("=").append(this.getPhoto()).append(", unread_message_count").append("=").append(this.getUnreadMessageCount()).append(", customData").append("=").append(this.getCustomData()).append("}");
      return stringBuilder.toString();
   }
}
