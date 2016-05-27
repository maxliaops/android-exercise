package com.quickblox.core.parser;

import android.os.Bundle;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.parser.QBJsonParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.result.Result;

public class QBParserDownloadFile extends QBJsonParser {

   public QBParserDownloadFile(JsonQuery query) {
      super(query);
   }

   protected Object parseJsonResponse(RestResponse response, Result result) throws QBResponseException {
      Bundle bundle = this.getBundle();
      if(bundle != null) {
         bundle.putLong("content_length", response.getContentLength());
      }

      return response.getInputStream();
   }
}
