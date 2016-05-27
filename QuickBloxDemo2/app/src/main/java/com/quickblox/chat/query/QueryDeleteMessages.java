package com.quickblox.chat.query;

import android.text.TextUtils;
import com.quickblox.chat.query.QueryAbsMessage;
import com.quickblox.core.RestMethod;
import com.quickblox.core.rest.RestRequest;
import java.util.Set;

public class QueryDeleteMessages extends QueryAbsMessage {

   private Set messageIDs;


   public QueryDeleteMessages(Set messageIDs) {
      this.messageIDs = messageIDs;
   }

   public String getUrl() {
      String relateDomain = super.getRelateDomain();
      String comSeparetedMessagesIDs = TextUtils.join(",", this.messageIDs);
      return this.buildQueryUrl(new Object[]{relateDomain, comSeparetedMessagesIDs});
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.DELETE);
   }
}
