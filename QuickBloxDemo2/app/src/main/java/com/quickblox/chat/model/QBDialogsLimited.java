package com.quickblox.chat.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.model.QBEntityLimited;
import java.util.ArrayList;

public class QBDialogsLimited extends QBEntityLimited {

   @SerializedName("items")
   private ArrayList items;


   public ArrayList getItems() {
      return this.items;
   }

   public void setItems(ArrayList items) {
      this.items = items;
   }

   public ArrayList getEntity() {
      return this.items;
   }
}
