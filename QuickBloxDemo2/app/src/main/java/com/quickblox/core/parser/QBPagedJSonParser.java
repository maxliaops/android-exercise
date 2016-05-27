package com.quickblox.core.parser;

import android.os.Bundle;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.model.QBEntityPaged;
import com.quickblox.core.parser.QBListJsonParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.result.Result;

public class QBPagedJSonParser extends QBListJsonParser {

   public QBPagedJSonParser(JsonQuery query) {
      super(query);
   }

   protected Object parseJsonResponse(RestResponse response, Result result) throws QBResponseException {
      QBEntityPaged qbEntityPaged = (QBEntityPaged)super.parseJsonResponse(response, result);
      Bundle bundle = this.getBundle();
      if(bundle != null) {
         bundle.putInt("current_page", qbEntityPaged.getCurrentPage().intValue());
         bundle.putInt("per_page", qbEntityPaged.getPerPage().intValue());
         bundle.putInt("total_entries", qbEntityPaged.getTotalEntries().intValue());
         int totalPages = qbEntityPaged.getTotalEntries().intValue() / qbEntityPaged.getPerPage().intValue();
         if(qbEntityPaged.getTotalEntries().intValue() % qbEntityPaged.getPerPage().intValue() > 0) {
            ++totalPages;
         }

         bundle.putInt("total_pages", totalPages);
      }

      return qbEntityPaged;
   }
}
