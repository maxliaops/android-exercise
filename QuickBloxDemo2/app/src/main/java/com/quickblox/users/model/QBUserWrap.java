package com.quickblox.users.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.model.QBEntityWrap;
import com.quickblox.users.model.QBUser;

public class QBUserWrap implements QBEntityWrap {

   @SerializedName("user")
   QBUser user;


   public QBUser getUser() {
      return this.user;
   }

   public void setUser(QBUser user) {
      this.user = user;
   }

   public QBUser getEntity() {
      return this.user;
   }
}
