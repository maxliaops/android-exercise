package com.quickblox.chat.query;

import com.quickblox.chat.model.QBUnreadMessagesDeserializer;
import com.quickblox.chat.parser.QBUnreadMessagesJsonParser;
import com.quickblox.core.RestMethod;
import com.quickblox.core.helper.ToStringHelper;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestRequest;
import java.util.Map;
import java.util.Set;

public class QueryGetUnreadMessages extends JsonQuery {

   private Set dialogIDs;


   public QueryGetUnreadMessages(Set dialogIDs) {
      QBUnreadMessagesJsonParser parser = new QBUnreadMessagesJsonParser(this);
      this.setParser(parser);
      parser.setDeserializer(QBUnreadMessagesDeserializer.class);
      parser.setTypeAdapterForDeserializer(new QBUnreadMessagesDeserializer());
      this.dialogIDs = dialogIDs;
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parameters = request.getParameters();
      if(this.dialogIDs != null && this.dialogIDs.size() > 0) {
         parameters.put("chat_dialog_ids", ToStringHelper.arrayToString(this.dialogIDs.toArray()));
      }

   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.GET);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Message", "unread"});
   }
}
