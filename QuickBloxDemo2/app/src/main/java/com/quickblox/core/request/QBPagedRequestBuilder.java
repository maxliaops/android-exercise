package com.quickblox.core.request;

import com.quickblox.core.request.QBRequestBuilder;

public class QBPagedRequestBuilder extends QBRequestBuilder {

   private int page;
   private int perPage;


   public QBPagedRequestBuilder() {}

   public QBPagedRequestBuilder(int perPage, int page) {
      this.setPage(page);
      this.setPerPage(perPage);
   }

   public int getPage() {
      return this.page;
   }

   public QBPagedRequestBuilder setPage(int page) {
      this.page = page;
      this.addRule("page", Integer.valueOf(page));
      return this;
   }

   public int getPerPage() {
      return this.perPage;
   }

   public QBPagedRequestBuilder setPerPage(int perPage) {
      this.perPage = perPage;
      this.addRule("per_page", Integer.valueOf(perPage));
      return this;
   }
}
