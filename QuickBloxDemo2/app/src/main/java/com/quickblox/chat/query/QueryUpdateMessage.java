package com.quickblox.chat.query;

import android.text.TextUtils;
import com.quickblox.chat.query.QueryAbsMessage;
import com.quickblox.core.RestMethod;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.rest.RestRequest;
import java.util.Map;

public class QueryUpdateMessage extends QueryAbsMessage {

   private final String dialogId;
   private final StringifyArrayList messagesIds;
   private boolean readStatus;


   public QueryUpdateMessage(String dialogId, StringifyArrayList messagesIds) {
      this(dialogId, messagesIds, true);
   }

   public QueryUpdateMessage(String dialogId, StringifyArrayList messagesIds, boolean readStatus) {
      this.dialogId = dialogId;
      this.messagesIds = messagesIds;
      this.readStatus = readStatus;
   }

   protected void setParams(RestRequest request) {
      super.setParams(request);
      Map parameters = request.getParameters();
      if(!TextUtils.isEmpty(this.dialogId)) {
         parameters.put("chat_dialog_id", this.dialogId);
      }

      parameters.put("read", Integer.valueOf(this.readStatus?1:0));
   }

   public String getUrl() {
      String relateDomain = super.getRelateDomain();
      return this.messagesIds == null?this.buildQueryUrl(new Object[]{relateDomain}):this.buildQueryUrl(new Object[]{relateDomain, this.messagesIds.getItemsAsString()});
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.PUT);
   }
}
