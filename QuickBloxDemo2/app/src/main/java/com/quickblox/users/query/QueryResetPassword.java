package com.quickblox.users.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.users.model.QBUser;
import java.util.Map;

public class QueryResetPassword extends JsonQuery {

   private QBUser user;


   public QueryResetPassword(QBUser user) {
      this.user = user;
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"users", "password/reset"});
   }

   protected void setParams(RestRequest request) {
      Map parametersMap = request.getParameters();
      this.putValue(parametersMap, "email", this.user.getEmail());
   }
}
