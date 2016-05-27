package com.quickblox.chat.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.model.QBEntityCount;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.LimitedQuery;
import com.quickblox.core.rest.RestRequest;
import java.util.Map;

public class QueryGetCountMessage extends LimitedQuery {

   private String dialogID;


   public QueryGetCountMessage(String dialogID) {
      QBJsonParser parser = this.getParser();
      parser.setDeserializer(QBEntityCount.class);
      this.dialogID = dialogID;
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parameters = request.getParameters();
      if(this.dialogID != null) {
         parameters.put("chat_dialog_id", this.dialogID);
         parameters.put("count", Integer.valueOf(1));
      }

   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Message"});
   }
}
