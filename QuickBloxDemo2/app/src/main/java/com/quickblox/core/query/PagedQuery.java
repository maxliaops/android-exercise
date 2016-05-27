package com.quickblox.core.query;

import com.quickblox.core.parser.QBPagedJSonParser;
import com.quickblox.core.query.ListEntityQuery;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.rest.RestRequest;
import java.util.Map;

public class PagedQuery extends ListEntityQuery {

   public PagedQuery() {
      this.setParser(new QBPagedJSonParser(this));
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      if(this.requestBuilder != null) {
         QBPagedRequestBuilder qbPagedRequestBuilder = (QBPagedRequestBuilder)this.requestBuilder;
         Map parametersMap = request.getParameters();
         this.requestBuilder.fillParametersMap(parametersMap);
         if(qbPagedRequestBuilder.getPage() > 0) {
            this.putValue(parametersMap, "page", Integer.valueOf(qbPagedRequestBuilder.getPage()));
         }

         if(qbPagedRequestBuilder.getPerPage() > 0) {
            this.putValue(parametersMap, "per_page", Integer.valueOf(qbPagedRequestBuilder.getPerPage()));
         }
      }

   }
}
