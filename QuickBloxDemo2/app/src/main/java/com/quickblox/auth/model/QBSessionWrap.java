package com.quickblox.auth.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.model.QBEntityWrap;

public class QBSessionWrap implements QBEntityWrap {

   @SerializedName("session")
   QBSession session;


   public QBSession getSession() {
      return this.session;
   }

   public void setSession(QBSession session) {
      this.session = session;
   }

   public QBSession getEntity() {
      return this.session;
   }
}
