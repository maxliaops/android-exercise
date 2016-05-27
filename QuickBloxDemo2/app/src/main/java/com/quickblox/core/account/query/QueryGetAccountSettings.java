package com.quickblox.core.account.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.account.model.QBAccountSettings;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;

public class QueryGetAccountSettings extends JsonQuery {

   private final String accountKey;


   public QueryGetAccountSettings(String accountKey) {
      this.accountKey = accountKey;
      this.getParser().setDeserializer(QBAccountSettings.class);
   }

   public String getUrl() {
      return "https://api.quickblox.com/account_settings.json";
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

   protected void setAuthentication(RestRequest request) {}

   protected void setParams(RestRequest request) {
      request.getHeaders().put("QB-Account-Key", this.accountKey);
   }
}
