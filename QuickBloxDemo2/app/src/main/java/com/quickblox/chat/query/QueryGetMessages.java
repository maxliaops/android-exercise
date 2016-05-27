package com.quickblox.chat.query;

import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBChatMessageDeserializer;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBHistoryDialogMessagesWrapLimited;
import com.quickblox.core.RestMethod;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.LimitedQuery;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.rest.RestRequest;
import java.util.Map;

public class QueryGetMessages extends LimitedQuery {

   private QBDialog qbDialog;


   public QueryGetMessages(QBDialog qbDialog, QBRequestGetBuilder requestBuilder) {
      this.qbDialog = qbDialog;
      QBJsonParser parser = this.getParser();
      parser.setDeserializer(QBHistoryDialogMessagesWrapLimited.class);
      parser.putJsonTypeAdapter(QBChatMessage.class, new QBChatMessageDeserializer());
      this.setRequestBuilder(requestBuilder);
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parameters = request.getParameters();
      if(this.qbDialog != null) {
         parameters.put("chat_dialog_id", this.qbDialog.getDialogId());
      }

   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Message"});
   }
}
