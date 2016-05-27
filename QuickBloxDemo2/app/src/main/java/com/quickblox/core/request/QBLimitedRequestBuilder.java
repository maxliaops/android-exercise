package com.quickblox.core.request;

import com.quickblox.core.request.QBRequestBuilder;

public class QBLimitedRequestBuilder extends QBRequestBuilder {

   private int recordsSkip;
   private int recordsLimit;


   public int getSkip() {
      return this.recordsSkip;
   }

   public QBLimitedRequestBuilder setSkip(int recordsSkip) {
      this.recordsSkip = recordsSkip;
      return this;
   }

   public int getLimit() {
      return this.recordsLimit;
   }

   public QBLimitedRequestBuilder setLimit(int recordsLimit) {
      this.recordsLimit = recordsLimit;
      return this;
   }

   @Deprecated
   public int getPagesSkip() {
      return this.recordsSkip;
   }

   @Deprecated
   public QBLimitedRequestBuilder setPagesSkip(int pagesSkip) {
      this.recordsSkip = pagesSkip;
      return this;
   }

   @Deprecated
   public int getPagesLimit() {
      return this.recordsLimit;
   }

   @Deprecated
   public QBLimitedRequestBuilder setPagesLimit(int pagesLimit) {
      this.recordsLimit = pagesLimit;
      return this;
   }
}
