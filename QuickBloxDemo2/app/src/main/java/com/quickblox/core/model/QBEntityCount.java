package com.quickblox.core.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.model.QBEntityLimited;

public class QBEntityCount extends QBEntityLimited {

   @SerializedName("items")
   private QBEntityCount.QBEntityCountWrap countWrap;


   public Integer getEntity() {
      return this.countWrap.getCount();
   }

   static class QBEntityCountWrap {

      private Integer count;


      public Integer getCount() {
         return this.count;
      }
   }
}
