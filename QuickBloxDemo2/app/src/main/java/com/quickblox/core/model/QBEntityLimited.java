package com.quickblox.core.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.model.QBEntityWrap;

public abstract class QBEntityLimited implements QBEntityWrap {

   @SerializedName("skip")
   protected Integer skip;
   @SerializedName("limit")
   protected Integer limit;
   @SerializedName("total_entries")
   protected Integer totalEntries;


   public Integer getSkip() {
      return this.skip;
   }

   public void setSkip(Integer skip) {
      this.skip = skip;
   }

   public Integer getLimit() {
      return this.limit;
   }

   public void setLimit(Integer limit) {
      this.limit = limit;
   }

   public Integer getTotalEntries() {
      return this.totalEntries;
   }

   public void setTotalEntries(Integer totalEntries) {
      this.totalEntries = totalEntries;
   }
}
