package com.quickblox.auth.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.core.server.BaseService;

public class QueryDeleteSession extends JsonQuery {

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.DELETE);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"session"});
   }

   protected void doneSuccess() {
      super.doneSuccess();

      try {
         BaseService.getBaseService().resetCredentials();
      } catch (BaseServiceException var2) {
         var2.printStackTrace();
      }

   }
}
