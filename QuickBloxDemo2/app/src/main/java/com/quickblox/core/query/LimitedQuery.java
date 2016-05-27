package com.quickblox.core.query;

import com.quickblox.core.parser.QBLimitedJsonParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.request.GenericQueryRule;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.request.QueryRule;
import com.quickblox.core.rest.RestRequest;
import java.util.Iterator;
import java.util.Map;

public class LimitedQuery extends JsonQuery {

   private Integer pagesLimit;
   private Integer pagesSkip;
   protected QBRequestGetBuilder requestBuilder;


   public LimitedQuery() {
      this.setParser(new QBLimitedJsonParser(this));
   }

   public Integer getPagesLimit() {
      return this.pagesLimit;
   }

   public void setLimit(Integer pagesLimit) {
      this.pagesLimit = pagesLimit;
   }

   public Integer getPagesSkip() {
      return this.pagesSkip;
   }

   public void setSkip(Integer pagesSkip) {
      this.pagesSkip = pagesSkip;
   }

   protected void setParams(RestRequest request) {
      Map parametersMap = request.getParameters();
      if(this.requestBuilder != null) {
         if(this.requestBuilder.getSkip() != 0) {
            this.setSkip(Integer.valueOf(this.requestBuilder.getSkip()));
         }

         if(this.requestBuilder.getLimit() != 0) {
            this.setLimit(Integer.valueOf(this.requestBuilder.getLimit()));
         }

         Iterator i$ = this.requestBuilder.getRules().iterator();

         while(i$.hasNext()) {
            GenericQueryRule gqr = (GenericQueryRule)i$.next();
            QueryRule qr = (QueryRule)gqr;
            parametersMap.put(qr.getRuleAsRequestParamGetQuery(), qr.getRuleAsRequestValue());
         }
      }

      this.putValue(parametersMap, "skip", this.pagesSkip);
      this.putValue(parametersMap, "limit", this.pagesLimit);
      super.setParams(request);
   }

   public void setRequestBuilder(QBRequestGetBuilder requestBuilder) {
      this.requestBuilder = requestBuilder;
   }

   public QBRequestGetBuilder getRequestBuilder() {
      return this.requestBuilder;
   }
}
