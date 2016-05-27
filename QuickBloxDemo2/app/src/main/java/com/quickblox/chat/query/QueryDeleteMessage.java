package com.quickblox.chat.query;

import com.quickblox.chat.query.QueryAbsMessage;
import com.quickblox.core.RestMethod;
import com.quickblox.core.rest.RestRequest;

public class QueryDeleteMessage extends QueryAbsMessage {

   private String messageId;


   public QueryDeleteMessage(String messageId) {
      this.messageId = messageId;
   }

   public String getUrl() {
      String relateDomain = super.getRelateDomain();
      return this.buildQueryUrl(new Object[]{relateDomain, this.messageId});
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.DELETE);
   }
}
