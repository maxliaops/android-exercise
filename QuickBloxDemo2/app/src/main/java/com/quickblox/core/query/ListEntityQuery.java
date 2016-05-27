package com.quickblox.core.query;

import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.request.QBRequestBuilder;

public class ListEntityQuery extends JsonQuery {

   protected QBRequestBuilder requestBuilder;


   public void setRequestBuilder(QBRequestBuilder requestBuilder) {
      this.requestBuilder = requestBuilder;
   }

   public QBRequestBuilder getRequestBuilder() {
      return this.requestBuilder;
   }
}
