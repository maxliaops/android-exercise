package com.quickblox.chat.query;

import com.quickblox.core.query.JsonQuery;

public class QueryAbsDialogVoid extends JsonQuery {

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Dialog"});
   }
}
