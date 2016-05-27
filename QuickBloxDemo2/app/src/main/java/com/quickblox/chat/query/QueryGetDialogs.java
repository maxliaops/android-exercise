package com.quickblox.chat.query;

import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.chat.model.QBDialogCustomDataDeserializer;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.model.QBDialogsLimited;
import com.quickblox.core.RestMethod;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.LimitedQuery;
import com.quickblox.core.rest.RestRequest;
import java.util.Map;

public class QueryGetDialogs extends LimitedQuery {

   private QBDialogType qbDialogType;


   public QueryGetDialogs(QBDialogType qbDialogType) {
      QBJsonParser parser = this.getParser();
      parser.setDeserializer(QBDialogsLimited.class);
      parser.putJsonTypeAdapter(QBDialogCustomData.class, new QBDialogCustomDataDeserializer());
      this.qbDialogType = qbDialogType;
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parameters = request.getParameters();
      if(this.qbDialogType != null) {
         parameters.put("type", Integer.valueOf(this.qbDialogType.getCode()));
      }

   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Dialog"});
   }
}
