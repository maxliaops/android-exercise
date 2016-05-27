package com.quickblox.chat.query;

import com.quickblox.core.RestMethod;
import com.quickblox.core.model.QBEntityCount;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.LimitedQuery;
import com.quickblox.core.rest.RestRequest;
import java.util.Map;

public class QueryGetCountDialogs extends LimitedQuery {

   public QueryGetCountDialogs() {
      QBJsonParser parser = this.getParser();
      parser.setDeserializer(QBEntityCount.class);
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parameters = request.getParameters();
      parameters.put("count", Integer.valueOf(1));
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Dialog"});
   }
}
