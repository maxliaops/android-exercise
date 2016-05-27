package com.quickblox.users.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.users.model.QBUser;
import com.quickblox.users.query.QueryBaseCreateUser;
import java.util.Map;

public class QueryUpdateUser extends QueryBaseCreateUser {

   public QueryUpdateUser(QBUser user) {
      super(user);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"users", this.user.getId()});
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.PUT);
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parametersMap = request.getParameters();
      if(this.user.getPassword() != null) {
         this.putValue(parametersMap, "user[password]", this.user.getPassword());
         this.putValue(parametersMap, "user[old_password]", this.user.getOldPassword());
      }

      if(this.user.getFileId() != null && this.user.getFileId().equals(Integer.valueOf(-1))) {
         this.putValue(parametersMap, "user[blob_id]", "");
      }

   }
}
