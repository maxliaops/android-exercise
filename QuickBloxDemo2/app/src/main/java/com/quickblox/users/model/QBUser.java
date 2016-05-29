package com.quickblox.users.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.model.QBEntity;
import java.util.Date;

public class QBUser extends QBEntity {

   @SerializedName("full_name")
   protected String fullName;
   protected String email;
   protected String login;
   protected String phone;
   protected String website;
   @SerializedName("last_request_at")
   protected Date lastRequestAt;
   @SerializedName("external_user_id")
   protected String externalId;
   @SerializedName("facebook_id")
   protected String facebookId;
   @SerializedName("twitter_id")
   protected String twitterId;
   @SerializedName("twitter_digits_id")
   protected String twitterDigitsId;
   @SerializedName("blob_id")
   protected Integer blobId;
   @SerializedName("user_tags")
   protected String tags;
   protected String password;
   protected String oldPassword;
   @SerializedName("custom_data")
   private String customData;
   private Class customDataClass;


   public QBUser() {}

   public QBUser(String login, String password, String email) {
      this.login = login;
      this.password = password;
      this.email = email;
   }

   public QBUser(String login, String password) {
      this.login = login;
      this.password = password;
   }

   public QBUser(Integer id) {
      this.id = id;
   }

   public QBUser(String login) {
      this.login = login;
   }

   public String getOldPassword() {
      return this.oldPassword;
   }

   public void setOldPassword(String oldPassword) {
      this.oldPassword = oldPassword;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getFullName() {
      return this.fullName;
   }

   public void setFullName(String fullName) {
      this.fullName = fullName;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getLogin() {
      return this.login;
   }

   public void setLogin(String login) {
      this.login = login;
   }

   public String getPhone() {
      return this.phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getWebsite() {
      return this.website;
   }

   public void setWebsite(String website) {
      this.website = website;
   }

   public Date getLastRequestAt() {
      return this.lastRequestAt;
   }

   public void setLastRequestAt(Date lastRequestAt) {
      this.lastRequestAt = lastRequestAt;
   }

   public String getExternalId() {
      return this.externalId;
   }

   public void setExternalId(String externalId) {
      this.externalId = externalId;
   }

   public String getFacebookId() {
      return this.facebookId;
   }

   public void setFacebookId(String facebookId) {
      this.facebookId = facebookId;
   }

   public String getTwitterId() {
      return this.twitterId;
   }

   public void setTwitterId(String twitterId) {
      this.twitterId = twitterId;
   }

   public String getTwitterDigitsId() {
      return this.twitterDigitsId;
   }

   public void setTwitterDigitsId(String twitterDigitsId) {
      this.twitterDigitsId = twitterDigitsId;
   }

   public Integer getFileId() {
      return this.blobId;
   }

   public void setFileId(Integer blobId) {
      this.blobId = blobId;
   }

   public void setCustomData(String customData) {
      this.customData = customData;
   }

   public String getCustomData() {
      return this.customData;
   }

   public void setCustomDataClass(Class customDataClass) {
      this.customDataClass = customDataClass;
   }

   public StringifyArrayList getTags() {
      StringifyArrayList tagsAsArray = new StringifyArrayList();
      if(this.tags != null) {
         String[] arr$ = this.tags.split(",");
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String tag = arr$[i$];
            tagsAsArray.add(tag.trim());
         }
      }

      return tagsAsArray;
   }

   public void setTags(StringifyArrayList tags) {
      if(tags != null) {
         this.tags = tags.getItemsAsString();
      }
   }

   public void copyFieldsTo(QBUser user) {
      if(user != null) {
         super.copyFieldsTo(user);
         user.setFullName(this.fullName);
         user.setEmail(this.email);
         user.setLogin(this.login);
         user.setPhone(this.phone);
         user.setWebsite(this.website);
         user.setLastRequestAt(this.lastRequestAt);
         user.setExternalId(this.externalId);
         user.setFacebookId(this.facebookId);
         user.setTwitterId(this.twitterId);
         user.setTwitterDigitsId(this.twitterDigitsId);
         user.setCustomData(this.customData);
         user.tags = this.tags;
      }

   }

   public String toString() {
      return "QBUser{id=" + this.id + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + ", fullName=\'" + this.fullName + '\'' + ", email=\'" + this.email + '\'' + ", login=\'" + this.login + '\'' + ", phone=\'" + this.phone + '\'' + ", website=\'" + this.website + '\'' + ", lastRequestAt=\'" + this.lastRequestAt + '\'' + ", externalId=" + this.externalId + ", facebookId=" + this.facebookId + ", twitterId=" + this.twitterId + ", twitterDigitsId=" + this.twitterDigitsId + ", blobId=" + this.blobId + ", tags=\'" + this.tags + '\'' + ", password=\'" + this.password + '\'' + ", oldPassword=\'" + this.oldPassword + '\'' + ", customData=\'" + this.customData + '\'' + "}" + "\n";
   }
}
