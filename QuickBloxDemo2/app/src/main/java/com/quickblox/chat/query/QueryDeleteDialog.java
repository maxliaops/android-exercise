package com.quickblox.chat.query;

import com.quickblox.chat.query.QueryAbsDialogVoid;
import com.quickblox.core.RestMethod;
import com.quickblox.core.rest.RestRequest;

public class QueryDeleteDialog extends QueryAbsDialogVoid {

   private String dialogID;


   public QueryDeleteDialog(String dialogID) {
      this.dialogID = dialogID;
   }

   protected void setMethod(RestRequest request) {
      request.setMethod(RestMethod.DELETE);
   }

   public String getUrl() {
      return this.buildQueryUrl(new Object[]{"chat", "Dialog", this.dialogID});
   }
}
