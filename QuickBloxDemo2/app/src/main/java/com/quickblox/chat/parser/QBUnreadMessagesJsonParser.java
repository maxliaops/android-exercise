package com.quickblox.chat.parser;

import android.os.Bundle;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.result.Result;
import java.util.Iterator;
import java.util.Map;

public class QBUnreadMessagesJsonParser extends QBJsonParser {

   public QBUnreadMessagesJsonParser(JsonQuery query) {
      super(query);
   }

   protected Object parseJsonResponse(RestResponse response, Result result) throws QBResponseException {
      Map jsonResponse = (Map)super.parseJsonResponse(response, result);
      if(jsonResponse == null) {
         return Integer.valueOf(0);
      } else {
         Bundle bundle = this.getBundle();
         if(bundle != null) {
            Iterator i$ = jsonResponse.keySet().iterator();

            while(i$.hasNext()) {
               String key = (String)i$.next();
               if(!key.equals("total")) {
                  bundle.putInt(key, ((Integer)jsonResponse.get(key)).intValue());
               }
            }
         }

         return jsonResponse.get("total");
      }
   }
}
