package com.quickblox.core.account.model;

import com.qb.gson.annotations.SerializedName;

public class QBAccountSettings {

   @SerializedName("api_endpoint")
   private String apiEndpoint;
   @SerializedName("account_id")
   private Integer accountId;
   @SerializedName("chat_endpoint")
   private String chatEndpoint;
   @SerializedName("s3_bucket_name")
   private String bucketName;


   public String getApiEndpoint() {
      return this.apiEndpoint;
   }

   public Integer getAccountId() {
      return this.accountId;
   }

   public String getChatEndpoint() {
      return this.chatEndpoint;
   }

   public String getBucketName() {
      return this.bucketName;
   }

   public void setChatEndpoint(String chatEndpoint) {
      this.chatEndpoint = chatEndpoint;
   }

   public void setApiEndpoint(String apiEndpoint) {
      this.apiEndpoint = apiEndpoint;
   }
}
