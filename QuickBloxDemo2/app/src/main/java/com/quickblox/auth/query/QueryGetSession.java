package com.quickblox.auth.query;

import com.quickblox.auth.model.QBSessionWrap;
import com.quickblox.auth.parsers.QBSGetSessionJsonParser;
import com.quickblox.core.RestMethod;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;

public class QueryGetSession extends JsonQuery {

   public QueryGetSession() {
      QBSGetSessionJsonParser qbsGetSesionJsonParser = new QBSGetSessionJsonParser(this);
      qbsGetSesionJsonParser.setDeserializer(QBSessionWrap.class);
      this.setParser(qbsGetSesionJsonParser);
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"session"});
   }
}
