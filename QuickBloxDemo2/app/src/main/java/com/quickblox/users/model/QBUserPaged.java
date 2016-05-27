package com.quickblox.users.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.model.QBEntityPaged;
import com.quickblox.users.model.QBUserWrap;
import java.util.ArrayList;
import java.util.Iterator;

public class QBUserPaged extends QBEntityPaged {

   @SerializedName("items")
   ArrayList items;


   public ArrayList getItems() {
      return this.items;
   }

   public void setItems(ArrayList items) {
      this.items = items;
   }

   public ArrayList getEntity() {
      ArrayList qbUsers = new ArrayList();
      Iterator i$ = this.items.iterator();

      while(i$.hasNext()) {
         QBUserWrap item = (QBUserWrap)i$.next();
         qbUsers.add(item.getUser());
      }

      return qbUsers;
   }
}
