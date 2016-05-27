package com.quickblox.users.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.users.model.QBUser;
import com.quickblox.users.query.QueryBaseCreateUser;
import java.util.Map;

public class QueryCreateUser extends QueryBaseCreateUser {

   public QueryCreateUser(QBUser user) {
      super(user);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"users"});
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parametersMap = request.getParameters();
      this.putValue(parametersMap, "user[password]", this.user.getPassword());
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.POST);
   }
}
