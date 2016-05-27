package com.quickblox.chat.query;

import com.quickblox.core.query.JsonQuery;

public class QueryAbsMessage extends JsonQuery {

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Message"});
   }

   protected String getRelateDomain() {
      return "chat/Message";
   }
}
