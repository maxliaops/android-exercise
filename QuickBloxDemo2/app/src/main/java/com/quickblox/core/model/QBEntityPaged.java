package com.quickblox.core.model;

import com.qb.gson.annotations.SerializedName;
import com.quickblox.core.model.QBListEntityWrap;

public abstract class QBEntityPaged implements QBListEntityWrap {

   @SerializedName("current_page")
   protected Integer currentPage;
   @SerializedName("per_page")
   protected Integer perPage;
   @SerializedName("total_entries")
   protected Integer totalEntries;


   public Integer getCurrentPage() {
      return this.currentPage;
   }

   public void setCurrentPage(Integer currentPage) {
      this.currentPage = currentPage;
   }

   public Integer getPerPage() {
      return this.perPage;
   }

   public void setPerPage(Integer perPage) {
      this.perPage = perPage;
   }

   public Integer getTotalEntries() {
      return this.totalEntries;
   }

   public void setTotalEntries(Integer totalEntries) {
      this.totalEntries = totalEntries;
   }
}
