package com.quickblox.users.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;

public class QuerySignOut extends JsonQuery {

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"login"});
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.DELETE);
   }
}
