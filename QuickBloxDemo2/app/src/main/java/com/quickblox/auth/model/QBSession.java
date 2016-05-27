package com.quickblox.auth.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.model.QBEntity;

public class QBSession extends QBEntity {

   @SerializedName("token")
   private String token;
   @SerializedName("application_id")
   private Integer appId;
   @SerializedName("user_id")
   private Integer userId;
   @SerializedName("device_id")
   private Integer deviceId;
   @SerializedName("ts")
   private Integer timestamp;
   @SerializedName("nonce")
   private Integer nonce;


   public String getToken() {
      return this.token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public Integer getAppId() {
      return this.appId;
   }

   public void setAppId(Integer appId) {
      this.appId = appId;
   }

   public Integer getUserId() {
      return this.userId;
   }

   public void setUserId(int userId) {
      this.userId = Integer.valueOf(userId);
   }

   public long getDeviceId() {
      return (long)this.deviceId.intValue();
   }

   public void setDeviceId(Integer deviceId) {
      this.deviceId = deviceId;
   }

   public long getTimestamp() {
      return (long)this.timestamp.intValue();
   }

   public void setTimestamp(Integer timestamp) {
      this.timestamp = timestamp;
   }

   public int getNonce() {
      return this.nonce.intValue();
   }

   public void setNonce(Integer nonce) {
      this.nonce = nonce;
   }

   public void copyFieldsTo(QBEntity entity) {
      super.copyFieldsTo(entity);
      QBSession session = (QBSession)entity;
      session.setToken(this.token);
      session.setAppId(this.appId);
      session.setUserId(this.userId.intValue());
      session.setDeviceId(this.deviceId);
      session.setTimestamp(this.timestamp);
      session.setNonce(this.nonce);
   }

   public String toString() {
      return "QBSession{token=\'" + this.token + '\'' + ", appId=" + this.appId + ", userId=" + this.userId + ", deviceId=" + this.deviceId + ", timestamp=" + this.timestamp + ", nonce=" + this.nonce + '}' + "\n";
   }
}
