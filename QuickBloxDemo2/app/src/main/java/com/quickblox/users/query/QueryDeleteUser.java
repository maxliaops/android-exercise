package com.quickblox.users.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import com.quickblox.users.model.QBUser;

public class QueryDeleteUser extends JsonQuery {

   public static final int BY_EXTERNAL_USER_ID = 0;
   private QBUser user;
   private Integer filterType;


   public QueryDeleteUser(QBUser user) {
      this.user = user;
   }

   public QueryDeleteUser(QBUser user, Integer filterType) {
      this.user = user;
      this.filterType = filterType;
   }

   public String getUrl() {
      return this.filterType != null && this.filterType.intValue() == 0?this.buildQueryUrl(new Object[]{"users", "external", this.user.getExternalId()}):this.buildQueryUrl(new Object[]{"users", this.user.getId()});
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.DELETE);
   }
}
